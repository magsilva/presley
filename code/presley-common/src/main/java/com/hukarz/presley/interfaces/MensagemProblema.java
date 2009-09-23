package com.hukarz.presley.interfaces;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;

public interface MensagemProblema extends Remote {
	public void setProblema(Problema problema) throws RemoteException;
	
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) throws RemoteException;

	public boolean atualizarStatusDoProblema() throws RemoteException, ProblemaInexistenteException;
	
	public Problema cadastrarProblema() 
		throws RemoteException, DescricaoInvalidaException, IOException, ProjetoInexistenteException, 
		ConhecimentoNaoEncontradoException, ProblemaInexistenteException ;

	public Problema getProblema(int id) throws RemoteException, ProblemaInexistenteException ;
	
	public boolean problemaExiste() throws RemoteException, ProblemaInexistenteException ;
	
	public boolean removerProblema() throws RemoteException, ProblemaInexistenteException ;

	public ArrayList<Problema> getListaProblema() throws RemoteException;
	
	public ArrayList<String> getConhecimentosAssociados() throws RemoteException, ProblemaInexistenteException ;
}
