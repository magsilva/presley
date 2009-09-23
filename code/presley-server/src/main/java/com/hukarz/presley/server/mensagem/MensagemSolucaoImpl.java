package com.hukarz.presley.server.mensagem;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.SolucaoIniexistenteException;
import com.hukarz.presley.interfaces.MensagemSolucao;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de solues.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class MensagemSolucaoImpl extends UnicastRemoteObject implements MensagemSolucao {
	
	ServicoSolucao servicoSolucao;
	ServicoProblema servicoProblema;
	ServicoDesenvolvedor servicoDesenvolvedor;
	Solucao solucao;
	Desenvolvedor desenvolvedor;
	Problema problema;
	
	public MensagemSolucaoImpl( ) throws RemoteException{
		servicoSolucao = new ServicoSolucaoImplDAO();
		servicoProblema = new ServicoProblemaImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}
	
	public void setSolucao(Solucao solucao) throws RemoteException{
		this.solucao = solucao;
	}

	
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) throws RemoteException{
		this.desenvolvedor = desenvolvedor;
	}

	public void setProblema(Problema problema) throws RemoteException{
		this.problema = problema;
	}

	/**
	 * Esse método atualiza a situacao de uma solução, ou seja, se ela foi útil ou
	 * não para a resolucao de um problema.
	 * @param id Identificador da solução.
	 * @param status Situacao da solução.
	 * @return true se a atualizacao foi realizada com sucesso.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean atualizarStatusDaSolucao(boolean status) throws RemoteException, SolucaoIniexistenteException {

		if (solucao == null) throw new SolucaoIniexistenteException(); 
		if (!servicoSolucao.solucaoExiste( solucao.getId() )) throw new SolucaoIniexistenteException();
		
		return servicoSolucao.atualizarStatusDaSolucao(solucao.getId(), solucao.isAjudou());
	}
	
	/**
	 * Esse método atualiza os dados uma solução
	 * @param solucao Solução a ser atualizada.
	 * @return true se a atualizacao foi realizada com sucesso.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean atualizarSolucao() throws RemoteException, SolucaoIniexistenteException {
		if (solucao == null) throw new SolucaoIniexistenteException(); 
		if (!servicoSolucao.solucaoExiste(solucao.getId())) throw new SolucaoIniexistenteException();
		
		return servicoSolucao.atualizarSolucao(solucao);
	}
	
	/**
	 * Esse método cadastra uma nova solução proposta por um desenvolvedor para
	 * um problema.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param idProblema Identificador do problema
	 * @param dataDaProposta Data em que a solucao foi proposta.
	 * @param mensagem Mensagem da solução sugerida.
	 * @return true se a solucao foi cadastrada na base de dados.
	 * @throws ProblemaInexistenteException 
	 * @throws SolucaoIniexistenteException 
	 */
	public Solucao cadastrarSolucao() throws RemoteException, ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException {
		
		if (solucao == null) throw new SolucaoIniexistenteException();
		
		if (!servicoProblema.problemaExiste( solucao.getProblema().getId() )) 
			throw new ProblemaInexistenteException();

		if (!servicoDesenvolvedor.desenvolvedorExiste( solucao.getDesenvolvedor().getEmail() )) 
			throw new DesenvolvedorInexistenteException();
		
		return servicoSolucao.cadastrarSolucao( solucao );
	}
	
	/**
	 * Esse método atualiza uma solução com os dados fornecidos no paramentro.
	 * @param solucao Solucao a ser atulizada 
	 * @return true se a solucao foi cadastrada na base de dados.
	 * @throws ProblemaInexistenteException, DesenvolvedorInexistenteException 
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean atualizarStatusDaSolucao() throws RemoteException, ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException {
		
		if (solucao == null) throw new SolucaoIniexistenteException(); 
		if (!servicoProblema.problemaExiste( solucao.getProblema().getId() )) 
			throw new ProblemaInexistenteException();

		if (!servicoDesenvolvedor.desenvolvedorExiste( solucao.getDesenvolvedor().getEmail() )) 
			throw new DesenvolvedorInexistenteException();
		
		return servicoSolucao.atualizarSolucao( solucao );
	}
	
	/**
	 * Esse método retorna um objeto do tipo Solucao que possui a id passada
	 * no parametro.
	 * @param id Identificador da solucao
	 * @return <Solucao>
	 * @throws SolucaoIniexistenteException 
	 */
	public Solucao getSolucao(int id) throws RemoteException, SolucaoIniexistenteException {
		
		solucao = servicoSolucao.getSolucao(id);
		
		if (solucao == null) {
			throw new SolucaoIniexistenteException();
		}
		
		return solucao;
	}
	
	/**
	 * Esse método retorna uma lista de soluções que foram aceitas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor() throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException {
		
		if (desenvolvedor == null) throw new DesenvolvedorInexistenteException();
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DescricaoInvalidaException();

		return servicoSolucao.listarSolucoesAceitasDoDesenvolvedor(desenvolvedor.getEmail());
	}
	
	/**
	 * Esse método retorna uma lista de soluções propostas por um desenvolvedor para uma
	 * série de problemas.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor() throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException {
		
		if (desenvolvedor == null) throw new DesenvolvedorInexistenteException();
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DescricaoInvalidaException();

		return servicoSolucao.listarSolucoesDoDesenvolvedor(desenvolvedor.getEmail());
	}
	
	/**
	 * Esse método retorna uma lista de soluções que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor() throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException {
		
		if (desenvolvedor == null) throw new DesenvolvedorInexistenteException();
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DescricaoInvalidaException();

		return servicoSolucao.listarSolucoesRejeitadasDoDesenvolvedor(desenvolvedor.getEmail());
	}
	
	/**
	 * Esse método remove uma solução proposta por um desenvolvedor da base de dados.
	 * @param id Identificador da solução.
	 * @return true se a solução foi removida com sucesso.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean removerSolucao() throws RemoteException, SolucaoIniexistenteException {
		if (solucao == null) throw new SolucaoIniexistenteException(); 
		if (!servicoSolucao.solucaoExiste(solucao.getId())) throw new SolucaoIniexistenteException();
		
		return servicoSolucao.removerSolucao(solucao.getId());
	}
	
	/**
	 * Esse método verifica se existe uma solucao com tal identificador.
	 * @param id Identificador da solucao.
	 * @return true se a solucao existe.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean solucaoExiste() throws RemoteException, SolucaoIniexistenteException {
		if (solucao == null) throw new SolucaoIniexistenteException(); 
		return servicoSolucao.solucaoExiste(solucao.getId());
	}

	/**
	 * Esse método retorna uma lista de soluções que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 */
	public ArrayList<Solucao> listarSolucoesDoProblema() throws RemoteException, ProblemaInexistenteException {		
		if (problema == null) throw new ProblemaInexistenteException();
		if (!servicoProblema.problemaExiste( problema.getId() )) throw new ProblemaInexistenteException();

		return servicoSolucao.getSolucoesDoProblema(problema);
	}
	

	/**
	 * Esse método retorna uma lista de soluções que foram retornadas de um desenvolvedor
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor() throws RemoteException, DesenvolvedorInexistenteException  {
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorInexistenteException();

		return servicoSolucao.listarSolucoesRetornadasDoDesenvolvedor(desenvolvedor);
	}
	
}
