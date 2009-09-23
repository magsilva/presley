package com.hukarz.presley.server.core;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.Permission;

import org.apache.log4j.Logger;

import com.hukarz.presley.server.conhecimento.ConhecimentoImpl;
import com.hukarz.presley.server.mensagem.MensagemProblemaImpl;
import com.hukarz.presley.server.mensagem.MensagemSolucaoImpl;
import com.hukarz.presley.server.mensagem.ValidacaoMensagemImpl;
import com.hukarz.presley.server.usuario.RegistroLogControleVersaoImpl;
import com.hukarz.presley.server.usuario.UsuarioImpl;
import com.hukarz.presley.server.util.CadastroProjetoImpl;


/**
 * Classe principal de testes do servidor
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class StartPresleyServer {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private ConhecimentoImpl		validacaoConhecimento;
	private MensagemProblemaImpl	validacaoProblema; 
	private ValidacaoMensagemImpl	validacaoMensagem;
	private MensagemSolucaoImpl		validacaoSolucao;
	private CadastroProjetoImpl		validacaoProjeto ;
	private UsuarioImpl				validacaoDesenvolvedor ;
	
	private RegistroLogControleVersaoImpl validacaoLogControleVersao ;
	
	public StartPresleyServer() {
		
		this.logger.info("Iniciando Servidor Presley...\n");
		try {
			this.logger.info("Criando Instancia do Servidor...");
			validacaoProblema		= new MensagemProblemaImpl();
			validacaoMensagem		= new ValidacaoMensagemImpl();
			validacaoSolucao		= new MensagemSolucaoImpl();
			validacaoConhecimento	= new ConhecimentoImpl();
			validacaoProjeto		= new CadastroProjetoImpl();
			validacaoDesenvolvedor	= new UsuarioImpl();

			Registry r = LocateRegistry.getRegistry();
			r.bind("MensagemProblema", validacaoProblema);
			r.bind("ValidacaoMensagem", validacaoMensagem);
			r.bind("MensagemSolucao", validacaoSolucao);
			r.bind("Conhecimento", validacaoConhecimento);
			r.bind("CadastroProjeto", validacaoProjeto);
			r.bind("Usuario", validacaoDesenvolvedor);
            
			this.logger.info("Instancia Criada com Sucesso!");
			validacaoLogControleVersao = new RegistroLogControleVersaoImpl();
			validacaoLogControleVersao.registrarLogDoArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String [] args){
		StartPresleyServer server = new StartPresleyServer();
	}
	

}
