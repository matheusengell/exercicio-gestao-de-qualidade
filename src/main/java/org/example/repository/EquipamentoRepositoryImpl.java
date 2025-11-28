package org.example.repository;

import org.example.database.Conexao;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.model.Equipamento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoRepositoryImpl {

    public Equipamento cadastrarEquipamento(Equipamento equipamento)throws SQLException{
    String query = """
            INSERT INTO Equipamento
            (nome, numeroDeSerie, areaSetor, statusOperacional) 
            VALUES (?,?,?,?)
            """;

    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, equipamento.getNome());
        stmt.setString(2, equipamento.getNumeroDeSerie());
        stmt.setString(3, equipamento.getAreaSetor());
        stmt.setString(4, equipamento.getStatusOperacional());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();

        if (rs.next()) {
            equipamento.setId(rs.getLong(1));
        }
    } catch (SQLException e) {
        System.err.println("Erro ao cadastrar equipamento no BD: " + e.getMessage());
        throw e;
    }


    return equipamento;
    }


    public Equipamento buscarId (Long id) throws SQLException {
        String query = """
            SELECT id, nome, numeroDeSerie, areaSetor, statusOperacional
            FROM Equipamento
            WHERE id = ?
            """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()){
                    String nome = rs.getString("nome");
                    String numeroDeSerie = rs.getString("numeroDeSerie");
                    String areaSetor = rs.getString("areaSetor");
                    String statusOperacional = rs.getString("statusOperacional");

                    Long idEncontrado = rs.getLong("id");

                    Equipamento equipamentoEncontrado = new Equipamento(nome, numeroDeSerie, areaSetor, statusOperacional);

                    equipamentoEncontrado.setId(idEncontrado);

                    return equipamentoEncontrado;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public void atualizarStatus(String status, long id)throws SQLException{
        String query = """
                UPDATE Equipamento
                SET statusOperacional = ?
                WHERE id = ?
                """;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1,status);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean equipamentoExiste(Long id) throws SQLException{
        String query = """
                SELECT COUNT(0)
                FROM Equipamento 
                WHERE id = ?
                """;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                return rs.getLong(1) > 0;
            }
        }
    return false;
    }

    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas)throws SQLException{
        List<EquipamentoContagemFalhasDTO> equipamentoContagemFalhasDTOS = new ArrayList<>();
        String query = """
                SELECT    e.id,
                e.nome,
                COUNT(f.id) AS totalFalhas
                FROM Equipamento e
                LEFT JOIN Falha f ON f.equipamentoId = e.id
                GROUP BY e.id, e.nome
                HAVING COUNT(f.id) >= ?      
                """;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, contagemMinimaFalhas);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                EquipamentoContagemFalhasDTO dto =
                        new EquipamentoContagemFalhasDTO(
                                rs.getLong("id"),
                                rs.getString("nome"),
                                rs.getInt("totalFalhas")
                        );
                equipamentoContagemFalhasDTOS.add(dto);
            }
        }
        return equipamentoContagemFalhasDTOS;
    }


}
