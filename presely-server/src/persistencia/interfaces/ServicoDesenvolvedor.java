package persistencia.interfaces;

import java.util.ArrayList;

import beans.TipoAtividade;
import beans.Conhecimento;
import beans.Desenvolvedor;

/**
 * 
 * @author Amilcar Jr
 * Esse interface possui operacoes para a manipulacao de informacoes sobre um desenvolvedor.
 * 
 * �ltima modificacao: 03/09/2008 por Amilcar Jr
 */

public interface ServicoDesenvolvedor {

	/**
	 * Esse m�todo adiciona um novo desenvolvedor na base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param localidade Local onde o desenvolvedor reside.
	 * @return true se o desenvolvedor foi criado com sucesso.
	 */
	public boolean criarDesenvolvedor(String email, String nome, String localidade);
	
	/**
	 * Esse m�todo remove um desenvolvedor da base de dados. 
	 * @param email Email do desenvolvedor.
	 * @return true se o desenvolvedor foi removido com sucesso.
	 */
	public boolean removerDesenvolvedor(String email);
	
	/**
	 * Esse m�todo atualiza os dados de um desenvolvedor previamente cadastrado na
	 * base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param novoEmail novo email do desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param localidade Local onde o desenvolvedor reside.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarDesenvolvedor(String email, String novoEmail, String nome, String localidade);
	
	/**
	 * Esse m�todo verifica se um dado desenvolvedor est� cadastrado no sistema.
	 * @param email Email do desenvolvedor
	 * @return true se o desenvolvedro estiver cadastrado no sistema.
	 */
	public boolean desenvolvedorExiste(String email);
	
	/**
	 * Esse m�todo adiciona um conhecimento previamente cadastrado a um desenvolvedor.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser adicionado.
	 * @return true se o conhecimento foi adicionado ao desenvolvedor.
	 */
	public boolean adicionarConhecimentoAoDesenvolvedor(String emailDesenvolvedor,
			String nomeConhecimento);
	
	/**
	 * Esse m�todo remove um conhecimento associado a um desenvolvedor espec�fico.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser removido do desenvolvedor.
	 * @return true se a associacao foi desfeita.
	 */
	public boolean removerConhecimentoDoDesenvolvedor(String emailDesenvolvedor,
			String nomeConhecimento);
	
	/**
	 * Este m�todo retorna o desenvolvedor que possui o email passado no parametro.
	 * @param email Email do desenvolvedor.
	 * @return <Desenvolvedor> 
	 */
	public Desenvolvedor getDesenvolvedor (String email);

	/**
	 * Esse m�todo retorna uma lista de conhecimentos que o desenvolvedor possui
	 * @param email Email do desenvolvedor
	 * @return ArrayList<Conhecimento>
	 */
	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email);
	
	/**
	 * Esse m�todo retorna uma lista de atividades atribuidas a um desenvolvedor
	 * @param email Email do desenvolvedor
	 * @return ArrayList<TipoAtividade>
	 */
	public ArrayList<TipoAtividade> getAtividadesDoDesenvolvedor(String email);

	/**
	 * Esse m�todo verifica se existe relacao entre um conhecimento e um desenvolvedor/
	 * @param emailDesenvolvedor Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a associacao existe.
	 */
	public boolean conhecimentoDoDesenvolvedorExiste(String emailDesenvolvedor, String nomeConhecimento);

	/**
	 * Esse m�todo retorna uma lista com todos os desenvolvedores cadastrados;
	 * @return ArrayList<Desenvolvedor> lista de todos os desenvolvedores
	 */
	public ArrayList<Desenvolvedor> getTodosDesenvolvedores();
	
	/**
	 * Esse m�todo retorna a quantidade de respostas que um desenvolvedor tem em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return quantidade de respostas.
	 */
	public int getQntResposta(String email, String conhecimento);
	
	/**
	 * Esse m�todo altera a quantidade de respostas que um desenvolvedor possui em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a atualiza��o foi feita com sucesso.
	 */
	public boolean updateQntResposta(String email, String conhecimento, int quantidade);
	
}
