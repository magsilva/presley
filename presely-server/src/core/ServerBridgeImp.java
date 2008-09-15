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
public class ServerBridgeImp implements ServerBridge, CorePresleyOperations{

	

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
		
		System.out.println("Analisando o Packet");
		if (packet.getId() == 1) {
			System.out.println("Packet tipo 1, cadastro de atividade");
			cadastraAtividade(packet);
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
