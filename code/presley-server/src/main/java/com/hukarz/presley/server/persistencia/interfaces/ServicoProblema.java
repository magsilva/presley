package com.hukarz.presley.server.persistencia.interfaces;

import java.util.ArrayList;
import java.util.Date;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;


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
	 * Esse m�todo cadastra um novo problema na base de dados.
	 * @param Problema a ser cadastrado.
	 * @return true se o problema foi cadastrado com sucesso.
	 */
	public Problema cadastrarProblema(Problema problema);
	
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
	public boolean removerProblema(Problema problema);
	
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

	/**
	 * Esse m�todo retorna um objeto problema 
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(String descricao,
			Date dataRelato,
			String mensagem,
			Desenvolvedor desenvolvedor_email);
	
	/**
	 * Esse m�todo retorna o todos os problemas de um desenvolvedor cadastrados no banco
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public ArrayList<Problema> getListaProblemas(Desenvolvedor desenvolvedor);
	
	/**
	 * Esse m�todo retorna todos os conhecimentos associados ao problema
	 * @param nomeProblema nome do problema.
	 * @return <String>
	 */	
	public ArrayList<String> getConhecimentosAssociados(String nomeProblema);
	
}

