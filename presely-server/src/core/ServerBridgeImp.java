package core;

import core.interfaces.CorePresleyOperations;
import server.ServerBridge;
import excessao.AtividadeInexistenteException;
import excessao.ConhecimentoInexistenteException;
import excessao.DataInvalidaException;
import excessao.DescricaoInvalidaException;
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
		int type = CorePresleyOperations.ERRO;
		
		switch ( packet.getId() ) {
		// Packet tipo 1: adicionaAtividade(TipoAtividade atividade) - ok
		case CorePresleyOperations.ADICIONA_ATIVIDADE:
			System.out.println("ADICIONA_ATIVIDADE");
			
			try {
				retorno = executeClientQuery.adicionaAtividade(packet);
				type = CorePresleyOperations.ADICIONA_ATIVIDADE;
			} catch (EmailInvalidoException e1) {
				retorno = "ERRO: Email inválido.";
			} catch (DescricaoInvalidaException e1) {
				retorno = "ERRO: Descrição inválida.";
			} catch (DataInvalidaException e1) {
				retorno = "ERRO: Data inválida.";
			} catch (Exception e1) {
				retorno = "ERRO: Operação falhou.";
			}
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 2: removerAtividade(TipoAtividade atividade) -ok
		case CorePresleyOperations.REMOVE_ATIVIDADE:
			System.out.println("REMOVE_ATIVIDADE");
			
			try {
				retorno = executeClientQuery.removerAtividade(packet);
				type = CorePresleyOperations.REMOVE_ATIVIDADE;
			} catch (AtividadeInexistenteException e2) {
				retorno = "ERRO: Atividade inexistente.";
			} catch (ProblemaInexistenteException e) {
				retorno = "ERRO: Problema inexistente.";
			}
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 3: BUSCA_ATIVIDADE()			
		case CorePresleyOperations.BUSCA_ATIVIDADE:
			System.out.println("BUSCA_ATIVIDADE");
			type = CorePresleyOperations.BUSCA_ATIVIDADE;
			retorno = executeClientQuery.buscaDesenvolvedores(packet);
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 4: ADICIONA_CONHECIMENTO
		case CorePresleyOperations.ADICIONA_CONHECIMENTO:
			System.out.println("ADICIONA_CONHECIMENTO");
			try {
				retorno = executeClientQuery.adicionaConhecimento(packet);
				type = CorePresleyOperations.ADICIONA_CONHECIMENTO;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descrição inválida.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Operação falhou.";
			}
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 5: LOG_IN
		case CorePresleyOperations.LOG_IN:
			System.out.println("LOG_IN");
			try {
					retorno = executeClientQuery.login(packet);
					type = CorePresleyOperations.LOG_IN;
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
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 6: LOG_OUT
		case CorePresleyOperations.LOG_OUT:
			System.out.println("LOG_OUT");
			type = CorePresleyOperations.LOG_OUT;
			retorno = executeClientQuery.logout(packet);
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 7: ENCERRAR_ATIVIDADE
		case CorePresleyOperations.ENCERRAR_ATIVIDADE:
			System.out.println("ENCERRAR_ATIVIDADE");
			type = CorePresleyOperations.ENCERRAR_ATIVIDADE;
			retorno = executeClientQuery.encerrarAtividade(packet);
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 8: ASSOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE:
			System.out.println("ASSOCIAR_CONHECIMENTO_ATIVIDADE");

			try {
				retorno = executeClientQuery.associaConhecimentoAtividade(packet);
				type = CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE;
			} catch (AtividadeInexistenteException e) {
				retorno = "ERRO: Atividade inexistente.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Operação falhou.";
			}

			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 9: DESSASOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE:
			System.out.println("DESSASOCIAR_CONHECIMENTO_ATIVIDADE");
			try {
				retorno = executeClientQuery.desassociaConhecimentoAtividade(packet);
				type = CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE;
			} catch (ConhecimentoInexistenteException e1) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (AtividadeInexistenteException e1) {
				retorno = "ERRO: Atividade inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 10: ASSOCAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.ASSOCAR_PROBLEMA_ATIVIDADE:
			System.out.println("ASSOCAR_PROBLEMA_ATIVIDADE");
			try {
				retorno = executeClientQuery.associaProblemaAtividade(packet);
				type = CorePresleyOperations.ASSOCAR_PROBLEMA_ATIVIDADE;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descrição inválida.";
			} catch (AtividadeInexistenteException e) {
				retorno = "ERRO: Atividade inexistente.";
			}
			pktRetorno= new PacketStruct(retorno, type);
			break;				

			// Packet tipo 11: DESSASOCIAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE:
			System.out.println("DESSASOCIAR_PROBLEMA_ATIVIDADE");
			try {
					retorno = executeClientQuery.desassociaProblemaAtividade(packet);
					type = CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE;
				} catch (ProblemaInexistenteException e1) {
					retorno = "ERRO: Problema inexistente.";
					e1.printStackTrace();
				}
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 12: BUSCA_DESENVOLVEDORES
		case CorePresleyOperations.BUSCA_DESENVOLVEDORES:
			System.out.println("BUSCA_DESENVOLVEDORES");
			
			retorno = executeClientQuery.buscaDesenvolvedores(packet);
			type = CorePresleyOperations.BUSCA_DESENVOLVEDORES;
			
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 13: QUALIFICA_DESENVOLVEDOR
		case CorePresleyOperations.QUALIFICA_DESENVOLVEDOR:
			System.out.println("QUALIFICA_DESENVOLVEDOR");
			retorno = executeClientQuery.qualificaDesenvolvedor(packet);
			type = CorePresleyOperations.QUALIFICA_DESENVOLVEDOR;
			
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 14: ENVIAR_MENSAGEM
		case CorePresleyOperations.ENVIAR_MENSAGEM:
			System.out.println("ENVIAR_MENSAGEM");
			retorno = executeClientQuery.enviarMensagem(packet);
			type = CorePresleyOperations.ENVIAR_MENSAGEM;
			
			pktRetorno = new PacketStruct(retorno, type);
			break;				

			// Packet tipo 15: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_DESENVOLVEDORES:
			System.out.println("GET_LISTA_DESENVOLVEDORES");
			retorno = executeClientQuery.getListaDesenvolvedores();
			type = CorePresleyOperations.GET_LISTA_DESENVOLVEDORES;
			
			pktRetorno = new PacketStruct(retorno, type);
			break;

			// Packet tipo 16: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_CONHECIMENTO:
			System.out.println("GET_LISTA_CONHECIMENTOS");
			retorno = executeClientQuery.getListaConhecimentos();
			type = CorePresleyOperations.GET_LISTA_CONHECIMENTO;
			
			pktRetorno = new PacketStruct(retorno, type);
			break;
		case CorePresleyOperations.GET_ONTOLOGIA:
			System.out.println("GET_ONTOLOGIA");
			retorno = executeClientQuery.getOntologia();
			type = CorePresleyOperations.GET_ONTOLOGIA;
			
			pktRetorno = new PacketStruct(retorno, type);
			break;				

		case CorePresleyOperations.ADICIONA_DESENVOLVEDOR:
			System.out.println("ADICIONA_DESENVOLVEDOR");
			try {
				retorno = executeClientQuery.adicionaDesenvolvedor(packet);
				type = CorePresleyOperations.ADICIONA_DESENVOLVEDOR;
			} catch (Exception e) {
				retorno = "ERRO: Operação falhou.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, type);
			break;				

		}
		return pktRetorno;
	}


}
