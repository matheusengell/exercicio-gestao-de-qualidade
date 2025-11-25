package org.example.repository;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public <FalhaCritica> List<FalhaCritica> falhaCriticaList() throws SQLException{
        String query = """
                SELECT equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras
                FROM Falha 
                WHERE criticidade = 'CRITICA'        
                """;
        List<FalhaCritica> falhaCriticas = new ArrayList<>();


    return falhaCriticas;
    }
}