package org.example.service.relatorioservice;

import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.model.Falha;
import org.example.repository.AcaoCorretivaRepositoryImpl;
import org.example.repository.EquipamentoRepositoryImpl;
import org.example.repository.FalhaRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService{
    FalhaRepositoryImpl falhaRepository = new FalhaRepositoryImpl();
    EquipamentoRepositoryImpl equipamentoRepository = new EquipamentoRepositoryImpl();
    AcaoCorretivaRepositoryImpl acaoCorretivaRepository = new AcaoCorretivaRepositoryImpl();

    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        return falhaRepository.gerarRelatorioTempoParada();
    }

    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        return List.of();
    }

    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {
        Falha falha = falhaRepository.buscarFalhaId(falhaId);

        if (falha == null){
            throw new RuntimeException();
        }

        Equipamento equipamento = equipamentoRepository.buscarId(falha.getEquipamentoId());

        if (equipamento == null){
            throw new RuntimeException();
        }

        return Optional.of(new FalhaDetalhadaDTO(falha, equipamento));
    }

    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException {
        if (contagemMinimaFalhas < 1){
            throw new RuntimeException();
        }
        return equipamentoRepository.gerarRelatorioManutencaoPreventiva(contagemMinimaFalhas);
    }
}
