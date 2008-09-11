package core;

import server.ServerBridge;
import facade.PacketStruct;
import facade.PrincipalSUBJECT;
import beans.Atividade;

/**
 * Classe principal de testes do servidor
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class StartPresleyServer {

	public StartPresleyServer() {
		Object objeto = null;
		PacketStruct packet = new PacketStruct("Dado 1", 1);
		
		try {
			/** Instanciando servidor.
			 * parametro1: "server" ou "client"
			 * parametro2: ip (no caso do servidor vai nulo).
			 * parametro3: porta que o servidor vai escutar.
			 */
			objeto = PrincipalSUBJECT.getInstance("server", null, 1099);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* instanciando server bridge para comunicação do servidor com classe externa. */
		ServerBridge trocaMsg = new ServerBridgeImp();
		
		/* setando serverBridge no servidor. */ 
		PrincipalSUBJECT.facade(1099, trocaMsg);
		
		trocaMsg.sendToServer(packet);
	}
	
	public static void main(String[] args) {
		System.out.println("Startou o servidor!");
		StartPresleyServer server = new StartPresleyServer();
		
	}

}
