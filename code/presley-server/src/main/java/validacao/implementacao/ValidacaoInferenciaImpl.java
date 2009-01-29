package validacao.implementacao;

import java.util.HashMap;

import persistencia.implementacao.ServicoInferenciaDAO;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;



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
