package com.hukarz.presley.server.mensagem;

import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoMensagemImplDAO;
import com.hukarz.presley.server.usuario.Usuario;



public class ValidacaoMensagemImpl {

	ServicoMensagemImplDAO servicoMensagem;
	Usuario validacaoDesenvolvedor;

	public ValidacaoMensagemImpl() {
		validacaoDesenvolvedor = new Usuario();
		servicoMensagem = new ServicoMensagemImplDAO();
	}

	/**
	 * Este m�todo adiciona uma mensagem enviada por um desenvolvedor a outros desenvolvedores que possivelmente ir�o resolver solucionar o problema.
	 * @param desenvolvedorOrigem Desenvolvedor que possui um problema e envia a mensagem.
	 * @param desenvolvedoresDestino Para quem as mensagens ser�o armazenadas.
	 * @param problema Problema em quest�o.
	 * @param texto Texto que caracteriza a mensagem.
	 * @return true caso a mensagem seja armazenada com sucesso no banco de dados.
	 * @throws DesenvolvedorInexistenteException 
	 */
	public boolean adicionarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) throws DesenvolvedorInexistenteException {

		for(Desenvolvedor d : desenvolvedoresDestino) {
			validacaoDesenvolvedor.setDesenvolvedor(d);
			if(!validacaoDesenvolvedor.desenvolvedorExiste()) throw new DesenvolvedorInexistenteException();
		}
		
		return servicoMensagem.adicionarMensagem(desenvolvedoresDestino, problema);
	}

	/**
	 * Este m�todo retorna todas as mensagens armazenadas para um determinado desenvolvedor.
	 * @param desenvolvedorDestino Desenvolvedor que ir� receber as mensagens.
	 * @return Cole��o com todas as mensagens referentes ao desenvolvedorDestino.
	 */
	public ArrayList<Mensagem> getMensagens(String emailDesenvolvedorDestino) {
		
		
		return servicoMensagem.getMensagens(emailDesenvolvedorDestino);
	}

}
