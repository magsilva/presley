package com.hukarz.presley.server.persistencia.interfaces;

import java.util.ArrayList;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Projeto;

public interface ServicoProjeto {

	/**
	 * Este m�todo cria um no registro de projeto na base de dados
	 * @param projeto a cadastrar
	 * @return true se o registro foi inserido na base de dados.
	 */
	public boolean criarProjeto(Projeto projeto);

	/**
	 * Este m�todo remove um projeto previamente cadastrado. 
	 * @param projeto a ser removido.
	 * @return true se o arquivo foi removido com sucesso
	 */
	public boolean removerProjeto(Projeto projeto);
	
	/**
	 * Esse m�todo retorna o projeto ativo do sistema
	 * @return Projeto
	 */
	public Projeto getProjetoAtivo();
	
	/**
	 * Este m�todo ativa/desativa um projeto previamente cadastrado. 
	 * @param projeto a ser ativado/desativado.
	 * @return true se o arquivo foi atualizado com sucesso
	 */
	public boolean atualizarStatusProjeto(Projeto projeto);

	/**
	 * Este m�todo verifica se um projeto foi previamente cadastrado. 
	 * @param projeto a ser pesquisado.
	 * @return true se o projeto foi encontrado
	 */
	public boolean projetoExiste(Projeto projeto);
	
	/**
	 * Este m�todo retorna todos os projetos cadastrados. 
	 * @return lista de projetos existentes
	 */	
	public ArrayList<Projeto> getProjetos();
}
