package com.hukarz.presley.server.core;


import java.io.IOException;
import org.apache.log4j.Logger;

import com.hukarz.presley.communication.facade.PrincipalSUBJECT;
import com.hukarz.presley.communication.server.ServerBridge;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoLogControleVersaoImpl;

/**
 * Classe principal de testes do servidor
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class StartPresleyServer {
	
	private ValidacaoLogControleVersaoImpl validacaoLogControleVersao = new ValidacaoLogControleVersaoImpl();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public StartPresleyServer(PresleyProperties properties) {
		
		int port = Integer.parseInt(properties.getProperty("rmi.port"));
		
		try {
			this.logger.info("Criando Instancia do Servidor...");
			// PrincipalSUBJECT.getInstance("server", null, 1099);
			PrincipalSUBJECT.getInstance(PrincipalSUBJECT.Mode.server, port);
			this.logger.info("Instancia Criada com Sucesso!");
			validacaoLogControleVersao.registrarLogDoArquivo();
			
			/* instanciando server bridge para comunicação do servidor com classe externa. */
			this.logger.info("Instanciando do Bridge...");
			ServerBridge trocaMsg = new ServerBridgeImp();
			this.logger.info("Bridge Criada!");
			
			/* setando serverBridge no servidor. */ 
			// PrincipalSUBJECT.facade(1099, trocaMsg);
			PrincipalSUBJECT.facade(PrincipalSUBJECT.RMI_PORT, trocaMsg);
			this.logger.info("Servidor Iniciado Com Sucesso!");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String [] args) throws IOException {
		PresleyProperties properties = PresleyProperties.getInstance();
		StartPresleyServer server = new StartPresleyServer(properties);
	}
}
