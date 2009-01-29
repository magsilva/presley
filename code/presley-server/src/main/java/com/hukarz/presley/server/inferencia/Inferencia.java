package com.hukarz.presley.server.inferencia;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoInferenciaImpl;


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

	private static ValidacaoInferenciaImpl validaInf;
	
	//	public static void main(String args[]){
	//		ArrayList<Desenvolvedor> lDesenv = getDesenvolvedores(new String[]{"java", "conector"}, 50);
	//		
	//		for(Desenvolvedor d : lDesenv){
	//			System.out.println(d);
	//		}
	//		
	//	}

	public static ArrayList<Desenvolvedor> getDesenvolvedores(Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores, Conhecimento conhecimento) {

		Map<Desenvolvedor, Double> porcentagemDesenvolvedorArq = getParticipacaoDesenvolvedores(arquivoDesenvolvedores);
		Map<Desenvolvedor, Double> porcentagemDesenvolvedorConhecimento = getParticipacaoDesenvolvedores(conhecimento);
		
		
		return new ArrayList<Desenvolvedor>();
	}
	
	/*	1º Passo 
	(Analisar a participação de cada Desenvolvedor nos Arquivos)
	 */
	private static Map<Desenvolvedor, Double> getParticipacaoDesenvolvedores(Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores){
		Map<Desenvolvedor, Integer> desenvolvedorPorArq = new HashMap<Desenvolvedor, Integer>();
		Map<Desenvolvedor, Double> porcentagemDesenvolvedorArq = new HashMap<Desenvolvedor, Double>();
		
		for (Iterator<ArquivoJava> arquivo = arquivoDesenvolvedores.keySet().iterator(); arquivo.hasNext();) {
			ArrayList<Desenvolvedor> desenvolvedoresArq = arquivoDesenvolvedores.get(arquivo);

			for (Iterator<Desenvolvedor> it = desenvolvedoresArq.iterator(); it.hasNext();) {
				Desenvolvedor desenvolvedor =  it.next() ;
				if ( desenvolvedorPorArq.get(desenvolvedor) == null)
					desenvolvedorPorArq.put(desenvolvedor, 1);
				else
					desenvolvedorPorArq.put(desenvolvedor, desenvolvedorPorArq.get(desenvolvedor) +1);
			}
		}
		
		int qtdeArquivos = arquivoDesenvolvedores.keySet().size();
		for (Iterator<Desenvolvedor> it = desenvolvedorPorArq.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();

			double porcentagem = (desenvolvedorPorArq.get(desenvolvedor) * 100)/qtdeArquivos;
			porcentagemDesenvolvedorArq.put(desenvolvedor, porcentagem);			
		}

		return porcentagemDesenvolvedorArq;
	}

	/*	2º Passo 
	(Analisar a participação de cada Desenvolvedor nas resolucao de problemas
	referentes ao conhecimento passado)
	 */
	private static Map<Desenvolvedor, Double> getParticipacaoDesenvolvedores(Conhecimento conhecimento){
		ServicoConhecimento servicoConhecimento = new ServicoConhecimentoImplDAO();
		
		ArrayList<Desenvolvedor> desenvolvedoresConhecimento = servicoConhecimento.getContribuintesConhecimento(conhecimento);

		Map<Desenvolvedor, Integer> desenvolvedorPorConhecimento = new HashMap<Desenvolvedor, Integer>();
		Map<Desenvolvedor, Double> porcentagemDesenvolvedorConhecimento = new HashMap<Desenvolvedor, Double>();
		
		for (Iterator<Desenvolvedor> it = desenvolvedoresConhecimento.iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();
			if ( desenvolvedorPorConhecimento.get(desenvolvedor) == null)
				desenvolvedorPorConhecimento.put(desenvolvedor, 1);
			else
				desenvolvedorPorConhecimento.put(desenvolvedor, desenvolvedorPorConhecimento.get(desenvolvedor) +1);
		}
		
		int qtdeContribuicoes = desenvolvedoresConhecimento.size();
		for (Iterator<Desenvolvedor> it = desenvolvedorPorConhecimento.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();

			double porcentagem = (desenvolvedorPorConhecimento.get(desenvolvedor) * 100)/qtdeContribuicoes;
			porcentagemDesenvolvedorConhecimento.put(desenvolvedor, porcentagem);			
		}
		
		
		return porcentagemDesenvolvedorConhecimento;
	}
	
	public static ArrayList<Desenvolvedor> getDesenvolvedores(String[] conhecimentos, double conf) throws DesenvolvedorInexistenteException{

		int max = conhecimentos.length;
		int cont = 1;
		validaInf = new ValidacaoInferenciaImpl();
		HashMap<Desenvolvedor, Double> mCand = new HashMap<Desenvolvedor, Double>();
//		System.out.println("Tamanho do array de strings de conhecimentos " + max);
		for(String c : conhecimentos){
//			System.out.println("Iterador" + c);
			HashMap<Desenvolvedor, Double> mCandidatos = validaInf.getDesenvolvedoresByConhecimento(c);
//			System.out.println(mCandidatos.toString());
			atualizaPossiveisDesenvolvedores(mCand, mCandidatos, ((double)cont)/max);

			cont++; // Proximo conhecimento
		}

		return (getDesenvolvedoresAptos(mCand, conf));

	}

	private static ArrayList<Desenvolvedor> getDesenvolvedoresAptos(HashMap<Desenvolvedor, Double> cand, double conf){
		int pos = 0;
		ArrayList<Desenvolvedor> arDes = new ArrayList<Desenvolvedor>();

		while(cand.size() != 0){
			Object[] obj = getMaiorCandidato(cand);

			if (((Double)obj[1] > conf)){
				arDes.add(pos++, (Desenvolvedor)obj[0]);
			}
		}

		return arDes;
	}

	private static void atualizaPossiveisDesenvolvedores(HashMap<Desenvolvedor, Double> cand, HashMap<Desenvolvedor, Double> aux, double peso){

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

	private static Object[] getMaiorCandidato(HashMap<Desenvolvedor, Double> cand){
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
