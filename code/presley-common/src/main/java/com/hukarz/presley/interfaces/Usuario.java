package com.hukarz.presley.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorExisteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.ListagemDeConhecimentoInexistenteException;
import com.hukarz.presley.excessao.SenhaInvalidaException;

public interface Usuario extends Remote {

	public void setDesenvolvedor(Desenvolvedor desenvolvedor) throws RemoteException;
	
	public boolean adicionarConhecimentoAoDesenvolvedor(
			String nomeConhecimento, int grau, int qntResposta) throws RemoteException, DescricaoInvalidaException, ConhecimentoInexistenteException, DesenvolvedorInexistenteException ;
	
	public boolean atualizarDesenvolvedor(String novoEmail,
			String nome, String cvsNome, String senha) throws RemoteException, DesenvolvedorInexistenteException, SenhaInvalidaException; 

	public boolean desenvolvedorTemConhecimento(String nomeConhecimento) throws RemoteException, DesenvolvedorInexistenteException, ConhecimentoInexistenteException; 

	public boolean criarDesenvolvedor() throws RemoteException, DesenvolvedorExisteException, SenhaInvalidaException, ListagemDeConhecimentoInexistenteException, DesenvolvedorInexistenteException; 

	public boolean desenvolvedorExiste() throws RemoteException, DesenvolvedorInexistenteException ;
	
	public ArrayList<TopicoConhecimento> getConhecimentosDoDesenvolvedor() 
		throws RemoteException, DescricaoInvalidaException, DesenvolvedorInexistenteException  ;
	
	public Desenvolvedor getDesenvolvedor(String email) throws RemoteException, DesenvolvedorInexistenteException ;
	
	public boolean removerConhecimentoDoDesenvolvedor( String nomeConhecimento) throws RemoteException, DesenvolvedorInexistenteException ;
	
	public boolean removerDesenvolvedor() throws RemoteException, DesenvolvedorInexistenteException ;
	
	public ArrayList<Desenvolvedor> getListaDesenvolvedores() throws RemoteException;
	
	public int getQntResposta(String nomeConhecimento) 
		throws RemoteException, ConhecimentoInexistenteException, DesenvolvedorInexistenteException ;
	
	public int getGrau(String nomeConhecimento) 
		throws RemoteException, ConhecimentoInexistenteException, DesenvolvedorInexistenteException ;
	
	public boolean updateQntResposta(String nomeConhecimento, int quantidade)
		throws RemoteException, DesenvolvedorInexistenteException, ConhecimentoInexistenteException;
	
	public boolean updateGrau(String email, String nomeConhecimento, int grau)
		throws RemoteException, DesenvolvedorInexistenteException, ConhecimentoInexistenteException;
	
	public Desenvolvedor autenticaDesenvolvedor(DadosAutenticacao dados) throws RemoteException, DesenvolvedorInexistenteException,
			EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException ;
	
	public Desenvolvedor getDesenvolvedorPorNome(String sNome) throws RemoteException, DesenvolvedorInexistenteException;

}
