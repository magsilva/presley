package com.hukarz.presley.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.SolucaoIniexistenteException;

public interface MensagemSolucao extends Remote {
	public void setSolucao(Solucao solucao) throws RemoteException;
	
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) throws RemoteException;

	public void setProblema(Problema problema) throws RemoteException;

	public boolean atualizarStatusDaSolucao(boolean status) throws RemoteException, SolucaoIniexistenteException ;
	
	public boolean atualizarSolucao() throws RemoteException, SolucaoIniexistenteException; 

	public Solucao cadastrarSolucao() throws RemoteException, ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException; 

	public boolean atualizarStatusDaSolucao() throws RemoteException, ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException; 

	public Solucao getSolucao(int id) throws RemoteException, SolucaoIniexistenteException; 

	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor() throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException; 

	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor() throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException ;
	
	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor() throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException; 

	public boolean removerSolucao() throws RemoteException, SolucaoIniexistenteException; 

	public boolean solucaoExiste() throws RemoteException, SolucaoIniexistenteException; 

	public ArrayList<Solucao> listarSolucoesDoProblema() throws RemoteException, ProblemaInexistenteException; 

	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor() throws RemoteException, DesenvolvedorInexistenteException;  
}
