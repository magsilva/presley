package core;

import beans.DadosAutenticacao;
import core.interfaces.CorePresleyOperations;
import server.ServerBridge;
import excessao.AtividadeInexistenteException;
import excessao.ConhecimentoInexistenteException;
import excessao.DataInvalidaException;
import excessao.DescricaoInvalidaException;
import excessao.DesenvolvedorExisteException;
import excessao.DesenvolvedorInexistenteException;
import excessao.EmailInvalidoException;
import excessao.ErroDeAutenticacaoException;
import excessao.ProblemaInexistenteException;
import excessao.SenhaInvalidaException;
import facade.PacketStruct;

/**
 * Esta classe implementa no servidor metodos para rotear
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class ServerBridgeImp implements ServerBridge {



	public ServerBridgeImp() {
		System.out.println("instanciando ServerBridgeImp");
	}

	/**
	 * Recebe o pacote no servidor e o encaminha 
	 * de forma adequada. Implementado da interface ServerBridge
	 * 
	 * @param packet O pacote recebido
	 * @return Packet O pacote resposta para o cliente
	 */
	public PacketStruct sendToServer(PacketStruct packet) {
		PacketStruct pktRetorno = null;
		Object retorno = null;
		ExecuteClientQuery executeClientQuery = new ExecuteClientQuery(); 
		int typeRetorno = CorePresleyOperations.ERRO;

		switch ( packet.getId() ) {
		// Packet tipo 1: adicionaAtividade(TipoAtividade atividade) - ok
		case CorePresleyOperations.ADICIONA_ATIVIDADE:
			System.out.println("ADICIONA_ATIVIDADE");

			try {
				retorno = executeClientQuery.adicionaAtividade(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_ATIVIDADE;
			} catch (EmailInvalidoException e1) {
				retorno = "ERRO: Email inválido.";
			} catch (DescricaoInvalidaException e1) {
				retorno = "ERRO: Descrição inválida.";
			} catch (DataInvalidaException e1) {
				retorno = "ERRO: Data inválida.";
			} catch (Exception e1) {
				retorno = "ERRO: Operação falhou.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 2: removerAtividade(TipoAtividade atividade) -ok
		case CorePresleyOperations.REMOVE_ATIVIDADE:
			System.out.println("REMOVE_ATIVIDADE");

			try {
				retorno = executeClientQuery.removerAtividade(packet);
				typeRetorno = CorePresleyOperations.REMOVE_ATIVIDADE;
			} catch (AtividadeInexistenteException e2) {
				retorno = "ERRO: Atividade inexistente.";
			} catch (ProblemaInexistenteException e) {
				retorno = "ERRO: Problema inexistente.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 3: BUSCA_ATIVIDADE()			
		case CorePresleyOperations.BUSCA_ATIVIDADE:
			System.out.println("BUSCA_ATIVIDADE");
			typeRetorno = CorePresleyOperations.BUSCA_ATIVIDADE;
			retorno = executeClientQuery.buscaAtividades();
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 4: ADICIONA_CONHECIMENTO
		case CorePresleyOperations.ADICIONA_CONHECIMENTO:
			System.out.println("ADICIONA_CONHECIMENTO");
			try {
				retorno = executeClientQuery.adicionaConhecimento(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_CONHECIMENTO;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descrição inválida.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Operação falhou.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 5: LOG_IN
		case CorePresleyOperations.LOG_IN:
			System.out.println("LOG_IN");
			System.out.println((DadosAutenticacao)packet.getData());
			try {
				retorno = executeClientQuery.login(packet);
				typeRetorno = CorePresleyOperations.LOG_IN;
			} catch (DesenvolvedorInexistenteException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Desenvolvedor inexistente.";
			} catch (EmailInvalidoException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Email inválido.";
			} catch (SenhaInvalidaException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Senha inválida.";
			} catch (ErroDeAutenticacaoException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Erro de autenticação.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 6: LOG_OUT
		case CorePresleyOperations.LOG_OUT:
			System.out.println("LOG_OUT");
			typeRetorno = CorePresleyOperations.LOG_OUT;
			retorno = executeClientQuery.logout(packet);
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 7: ENCERRAR_ATIVIDADE
		case CorePresleyOperations.ENCERRAR_ATIVIDADE:
			System.out.println("ENCERRAR_ATIVIDADE");
			typeRetorno = CorePresleyOperations.ENCERRAR_ATIVIDADE;
			try {
				retorno = executeClientQuery.encerrarAtividade(packet);
			} catch (AtividadeInexistenteException e3) {
				e3.printStackTrace();
				retorno = "ERRO: Atividade Inexistente.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 8: ASSOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE:
			System.out.println("ASSOCIAR_CONHECIMENTO_ATIVIDADE");

			try {
				retorno = executeClientQuery.associaConhecimentoAtividade(packet);
				typeRetorno = CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE;
			} catch (AtividadeInexistenteException e) {
				retorno = "ERRO: Atividade inexistente.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Operação falhou.";
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 9: DESSASOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE:
			System.out.println("DESSASOCIAR_CONHECIMENTO_ATIVIDADE");
			try {
				retorno = executeClientQuery.desassociaConhecimentoAtividade(packet);
				typeRetorno = CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE;
			} catch (ConhecimentoInexistenteException e1) {
				retorno = "ERRO: Conhecimento inexistente.";
				e1.printStackTrace();
			} catch (AtividadeInexistenteException e1) {
				retorno = "ERRO: Atividade inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 10: ASSOCAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.ASSOCIAR_PROBLEMA_ATIVIDADE:
			System.out.println("ASSOCIAR_PROBLEMA_ATIVIDADE");
			try {
				retorno = executeClientQuery.associaProblemaAtividade(packet);
				typeRetorno = CorePresleyOperations.ASSOCIAR_PROBLEMA_ATIVIDADE;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descrição inválida.";
				e.printStackTrace();
			} catch (AtividadeInexistenteException e) {
				retorno = "ERRO: Atividade inexistente.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 11: DESSASOCIAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE:
			System.out.println("DESSASOCIAR_PROBLEMA_ATIVIDADE");
			try {
				retorno = executeClientQuery.desassociaProblemaAtividade(packet);
				typeRetorno = CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE;
			} catch (ProblemaInexistenteException e1) {
				retorno = "ERRO: Problema inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 12: BUSCA_DESENVOLVEDORES
		case CorePresleyOperations.BUSCA_DESENVOLVEDORES:
			System.out.println("BUSCA_DESENVOLVEDORES");

			try {
				retorno = executeClientQuery.buscaDesenvolvedores(packet);
				typeRetorno = CorePresleyOperations.BUSCA_DESENVOLVEDORES;
			} catch (DesenvolvedorInexistenteException e2) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e2.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 13: QUALIFICA_DESENVOLVEDOR
		case CorePresleyOperations.QUALIFICA_DESENVOLVEDOR:
			System.out.println("QUALIFICA_DESENVOLVEDOR");
			try {
					retorno = executeClientQuery.qualificaDesenvolvedor(packet);
					typeRetorno = CorePresleyOperations.QUALIFICA_DESENVOLVEDOR;
				} catch (ConhecimentoInexistenteException e2) {
					retorno = "ERRO: Conhecimento inexistente.";
					e2.printStackTrace();
				} catch (DesenvolvedorInexistenteException e2) {
					retorno = "ERRO: Desenvolvedor inexistente.";
					e2.printStackTrace();
				}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 14: ENVIAR_MENSAGEM
		case CorePresleyOperations.ENVIAR_MENSAGEM:
			System.out.println("ENVIAR_MENSAGEM");
			try {
				retorno = executeClientQuery.enviarMensagem(packet);
				typeRetorno = CorePresleyOperations.ENVIAR_MENSAGEM;
			} catch (DesenvolvedorInexistenteException e2) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e2.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 15: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_DESENVOLVEDORES:
			System.out.println("GET_LISTA_DESENVOLVEDORES");
			retorno = executeClientQuery.getListaDesenvolvedores();
			typeRetorno = CorePresleyOperations.GET_LISTA_DESENVOLVEDORES;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

			// Packet tipo 16: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_CONHECIMENTO:
			System.out.println("GET_LISTA_CONHECIMENTOS");
			retorno = executeClientQuery.getListaConhecimentos();
			typeRetorno = CorePresleyOperations.GET_LISTA_CONHECIMENTO;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;
			// Packet tipo 17: GET_ONTOLOGIA
		case CorePresleyOperations.GET_ONTOLOGIA:
			System.out.println("GET_ONTOLOGIA");
			try {
				retorno = executeClientQuery.getOntologia();
				typeRetorno = CorePresleyOperations.GET_ONTOLOGIA;
			} catch (ConhecimentoInexistenteException e1) {
				retorno = "ERRO: Conhecimento inexistente.";
				e1.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 18: ADICIONA_DESENVOLVEDOR
		case CorePresleyOperations.ADICIONA_DESENVOLVEDOR:
			System.out.println("ADICIONA_DESENVOLVEDOR");

			try {
				retorno = executeClientQuery.adicionaDesenvolvedor(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_DESENVOLVEDOR;
			} catch (DesenvolvedorExisteException e) {
				retorno = "ERRO: Desenvolvedor já Cadastrado no Banco.";
				e.printStackTrace();
			} catch (SenhaInvalidaException e) {
				retorno = "ERRO: Senha Invalida.";
				e.printStackTrace();
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descricao Invalida.";
				e.printStackTrace();
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
				e.printStackTrace();
			} catch (DesenvolvedorInexistenteException e) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;	
			
			// Packet tipo 19: GET_LISTA_PROBLEMAS
		case CorePresleyOperations.GET_LISTA_PROBLEMAS:
			System.out.println("GET_LISTA_PROBLEMAS");

				try {
					retorno = executeClientQuery.getListaProblemas();
				} catch (Exception e) {
					e.printStackTrace();
				}
				typeRetorno = CorePresleyOperations.GET_LISTA_PROBLEMAS;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

			// Packet tipo 20: BUSCA_CONHECIMENTOS_RELACIONADOS
		case CorePresleyOperations.BUSCA_CONHECIMENTOS_RELACIONADOS:
			System.out.println("BUSCA_CONHECIMENTOS_RELACIONADOS");

				try {
					retorno = executeClientQuery.getListaConhecimentosEnvolvidos(packet);
				} catch (ConhecimentoInexistenteException e) {
					e.printStackTrace();
				}
				typeRetorno = CorePresleyOperations.BUSCA_CONHECIMENTOS_RELACIONADOS;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;
		case CorePresleyOperations.REMOVER_CONHECIMENTO:
			System.out.println("REMOVER_CONHECIMENTO");

				try {
					retorno = executeClientQuery.removerConhecimento(packet);
				} catch (ConhecimentoInexistenteException e) {
					retorno = "ERRO: Conhecimento inexistente.";
					e.printStackTrace();
				}
				typeRetorno = CorePresleyOperations.REMOVER_CONHECIMENTO;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

		}
		return pktRetorno;
		
	}
}