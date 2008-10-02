package persistencia.interfaces;

import java.sql.Date;
import java.util.ArrayList;

import beans.Conhecimento;
import beans.Problema;

/**
 * 
 * @author Amilcar Jr, Michael Linden
 * Essa interface contém métodos para a administracao de problemas relatados
 * por um desenvolvedor.
 * 
 * Última modificacao: 03/09/2008
 */

public interface ServicoProblema {

	/**
	 * Esse método cadastra um novo problema associado a um atividade na base de dados.
	 * @param idAtividade Identificador da atividade.
	 * @param descricao Descricao do problema relatado.
	 * @param dataDoRelato Data em que o problema foi encontrado.
	 * @param mensagem Mensagem a ser exibida a respeito do tipo do problema.
	 * @return true se o problema foi cadastrado com sucesso.
	 */
	public boolean cadastrarProblema(int idAtividade, String descricao,
			Date dataDoRelato, String mensagem, ArrayList<Conhecimento> conhecimento);
	
	/**
	 * Esse método atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou não.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema(int id, boolean status);
	
	/**
	 * Esse método remove um problema relatado da base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema foi removido da base de dados.
	 */
	public boolean removerProblema(int id);
	
	/**
	 * Esse método recupera uma lista de problemas relatados durante o desenvolvimento
	 * de uma atividade.
	 * @param idAtividade Identificador da atividade
	 * @return ArrayList<Problema>
	 */
	public ArrayList<Problema> listarProblemasDaAtividade(int idAtividade);
	
	/**
	 * Esse método verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 */
	public boolean problemaExiste(int id);
	
	/**
	 * Esse método retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id);

	/**
	 * Esse método retorna o todos os problemas cadastrados no banco
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public ArrayList<Problema> getListaProblemas();
	
	/**
	 * Esse método retorna todos os conhecimentos associados ao problema
	 * @param nomeProblema nome do problema.
	 * @return <String>
	 */	
	public ArrayList<String> getConhecimentosAssociados(String nomeProblema);
}

