package persistencia.interfaces;

import java.sql.Date;
import java.util.ArrayList;

import beans.Problema;

/**
 * 
 * @author Amilcar Jr, Michael Linden
 * Essa interface cont�m m�todos para a administracao de problemas relatados
 * por um desenvolvedor.
 * 
 * �ltima modificacao: 03/09/2008
 */

public interface ServicoProblema {

	/**
	 * Esse m�todo cadastra um novo problema associado a um atividade na base de dados.
	 * @param idAtividade Identificador da atividade.
	 * @param descricao Descricao do problema relatado.
	 * @param dataDoRelato Data em que o problema foi encontrado.
	 * @param mensagem Mensagem a ser exibida a respeito do tipo do problema.
	 * @return true se o problema foi cadastrado com sucesso.
	 */
	public boolean cadastrarProblema(int idAtividade, String descricao,
			Date dataDoRelato, String mensagem);
	
	/**
	 * Esse m�todo atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou n�o.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema(int id, boolean status);
	
	/**
	 * Esse m�todo remove um problema relatado da base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema foi removido da base de dados.
	 */
	public boolean removerProblema(int id);
	
	/**
	 * Esse m�todo recupera uma lista de problemas relatados durante o desenvolvimento
	 * de uma atividade.
	 * @param idAtividade Identificador da atividade
	 * @return ArrayList<Problema>
	 */
	public ArrayList<Problema> listarProblemasDaAtividade(int idAtividade);
	
	/**
	 * Esse m�todo verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 */
	public boolean problemaExiste(int id);
	
	/**
	 * Esse m�todo retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id);
}

