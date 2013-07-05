package com.hukarz.presley.server.core.interfaces;

import java.util.ArrayList;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.excessao.SenhaInvalidaException;


/**
 * 
 * @author Eduardo Serafim, Joao Paulo
 * Essa interface contem metodos b�sicos que o Cliente e Servidor Presley devem implementar.
 * 
 * �ltima modificacao: 15/09/2008 por EduardoPS
 */


/**
 * @author Adm Cleyton
 *
 */
public interface CorePresleyOperations {
	
	public static final int ERRO = -1;
	//	public static final int ADICIONA_ATIVIDADE = 1;
	//	public static final int REMOVE_ATIVIDADE = 2;
	//	public static final int BUSCA_ATIVIDADE = 3;
	public static final int ADICIONA_CONHECIMENTO = 4;
	public static final int LOG_IN = 5;
	public static final int LOG_OUT = 6;
	//	public static final int ENCERRAR_ATIVIDADE = 7;
	//	public static final int ASSOCIAR_CONHECIMENTO_ATIVIDADE = 8;
	//	public static final int DESSASOCIAR_CONHECIMENTO_ATIVIDADE = 9;
	//	public static final int ASSOCIAR_PROBLEMA_ATIVIDADE = 10;
	//	public static final int DESSASOCIAR_PROBLEMA_ATIVIDADE = 11;
	public static final int BUSCA_DESENVOLVEDORES = 12;
	// public static final int QUALIFICA_DESENVOLVEDOR = 13;
	public static final int ENVIAR_MENSAGEM = 14;
	public static final int GET_LISTA_DESENVOLVEDORES = 15;
	public static final int GET_LISTA_CONHECIMENTO = 16;
	public static final int ADICIONA_DESENVOLVEDOR = 17;
	public static final int GET_ARVORECONHECIMENTOS = 18;
	public static final int GET_LISTA_PROBLEMAS = 19;
	// public static final int BUSCA_CONHECIMENTOS_RELACIONADOS = 20;
	public static final int REMOVER_CONHECIMENTO = 21;
	public static final int CONHECIMENTO_POSSUI_FILHOS = 22;
	public static final int REMOVER_DESENVOLVEDOR = 23;
	public static final int OBTER_MENSAGENS = 24;
	public static final int BUSCA_CONHECIMENTOS_PROBLEMA = 25;
	public static final int ADICIONA_PROBLEMA = 26;
	public static final int REMOVER_PROBLEMA = 27;
	public static final int ADICIONA_SOLUCAO = 28;
	public static final int GET_LISTA_SOLUCOES_PROBLEMA = 29;
	public static final int ATUALIZAR_STATUS_SOLUCAO = 30;
	public static final int ATUALIZAR_STATUS_PROBLEMA = 31;
	public static final int ATUALIZAR_SOLUCAO = 32;
	public static final int GET_LISTA_SOLUCOES_RETORNADAS = 33;
	public static final int ASSOCIA_ARQUIVO_CONHECIMENTO = 34;
	
	public static final int GET_PROJETOS_ATIVO = 35;
	public static final int CRIAR_PROJETO = 36;
	public static final int REMOVER_PROJETO = 37;
	public static final int ATUALIZAR_STATUS_PROJETO = 38;
	public static final int GET_PROJETOS = 39;


	public static final int GET_DESENVOLVEDOR_POR_NOME = 40;
	/**
	 * Este m�todo solicita a arvore de ontologia
	 * C�DIGO DA OPERA��O -> 04
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 * @throws Exception 
	 * @throws ConhecimentoInexistenteException 
	 * @throws DescricaoInvalidaException 
	 */
	public boolean adicionaConhecimento(Conhecimento novoConhecimento, Conhecimento pai) throws DescricaoInvalidaException, ConhecimentoInexistenteException, Exception;

	/**
	 * Este m�todo executa autentica��o no servidor
	 * C�DIGO DA OPERA��O -> 05
	 * @param String user
	 * @param String passwd  
	 * @return Desenvolvedor 
	 * @throws ErroDeAutenticacaoException 
	 * @throws SenhaInvalidaException 
	 * @throws EmailInvalidoException 
	 * @throws DesenvolvedorInexistenteException 
	 */
	public Desenvolvedor login(DadosAutenticacao authData) throws DesenvolvedorInexistenteException, EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException;

	/**
	 * Este m�todo solicita logout do servidor
	 * C�DIGO DA OPERA��O -> 06
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 */
	public boolean logout(Desenvolvedor desenvolvedor);

	/**
	 * Este m�todo envia uma mensagem para os desenvolvedores selecionados
	 * C�DIGO DA OPERA��O -> 14
	 * @param ArrayList<Desenvolvedor> desenvolvedor
	 * @param Problema problema
	 * @return true se a mensagem foi realizada com sucesso.
	 * @throws DesenvolvedorInexistenteException 
	 */
	public boolean enviarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) throws DesenvolvedorInexistenteException;
	
	/**
	 * Este m�todo retorna uma lista com todos os desenvolvedores cadastrados
	 * C�DIGO DA OPERA��O -> 15
	 * @return ArrayList<Desenvolvedor> lista de desenvolvedores existentes.
	 */
	public ArrayList<Desenvolvedor> getListaDesenvolvedores();
	

	/**
	 * Este m�todo retorna uma lista com todos os conhecimentos cadastrados
	 * C�DIGO DA OPERA��O -> 16
	 * @return ArrayList<Conhecimento> lista de conhecimentos existentes.
	 */
	public ArrayList<Conhecimento> getListaConhecimentos();
	

	/**
	 * Este m�todo adiciona um desenvolvedor no banco de dados
	 * C�DIGO DA OPERA��O -> 17
	 * @param Desenvolvedor desenvolvedor
	 * @return true se o desenvolvedor foi adicionado com sucesso.
	 * @throws Exception 
	 */
	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception;


	/**
	 * Este m�todo solicita a arvore de ontologia
	 * C�DIGO DA OPERA��O -> 18
	 * @return Tree Arvore de conhecimentos do sistema.
	 * @throws ConhecimentoInexistenteException 
	 */
	public Tree getArvoreConhecimentos() throws ConhecimentoInexistenteException;	
	
	public boolean removerConhecimento(Conhecimento conhecimento) throws ConhecimentoInexistenteException;
	
	public boolean possuiFilhos(Conhecimento conhecimento) throws ConhecimentoInexistenteException;

	public boolean removerDesenvolvedor(Desenvolvedor desenvolvedor);

	/**
	 * Este m�todo Adiciona um problema 
	 * C�DIGO DA OPERA��O -> 26
	 * @param Problema problema
	 * @throws Exception 
	 */
	public boolean adicionaProblema(Problema problema) throws Exception;

	/**
	 * Este m�todo remove um problema 
	 * C�DIGO DA OPERA��O -> 27
	 * @param Problema problema
	 * @throws Exception 
	 */
	public boolean removerProblema(Problema problema) throws ProblemaInexistenteException;
	
	/**
	 * Este m�todo Adiciona uma Solucao para um problema 
	 * C�DIGO DA OPERA��O -> 28
	 * @param Problema problema
	 * @throws Exception 
	 */
	public Solucao adicionaSolucao(Solucao solucao) throws Exception;
	
	/**
	 * Este m�todo Lista as Solu��es fornceda a um problema 
	 * C�DIGO DA OPERA��O -> 29
	 * @param Problema problema
	 * @throws Exception 
	 */
	public ArrayList<Solucao> listarSolucoesDoProblema(Problema problema) throws Exception;
	
	/**
	 * Este m�todo atualiza o status de uma Solucao 
	 * C�DIGO DA OPERA��O -> 30 	
	 * @param Solucao solucao
	 * @throws Exception 
	 */
	public boolean atualizarStatusSolucao(Solucao solucao) throws Exception;
	
	/**
	 * Este m�todo atualiza o status de um Problema 
	 * C�DIGO DA OPERA��O -> 31	
	 * @param Problema problema
	 * @throws Exception 
	 */
	public boolean atualizarStatusProblema(Problema problema) throws Exception;
	
	/**
	 * Este m�todo atualiza os dados de uma Solucao 
	 * C�DIGO DA OPERA��O -> 32 	
	 * @param Solucao solucao
	 * @throws Exception 
	 */
	public boolean atualizarSolucao(Solucao solucao) throws Exception;
	
	/**
	 * Este m�todo Lista as Solu��es de um desenvolvedor que foram retornadas 
	 * C�DIGO DA OPERA��O -> 33
	 * @param ArrayList<Solucao>
	 * @throws Exception 
	 */
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(
			Desenvolvedor desenvolvedor) throws Exception;
	

	/**
	 * Este m�todo retorna as informa��es necessarias sobre o projeto em execu��o no presley
	 * C�DIGO DA OPERA��O -> 35
	 * @return
	 */
	public Projeto getProjetoAtivo();
	
	/**
	 * Este m�todo cadastra as informa��es sobre o projeto em execu��o no presley
	 * C�DIGO DA OPERA��O -> 36
	 * @return
	 */
	public boolean criarProjeto(Projeto projeto) throws NomeInvalidoException;
	
	
	/**
	 * Este m�todo exclui um projeto em execu��o no presley
	 * C�DIGO DA OPERA��O -> 37
	 * @return
	 */
	public boolean removerProjeto(Projeto projeto) throws ProjetoInexistenteException;
	

	/**
	 * Este m�todo atualiza o status do projeto no presley (Ativado/Desativado)
	 * C�DIGO DA OPERA��O -> 38
	 * @return
	 */
	public boolean atualizarStatusProjeto(Projeto projeto) throws ProjetoInexistenteException;

	/**
	 * Este m�todo uma lista com todos os projetos cadastrados
	 * C�DIGO DA OPERA��O -> 39
	 * @return ArrayList<Projeto> lista de projetos.
	 */
	public ArrayList<Projeto> getListaProjetos(PacketStruct packet);

	/**
	 * Este m�todo retorna o desenvolvedor com o nome passado como paramentro
	 * C�DIGO DA OPERA��O -> 40
	 * @return Desenvolvedor.
	 */
	public Desenvolvedor getDesenvolvedorPorNome(String nome) throws DesenvolvedorInexistenteException ;
}
