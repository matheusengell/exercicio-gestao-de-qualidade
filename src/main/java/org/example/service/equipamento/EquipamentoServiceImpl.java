package org.example.service.equipamento;

import org.example.model.Equipamento;
import org.example.repository.EquipamentoRepositoryImpl;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class EquipamentoServiceImpl implements EquipamentoService{

    EquipamentoRepositoryImpl equipamentoRepository = new EquipamentoRepositoryImpl();

    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {
        if (equipamento == null){
            throw new IllegalArgumentException("O equipamento não deveria ser nulo");
        }

        Equipamento equipamentoNovo = equipamentoRepository.cadastrarEquipamento(equipamento);

        return equipamentoNovo;
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException {
        Equipamento equipamentoEncontrado = equipamentoRepository.buscarId(id);
        if (equipamentoEncontrado == null){
            throw new NoSuchElementException("Equipamento não encontrado!");
        }
        return equipamentoEncontrado;
    }
}
