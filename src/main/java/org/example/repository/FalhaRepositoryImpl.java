package org.example.repository;

import org.example.database.Conexao;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Falha;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FalhaRepositoryImpl {

    public Falha registrarFalha(Falha falha)throws SQLException{
        String query = """
                INSERT INTO Falha (equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras)
                VALUES 
                (?,?,?,?,'ABERTA',?)
                """;
    try (Connection conn = Conexao.conectar();
         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
        stmt.setLong(1, falha.getEquipamentoId());
        stmt.setObject(2, falha.getDataHoraOcorrencia());
        stmt.setString(3, falha.getDescricao());
        stmt.setString(4, falha.getCriticidade());
        stmt.setBigDecimal(5, falha.getTempoParadaHoras());
        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();

        if (rs.next()){
            falha.setId(rs.getLong(1));
        }

        falha.setStatus("ABERTA");

    }catch (SQLException e){
        e.printStackTrace();
    }
   return falha;
    }

    public List<Falha> falhaCriticaList() throws SQLException{
        List<Falha> falhaCriticas = new ArrayList<>();
        String query = """
                SELECT equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras
                FROM Falha 
                WHERE criticidade = 'CRITICA' 
                AND status = 'ABERTA'       
         """;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
        ResultSet rs = stmt.executeQuery();

        while (rs.next()){
            long equipamentoId = rs.getLong("equipamentoId");
            Timestamp dataHora = rs.getTimestamp("dataHoraOcorrencia");
            String descricao = rs.getString("descricao");
            String  criticidade = rs.getString("criticidade");
            String status = rs.getString("status");
            BigDecimal tempoParada = rs.getBigDecimal("tempoParadaHoras");
            Falha falha = new Falha(equipamentoId, dataHora.toLocalDateTime(), descricao, criticidade,status, tempoParada);
            falhaCriticas.add(falha);
        }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    return falhaCriticas;
    }

    public void atualizarFalha(String status, long id)throws SQLException{
        String query = """
               UPDATE Falha
               SET status = ?
               WHERE id = ? 
                """;
        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, status);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }

    public Falha buscarFalhaId( long id)throws SQLException{
        String query = """
                SELECT id, equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras
                FROM Falha
                WHERE id = ?
                """;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long equipamentoId = rs.getLong("equipamentoId");
                LocalDateTime dataHoraOcorrencia = rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime();
                String descricao = rs.getString("descricao");
                String criticidade = rs.getString("criticidade");
                String status = rs.getString("status");
                BigDecimal tempoParadaHoras = rs.getBigDecimal("tempoParadaHoras");


                return new Falha(equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras);
            }

        }
            return null;
    }

    public List<RelatorioParadaDTO> gerarRelatorioTempoParada()throws SQLException{
        List<RelatorioParadaDTO> relatorioParadaDTOS = new ArrayList<>();
        String query = """
                SELECT f.equipamentoId, e.nome, f.tempoParadaHoras
                FROM Falha f
                JOIN Equipamento e
                ON f.equipamentoId = e.id
                """;
        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                long equipamentoId = rs.getLong("equipamentoId");
                String nome = rs.getString("nome");
                double horasParadas = rs.getDouble("tempoParadaHoras");
                relatorioParadaDTOS.add(new RelatorioParadaDTO(equipamentoId, nome, horasParadas));
            }
        }
        return relatorioParadaDTOS;
    }

    public Falha buscarDetalhesCompletosFalha(long falhaId)throws SQLException{
    String query = """
            SELECT id
             ,equipamentoId
             ,dataHoraOcorrencia
             ,descricao
             ,criticidade
             ,status
             ,tempoParadaHoras
                 FROM Falha
                 WHERE id = ?
            """;
    try (Connection conn = Conexao.conectar();
    PreparedStatement stmt = conn.prepareStatement(query)){
    stmt.setLong(1, falhaId);
    ResultSet rs = stmt.executeQuery();

    while (rs.next()){
        Long equipamentoId = rs.getLong("equipamentoId");
        LocalDateTime dataHoraOcorrencia = rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime();
        String descricao = rs.getString("descricao");
        String criticidade = rs.getString("criticidade");
        String status = rs.getString("status");
        BigDecimal tempoParadaHoras = rs.getBigDecimal("tempoParadaHoras");

        return new Falha(equipamentoId, dataHoraOcorrencia, descricao,criticidade,status, tempoParadaHoras);
    }
    }
    return null;
    }


}