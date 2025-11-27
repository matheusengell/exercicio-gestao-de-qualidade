    package org.example.service.acaocorretiva;

    import org.example.model.AcaoCorretiva;
    import org.example.model.Equipamento;
    import org.example.model.Falha;
    import org.example.repository.AcaoCorretivaRepositoryImpl;
    import org.example.repository.EquipamentoRepositoryImpl;
    import org.example.repository.FalhaRepositoryImpl;

    import java.sql.SQLException;

    public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{

        AcaoCorretivaRepositoryImpl acaoCorretivaRepository = new AcaoCorretivaRepositoryImpl();
        FalhaRepositoryImpl falhaRepository = new FalhaRepositoryImpl();
        EquipamentoRepositoryImpl equipamentoRepository = new EquipamentoRepositoryImpl();

        @Override
        public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {
               Falha falha = falhaRepository.buscarFalhaId(acao.getFalhaId());

               if (falha == null){
                   throw new RuntimeException("Falha não encontrada!");
               }

               acao = acaoCorretivaRepository.cadastrarAcao(acao);
                 falhaRepository.atualizarFalha("RESOLVIDA", acao.getFalhaId());

                 if(falha.getCriticidade().equals("CRITICA")){
                equipamentoRepository.atualizarStatus("OPERACIONAL",falha.getEquipamentoId());
            }



            return acao;
        }
    }
