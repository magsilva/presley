package gui.view.comunication;

import java.util.ArrayList;

import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.Problema;
import beans.TipoAtividade;
import beans.Tree;

/**
 * 
 * @author Eduardo Serafim, Joao Paulo
 * Essa interface contem metodos b�sicos que o Cliente e Servidor Presley devem implementar.
 * 
 * �ltima modificacao: 15/09/2008 por EduardoPS
 */


public interface CorePresleyOperations {
	
	/**
	 * Este m�todo cadastra uma nova atividade na base de dados.
	 * C�DIGO DA OPERA��O -> 01
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi adicionada com sucesso.
	 */
	public boolean adicionaAtividade(TipoAtividade atividade);
	
	/**
	 * Este m�todo remove a atividade da base de dados.
	 * C�DIGO DA OPERA��O -> 02
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi removida com sucesso.
	 */
	public boolean removerAtividade(TipoAtividade atividade);

	/**
	 * Este m�todo solicita a arvore de ontologia
	 * C�DIGO DA OPERA��O -> 03
	 * @return Tree Arvore de ontologia do sistema.
	 */
	public Tree getOntologia();	
	
	/**
	 * Este m�todo solicita a arvore de ontologia
	 * C�DIGO DA OPERA��O -> 04
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 */
	public boolean AdicionaConhecimento(Conhecimento conhecimento);

	/**
	 * Este m�todo executa autentica��o no servidor
	 * C�DIGO DA OPERA��O -> 05
	 * @param String user
	 * @param String passwd  
	 * @return Desenvolvedor 
	 */
	public Desenvolvedor login(String user, String passwd);

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
	 */
	public boolean encerrarAtividade(TipoAtividade atividade);

	/**
	 * Este m�todo associa conhecimentos a uma atividade
	 * C�DIGO DA OPERA��O -> 08
	 * @param ArrayList<Conhecimento> listaConhecimento
	 * @param TipoAtividade atividade
	 * @return true se a associa��o foi realizada com sucesso.
	 */
	public boolean associaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade);

	/**
	 * Este m�todo desassocia conhecimentos a uma atividade
	 * C�DIGO DA OPERA��O -> 09
	 * @param ArrayList<Conhecimento> listaConhecimento
	 * @param TipoAtividade atividade
	 * @return true se a desassocia��o foi realizada com sucesso.
	 */
	public boolean desassociaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade);

	/**
	 * Este m�todo associa um problema a uma atividade
	 * C�DIGO DA OPERA��O -> 10
	 * @param Problema problema
	 * @param TipoAtividade atividade
	 * @return true se a associa��o foi realizada com sucesso.
	 */
	public boolean associaProblemaAtividade(Problema problema, TipoAtividade atividade);

	/**
	 * Este m�todo desassocia um problema a uma atividade
	 * C�DIGO DA OPERA��O -> 11
	 * @param Problema problema
	 * @param TipoAtividade atividade
	 * @return true se a desassocia��o foi realizada com sucesso.
	 */
	public boolean desassociaProblemaAtividade(Problema problema, TipoAtividade atividade);

	/**
	 * Este m�todo retorna uma lista de desenvolvedores para resolver um problema
	 * C�DIGO DA OPERA��O -> 12
	 * @param Problema problema
	 * @param TipoAtividade atividade
	 * @param int grauDeConfiaca varia de 1 -> 100
	 * @return true se a desassocia��o foi realizada com sucesso.
	 */
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(Problema problema, ArrayList<Conhecimento> listaConhecimento, int grauDeConfianca);

	/**
	 * Este m�todo qualifica o desenvolvedor de acordo com as respostas dele aos problemas
	 * C�DIGO DA OPERA��O -> 13
	 * @param Desenvolvedor desenvolvedor
	 * @param Problema problema
	 * @param boolean qualifica��o
	 * @return true se a desassocia��o foi realizada com sucesso.
	 */
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor, Problema problema, boolean qualificacao);

	/**
	 * Este m�todo envia uma mensagem para os desenvolvedores selecionados
	 * C�DIGO DA OPERA��O -> 14
	 * @param ArrayList<Desenvolvedor> desenvolvedor
	 * @param Problema problema
	 * @return true se a mensagem foi realizada com sucesso.
	 */
	public boolean enviarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema);
	
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





}
