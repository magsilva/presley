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
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
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
		
		Map<Desenvolvedor, Double> participacaoDesenvolvedor = somarParticipacaoDosDesenvolvedores(porcentagemDesenvolvedorArq, porcentagemDesenvolvedorConhecimento);

		return retornarMelhoresDesenvolvedores(participacaoDesenvolvedor, 5);
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
		ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
		
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

			// Calcula a porcentagem de participação do desenvolvedor nos registros do conhecimento
			double porcentagem = (desenvolvedorPorConhecimento.get(desenvolvedor) * 100)/qtdeContribuicoes;
			porcentagemDesenvolvedorConhecimento.put(desenvolvedor, porcentagem);			
		}
		

		// Calcula o conhecimento informado pelo cadastro de desenvolvedor
		desenvolvedoresConhecimento = servicoDesenvolvedor.getDesenvolvedoresPorConhecimento(conhecimento);
		for (Iterator<Desenvolvedor> iterator = desenvolvedoresConhecimento.iterator(); iterator.hasNext();) {
			Desenvolvedor desenvolvedor =  iterator.next();
			double porcentagem = porcentagemDesenvolvedorConhecimento.get(desenvolvedor) ;

			if (porcentagem ==0)
				porcentagem = desenvolvedor.getListaConhecimento().get(conhecimento);
			else
				porcentagem *= desenvolvedor.getListaConhecimento().get(conhecimento); 
					
			porcentagemDesenvolvedorConhecimento.put(desenvolvedor, porcentagem);			
		}
		
		
		return porcentagemDesenvolvedorConhecimento;
	}

	
	/*	3º Passo 
	(Soma os vetores de participação dos Desenvolvedor nos Arquivos e nas mensagens)
	 */
	private static Map<Desenvolvedor, Double> somarParticipacaoDosDesenvolvedores(Map<Desenvolvedor, Double> porcentagemDesenvolvedorArq, 
			Map<Desenvolvedor, Double> porcentagemDesenvolvedorConhecimento ){
		Map<Desenvolvedor, Double> somaPorcentagemParticipacaoDesenvolvedor = porcentagemDesenvolvedorArq ;
		
		for (Iterator<Desenvolvedor> it = porcentagemDesenvolvedorConhecimento.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();
			double porcentagem = porcentagemDesenvolvedorConhecimento.get(desenvolvedor) ;
			
			if ( somaPorcentagemParticipacaoDesenvolvedor.keySet().contains(desenvolvedor) ){
				porcentagem += somaPorcentagemParticipacaoDesenvolvedor.get(desenvolvedor) ;
			}
			
			somaPorcentagemParticipacaoDesenvolvedor.put(desenvolvedor, porcentagem);			
		}
		
		return somaPorcentagemParticipacaoDesenvolvedor;
	}
	

	/*	4º Passo 
	(Seleciona os melhores desenvolvedores como retorno)
	 */
	private static ArrayList<Desenvolvedor> retornarMelhoresDesenvolvedores(Map<Desenvolvedor, Double> participacaoDesenvolvedor, int qtde ){
		ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();
		Desenvolvedor desenvolvedorMenor = new Desenvolvedor();
		
		for (Iterator<Desenvolvedor> it = participacaoDesenvolvedor.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();
			double porcentagem = participacaoDesenvolvedor.get(desenvolvedor) ;
			
			if ( listaDesenvolvedores.size() < qtde )
				listaDesenvolvedores.add( desenvolvedor );
			else {
				double porcentagemRetorno = participacaoDesenvolvedor.get(desenvolvedorMenor) ;
				if (porcentagem > porcentagemRetorno){
					listaDesenvolvedores.remove(desenvolvedorMenor);
					listaDesenvolvedores.add(desenvolvedor);
				}
			}
				
			
			// Seleciona o Desenvolvedor com menor porcentagem
			if ( listaDesenvolvedores.size() == qtde ){
				for (int i = 0; i == qtde; i++) {
					if (i==0)
						desenvolvedorMenor = listaDesenvolvedores.get(i);
					else{
						double porcentagemDesenvolvedor = participacaoDesenvolvedor.get(listaDesenvolvedores.get(i)) ;
						double porcentagemDesenvolvedorMenor = participacaoDesenvolvedor.get(desenvolvedorMenor) ;
						
						if (porcentagemDesenvolvedor < porcentagemDesenvolvedorMenor)
							desenvolvedorMenor = listaDesenvolvedores.get(i);
					}
				}
			}
		}
		
		
		return listaDesenvolvedores;
	}
}
