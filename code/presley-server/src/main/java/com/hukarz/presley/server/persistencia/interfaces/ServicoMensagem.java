package com.hukarz.presley.server.persistencia.interfaces;

import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;

public interface ServicoMensagem {
	public ArrayList<Mensagem> getMensagens(String emailDesenvolvedorDestino);
	
	public boolean adicionarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema);

	public boolean existeResposta(String emailDesenvolvedorDestino, String emailDesenvolvedorOrigem);
}
