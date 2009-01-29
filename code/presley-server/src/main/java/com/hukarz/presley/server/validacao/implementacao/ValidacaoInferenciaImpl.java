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
//		System.out.println("GET DENSENVOLVEDORES BY CONHECIMENTO");
		HashMap<Desenvolvedor, Double> m = null;
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Antes de chamar o banco");
		m = servico.getDesenvolvedoresByConhecimento(c);
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!Depois de chamar o banco");
//		if(m == null) {
//			System.out.println("M NULL!!!!");
//		}
//		System.out.println(m.toString());
		return m;
	}
}
