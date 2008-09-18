package validacao.implementacao;

import java.util.HashMap;

import persistencia.implementacao.ServicoInferenciaDAO;
import excessao.DesenvolvedorInexistenteException;

import beans.Desenvolvedor;

public class ValidacaoInferenciaImpl {
	
	private ServicoInferenciaDAO servico;
	
	public ValidacaoInferenciaImpl(){
		servico = new ServicoInferenciaDAO();
	}
	
	public HashMap<Desenvolvedor, Double> getDesenvolvedoresByConhecimento(String c) throws DesenvolvedorInexistenteException{
		
		HashMap<Desenvolvedor, Double> m;
		
		m = servico.getDesenvolvedoresByConhecimento(c);
		
		return m;
	}
}
