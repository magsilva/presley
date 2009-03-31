package com.hukarz.presley.server.persistencia.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TipoAtividade;


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
	 * @param cvsNome Local onde o desenvolvedor reside.
	 * @return true se o desenvolvedor foi criado com sucesso.
	 */
	public boolean criarDesenvolvedor(String email, String nome, String cvsNome, String senha);
	
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
	 * @param cvsNome Local onde o desenvolvedor reside.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarDesenvolvedor(String email, String novoEmail, String nome, String cvsNome,
			String senha);
	
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
			String nomeConhecimento, double grau, int qntResposta);
	
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
	 * Este método retorna o desenvolvedor que possui o Identificador de CVS
	 * passado como parametro.
	 * @param email Email do desenvolvedor.
	 * @return <Desenvolvedor> 
	 */
	public Desenvolvedor getDesenvolvedorCVS(String cvsNome);
	
	/**
	 * Esse método retorna uma lista de conhecimentos que o desenvolvedor possui
	 * @param email Email do desenvolvedor
	 * @return ArrayList<Conhecimento>
	 */
	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email);
	
	/**
	 * Esse método retorna uma lista de conhecimentos que o desenvolvedor possui
	 * @param email Email do desenvolvedor
	 * @return ArrayList<Conhecimento>
	 */
	public HashMap<Conhecimento, Double> getConhecimentosDoDesenvolvedor(String email, int x);
	
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
	 * Esse método retorna o grau de conhecimento que um desenvolvedor tem em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return grau de conhecimento.
	 */
	public int getGrau(String email, String conhecimento);
	
	/**
	 * Esse método altera a quantidade de respostas que um desenvolvedor possui em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a atualização foi feita com sucesso.
	 */
	public boolean updateQntResposta(String email, String conhecimento, int quantidade);
	
	/**
	 * Esse método altera o grau de conhecimento de um desenvolvedor em um determinado conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param conhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a atualização foi feita com sucesso.
	 */
	public boolean updateGrau(String email, String conhecimento, int grau);
	
	/**
	 * Metodo que autentica um desenvolvedor e retorna o objeto desenvolvedor
	 * @param email Email do desenvolvedor
	 * @param senha Senha do Desenvolvedor
	 * @return
	 */
	public Desenvolvedor autenticaDesenvolvedor(String email, String senha);
	
	/**
	 * Esse Metodo retorna todos os desenvolvedores que no seu cadastro informou
	 * ter algum grau de conhecimento sobre algum item do dominio
	 * @param conhecimento
	 * @return
	 */
	public ArrayList<Desenvolvedor> getDesenvolvedoresPorConhecimento(Conhecimento conhecimento);
}
