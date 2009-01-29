package gui.view.comunication;

import java.io.IOException;
import java.util.ArrayList;

import beans.Arquivo;
import beans.Conhecimento;
import beans.DadosAutenticacao;
import beans.Desenvolvedor;
import beans.Problema;
import beans.Projeto;
import beans.Solucao;
import beans.TipoAtividade;
import beans.Tree;
import excessao.AtividadeInexistenteException;
import excessao.ConhecimentoInexistenteException;
import excessao.DescricaoInvalidaException;
import excessao.DesenvolvedorInexistenteException;
import excessao.EmailInvalidoException;
import excessao.ErroDeAutenticacaoException;
import excessao.ProblemaInexistenteException;
import excessao.SenhaInvalidaException;

/**
 * 
 * @author Eduardo Serafim, Joao Paulo
 * Essa interface contem metodos b�sicos que o Cliente e Servidor Presley devem implementar.
 * 
 * �ltima modificacao: 15/09/2008 por EduardoPS
 */


public interface CorePresleyOperations {
	
	public static final int ERRO = -1;
	public static final int ADICIONA_ATIVIDADE = 1;
	public static final int REMOVE_ATIVIDADE = 2;
	public static final int BUSCA_ATIVIDADE = 3;
	public static final int ADICIONA_CONHECIMENTO = 4;
	public static final int LOG_IN = 5;
	public static final int LOG_OUT = 6;
	public static final int ENCERRAR_ATIVIDADE = 7;
	public static final int ASSOCIAR_CONHECIMENTO_ATIVIDADE = 8;
	public static final int DESSASOCIAR_CONHECIMENTO_ATIVIDADE = 9;
	public static final int ASSOCIAR_PROBLEMA_ATIVIDADE = 10;
	public static final int DESSASOCIAR_PROBLEMA_ATIVIDADE = 11;
	public static final int BUSCA_DESENVOLVEDORES = 12;
	public static final int QUALIFICA_DESENVOLVEDOR = 13;
	public static final int ENVIAR_MENSAGEM = 14;
	public static final int GET_LISTA_DESENVOLVEDORES = 15;
	public static final int GET_LISTA_CONHECIMENTO = 16;
	public static final int ADICIONA_DESENVOLVEDOR = 17;
	public static final int GET_ONTOLOGIA = 18;
	public static final int GET_LISTA_PROBLEMAS = 19;
	public static final int BUSCA_CONHECIMENTOS_RELACIONADOS = 20;
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
	public static final int GET_PROJETO = 35;
	
	
	/**
	 * Este m�todo cadastra uma nova atividade na base de dados.
	 * C�DIGO DA OPERA��O -> 01
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi adicionada com sucesso.
	 * @throws Exception 
	 */
	public boolean adicionaAtividade(TipoAtividade atividade) throws Exception;
	
	/**
	 * Este m�todo remove a atividade da base de dados.
	 * C�DIGO DA OPERA��O -> 02
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi removida com sucesso.
	 * @throws AtividadeInexistenteException 
	 * @throws ProblemaInexistenteException 
	 */
	public boolean removerAtividade(TipoAtividade atividade) throws AtividadeInexistenteException, ProblemaInexistenteException;

	/**
	 * Este m�todo retorna as atividades cadastradas
	 * C�DIGO DA OPERA��O -> 03
	 * @return ArrayList<TipoAtividade> lista de atividades.
	 */
	public ArrayList<TipoAtividade> buscaAtividades();
	
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
	 * Este m�todo seta a atividade como encerrada
	 * C�DIGO DA OPERA��O -> 07
	 * @param TipoAtividade atividade
	 * @return true se a atividade for encerrada com sucesso.
	 * @throws AtividadeInexistenteException 
	 */
	public boolean encerrarAtividade(TipoAtividade atividade) throws AtividadeInexistenteException;

	/**
	 * Este m�todo associa conhecimentos a uma atividade
	 * C�DIGO DA OPERA��O -> 08
	 * @param ArrayList<Conhecimento> listaConhecimento
	 * @param TipoAtividade atividade
	 * @return true se a associa��o foi realizada com sucesso.
	 * @throws ConhecimentoInexistenteException 
	 * @throws AtividadeInexistenteException 
	 * @throws Exception 
	 */
	public boolean associaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) throws AtividadeInexistenteException, ConhecimentoInexistenteException, Exception;

	/**
	 * Este m�todo desassocia conhecimentos a uma atividade
	 * C�DIGO DA OPERA��O -> 09
	 * @param ArrayList<Conhecimento> listaConhecimento
	 * @param TipoAtividade atividade
	 * @return true se a desassocia��o foi realizada com sucesso.
	 * @throws AtividadeInexistenteException 
	 * @throws ConhecimentoInexistenteException 
	 * @throws Exception 
	 */
	public boolean desassociaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) throws ConhecimentoInexistenteException, AtividadeInexistenteException, Exception;

	/**
	 * Este m�todo associa um problema a uma atividade
	 * C�DIGO DA OPERA��O -> 10
	 * @param Problema problema
	 * @param TipoAtividade atividade
	 * @return true se a associa��o foi realizada com sucesso.
	 * @throws AtividadeInexistenteException 
	 * @throws DescricaoInvalidaException 
	 * @throws Exception 
	 */
	public boolean associaProblemaAtividade(
				Problema problema, 
				TipoAtividade atividade,
				ArrayList<Conhecimento> listaConhecimento) 
			throws DescricaoInvalidaException, 
			AtividadeInexistenteException, 
			Exception;

	/**
	 * Este m�todo desassocia um problema a uma atividade
	 * C�DIGO DA OPERA��O -> 11
	 * @param Problema problema
	 * @return true se a desassocia��o foi realizada com sucesso.
	 * @throws ProblemaInexistenteException 
	 */
	public boolean desassociaProblemaAtividade(Problema problema) throws ProblemaInexistenteException;

	/**
	 * Este m�todo retorna uma lista de desenvolvedores para resolver um problema
	 * C�DIGO DA OPERA��O -> 12
	 * @param ArrayList<String> lista de conhecimentos associados ao problema
	 * @param int grauDeConfiaca varia de 1 -> 100
	 * @return ArrayList<Desenvolvedor> lista de desenvolvedores aptos a resolver o problema
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(ArrayList<String> conhecimentos, int grauDeConfianca) throws Exception;

	/**
	 * Este m�todo qualifica o desenvolvedor de acordo com as respostas dele aos problemas
	 * C�DIGO DA OPERA��O -> 13
	 * @param Desenvolvedor desenvolvedor
	 * @param Problema problema
	 * @param boolean qualifica��o
	 * @return true se a desassocia��o foi realizada com sucesso.
	 * @throws DesenvolvedorInexistenteException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor, Problema problema, boolean qualificacao) throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException;

	/**
	 * Este m�todo envia uma mensagem para os desenvolvedores selecionados
	 * C�DIGO DA OPERA��O -> 14
	 * @param ArrayList<Desenvolvedor> desenvolvedor
	 * @param Problema problema
	 * @return true se a mensagem foi realizada com sucesso.
	 * @throws DesenvolvedorInexistenteException 
	 */
	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem, ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema, String mensagem) throws DesenvolvedorInexistenteException;
	
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
	 * @return Tree Arvore de ontologia do sistema.
	 * @throws ConhecimentoInexistenteException 
	 */
	public Tree getOntologia() throws ConhecimentoInexistenteException;	
	
	/**
	 * Este m�todo uma lista com todos os problemas cadastrados
	 * C�DIGO DA OPERA��O -> 19
	 * @return ArrayList<Problema> lista de problemas.
	 */
	public ArrayList<Problema> getListaProblemas();
	
	public ArrayList<Conhecimento> getListaConhecimentosEnvolvidos(TipoAtividade atividade) throws ConhecimentoInexistenteException;

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
	 * @param Problema problema
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
	 * Este m�todo associa um arquivo a um conhecimento e cria a lista de palavras-chave
	 *  do arquivo
	 * C�DIGO DA OPERA��O -> 34
	 * @param conhecimento
	 * @return Retorna o conhecimento atualizado
	 * @throws ConhecimentoInexistenteException
	 * @throws IOException
	 */
	public Conhecimento associaArquivo(Conhecimento conhecimento, Arquivo arquivo) throws Exception;

	/**
	 * Este m�todo retorna as informa��es necessarias sobre o projeto em execu��o no presley
	 * C�DIGO DA OPERA��O -> 34
	 * @return
	 */
	public Projeto getProjetoAtivo();
}
