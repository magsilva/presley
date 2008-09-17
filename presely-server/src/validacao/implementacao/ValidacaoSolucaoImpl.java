package validacao.implementacao;


import java.sql.Date;
import java.util.ArrayList;
import beans.Solucao;
import persistencia.implementacao.ServicoSolucaoImplDAO;
import persistencia.interfaces.ServicoSolucao;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de solues.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoSolucaoImpl {
	
	ServicoSolucao servicoSolucao;
	
	public ValidacaoSolucaoImpl() {
		servicoSolucao = new ServicoSolucaoImplDAO();
	}
	
	/**
	 * Esse m�todo atualiza a situacao de uma solu��o, ou seja, se ela foi �til ou
	 * n�o para a resolucao de um problema.
	 * @param id Identificador da solu��o.
	 * @param status Situacao da solu��o.
	 * @return true se a atualizacao foi realizada com sucesso.
	 */
	public boolean atualizarStatusDaSolucao(int id, boolean status) {
		
		return servicoSolucao.atualizarStatusDaSolucao(id, status);
	}
	
	/**
	 * Esse m�todo cadastra uma nova solu��o proposta por um desenvolvedor para
	 * um problema.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param idProblema Identificador do problema
	 * @param dataDaProposta Data em que a solucao foi proposta.
	 * @param mensagem Mensagem da solu��o sugerida.
	 * @return true se a solucao foi cadastrada na base de dados.
	 */
	public boolean cadastrarSolucao(String emailDesenvolvedor, int idProblema,
			Date dataDaProposta, String mensagem) {

		return servicoSolucao.cadastrarSolucao(emailDesenvolvedor, idProblema, dataDaProposta, mensagem);
	}
	
	/**
	 * Esse m�todo retorna um objeto do tipo Solucao que possui a id passada
	 * no parametro.
	 * @param id Identificador da solucao
	 * @return <Solucao>
	 */
	public Solucao getSolucao(int id) {

		return servicoSolucao.getSolucao(id);
	}
	
	/**
	 * Esse m�todo retorna uma lista de solu��es que foram aceitas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(
			String emailDesenvolvedor) {

		return servicoSolucao.listarSolucoesAceitasDoDesenvolvedor(emailDesenvolvedor);
	}
	
	/**
	 * Esse m�todo retorna uma lista de solu��es propostas por um desenvolvedor para uma
	 * s�rie de problemas.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(
			String emailDesenvolvedor) {

		return servicoSolucao.listarSolucoesDoDesenvolvedor(emailDesenvolvedor);
	}
	
	/**
	 * Esse m�todo retorna uma lista de solu��es que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 */
	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(
			String emailDesenvolvedor) {

		return servicoSolucao.listarSolucoesRejeitadasDoDesenvolvedor(emailDesenvolvedor);
	}
	
	/**
	 * Esse m�todo remove uma solu��o proposta por um desenvolvedor da base de dados.
	 * @param id Identificador da solu��o.
	 * @return true se a solu��o foi removida com sucesso.
	 */
	public boolean removerSolucao(int id) {

		return servicoSolucao.removerSolucao(id);
	}
	
	/**
	 * Esse m�todo verifica se existe uma solucao com tal identificador.
	 * @param id Identificador da solucao.
	 * @return true se a solucao existe.
	 */
	public boolean solucaoExiste(int id) {

		return servicoSolucao.solucaoExiste(id);
	}

}
