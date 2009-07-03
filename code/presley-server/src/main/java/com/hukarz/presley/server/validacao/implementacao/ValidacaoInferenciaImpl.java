package com.hukarz.presley.server.validacao.implementacao;

import java.util.HashMap;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoInferenciaDAO;



public class ValidacaoInferenciaImpl {
	
	private ServicoInferenciaDAO servico;
	
	public ValidacaoInferenciaImpl(){
		servico = new ServicoInferenciaDAO();
	}
	
	public HashMap<Desenvolvedor, Double> getDesenvolvedoresByConhecimento(String c) throws DesenvolvedorInexistenteException{
		HashMap<Desenvolvedor, Double> m = null;
		m = servico.getDesenvolvedoresByConhecimento(c);
		return m;
	}
}
