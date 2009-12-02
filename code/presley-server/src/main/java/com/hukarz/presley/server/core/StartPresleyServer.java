package com.hukarz.presley.server.core;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.hukarz.presley.communication.facade.PrincipalSUBJECT;
import com.hukarz.presley.communication.server.ServerBridge;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoLogControleVersaoImpl;


public class StartPresleyServer {
	
	private ValidacaoLogControleVersaoImpl validacaoLogControleVersao = new ValidacaoLogControleVersaoImpl();
	private static Properties presleyProperties;
	
	public StartPresleyServer(PresleyProperties properties) {
		
		int port = Integer.parseInt(properties.getProperty("rmi.port"));
		
		try {
			PrincipalSUBJECT.getInstance(PrincipalSUBJECT.Mode.server, port);
			validacaoLogControleVersao.registrarLogDoArquivo();
			ServerBridge trocaMsg = new ServerBridgeImp();
			PrincipalSUBJECT.facade(PrincipalSUBJECT.RMI_PORT, trocaMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String [] args) throws IOException {
		PresleyProperties properties = PresleyProperties.getInstance();
		StartPresleyServer server = new StartPresleyServer(properties);
	}
	

}
