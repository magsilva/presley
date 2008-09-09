package core;

import beans.Atividade;

import comunicacao.Comunicacao;
import comunicacao.Packet;

/**
 * Classe principal de testes do servidor
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class StartPresleyServer {

	public static void main(String[] args) {
		System.out.println("Startou o servidor!");
		Comunicacao com = new Comunicacao();
		Packet packet = new Packet();
		packet.data = new Atividade();
		((Atividade) packet.data).setDescricao("Descricao da atividade 1");
		System.out.println("Atividade iniciada com descricao: " + ((Atividade) packet.data).getDescricao());
		com.sendToServer(packet);
	}

}
