package com.hukarz.presley.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.ArquivoInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;

public interface MensagemArquivo extends Remote {
	public void setProblema(Problema problema) throws RemoteException;

	public void setArquivo(Arquivo arquivo) throws RemoteException;

	public boolean atualizarArquivo(Arquivo arquivoNovo) throws RemoteException, ArquivoInexistenteException ;

	public boolean arquivoExiste() throws RemoteException, ArquivoInexistenteException ;

	public boolean criarArquivo() throws RemoteException, ArquivoInexistenteException ;

	public Arquivo getArquivo() throws RemoteException, ArquivoInexistenteException ;
	
	public boolean removerArquivo() throws RemoteException, ArquivoInexistenteException ;

	public Map<ArquivoJava, ArrayList<Desenvolvedor>> getDesenvolvedoresArquivos() throws RemoteException, ProblemaInexistenteException;
}
