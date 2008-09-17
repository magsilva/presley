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
 * Última modificacao: 03/09/2008 por Amilcar Jr
 */

public interface ServicoDesenvolvedor {

	/**
	 * Esse método adiciona um novo desenvolvedor na base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param localidade Local onde o desenvolvedor reside.
	 * @return true se o desenvolvedor foi criado com sucesso.
	 */
	public boolean criarDesenvolvedor(String email, String nome, String localidade);
	
	/**
	 * Esse método remove um desenvolvedor da base de dados. 
	 * @param email Email do desenvolvedor.
	 * @return true se o desenvolvedor foi removido com sucesso.
	 */
	public boolean removerDesenvolvedor(String email);
	
	/**
	 * Esse método atualiza os dados de um desenvolvedor previamente cadastrado na
	 * base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param novoEmail novo email do desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param localidade Local onde o desenvolvedor reside.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarDesenvolvedor(String email, String novoEmail, String nome, String localidade);
	
	/**
	 * Esse método verifica se um dado desenvolvedor está cadastrado no sistema.
	 * @param email Email do desenvolvedor
	 * @return true se o desenvolvedro estiver cadastrado no sistema.
	 */
	public boolean desenvolvedorExiste(String email);
	
	/**
	 * Esse método adiciona um conhecimento previamente cadastrado a um desenvolvedor.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser adicionado.
	 * @return true se o conhecimento foi adicionado ao desenvolvedor.
	 */
	public boolean adicionarConhecimentoAoDesenvolvedor(String emailDesenvolvedor,
			String nomeConhecimento);
	
	/**
	 * Esse método remove um conhecimento associado a um desenvolvedor específico.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser removido do desenvolvedor.
	 * @return true se a associacao foi desfeita.
	 */
	public boolean removerConhecimentoDoDesenvolvedor(String emailDesenvolvedor,
			String nomeConhecimento);
	
	/**
	 * Este método retorna o desenvolvedor que possui o email passado no parametro.
	 * @param email Email do desenvolvedor.
	 * @return <Desenvolvedor> 
	 */
	public Desenvolvedor getDesenvolvedor (String email);

	/**
	 * Esse método retorna uma lista de conhecimentos que o desenvolvedor possui
	 * @param email Email do desenvolvedor
	 * @return ArrayList<Conhecimento>
	 */
	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email);
	
	/**
	 * Esse método retorna uma lista de atividades atribuidas a um desenvolvedor
	 * @param email Email do desenvolvedor
	 * @return ArrayList<TipoAtividade>
	 */
	public ArrayList<TipoAtividade> getAtividadesDoDesenvolvedor(String email);

	/**
	 * Esse método verifica se existe relacao entre um conhecimento e um desenvolvedor/
	 * @param emailDesenvolvedor Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a associacao existe.
	 */
	public boolean conhecimentoDoDesenvolvedorExiste(String emailDesenvolvedor, String nomeConhecimento);

	/**
	 * Esse método retorna uma lista com todos os desenvolvedores cadastrados;
	 * @return ArrayList<Desenvolvedor> lista de todos os desenvolvedores
	 */
	public ArrayList<Desenvolvedor> getTodosDesenvolvedores();
	
	/**
	 * Esse método retorna a quantidade de respostas que um desenvolvedor tem em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return quantidade de respostas.
	 */
	public int getQntResposta(String email, String conhecimento);
	
	/**
	 * Esse método altera a quantidade de respostas que um desenvolvedor possui em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a atualização foi feita com sucesso.
	 */
	public boolean updateQntResposta(String email, String conhecimento, int quantidade);
	
}
