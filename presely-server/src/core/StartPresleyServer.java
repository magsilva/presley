package core;

import server.ServerBridge;
import facade.PrincipalSUBJECT;

/**
 * Classe principal de testes do servidor
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class StartPresleyServer {

	public StartPresleyServer() {
		
		System.out.println("Iniciando Servidor Presley...\n");
		try {
			/** Instanciando servidor.
			 * parametro1: "server" ou "client"
			 * parametro2: ip (no caso do servidor vai nulo).
			 * parametro3: porta que o servidor vai escutar.
			 */
			System.out.println("   Criando Instancia do Servidor...");
			PrincipalSUBJECT.getInstance("server", null, 1099);
			System.out.println("   ...Instancia Criada com Sucesso!\n");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* instanciando server bridge para comunicação do servidor com classe externa. */
		System.out.println("   Instanciando do Bridge...");
		ServerBridge trocaMsg = new ServerBridgeImp();
		System.out.println("   ...Bridge Criada!");

		/* setando serverBridge no servidor. */ 
		PrincipalSUBJECT.facade(1099, trocaMsg);
		System.out.println("\n...Servidor Iniciado Com Sucesso!!");
		
		
	}
	
	public static void main(String [] args){
		@SuppressWarnings("unused")
		StartPresleyServer server = new StartPresleyServer();
	}
	

}
