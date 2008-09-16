package core;

import java.sql.Date;

import core.interfaces.CorePresleyOperations;
import server.ServerBridge;
import validacao.ValidacaoAtividade;
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
		PacketStruct sucesso = new PacketStruct("Sucesso", -1);

		ExecuteClientQuery executeClientQuery = new ExecuteClientQuery(); 

		switch ( packet.getId() ) {
				// Packet tipo 1: adicionaAtividade(TipoAtividade atividade)
				case CorePresleyOperations.ADICIONA_ATIVIDADE:
					System.out.println("ADICIONA_ATIVIDADE");
					executeClientQuery.adicionaAtividade(packet);
				break;				
				
				// Packet tipo 2: removerAtividade(TipoAtividade atividade)
				case CorePresleyOperations.REMOVE_ATIVIDADE:
					System.out.println("REMOVE_ATIVIDADE");
					executeClientQuery.removerAtividade(packet);
				break;				
				
				// Packet tipo 3: BUSCA_ATIVIDADE()
				case CorePresleyOperations.BUSCA_ATIVIDADE:
					System.out.println("BUSCA_ATIVIDADE");
					executeClientQuery.buscaDesenvolvedores(packet);
				break;				
				
				// Packet tipo 4: ADICIONA_CONHECIMENTO
				case CorePresleyOperations.ADICIONA_CONHECIMENTO:
					System.out.println("ADICIONA_CONHECIMENTO");
					executeClientQuery.adicionaConhecimento(packet);
				break;				
				
				// Packet tipo 5: LOG_IN
				case CorePresleyOperations.LOG_IN:
					System.out.println("LOG_IN");
					executeClientQuery.login(packet);
				break;				
				
				// Packet tipo 6: LOG_OUT
				case CorePresleyOperations.LOG_OUT:
					System.out.println("LOG_OUT");
					executeClientQuery.logout(packet);
				break;				
				
				// Packet tipo 7: ENCERRAR_ATIVIDADE
				case CorePresleyOperations.ENCERRAR_ATIVIDADE:
					System.out.println("ENCERRAR_ATIVIDADE");
					executeClientQuery.encerrarAtividade(packet);
				break;				
				
				// Packet tipo 8: ASSOCIAR_CONHECIMENTO_ATIVIDADE
				case CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE:
					System.out.println("ASSOCIAR_CONHECIMENTO_ATIVIDADE");
					executeClientQuery.associaConhecimentoAtividade(packet);
				break;				
				
				// Packet tipo 9: DESSASOCIAR_CONHECIMENTO_ATIVIDADE
				case CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE:
					System.out.println("DESSASOCIAR_CONHECIMENTO_ATIVIDADE");
					executeClientQuery.desassociaConhecimentoAtividade(packet);
				break;				
				
				// Packet tipo 10: ASSOCAR_PROBLEMA_ATIVIDADE
				case CorePresleyOperations.ASSOCAR_PROBLEMA_ATIVIDADE:
					System.out.println("ASSOCAR_PROBLEMA_ATIVIDADE");
					executeClientQuery.associaProblemaAtividade(packet);
				break;				
				
				// Packet tipo 11: DESSASOCIAR_PROBLEMA_ATIVIDADE
				case CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE:
					System.out.println("DESSASOCIAR_PROBLEMA_ATIVIDADE");
					executeClientQuery.desassociaProblemaAtividade(packet);
				break;				
				
				// Packet tipo 12: BUSCA_DESENVOLVEDORES
				case CorePresleyOperations.BUSCA_DESENVOLVEDORES:
					System.out.println("BUSCA_DESENVOLVEDORES");
					executeClientQuery.buscaDesenvolvedores(packet);
				break;				
				
				// Packet tipo 13: QUALIFICA_DESENVOLVEDOR
				case CorePresleyOperations.QUALIFICA_DESENVOLVEDOR:
					System.out.println("QUALIFICA_DESENVOLVEDOR");
					executeClientQuery.qualificaDesenvolvedor(packet);
				break;				
				
				// Packet tipo 14: ENVIAR_MENSAGEM
				case CorePresleyOperations.ENVIAR_MENSAGEM:
					System.out.println("ENVIAR_MENSAGEM");
					executeClientQuery.enviarMensagem(packet);
				break;				
				
				// Packet tipo 15: GET_LISTA_DESENVOLVEDORES
				case CorePresleyOperations.GET_LISTA_DESENVOLVEDORES:
					System.out.println("GET_LISTA_DESENVOLVEDORES");
					executeClientQuery.getListaDesenvolvedores();
				break;				

		}
		if (packet.getId() == 1) {
			System.out.println("Packet tipo 1, cadastro de atividade");
		}
		return sucesso;
	}

	/**
	 * Este metodo realiza a validacao dos dados de uma atividade e 
	 * executa seu cadastro no banco de dados remoto.
	 * 
	 * @param packet O pacote recebido do cliente
	 * @return boolean true se a atividade foi validada e cadastrada corretamente, caso contrario, false
	 */
	private boolean cadastraAtividade(PacketStruct packet) {
		System.out.println("Chamar o modulo de validacao pra cadastrar essa bagaca!");
		TipoAtividade novaAtividade = (TipoAtividade) packet.getData();
		System.out.println(novaAtividade);

		ValidacaoAtividadeImpl validacaoAtividade = new ValidacaoAtividadeImpl();

		try {
			validacaoAtividade.cadastrarAtividade(novaAtividade.getDesenvolvedor().getEmail(), 
					novaAtividade.getSupervisor().getEmail(), 
					novaAtividade.getDescricao(), 
					novaAtividade.getDataInicio(), 
					novaAtividade.getDataFinal());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}


}
