package org.example.service.falha;

import org.example.model.Falha;
import org.example.repository.EquipamentoRepositoryImpl;
import org.example.repository.FalhaRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService{
    FalhaRepositoryImpl falhaRepository = new FalhaRepositoryImpl();
    EquipamentoRepositoryImpl equipamentoRepository = new EquipamentoRepositoryImpl();

    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {
        try {
            if (!equipamentoRepository.equipamentoExiste(falha.getEquipamentoId())){
                throw new IllegalArgumentException("Equipamento não encontrado!");
            }

            Falha falhaSalva = falhaRepository.registrarFalha(falha);

            if ("CRITICA".equals(falha.getCriticidade())) {
                equipamentoRepository.atualizarStatus("EM_MANUTENCAO", falha.getEquipamentoId());
            }

            return falhaSalva;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {
        return falhaRepository.falhaCriticaList();
    }
}
