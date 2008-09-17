package validacao.implementacao;

import java.util.HashMap;

import persistencia.implementacao.ServicoInferenciaDAO;
import validacao.excessao.DesenvolvedorInexistenteException;

import beans.Desenvolvedor;

public class ValidacaoInferenciaImpl {
	
	private ServicoInferenciaDAO servico;
	
	public ValidacaoInferenciaImpl(){
		servico = new ServicoInferenciaDAO();
	}
	
	public HashMap<Desenvolvedor, Double> getDesenvolvedoresByConhecimento(String c){
		
		HashMap<Desenvolvedor, Double> m;
		
		try{
			m = servico.getDesenvolvedoresByConhecimento(c);
		} catch(DesenvolvedorInexistenteException e){
			return null;
		}
		
		return m;
	}
}
