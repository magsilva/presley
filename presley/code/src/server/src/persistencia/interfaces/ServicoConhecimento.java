package persistencia.interfaces;

import beans.Conhecimento;

/**
 * 
 * @author Amilcar Jr
 * Esta interface possui operacoes para o gerenciamento de conhecimentos envolvidos
 * em uma atividade.
 * 
 * Última modificacao: 03/09/2008 por Amilcar Jr
 */

public interface ServicoConhecimento {

	/**
	 * Este método cria um novo conhecimento na base de dados
	 * @param nome Nome do novo conhecimento
	 * @param descricao Descricao do novo conhecimento
	 * @return true se o conhecimento foi inserido na base de dados.
	 */
	public boolean criarConhecimento(String nome, String descricao);
	
	/**
	 * Este método remove um conhecimento previamente cadastrado. 
	 * @param nome Nome do conhecimento a ser removido.
	 * @return true se o conhecimento foi removido com sucesso
	 */
	public boolean removerConhecimento(String nome);
	
	/**
	 * Este método verifica se um conhecimento existe na base de dados.
	 * @param nome Nome do conhecimento para verificacao.
	 * @return true se o conhecimento existe.
	 */
	public boolean conhecimentoExiste(String nome);
	
	/**
	 * Este método atualiza um conhecimento previamente cadastrado na base da dados 
	 * @param nome Nome do conhecimento a ser atualizado.
	 * @param novoNome Novo nome do conhecimento.
	 * @param descricao Nova descricao do conhecimento.
	 * @return true se o conhecimento foi atualizado.
	 */
	public boolean atualizarConhecimento(String nome, String novoNome, String descricao);
	
	/**
	 * Esse método retorna um objeto do tipo conhecimento que possui o nome
	 * passado por parametro.
	 * @param nome Nome do conhecimento a ser retornado.
	 * @return <Conhecimento>
	 */
	public Conhecimento getConhecimento(String nome);
	
}
