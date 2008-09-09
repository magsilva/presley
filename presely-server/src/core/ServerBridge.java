package core;

import comunicacao.Packet;

/**
 * Esta interface relaciona um servidor com uma determinada
 * api de comunicacao. Essa interface deverah ser provida 
 * pela api de comunicacao.
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public interface ServerBridge {

	 /**
     * Este metodo recebe o pacote no servidor e o encaminha 
     * de forma adequada. Deve ser implementado no servidor.
     * 
     * @param packet O pacote recebido
     * @return Packet o pacote resposta para o cliente
     */
	public Packet sendPacket(Packet packet);
	
}
