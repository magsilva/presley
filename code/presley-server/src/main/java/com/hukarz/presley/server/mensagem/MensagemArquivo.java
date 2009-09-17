package com.hukarz.presley.server.mensagem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.ArquivoInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoLogControleVersaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoLogControleVersao;


public class MensagemArquivo {
	ServicoArquivo servicoArquivo;
	ServicoDesenvolvedor servicoDesenvolvedor;
	Arquivo arquivo;
	Problema problema;
	
	private Logger logger = Logger.getLogger(this.getClass());

	public MensagemArquivo() {
		servicoArquivo = new ServicoArquivoImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	/**
	 * Este metodo atualiza um Arquivo previamente cadastrado na base da dados 
	 * @param arquivoAnterior
	 * @param arquivoNovo
	 * @return true se o Arquivo foi atualizado.
	 * @throws ArquivoInexistenteException
	 */
	public boolean atualizarArquivo(Arquivo arquivoNovo) throws ArquivoInexistenteException {
		if (arquivo == null) throw new ArquivoInexistenteException();
		if (!servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
		
		return servicoArquivo.atualizarArquivo(arquivo, arquivoNovo);
	}
	
	/**
	 * Este mtodo verifica se um Arquivo existe na base de dados.
	 * @parame nome Nome do conhecimento para verificacao.
	 * @return true se o conhecimento existe.
	 * @throws ArquivoInexistenteException 
	 */
	public boolean arquivoExiste() throws ArquivoInexistenteException {
		if (arquivo == null) throw new ArquivoInexistenteException();
		return servicoArquivo.arquivoExiste(arquivo);
	}
	
	/**
	 * Este metodo cria um novo Arquivo na base de dados
	 * @param arquivo
	 * @return true se o Arquivo foi inserido na base de dados.
	 * @throws ArquivoInexistenteException
	 */
	public boolean criarArquivo() throws ArquivoInexistenteException {
		// if (!ValidacaoUtil.validaNome(nome)) throw new NomeInvalidoException();
		
		if (arquivo == null) throw new ArquivoInexistenteException();
		if (servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
		
		return servicoArquivo.criarArquivo(arquivo);
		
	}
	
	/**
	 * @param arquivo
	 * @return
	 * @throws ArquivoInexistenteException
	 */
	public Arquivo getArquivo() throws ArquivoInexistenteException {
		if (arquivo == null) throw new ArquivoInexistenteException();
		arquivo = servicoArquivo.getArquivo(arquivo);
		if (arquivo == null) throw new ArquivoInexistenteException();
		
		return arquivo;
	}
	
	/**
	 * Este metodo remove um Arquivo previamente cadastrado. 
	 * @param arquivo Arquivo a ser removido.
	 * @return true se o Arquivo foi removido com sucesso
	 */
	public boolean removerArquivo() throws ArquivoInexistenteException {
		if (arquivo == null) throw new ArquivoInexistenteException();
		if (!servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
				
		return servicoArquivo.removerArquivo(arquivo);
	}

	/**
	 * Cria uma lista com os Desenvolvedores de cada arquivo java	
	 * @param problema
	 * @return
	 * @throws ProblemaInexistenteException 
	 */	
	public Map<ArquivoJava, ArrayList<Desenvolvedor>> getDesenvolvedoresArquivos() throws ProblemaInexistenteException{
		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		if (problema == null) throw new ProblemaInexistenteException();
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = new HashMap<ArquivoJava, ArrayList<Desenvolvedor>>();
		ServicoLogControleVersao servicoLogControleVersao = new ServicoLogControleVersaoImplDAO();
		
		cadastrarArquivosProblema();

		Collection<ArquivoJava> listaArquivo = problema.getClassesRelacionadas().values();

		for (ArquivoJava arquivo : listaArquivo) {
			ArrayList<Desenvolvedor> desenvolvedores = servicoLogControleVersao.getDesenvolvedoresArquivo(arquivo, problema.getData()); 
			
			arquivoDesenvolvedores.put((ArquivoJava) arquivo, desenvolvedores);
		}

		return arquivoDesenvolvedores;
	}
	
	private void cadastrarArquivosProblema() throws ProblemaInexistenteException {
		// Cadastra as classes envolvidas no problema
		if (problema == null) throw new ProblemaInexistenteException();
		Map<ClasseJava, ArquivoJava> arquivos = problema.getClassesRelacionadas();
		for (Iterator<ClasseJava> it = arquivos.keySet().iterator(); it.hasNext();) {  
			ClasseJava classe = it.next();  
			ArquivoJava arquivoJava = arquivos.get(classe);  
			
			if (!servicoArquivo.arquivoExiste(arquivoJava)){
				servicoArquivo.criarArquivo(arquivoJava);
			}
			arquivoJava.setId( servicoArquivo.getArquivo(arquivoJava).getId() );
		}
				
	}
}
