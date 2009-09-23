package com.hukarz.presley.server.mensagem;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.interfaces.ValidacaoMensagem;
import com.hukarz.presley.server.persistencia.implementacao.ServicoMensagemImplDAO;
import com.hukarz.presley.server.usuario.UsuarioImpl;

public class ValidacaoMensagemImpl extends UnicastRemoteObject implements ValidacaoMensagem {

	ServicoMensagemImplDAO servicoMensagem;
	UsuarioImpl validacaoDesenvolvedor;

	public ValidacaoMensagemImpl() throws RemoteException {
		validacaoDesenvolvedor = new UsuarioImpl();
		servicoMensagem = new ServicoMensagemImplDAO();
	}

	/**
	 * Este método adiciona uma mensagem enviada por um desenvolvedor a outros desenvolvedores que possivelmente irão resolver solucionar o problema.
	 * @param desenvolvedorOrigem Desenvolvedor que possui um problema e envia a mensagem.
	 * @param desenvolvedoresDestino Para quem as mensagens serão armazenadas.
	 * @param problema Problema em questão.
	 * @param texto Texto que caracteriza a mensagem.
	 * @return true caso a mensagem seja armazenada com sucesso no banco de dados.
	 * @throws DesenvolvedorInexistenteException 
	 * @throws RemoteException 
	 */
	public boolean adicionarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) throws DesenvolvedorInexistenteException, RemoteException {

		for(Desenvolvedor d : desenvolvedoresDestino) {
			validacaoDesenvolvedor.setDesenvolvedor(d);
			if(!validacaoDesenvolvedor.desenvolvedorExiste()) throw new DesenvolvedorInexistenteException();
		}
		
		return servicoMensagem.adicionarMensagem(desenvolvedoresDestino, problema);
	}

	/**
	 * Este método retorna todas as mensagens armazenadas para um determinado desenvolvedor.
	 * @param desenvolvedorDestino Desenvolvedor que irá receber as mensagens.
	 * @return Coleção com todas as mensagens referentes ao desenvolvedorDestino.
	 */
	public ArrayList<Mensagem> getMensagens(String emailDesenvolvedorDestino) throws RemoteException{
		return servicoMensagem.getMensagens(emailDesenvolvedorDestino);
	}

}
