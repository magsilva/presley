package validacao.implementacao;


import java.sql.Date;
import java.util.ArrayList;

import beans.Solucao;
import persistencia.implementacao.ServicoSolucaoImplDAO;
import persistencia.interfaces.ServicoSolucao;
import validacao.interfaces.ValidacaoSolucao;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de solues.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoSolucaoImpl implements ValidacaoSolucao{
	
	ServicoSolucao servicoSolucao;
	
	public ValidacaoSolucaoImpl() {
		servicoSolucao = new ServicoSolucaoImplDAO();
	}
	
	public boolean atualizarStatusDaSolucao(int id, boolean status) {
		
		return servicoSolucao.atualizarStatusDaSolucao(id, status);
	}

	public boolean cadastrarSolucao(String emailDesenvolvedor, int idProblema,
			Date dataDaProposta, String mensagem) {

		return servicoSolucao.cadastrarSolucao(emailDesenvolvedor, idProblema, dataDaProposta, mensagem);
	}

	public Solucao getSolucao(int id) {

		return servicoSolucao.getSolucao(id);
	}

	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(
			String emailDesenvolvedor) {

		return servicoSolucao.listarSolucoesAceitasDoDesenvolvedor(emailDesenvolvedor);
	}

	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(
			String emailDesenvolvedor) {

		return servicoSolucao.listarSolucoesDoDesenvolvedor(emailDesenvolvedor);
	}

	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(
			String emailDesenvolvedor) {

		return servicoSolucao.listarSolucoesRejeitadasDoDesenvolvedor(emailDesenvolvedor);
	}

	public boolean removerSolucao(int id) {

		return servicoSolucao.removerSolucao(id);
	}

	public boolean solucaoExiste(int id) {

		return servicoSolucao.solucaoExiste(id);
	}

}
