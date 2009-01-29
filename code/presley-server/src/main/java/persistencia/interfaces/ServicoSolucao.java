package persistencia.interfaces;

import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;


public interface ServicoSolucao {
	
	/**
	 * Esse método cadastra uma nova solução proposta por um desenvolvedor para
	 * um problema.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param idProblema Identificador do problema
	 * @param dataDaProposta Data em que a solucao foi proposta.
	 * @param mensagem Mensagem da solução sugerida.
	 * @return Solucao solucao cadastrada na base de dados.
	 */
	public Solucao cadastrarSolucao(Solucao solucao);
	
	/**
	 * Esse método atualiza a situacao de uma solução, ou seja, se ela foi útil ou
	 * não para a resolucao de um problema.
	 * @param id Identificador da solução.
	 * @param status Situacao da solução.
	 * @return true se a atualizacao foi realizada com sucesso.
	 */
	public boolean atualizarStatusDaSolucao(int id, boolean status);
	
	/**
	 * Esse método remove uma solução proposta por um desenvolvedor da base de dados.
	 * @param id Identificador da solução.
	 * @return true se a solução foi removida com sucesso.
	 */
	public boolean removerSolucao(int id);
	
	/**
	 * Esse método retorna uma lista de soluções propostas por um desenvolvedor para uma
	 * série de problemas.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(String emailDesenvolvedor);

	/** 
	 * Esse metodo retorna uma lista de Soluçoes propostas de um desenvolvedor, no qual
	 * o desenvolvedor com problema não entendeu a solução
	 * @param desenvolvedor
	 * @return
	 */
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(
			Desenvolvedor desenvolvedor);
	
	/**
	 * Esse método retorna uma lista de soluções que foram aceitas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(String emailDesenvolvedor);
	
	/**
	 * Esse método verifica se existe uma solucao com tal identificador.
	 * @param id Identificador da solucao.
	 * @return true se a solucao existe.
	 */
	public boolean solucaoExiste(int id);
	
	/**
	 * Esse método retorna um objeto do tipo Solucao que possui a id passada
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
	 * Esse método retorna uma lista de soluções que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(String emailDesenvolvedor);

	/**
	 * Esse método atualiza os dados de uma solução
	 * @param solucao Solução a ser atualizada
	 * @return true se a atualizacao foi realizada com sucesso.
	 */
	public boolean atualizarSolucao(Solucao solucao);

}

