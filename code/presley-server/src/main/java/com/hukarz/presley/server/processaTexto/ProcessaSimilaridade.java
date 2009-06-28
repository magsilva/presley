package com.hukarz.presley.server.processaTexto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;

public class ProcessaSimilaridade {

	ProcessaDocumento processaDocumento = new ProcessaDocumento();
	ServicoConhecimento servicoConhecimento = new ServicoConhecimentoImplDAO();
	ServicoArquivo servicoArquivo = new ServicoArquivoImplDAO();
	int qtdeArquivos = 0;
	
	public Conhecimento verificaConhecimentoDoTexto(String texto) throws IOException, ConhecimentoNaoEncontradoException{
		ArrayList<Arquivo> arquivos = servicoArquivo.getListaArquivo();
		qtdeArquivos = arquivos.size();		
		
		Arquivo arquivoTexto = processaDocumento.transformaTextoEmArquivo(texto);
		Arquivo arquivoMaisSimilar = null; 
		
		double grauDeSimilaridadeMaior = 0;
		for (Arquivo arquivo : arquivos) {
			
			double grauDeSimilaridade = calculaGrauDeSimilaridadeEntreTextos(arquivo, arquivoTexto);
			
			if (grauDeSimilaridadeMaior < grauDeSimilaridade){
				arquivoMaisSimilar = arquivo;
				grauDeSimilaridadeMaior = grauDeSimilaridade;
			}
		}
		
		
		if (arquivoMaisSimilar == null)
			throw new ConhecimentoNaoEncontradoException( );
		
		//System.out.println( arquivoMaisSimilar.getNome() );
		return servicoConhecimento.getConhecimentoAssociado(arquivoMaisSimilar); 
	}
	
	/*
	            K
	         Somatorio gih(a,b)
	            h=1
	gs(X,Y)=___________
	             N
	 */
	public double calculaGrauDeSimilaridadeEntreTextos(Arquivo documento1, Arquivo documento2){
		double somatorioGrauIgualdade = 0;
		
		Map<String, Integer> texto1 = documento1.getTermosSelecionados();
		Map<String, Integer> texto2 = documento2.getTermosSelecionados();
		
		List<String> listTermosComuns = termosComuns(texto1, texto2);
		
		for(String termo : listTermosComuns) {
			double frqTF1 = (double) (Double.parseDouble(texto1.get(termo).toString()) / documento1.getQtdPalavrasTotal());
			double frqTF2 = (double) (Double.parseDouble(texto2.get(termo).toString()) / documento2.getQtdPalavrasTotal());
			
			double frqTF_IDF = 1;
			//Math.log( qtdeArquivos / servicoArquivo.getQuantidadeArquivosComTermo(termo) );
//			System.out.println( termo + " Doc1 " + frqRelativa1 + " Doc2 " + frqRelativa2);
			somatorioGrauIgualdade += calculaGrauDeIgualdade( frqTF1 * frqTF_IDF, frqTF2 * frqTF_IDF) ;
		}
		
		//return somatorioGrauIgualdade/( texto1.size() + texto2.size() - listTermosComuns.size());
		return somatorioGrauIgualdade;
	}

	private static List<String> termosComuns(Map<String, Integer> texto1, Map<String, Integer> texto2){
		List<String> list = new ArrayList<String>(); 
		
		for (Iterator<String> it = texto1.keySet().iterator(); it.hasNext();) {
			String key = it.next();  
			
			if ( texto2.get(key) != null )
				list.add( key ) ;
		}
		
		return list;
	}

	
	/*                               _    _     _    _
	gi(a,b)= [(a -> b) ^ (b -> a) + (a -> b) ^ (b -> a)] / 2
	*/		
	private static double calculaGrauDeIgualdade(double frqRelativa1, double frqRelativa2){
		double primeiroMin, segundoMin  = 0;
		
		// [(a -> b) ^ (b -> a) 			a->b pode ser calculado tb por b/a
		double a_b = calculaIntervalo( frqRelativa2/frqRelativa1 ) ;
		double b_a = calculaIntervalo( frqRelativa1/frqRelativa2 ) ;
		if (a_b < b_a)
			primeiroMin  = a_b;
		else	
			primeiroMin  = b_a;
		
		//  _    _     _    _
		// (a -> b) ^ (b -> a)
		frqRelativa1 = (double) 1-frqRelativa1;
		frqRelativa2 = (double) 1-frqRelativa2;
		
		a_b = calculaIntervalo( frqRelativa2/frqRelativa1 ) ;
		b_a = calculaIntervalo( frqRelativa1/frqRelativa2 ) ;
		if (a_b < b_a)
			segundoMin  = a_b;
		else	
			segundoMin  = b_a;
		
		return (primeiroMin+segundoMin)/2;
	}
	

	private static double calculaIntervalo(double valor){
		
		if (valor > 1)
			valor = 1;
		
		if (valor < 0)
			valor = 0;
		
		return valor;
	}
// 		if ( (valor > 1) || (valor < 0))
	
}
