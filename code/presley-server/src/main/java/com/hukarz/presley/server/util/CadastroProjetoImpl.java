package com.hukarz.presley.server.util;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.interfaces.CadastroProjeto;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;

public class CadastroProjetoImpl extends UnicastRemoteObject implements CadastroProjeto {

	ServicoProjeto servicoProjeto; 
	Projeto projeto;
	
	public CadastroProjetoImpl() throws RemoteException {
		servicoProjeto = new ServicoProjetoImplDAO() ;
	}

	public void setProjeto(Projeto projeto) throws RemoteException {
		this.projeto = projeto;
	}

	public Projeto getProjetoAtivo() throws RemoteException {
		projeto = servicoProjeto.getProjetoAtivo() ; 
		return projeto;
	}
	
	public boolean criarProjeto() throws RemoteException, NomeInvalidoException, ProjetoInexistenteException {
		if (projeto == null) throw new ProjetoInexistenteException();
		if (servicoProjeto.projetoExiste(projeto)) throw new NomeInvalidoException();
			
		return servicoProjeto.criarProjeto(projeto) ;
	}
	
	public boolean removerProjeto() throws RemoteException, ProjetoInexistenteException {
		if (projeto == null) throw new ProjetoInexistenteException();
		if (!servicoProjeto.projetoExiste(projeto)) throw new ProjetoInexistenteException();
		
		return servicoProjeto.removerProjeto(projeto);
	}
	
	public boolean atualizarStatusProjeto() throws RemoteException, ProjetoInexistenteException {
		if (projeto == null) throw new ProjetoInexistenteException();
		if (!servicoProjeto.projetoExiste(projeto)) throw new ProjetoInexistenteException();
		
		return servicoProjeto.atualizarStatusProjeto(projeto);
	}
	
	public ArrayList<Projeto> getProjetos() throws RemoteException{
		return servicoProjeto.getProjetos();
	}
}
