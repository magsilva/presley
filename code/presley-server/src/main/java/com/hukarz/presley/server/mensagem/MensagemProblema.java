package com.hukarz.presley.server.mensagem;

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
import com.hukarz.presley.server.inferencia.identificador.Identificador;
import com.hukarz.presley.server.inferencia.recomendador.Recomendador;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoMensagemImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoMensagem;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;
import com.hukarz.presley.server.util.Util;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de problemas.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class MensagemProblema {
	
	ServicoSolucao  servicoSolucao;
	ServicoProblema servicoProblema;
	ServicoArquivo  servicoArquivo;
	ServicoMensagem servicoMensagem;
	ServicoProjeto  servicoProjeto;
	
	MensagemArquivo validacaoArquivo ;
	
	Problema problema ;
	Desenvolvedor desenvolvedor;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public MensagemProblema() {
		servicoProblema = new ServicoProblemaImplDAO();
		servicoSolucao  = new ServicoSolucaoImplDAO();
		servicoArquivo  = new ServicoArquivoImplDAO();
		servicoMensagem = new ServicoMensagemImplDAO();
		servicoProjeto	= new ServicoProjetoImplDAO();
		
		validacaoArquivo = new MensagemArquivo();
	}
	
	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}

	/**
	 * Esse método atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou não.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema() throws ProblemaInexistenteException{
		
		if (problema == null) throw new ProblemaInexistenteException();
		if (!servicoProblema.problemaExiste(problema.getId())) throw new ProblemaInexistenteException();
		
		return servicoProblema.atualizarStatusDoProblema(problema.getId(), problema.isResolvido());
	}
	
	/**
	 * Esse método cadastra um novo problema na base de dados.
	 * @param idAtividade Identificador da atividade.
	 * @param descricao Descricao do problema relatado.
	 * @param dataDoRelato Data em que o problema foi encontrado.
	 * @param mensagem Mensagem a ser exibida a respeito do tipo do problema.
	 * @return true se o problema foi cadastrado com sucesso.
	 * @throws IOException 
	 * @throws ProjetoInexistenteException 
	 * @throws ConhecimentoNaoEncontradoException 
	 * @throws ProblemaInexistenteException 
	 */
	public Problema cadastrarProblema() 
		throws DescricaoInvalidaException, IOException, ProjetoInexistenteException, 
		ConhecimentoNaoEncontradoException, ProblemaInexistenteException {

		if (problema == null) throw new ProblemaInexistenteException();
		if (!Util.validaDescricao( problema.getDescricao() )) throw new DescricaoInvalidaException();
		if (!servicoProjeto.projetoExiste( problema.getProjeto() )) throw new ProjetoInexistenteException();

		this.logger.debug("Validação");

		// Cria uma lista com os Desenvolvedores de cada arquivo java		
		validacaoArquivo.setProblema(problema);
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = validacaoArquivo.getDesenvolvedoresArquivos();
		
		this.logger.debug("Lista com os Desenvolvedores de cada arquivo"); 

		// Identifica o conhecimeto do problema a se cadastrar
		Identificador processaSimilaridade = new Identificador();
	
		StringBuilder comentariosCodigo = new StringBuilder();
		
		for (Iterator<ArquivoJava> it = problema.getClassesRelacionadas().values().iterator(); it.hasNext();) {  
			ArquivoJava arquivoJava = it.next();  
			try {
				comentariosCodigo.append(arquivoJava.getTexto() +" ");
			} catch (IOException e) {
				//e.printStackTrace();
			}			
		}
		
		processaSimilaridade.setTexto( problema.getDescricao() + "  " + problema.getMensagem() + " " + comentariosCodigo);
		problema.setConhecimento( processaSimilaridade.verificaConhecimentoDoTexto( ) ) ;
		
/*
		Conhecimento conhecimento = new Conhecimento();
		conhecimento.setNome("Core");
		problema.setConhecimento( conhecimento );
*/
		this.logger.debug("Conhecimeto do problema");
	
		problema = servicoProblema.cadastrarProblema(problema) ;

		this.logger.debug("Cadastro do Problema");
		
		if (problema.isTemResposta()){
			// Retorna os desenvolvedores que receberão o problema
			Recomendador inferencia = new Recomendador();
			inferencia.setArquivoDesenvolvedores(arquivoDesenvolvedores);
			inferencia.setProblema(problema);
			ArrayList<Desenvolvedor> desenvolvedores = inferencia.getDesenvolvedores();
			
			servicoMensagem.adicionarMensagem(desenvolvedores, problema);
			
			this.logger.debug("Mensagem Adcionada");
		}

		return problema;
	}

	/**
	 * Esse método retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id) throws ProblemaInexistenteException {
		if (problema == null) throw new ProblemaInexistenteException();
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();
		
		problema = servicoProblema.getProblema(id);
		
		return problema; 
	}
	
	/**
	 * Esse método verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 * @throws ProblemaInexistenteException 
	 */
	public boolean problemaExiste() throws ProblemaInexistenteException {
		if (problema == null) throw new ProblemaInexistenteException();
		return servicoProblema.problemaExiste(problema.getId());
	}
	
	/**
	 * Esse método remove um problema relatado da base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema foi removido da base de dados.
	 * @throws ProblemaInexistenteException 
	 */
	public boolean removerProblema() throws ProblemaInexistenteException {
		if (problema == null) throw new ProblemaInexistenteException();
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

	public ArrayList<Problema> getListaProblema() {
		ArrayList<Problema> problemas = servicoProblema.getListaProblemas(desenvolvedor);
		return problemas;
	}
	
	public ArrayList<String> getConhecimentosAssociados() throws ProblemaInexistenteException {		
		if (problema == null) throw new ProblemaInexistenteException();
		ArrayList<String> problemas = servicoProblema.getConhecimentosAssociados(problema.getDescricao());
		return problemas;
	}

}
