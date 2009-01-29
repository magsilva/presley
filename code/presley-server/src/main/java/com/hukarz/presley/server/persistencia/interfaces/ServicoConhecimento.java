package com.hukarz.presley.server.persistencia.interfaces;

import java.util.ArrayList;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;


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
	
	/**
	 * Esse metodo cria uma associacao de herança entre dois conhecimentos
	 * passados por parametro.
	 * @param nomeConhecimentoPai Nome do conhecimento Pai.
	 * @param nomeConhecimentoFilho Nome do conhecimento Filho.
	 * @return true de a associacao foi realizada com sucesso.
	 */
	public boolean associaConhecimentos(String nomeConhecimentoPai, String nomeConhecimentoFilho);
	
	/**
	 * Esse metodo desfaz uma associacao de herança entre dois conhecimentos
	 * passados por parametro.
	 * @param nomeConhecimentoPai Nome do conhecimento Pai.
	 * @param nomeConhecimentoFilho Nome do conhecimento Filho.
	 * @return true de a desassociacao foi realizada com sucesso.
	 */
	public boolean desassociaConhecimentos(String nomeConhecimentoPai, String nomeConhecimentoFilho);
	
	/**
	 * Metodo que retorna os conhecimentos filhos de um conhecimento.
	 * @param idConhecimentoPai
	 * @return ArrayList<Conhecimento> Lista dos conhecimentos filhos.
	 * @throws ConhecimentoInexistenteException
	 */
	public ArrayList<Conhecimento> getFilhos(String nomeConhecimentoPai) throws ConhecimentoInexistenteException;
	
	/**
	 * Metodo que retorna os conhecimentos pais de um conhecimento.
	 * @param idConhecimentoPai
	 * @return ArrayList<Conhecimento> Lista dos conhecimentos filhos.
	 * @throws ConhecimentoInexistenteException
	 */
	public ArrayList<Conhecimento> getPais(String nomeConhecimentoFilho) throws ConhecimentoInexistenteException;

	public ArrayList<Conhecimento> getListaConhecimento();

	/**
	 * Esse metodo cria uma associacao do conhecimento com seus arquivos relacionados
	 * @param conhecimento conhecimento
	 * @param arquivos lista de arquivos relacionados 
	 * @return
	 */
	public boolean associaArquivo(Conhecimento conhecimento, Arquivo arquivo);
	
	/**
	 * Esse metodo retorna os desenvolvedores que tiveram problemas relacionados ao
	 * conhecimento fornecido ou que contribuiram com soluções validas
	 * @param conhecimento
	 * @return
	 */
	public ArrayList<Desenvolvedor> getContribuintesConhecimento(Conhecimento conhecimento); 
	
}

