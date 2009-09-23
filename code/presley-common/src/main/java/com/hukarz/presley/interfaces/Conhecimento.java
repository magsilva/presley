package com.hukarz.presley.interfaces;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.NomeInvalidoException;

public interface Conhecimento extends Remote{
	public void setConhecimento(TopicoConhecimento conhecimento) throws RemoteException;
	
	public boolean atualizarConhecimento(String novoNome, String novaDescricao) 
	throws RemoteException, ConhecimentoInexistenteException, DescricaoInvalidaException, NomeInvalidoException;
	
	public boolean conhecimentoExiste() throws RemoteException, ConhecimentoInexistenteException;
	
	public boolean criarConhecimento() throws RemoteException, NomeInvalidoException,
	DescricaoInvalidaException,	ConhecimentoInexistenteException;

	public boolean removerConhecimento() throws RemoteException, ConhecimentoInexistenteException;
	
	public boolean associaConhecimentos(TopicoConhecimento conhecimentoFilho) 
	throws RemoteException, ConhecimentoInexistenteException;

	public boolean desassociaConhecimentos(TopicoConhecimento conhecimentoFilho) throws RemoteException, ConhecimentoInexistenteException;

	public ArrayList<TopicoConhecimento> getListaConhecimento()throws RemoteException;

	public boolean possuiFilhos() throws RemoteException, ConhecimentoInexistenteException;

	public TopicoConhecimento associaArquivo() throws RemoteException, ConhecimentoInexistenteException, IOException;

    public Tree getArvoreDeConhecimentos() throws RemoteException, ConhecimentoInexistenteException;
}
