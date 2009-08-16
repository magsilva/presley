package com.hukarz.presley.server.validacao.implementacao;

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
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoLogControleVersaoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoLogControleVersaoLine10ImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoLogControleVersao;


public class ValidacaoArquivoImpl {
	ServicoArquivo servicoArquivo;
	ServicoDesenvolvedor servicoDesenvolvedor;
	private Logger logger = Logger.getLogger(this.getClass());

	public ValidacaoArquivoImpl() {
		servicoArquivo = new ServicoArquivoImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}

	/**
	 * Este metodo atualiza um conhecimento previamente cadastrado na base da dados 
	 * @param nome Nome do conhecimento a ser atualizado.
	 * @param novoNome Novo nome do conhecimento.
	 * @param descricao Nova descricao do conhecimento.
	 * @return true se o conhecimento foi atualizado.
	 * @throws NomeInvalidoException 
	 */
	public boolean atualizarArquivo(Arquivo arquivoAnterior, Arquivo arquivoNovo) throws ArquivoInexistenteException {
		if (!servicoArquivo.arquivoExiste(arquivoAnterior)) throw new ArquivoInexistenteException();
		
		return servicoArquivo.atualizarArquivo(arquivoAnterior, arquivoNovo);
	}
	
	/**
	 * Este mtodo verifica se um conhecimento existe na base de dados.
	 * @parame nome Nome do conhecimento para verificacao.
	 * @return true se o conhecimento existe.
	 */
	public boolean arquivoExiste(Arquivo arquivo) {
		
		return servicoArquivo.arquivoExiste(arquivo);
	}
	
	/**
	 * Este metodo cria um novo conhecimento na base de dados
	 * @param nome Nome do novo conhecimento
	 * @param descricao Descricao do novo conhecimento
	 * @return true se o conhecimento foi inserido na base de dados.
	 */
	public boolean criarArquivo(Arquivo arquivo) throws ArquivoInexistenteException {
		
		// if (!ValidacaoUtil.validaNome(nome)) throw new NomeInvalidoException();
		
		if (servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
		
		return servicoArquivo.criarArquivo(arquivo);
		
	}
	
	/**
	 * Esse metodo retorna um objeto do tipo conhecimento que possui o nome
	 * passado por parametro.
	 * @param nome Nome do conhecimento a ser retornado.
	 * @return <Conhecimento>
	 */
	public Arquivo getArquivo(Arquivo arquivo) throws ArquivoInexistenteException {
		Arquivo arquivoRetorno = servicoArquivo.getArquivo(arquivo);
		if (arquivoRetorno == null) throw new ArquivoInexistenteException();
		
		return arquivoRetorno;
	}
	
	/**
	 * Este metodo remove um conhecimento previamente cadastrado. 
	 * @param nome Nome do conhecimento a ser removido.
	 * @return true se o conhecimento foi removido com sucesso
	 */
	public boolean removerArquivo(Arquivo arquivo) throws ArquivoInexistenteException {
		if (!servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
				
		return servicoArquivo.removerArquivo(arquivo);
	}

	/**
	 * Cria uma lista com os Desenvolvedores de cada arquivo java	
	 * @param problema
	 * @return
	 */	
	public Map<ArquivoJava, ArrayList<Desenvolvedor>> getDesenvolvedoresArquivos(Problema problema){
		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = new HashMap<ArquivoJava, ArrayList<Desenvolvedor>>();
		ServicoLogControleVersao servicoLogControleVersao = new ServicoLogControleVersaoImplDAO();
		
		cadastrarArquivosProblema(problema);

		Collection<ArquivoJava> listaArquivo = problema.getClassesRelacionadas().values();

		for (ArquivoJava arquivo : listaArquivo) {
			ArrayList<Desenvolvedor> desenvolvedores = servicoLogControleVersao.getDesenvolvedoresArquivo(arquivo, problema.getData()); 
			
			arquivoDesenvolvedores.put((ArquivoJava) arquivo, desenvolvedores);
		}

		return arquivoDesenvolvedores;
	}
	
	private void cadastrarArquivosProblema(Problema problema) {
		// Cadastra as classes envolvidas no problema
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
