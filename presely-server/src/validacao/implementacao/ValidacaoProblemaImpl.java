package validacao.implementacao;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import beans.Conhecimento;
import beans.Problema;
import beans.Solucao;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoProblemaImplDAO;
import persistencia.implementacao.ServicoSolucaoImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoProblema;
import persistencia.interfaces.ServicoSolucao;
import excessao.AtividadeInexistenteException;
import excessao.DescricaoInvalidaException;
import excessao.ProblemaInexistenteException;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de problemas.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoProblemaImpl {
	
	ServicoAtividade servicoAtividade;
	ServicoSolucao servicoSolucao;
	ServicoProblema servicoProblema;
	
	public ValidacaoProblemaImpl() {
		servicoProblema = new ServicoProblemaImplDAO();
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoSolucao = new ServicoSolucaoImplDAO();
	}
	
	/**
	 * Esse m�todo atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou n�o.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema(int id, boolean status) throws ProblemaInexistenteException{
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();
		
		return servicoProblema.atualizarStatusDoProblema(id, status);
	}
	
	/**
	 * Esse m�todo cadastra um novo problema associado a um atividade na base de dados.
	 * @param idAtividade Identificador da atividade.
	 * @param descricao Descricao do problema relatado.
	 * @param dataDoRelato Data em que o problema foi encontrado.
	 * @param mensagem Mensagem a ser exibida a respeito do tipo do problema.
	 * @return true se o problema foi cadastrado com sucesso.
	 */
	public boolean cadastrarProblema(int idAtividade, String descricao,
			Date dataDoRelato, String mensagem, ArrayList<Conhecimento> listaConhecimentos) 
			throws DescricaoInvalidaException, AtividadeInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new AtividadeInexistenteException();
		
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		
		for(Conhecimento c  : listaConhecimentos)
			System.out.println("chamada2 >>>>>>>>>>>>>>>>>>>>>> " + c.getNome());


		return servicoProblema.cadastrarProblema(idAtividade, descricao, dataDoRelato, mensagem, listaConhecimentos);
	}
	
	/**
	 * Esse m�todo retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id) throws ProblemaInexistenteException {
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();

		return servicoProblema.getProblema(id);
	}
	
	/**
	 * Esse m�todo recupera uma lista de problemas relatados durante o desenvolvimento
	 * de uma atividade.
	 * @param idAtividade Identificador da atividade
	 * @return ArrayList<Problema>
	 */
	public ArrayList<Problema> listarProblemasDaAtividade(int idAtividade) throws AtividadeInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new AtividadeInexistenteException();
		
		return servicoProblema.listarProblemasDaAtividade(idAtividade);
	}
	
	/**
	 * Esse m�todo verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 */
	public boolean problemaExiste(int id) {

		return servicoProblema.problemaExiste(id);
	}
	
	/**
	 * Esse m�todo remove um problema relatado da base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema foi removido da base de dados.
	 * @throws ProblemaInexistenteException 
	 */
	public boolean removerProblema(int id) throws ProblemaInexistenteException {
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();
		
		// Remover Solucoes do Problema
		ArrayList<Solucao> solucoes = servicoSolucao.getSolucoesDoProblema(id);
		Iterator<Solucao> it1 = solucoes.iterator();
		
		while (it1.hasNext()) {
			Solucao solucao = it1.next();
			servicoSolucao.removerSolucao(solucao.getId());
		}

		return servicoProblema.removerProblema(id);
	}

	public ArrayList<Problema> getListaProblema() {
		ArrayList<Problema> problemas = servicoProblema.getListaProblemas();
		return problemas;
	}

}
