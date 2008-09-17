package inferencia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import validacao.implementacao.ValidacaoInferenciaImpl;
import beans.Desenvolvedor;

/*
 * Created on 09/09/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Presley
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Inferencia {
	
	private ValidacaoInferenciaImpl validaInf;
	
//	public static void main(String args[]){
//		ArrayList<Desenvolvedor> lDesenv = getDesenvolvedores(new String[]{"java", "conector"}, 50);
//		
//		for(Desenvolvedor d : lDesenv){
//			System.out.println(d);
//		}
//		
//	}
	
	public Inferencia(){
		validaInf = new ValidacaoInferenciaImpl();
	}
	
	public ArrayList<Desenvolvedor> getDesenvolvedores(String[] conhecimentos, double conf){
		
		int max = conhecimentos.length;
		int cont = 1;
		
		HashMap<Desenvolvedor, Double> mCand = new HashMap<Desenvolvedor, Double>();
		
		for(String c : conhecimentos){
			
			HashMap<Desenvolvedor, Double> mCandidatos = validaInf.getDesenvolvedoresByConhecimento(c);
			
			atualizaPossiveisDesenvolvedores(mCand, mCandidatos, ((double)cont)/max);
 
			cont++; // Proximo conhecimento
		}
		
		return (getDesenvolvedoresAptos(mCand, conf));
		
	}
	
	public ArrayList<Desenvolvedor> getDesenvolvedoresAptos(HashMap<Desenvolvedor, Double> cand, double conf){
		int pos = 0;
		ArrayList<Desenvolvedor> arDes = new ArrayList<Desenvolvedor>();
		
		while(cand.size() != 0){
			Object[] obj = getMaiorCandidato(cand);
			
			if(((Double)obj[1] > conf)){
				arDes.add(pos++, (Desenvolvedor)obj[0]);
			}
		}
		
		return arDes;
	}

	public void atualizaPossiveisDesenvolvedores(HashMap<Desenvolvedor, Double> cand, HashMap<Desenvolvedor, Double> aux, double peso){
		
		Set<Entry<Desenvolvedor, Double>> conj = aux.entrySet();
		
		for(Entry<Desenvolvedor, Double> d : conj){
			double value = peso*d.getValue();
			if(cand.containsKey(d.getKey())){
				cand.put(d.getKey(), cand.get(d.getKey())+value);
			} else{
				cand.put(d.getKey(), value);
			}
		}
	}
	
	public Object[] getMaiorCandidato(HashMap<Desenvolvedor, Double> cand){
		double temp = 0.0;
		
		Desenvolvedor d = null;
		
		Set<Entry<Desenvolvedor, Double>> s = cand.entrySet();
		
		/* Buscando o maior candidato da tabela */
		for(Entry<Desenvolvedor, Double> row : s){
			if(row.getValue() > temp){
				temp = row.getValue();
				d = row.getKey();
			}
		}
		/* ---------------- */
		
		cand.remove(d);
		Object[] obj = new Object[2];
		obj[0] = d;
		obj[1] = temp;
		
		return (obj[0]==null)?null:obj;
		
	}
    
}
