package com.hukarz.presley.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;

public interface CadastroProjeto extends Remote {
	public void setProjeto(Projeto projeto) throws RemoteException ;

	public Projeto getProjetoAtivo() throws RemoteException ;
	
	public boolean criarProjeto() throws RemoteException, NomeInvalidoException, ProjetoInexistenteException ;
	
	public boolean removerProjeto() throws RemoteException, ProjetoInexistenteException;
	
	public boolean atualizarStatusProjeto() throws RemoteException, ProjetoInexistenteException ;
	
	public ArrayList<Projeto> getProjetos() throws RemoteException;

}
