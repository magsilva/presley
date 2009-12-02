package com.hukarz.presley.server.persistencia.interfaces;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Conhecimento;


public interface ServicoArquivo {

	/**
	 * Este m�todo cria um no registro de arquivo na base de dados
	 * @param arquivo a cadastrar
	 * @return true se o registro foi inserido na base de dados.
	 */
	public boolean criarArquivo(Arquivo arquivo);

	/**
	 * Este m�todo remove um arquivo previamente cadastrado. 
	 * @param arquivo a ser removido.
	 * @return true se o arquivo foi removido com sucesso
	 */
	public boolean removerArquivo(Arquivo arquivo);
	
	/**
	 * Este m�todo verifica se um arquivo existe na base de dados.
	 * @param arquivo para verificacao.
	 * @return true se o arquivo existe.
	 */
	public boolean arquivoExiste(Arquivo arquivo);
	
	/**
	 * Este m�todo atualiza um arquivo previamente cadastrado na base da dados 
	 * @param arquivo arquivo a ser atualizado.
	 * @param novoNome Novo arquivo.
	 * @return true se o arquivo foi atualizado.
	 */
	public boolean atualizarArquivo(Arquivo arquivoAnterior, Arquivo arquivoNovo);
	
	/**
	 * Esse m�todo retorna um arquivo que possui o id passado por parametro.
	 * @param id Id do arquivo a ser retornado.
	 * @return Arquivo
	 */
	public Arquivo getArquivo(Arquivo arquivo);
	
	
	/**
	 * Esse m�todo retorna uma lista com todos os arquivos cadastrados no BD
	 * @return
	 */
	public ArrayList<Arquivo> getListaArquivo();
	
	/**
	 * Este m�todo associa palavras a um arquivo previamente cadastrado na base da dados 
	 * @param arquivo arquivo a ser atualizado.
	 * @return Arquivo o arquivo atualizado.
	 */
	public Arquivo associaPalavrasArquivo(Arquivo arquivo, Map<String, Integer> termosSelecionados);
	
	/**
	 * Esse m�todo retorna a quantidade de Arquivos existentes no Banco de Dados
	 * @return
	 * @throws SQLException 
	 */
	public int getQuantidadeArquivosCadastrados() ;
	
	/**
	 * Esse m�todo retorna a quantidade de Arquivos que contem o termo passado 
	 * como parametro
	 * @param termo
	 * @return
	 * @throws SQLException 
	 */
	public int getQuantidadeArquivosComTermo(String termo) ;
	
	public Map<Integer, Conhecimento> getIdArquivosPorConhecimento();
}
