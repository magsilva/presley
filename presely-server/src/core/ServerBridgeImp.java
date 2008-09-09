package core;

import java.sql.Date;
import validacao.ValidacaoAtividade;
import beans.Atividade;
import comunicacao.Packet;

/**
 * Esta classe implementa no servidor metodos para rotear
 * os pacotes que chegam do modulo de comunicacao.
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class ServerBridgeImp implements ServerBridge{

	/**
     * Recebe o pacote no servidor e o encaminha 
     * de forma adequada. Implementado da interface ServerBridge
     * 
     * @param packet O pacote recebido
     * @return Packet O pacote resposta para o cliente
     */
	public Packet sendPacket(Packet packet) {
		System.out.println("Analisando o Packet");
		if (packet.tipo == 1) {
			System.out.println("Packet tipo 1, cadastro de atividade");
			cadastraAtividade(packet);
		}
		return packet;
	}
	
	/**
     * Este metodo realiza a validacao dos dados de uma atividade e 
     * executa seu cadastro no banco de dados remoto.
     * 
     * @param packet O pacote recebido do cliente
     * @return boolean true se a atividade foi validada e cadastrada corretamente, caso contrario, false
     */
	private boolean cadastraAtividade(Packet packet) {
		ValidacaoAtividade val = new ValidacaoAtividade();
		Date dataInicio = null;
		Date dataFim = null;
		String emailGerente = null;
		String emailDesenvolvedor = null;
		val.cadastrarAtividade(emailDesenvolvedor, emailGerente, ((Atividade)packet.data).getDescricao(), dataInicio, dataFim);
		System.out.println("Atividade cadastrada com descricao: " + ((Atividade)packet.data).getDescricao());
		return true;
	}
	
}
