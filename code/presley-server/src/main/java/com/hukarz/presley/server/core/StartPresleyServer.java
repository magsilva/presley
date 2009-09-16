package com.hukarz.presley.server.core;


import org.apache.log4j.Logger;

import com.hukarz.presley.communication.facade.PrincipalSUBJECT;
import com.hukarz.presley.communication.server.ServerBridge;
import com.hukarz.presley.server.usuario.ValidacaoLogControleVersaoImpl;


/**
 * Classe principal de testes do servidor
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class StartPresleyServer {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private ValidacaoLogControleVersaoImpl validacaoLogControleVersao = new ValidacaoLogControleVersaoImpl();
	
	public StartPresleyServer() {
		
		this.logger.info("Iniciando Servidor Presley...\n");
		try {
			this.logger.info("Criando Instancia do Servidor...");
			PrincipalSUBJECT.getInstance("server", null, 1099);
			this.logger.info("Instancia Criada com Sucesso!");
			validacaoLogControleVersao.registrarLogDoArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* instanciando server bridge para comunicação do servidor com classe externa. */
		this.logger.info("Instanciando do Bridge...");
		ServerBridge trocaMsg = new ServerBridgeImp();
		this.logger.info("Bridge Criada!");

		/* setando serverBridge no servidor. */ 
		PrincipalSUBJECT.facade(1099, trocaMsg);
		this.logger.info("Servidor Iniciado Com Sucesso!");
	}
	public static void main(String [] args){
		StartPresleyServer server = new StartPresleyServer();
	}
	

}
