package com.hukarz.presley.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;

public interface ValidacaoMensagem extends Remote {
	public boolean adicionarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) throws DesenvolvedorInexistenteException, RemoteException;
	
	public ArrayList<Mensagem> getMensagens(String emailDesenvolvedorDestino) throws RemoteException;
}
