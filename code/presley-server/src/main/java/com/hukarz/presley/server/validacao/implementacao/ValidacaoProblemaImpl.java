package com.hukarz.presley.server.validacao.implementacao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.server.inferencia.Inference;
import com.hukarz.presley.server.inferencia.InferenceFactory;
import com.hukarz.presley.server.persistencia.implementacao.ServicoMensagemImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoMensagem;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;
import com.hukarz.presley.server.processaTexto.ProcessaSimilaridade;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de problemas.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoProblemaImpl {
	
	ServicoSolucao  servicoSolucao;
	ServicoProblema servicoProblema;
	ServicoMensagem servicoMensagem;
	ServicoProjeto  servicoProjeto;
	
	ValidacaoArquivoImpl validacaoArquivo ;
	
	public ValidacaoProblemaImpl() {
		servicoProblema = new ServicoProblemaImplDAO();
		servicoSolucao  = new ServicoSolucaoImplDAO();
		servicoMensagem = new ServicoMensagemImplDAO();
		servicoProjeto	= new ServicoProjetoImplDAO();
		
		validacaoArquivo = new ValidacaoArquivoImpl();
	}
	
	/**
	 * Esse método atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou não.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema(int id, boolean status) throws ProblemaInexistenteException{
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();
		
		return servicoProblema.atualizarStatusDoProblema(id, status);
	}
	
	public Problema cadastrarProblema(Problema problema) 
	throws DescricaoInvalidaException, IOException, ProjetoInexistenteException, ConhecimentoNaoEncontradoException {

		if (!ValidacaoUtil.validaDescricao( problema.getDescricao() )) throw new DescricaoInvalidaException();

		if (!servicoProjeto.projetoExiste( problema.getProjeto() )) throw new ProjetoInexistenteException();


		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = validacaoArquivo.getDesenvolvedoresArquivos(problema);
		

		// Identifica o conhecimeto do problema a se cadastrar
		ProcessaSimilaridade processaSimilaridade = new ProcessaSimilaridade();
	
		StringBuilder comentariosCodigo = new StringBuilder();
		
		for (Iterator<ArquivoJava> it = problema.getClassesRelacionadas().values().iterator(); it.hasNext();) {  
			ArquivoJava arquivoJava = it.next();  
			try {
				comentariosCodigo.append(arquivoJava.getTexto() +" ");
			} catch (IOException e) {
				//e.printStackTrace();
			}			
		}
				
		String texto = problema.getDescricao() + "  " + problema.getMensagem() + " " + comentariosCodigo;
		problema.setConhecimento(processaSimilaridade.verificaConhecimentoDoTexto(texto)) ;
		problema = servicoProblema.cadastrarProblema(problema);
		
		if (problema.isTemResposta()) {
			Inference inferencia = InferenceFactory.createInstance();
			ArrayList<Desenvolvedor> desenvolvedores = inferencia.getDesenvolvedores(arquivoDesenvolvedores, 
					problema);
			servicoMensagem.adicionarMensagem(desenvolvedores, problema);
		}

		return problema;
	}

	/**
	 * Esse método retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id) throws ProblemaInexistenteException {
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();

		return servicoProblema.getProblema(id);
	}
	
	/**
	 * Esse método verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 */
	public boolean problemaExiste(int id) {

		return servicoProblema.problemaExiste(id);
	}
	
	/**
	 * Esse método remove um problema relatado da base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema foi removido da base de dados.
	 * @throws ProblemaInexistenteException 
	 */
	public boolean removerProblema(Problema problema) throws ProblemaInexistenteException {
		if (!servicoProblema.problemaExiste(problema.getId())) throw new ProblemaInexistenteException();
		
		// Remover Solucoes do Problema
		ArrayList<Solucao> solucoes = servicoSolucao.getSolucoesDoProblema(problema);
		Iterator<Solucao> it1 = solucoes.iterator();
		
		while (it1.hasNext()) {
			Solucao solucao = it1.next();
			servicoSolucao.removerSolucao(solucao.getId());
		}

		return servicoProblema.removerProblema(problema);
	}

	public ArrayList<Problema> getListaProblema(Desenvolvedor desenvolvedor) {
		ArrayList<Problema> problemas = servicoProblema.getListaProblemas(desenvolvedor);
		return problemas;
	}
	
	public ArrayList<String> getConhecimentosAssociados(String nomeDoProblema) {		
		ArrayList<String> problemas = servicoProblema.getConhecimentosAssociados(nomeDoProblema);
		return problemas;
	}

}
