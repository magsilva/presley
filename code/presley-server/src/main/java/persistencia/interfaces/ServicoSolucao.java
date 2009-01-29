package persistencia.interfaces;

import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;


public interface ServicoSolucao {
	
	/**
	 * Esse m�todo cadastra uma nova solu��o proposta por um desenvolvedor para
	 * um problema.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param idProblema Identificador do problema
	 * @param dataDaProposta Data em que a solucao foi proposta.
	 * @param mensagem Mensagem da solu��o sugerida.
	 * @return Solucao solucao cadastrada na base de dados.
	 */
	public Solucao cadastrarSolucao(Solucao solucao);
	
	/**
	 * Esse m�todo atualiza a situacao de uma solu��o, ou seja, se ela foi �til ou
	 * n�o para a resolucao de um problema.
	 * @param id Identificador da solu��o.
	 * @param status Situacao da solu��o.
	 * @return true se a atualizacao foi realizada com sucesso.
	 */
	public boolean atualizarStatusDaSolucao(int id, boolean status);
	
	/**
	 * Esse m�todo remove uma solu��o proposta por um desenvolvedor da base de dados.
	 * @param id Identificador da solu��o.
	 * @return true se a solu��o foi removida com sucesso.
	 */
	public boolean removerSolucao(int id);
	
	/**
	 * Esse m�todo retorna uma lista de solu��es propostas por um desenvolvedor para uma
	 * s�rie de problemas.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(String emailDesenvolvedor);

	/** 
	 * Esse metodo retorna uma lista de Solu�oes propostas de um desenvolvedor, no qual
	 * o desenvolvedor com problema n�o entendeu a solu��o
	 * @param desenvolvedor
	 * @return
	 */
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(
			Desenvolvedor desenvolvedor);
	
	/**
	 * Esse m�todo retorna uma lista de solu��es que foram aceitas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(String emailDesenvolvedor);
	
	/**
	 * Esse m�todo verifica se existe uma solucao com tal identificador.
	 * @param id Identificador da solucao.
	 * @return true se a solucao existe.
	 */
	public boolean solucaoExiste(int id);
	
	/**
	 * Esse m�todo retorna um objeto do tipo Solucao que possui a id passada
	 * no parametro.
	 * @param id Identificador da solucao
	 * @return <Solucao>
	 */
	public Solucao getSolucao(int id);
	
	/**
	 * Esse metodo retorna todas as solucoes propostas para um problema.
	 * @Param idProblema identificador do problema.
	 * @return ArrayList<Solucao> Uma lista com todas as solucoes propostas para o problema.
	 */
	public ArrayList<Solucao> getSolucoesDoProblema(Problema problema);
	
	/**
	 * Esse m�todo retorna uma lista de solu��es que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(String emailDesenvolvedor);

	/**
	 * Esse m�todo atualiza os dados de uma solu��o
	 * @param solucao Solu��o a ser atualizada
	 * @return true se a atualizacao foi realizada com sucesso.
	 */
	public boolean atualizarSolucao(Solucao solucao);

}

