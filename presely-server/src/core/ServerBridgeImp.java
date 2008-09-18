package core;

import java.sql.Date;

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
import validacao.implementacao.ValidacaoAtividadeImpl;
import beans.TipoAtividade;
import facade.PacketStruct;

/**
 * Esta classe implementa no servidor metodos para rotear
 * os pacotes que chegam do modulo de comunicacao.
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

		switch ( packet.getId() ) {
		// Packet tipo 1: adicionaAtividade(TipoAtividade atividade) - ok
		case CorePresleyOperations.ADICIONA_ATIVIDADE:
			System.out.println("ADICIONA_ATIVIDADE");
			try {
				retorno = executeClientQuery.adicionaAtividade(packet);
			} catch (EmailInvalidoException e1) {
				retorno = e1;
			} catch (DescricaoInvalidaException e1) {
				retorno = e1;
			} catch (DataInvalidaException e1) {
				retorno = e1;
			} catch (Exception e1) {
				retorno = e1;
			}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.ADICIONA_ATIVIDADE);
			break;				

			// Packet tipo 2: removerAtividade(TipoAtividade atividade) -ok
		case CorePresleyOperations.REMOVE_ATIVIDADE:
			System.out.println("REMOVE_ATIVIDADE");
			try {
				retorno = executeClientQuery.removerAtividade(packet);
			} catch (AtividadeInexistenteException e2) {
				retorno = e2;
			} catch (ProblemaInexistenteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.REMOVE_ATIVIDADE);
			break;				

			// Packet tipo 3: BUSCA_ATIVIDADE()			
		case CorePresleyOperations.BUSCA_ATIVIDADE:
			System.out.println("BUSCA_ATIVIDADE");
			retorno = executeClientQuery.buscaDesenvolvedores(packet);
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.BUSCA_ATIVIDADE);
			break;				

			// Packet tipo 4: ADICIONA_CONHECIMENTO
		case CorePresleyOperations.ADICIONA_CONHECIMENTO:
			System.out.println("ADICIONA_CONHECIMENTO");
			try {
				retorno = executeClientQuery.adicionaConhecimento(packet);
			} catch (DescricaoInvalidaException e) {
				retorno = e;
			} catch (ConhecimentoInexistenteException e) {
				retorno = e;
			} catch (Exception e) {
				retorno = e;
			}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.ADICIONA_CONHECIMENTO);
			break;				

			// Packet tipo 5: LOG_IN
		case CorePresleyOperations.LOG_IN:
			System.out.println("LOG_IN");
			try {
					retorno = executeClientQuery.login(packet);
				} catch (DesenvolvedorInexistenteException e2) {
					e2.printStackTrace();
					retorno = e2;
				} catch (EmailInvalidoException e2) {
					e2.printStackTrace();
					retorno = e2;
				} catch (SenhaInvalidaException e2) {
					e2.printStackTrace();
					retorno = e2;
				} catch (ErroDeAutenticacaoException e2) {
					e2.printStackTrace();
					retorno = e2;
				}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.LOG_IN);
			break;				

			// Packet tipo 6: LOG_OUT
		case CorePresleyOperations.LOG_OUT:
			System.out.println("LOG_OUT");
			retorno = executeClientQuery.logout(packet);
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.LOG_OUT);
			break;				

			// Packet tipo 7: ENCERRAR_ATIVIDADE
		case CorePresleyOperations.ENCERRAR_ATIVIDADE:
			System.out.println("ENCERRAR_ATIVIDADE");
			retorno = executeClientQuery.encerrarAtividade(packet);
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.ENCERRAR_ATIVIDADE);
			break;				

			// Packet tipo 8: ASSOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE:
			System.out.println("ASSOCIAR_CONHECIMENTO_ATIVIDADE");

			try {
				retorno = executeClientQuery.associaConhecimentoAtividade(packet);
			} catch (AtividadeInexistenteException e) {
				retorno = e;
			} catch (ConhecimentoInexistenteException e) {
				retorno = e;
			} catch (Exception e) {
				retorno = e;
			}

			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE);
			break;				

			// Packet tipo 9: DESSASOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE:
			System.out.println("DESSASOCIAR_CONHECIMENTO_ATIVIDADE");
			try {
				retorno = executeClientQuery.desassociaConhecimentoAtividade(packet);
			} catch (ConhecimentoInexistenteException e1) {
				retorno = e1;
			} catch (AtividadeInexistenteException e1) {
				retorno = e1;
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE);
			break;				

			// Packet tipo 10: ASSOCAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.ASSOCAR_PROBLEMA_ATIVIDADE:
			System.out.println("ASSOCAR_PROBLEMA_ATIVIDADE");
			try {
				retorno = executeClientQuery.associaProblemaAtividade(packet);
			} catch (DescricaoInvalidaException e) {
				retorno = e;
			} catch (AtividadeInexistenteException e) {
				retorno = e;
			}
			pktRetorno= new PacketStruct(retorno, CorePresleyOperations.ASSOCAR_PROBLEMA_ATIVIDADE);
			break;				

			// Packet tipo 11: DESSASOCIAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE:
			System.out.println("DESSASOCIAR_PROBLEMA_ATIVIDADE");
			try {
					retorno = executeClientQuery.desassociaProblemaAtividade(packet);
				} catch (ProblemaInexistenteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE);
			break;				

			// Packet tipo 12: BUSCA_DESENVOLVEDORES
		case CorePresleyOperations.BUSCA_DESENVOLVEDORES:
			System.out.println("BUSCA_DESENVOLVEDORES");
			retorno = executeClientQuery.buscaDesenvolvedores(packet);
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.BUSCA_DESENVOLVEDORES);
			break;				

			// Packet tipo 13: QUALIFICA_DESENVOLVEDOR
		case CorePresleyOperations.QUALIFICA_DESENVOLVEDOR:
			System.out.println("QUALIFICA_DESENVOLVEDOR");
			retorno = executeClientQuery.qualificaDesenvolvedor(packet);
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.QUALIFICA_DESENVOLVEDOR);
			break;				

			// Packet tipo 14: ENVIAR_MENSAGEM
		case CorePresleyOperations.ENVIAR_MENSAGEM:
			System.out.println("ENVIAR_MENSAGEM");
			retorno = executeClientQuery.enviarMensagem(packet);
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.ENVIAR_MENSAGEM);
			break;				

			// Packet tipo 15: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_DESENVOLVEDORES:
			System.out.println("GET_LISTA_DESENVOLVEDORES");
			retorno = executeClientQuery.getListaDesenvolvedores();
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.GET_LISTA_DESENVOLVEDORES);
			break;

			// Packet tipo 16: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_CONHECIMENTO:
			System.out.println("GET_LISTA_CONHECIMENTOS");
			retorno = executeClientQuery.getListaConhecimentos();
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.GET_LISTA_CONHECIMENTO);
			break;
		case CorePresleyOperations.GET_ONTOLOGIA:
			System.out.println("GET_ONTOLOGIA");
			retorno = executeClientQuery.getOntologia();
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.GET_ONTOLOGIA);
			break;				

		case CorePresleyOperations.ADICIONA_DESENVOLVEDOR:
			System.out.println("ADICIONA_DESENVOLVEDOR");
			try {
				retorno = executeClientQuery.adicionaDesenvolvedor(packet);
			} catch (Exception e) {
				retorno = e;
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, CorePresleyOperations.ADICIONA_DESENVOLVEDOR);
			break;				

		}
		return pktRetorno;
	}


}
