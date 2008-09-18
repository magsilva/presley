package gui.view.comunication;

import java.util.ArrayList;

import excecao.*;
import beans.Conhecimento;
import beans.DadosAutenticacao;
import beans.Desenvolvedor;
import beans.Problema;
import beans.TipoAtividade;
import beans.Tree;

/**
 * 
 * @author Eduardo Serafim, Joao Paulo
 * Essa interface contem metodos básicos que o Cliente e Servidor Presley devem implementar.
 * 
 * Última modificacao: 15/09/2008 por EduardoPS
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
	public static final int ASSOCAR_PROBLEMA_ATIVIDADE = 10;
	public static final int DESSASOCIAR_PROBLEMA_ATIVIDADE = 11;
	public static final int BUSCA_DESENVOLVEDORES = 12;
	public static final int QUALIFICA_DESENVOLVEDOR = 13;
	public static final int ENVIAR_MENSAGEM = 14;
	public static final int GET_LISTA_DESENVOLVEDORES = 15;
	public static final int GET_LISTA_CONHECIMENTO = 16;
	public static final int ADICIONA_DESENVOLVEDOR = 17;
	public static final int GET_ONTOLOGIA = 18;
	public static final int GET_LISTA_PROBLEMAS = 19;
	
	/**
	 * Este método cadastra uma nova atividade na base de dados.
	 * CÓDIGO DA OPERAÇÃO -> 01
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi adicionada com sucesso.
	 * @throws Exception 
	 */
	public boolean adicionaAtividade(TipoAtividade atividade) throws Exception;
	
	/**
	 * Este método remove a atividade da base de dados.
	 * CÓDIGO DA OPERAÇÃO -> 02
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi removida com sucesso.
	 * @throws AtividadeInexistenteException 
	 */
	public boolean removerAtividade(TipoAtividade atividade) throws AtividadeInexistenteException;

	/**
	 * Este método retorna as atividades cadastradas
	 * CÓDIGO DA OPERAÇÃO -> 03
	 * @return ArrayList<TipoAtividade> lista de atividades.
	 */
	public ArrayList<TipoAtividade> buscaAtividades();
	
	/**
	 * Este método solicita a arvore de ontologia
	 * CÓDIGO DA OPERAÇÃO -> 04
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 * @throws Exception 
	 * @throws ConhecimentoInexistenteException 
	 * @throws DescricaoInvalidaException 
	 */
	public boolean adicionaConhecimento(Conhecimento conhecimento) throws DescricaoInvalidaException, ConhecimentoInexistenteException, Exception;

	/**
	 * Este método executa autenticação no servidor
	 * CÓDIGO DA OPERAÇÃO -> 05
	 * @param String user
	 * @param String passwd  
	 * @return Desenvolvedor 
	 */
	public Desenvolvedor login(DadosAutenticacao authData);

	/**
	 * Este método solicita logout do servidor
	 * CÓDIGO DA OPERAÇÃO -> 06
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 */
	public boolean logout(Desenvolvedor desenvolvedor);

	/**
	 * Este método seta a atividade como encerrada
	 * CÓDIGO DA OPERAÇÃO -> 07
	 * @param TipoAtividade atividade
	 * @return true se a atividade for encerrada com sucesso.
	 */
	public boolean encerrarAtividade(TipoAtividade atividade);

	/**
	 * Este método associa conhecimentos a uma atividade
	 * CÓDIGO DA OPERAÇÃO -> 08
	 * @param ArrayList<Conhecimento> listaConhecimento
	 * @param TipoAtividade atividade
	 * @return true se a associação foi realizada com sucesso.
	 * @throws ConhecimentoInexistenteException 
	 * @throws AtividadeInexistenteException 
	 * @throws Exception 
	 */
	public boolean associaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) throws AtividadeInexistenteException, ConhecimentoInexistenteException, Exception;

	/**
	 * Este método desassocia conhecimentos a uma atividade
	 * CÓDIGO DA OPERAÇÃO -> 09
	 * @param ArrayList<Conhecimento> listaConhecimento
	 * @param TipoAtividade atividade
	 * @return true se a desassociação foi realizada com sucesso.
	 * @throws AtividadeInexistenteException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public boolean desassociaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) throws ConhecimentoInexistenteException, AtividadeInexistenteException;

	/**
	 * Este método associa um problema a uma atividade
	 * CÓDIGO DA OPERAÇÃO -> 10
	 * @param Problema problema
	 * @param TipoAtividade atividade
	 * @return true se a associação foi realizada com sucesso.
	 * @throws AtividadeInexistenteException 
	 * @throws DescricaoInvalidaException 
	 */
	public boolean associaProblemaAtividade(Problema problema, TipoAtividade atividade) throws DescricaoInvalidaException, AtividadeInexistenteException;

	/**
	 * Este método desassocia um problema a uma atividade
	 * CÓDIGO DA OPERAÇÃO -> 11
	 * @param Problema problema
	 * @return true se a desassociação foi realizada com sucesso.
	 */
	public boolean desassociaProblemaAtividade(Problema problema);

	/**
	 * Este método retorna uma lista de desenvolvedores para resolver um problema
	 * CÓDIGO DA OPERAÇÃO -> 12
	 * @param Problema problema
	 * @param TipoAtividade atividade
	 * @param int grauDeConfiaca varia de 1 -> 100
	 * @return true se a desassociação foi realizada com sucesso.
	 */
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(ArrayList<String> listaConhecimento, int grauDeConfianca);

	/**
	 * Este método qualifica o desenvolvedor de acordo com as respostas dele aos problemas
	 * CÓDIGO DA OPERAÇÃO -> 13
	 * @param Desenvolvedor desenvolvedor
	 * @param Problema problema
	 * @param boolean qualificação
	 * @return true se a desassociação foi realizada com sucesso.
	 */
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor, Problema problema, boolean qualificacao);

	/**
	 * Este método envia uma mensagem para os desenvolvedores selecionados
	 * CÓDIGO DA OPERAÇÃO -> 14
	 * @param ArrayList<Desenvolvedor> desenvolvedor
	 * @param Problema problema
	 * @return true se a mensagem foi realizada com sucesso.
	 */
	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem, ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema, String mensagem);
	
	/**
	 * Este método retorna uma lista com todos os desenvolvedores cadastrados
	 * CÓDIGO DA OPERAÇÃO -> 15
	 * @return ArrayList<Desenvolvedor> lista de desenvolvedores existentes.
	 */
	public ArrayList<Desenvolvedor> getListaDesenvolvedores();
	

	/**
	 * Este método retorna uma lista com todos os conhecimentos cadastrados
	 * CÓDIGO DA OPERAÇÃO -> 16
	 * @return ArrayList<Conhecimento> lista de conhecimentos existentes.
	 */
	public ArrayList<Conhecimento> getListaConhecimentos();
	

	/**
	 * Este método adiciona um desenvolvedor no banco de dados
	 * CÓDIGO DA OPERAÇÃO -> 17
	 * @param Desenvolvedor desenvolvedor
	 * @return true se o desenvolvedor foi adicionado com sucesso.
	 * @throws Exception 
	 */
	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception;


	/**
	 * Este método solicita a arvore de ontologia
	 * CÓDIGO DA OPERAÇÃO -> 18
	 * @return Tree Arvore de ontologia do sistema.
	 */
	public Tree getOntologia();	
	
	/**
	 * Este método uma lista com todos os problemas cadastrados
	 * CÓDIGO DA OPERAÇÃO -> 19
	 * @return ArrayList<Problema> lista de problemas.
	 */
	public ArrayList<Problema> getListaProblemas();
}
