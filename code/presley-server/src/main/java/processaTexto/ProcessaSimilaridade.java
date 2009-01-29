package processaTexto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.interfaces.ServicoConhecimento;

import beans.Arquivo;
import beans.Conhecimento;


public class ProcessaSimilaridade {

	ProcessaDocumento processaDocumento = new ProcessaDocumento();
	ServicoConhecimento servicoConhecimento = new ServicoConhecimentoImplDAO();  
	
	public Conhecimento verificaConhecimentoDoTexto(String texto) throws IOException{

		ArrayList<Conhecimento> conhecimentos = servicoConhecimento.getListaConhecimento();
		Arquivo arquivoTexto = processaDocumento.transformaTextoEmArquivo(texto);
		Conhecimento conhecimentoMaisSimilar = null; 
		
		double grauDeSimilaridadeMaior = 0;
		for (Iterator<Conhecimento> iterator = conhecimentos.iterator(); iterator.hasNext();) {
			Conhecimento conhecimento = iterator.next();
			double grauDeSimilaridade = calculaGrauDeSimilaridade( conhecimento, arquivoTexto) ;
			
			if (grauDeSimilaridadeMaior < grauDeSimilaridade){
				conhecimentoMaisSimilar = conhecimento;
				grauDeSimilaridadeMaior = grauDeSimilaridade;
			}
		}
		
		return conhecimentoMaisSimilar; 
	}
	
	private double calculaGrauDeSimilaridade(Conhecimento conhecimento, Arquivo arquivoTexto ){
		Arquivo arquivoGeralConhecimento = unirArquivosDoConhecimento(conhecimento);
		return calculaGrauDeSimilaridadeEntreTextos(arquivoGeralConhecimento, arquivoTexto);
	}
	
	private Arquivo unirArquivosDoConhecimento(Conhecimento conhecimento){
		ArrayList<Arquivo> arquivosConhecimento = conhecimento.getArquivos();
		Arquivo arquivoRetorno = new Arquivo("");
		
		Map<String, Integer> termosTotais = new HashMap<String, Integer>() ;
		for (Iterator<Arquivo> iterator = arquivosConhecimento.iterator(); iterator.hasNext();) {
			Arquivo arquivo = iterator.next();
			arquivoRetorno.setQtdPalavrasTotal( arquivoRetorno.getQtdPalavrasTotal() + arquivo.getQtdPalavrasTotal() ) ;

			// faz o somatorio das palavras encontradas no arquivos do conhecimento
			Map<String, Integer> termosArquivo = arquivo.getTermosSelecionados(); 
			Set<String> palavras = termosArquivo.keySet();  
			for (Iterator<String> iterator2 = palavras.iterator(); iterator2.hasNext();) {
				String palavra = iterator2.next();
				if ( termosTotais.get(palavra) == null){
					termosTotais.put(palavra, termosArquivo.get(palavra));
				} else {
					termosTotais.put(palavra, termosTotais.get(palavra) + termosArquivo.get(palavra));
				}
					
			}
		}
		
		arquivoRetorno.setTermosSelecionados(termosTotais);
		
		return arquivoRetorno ; 
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
			double frqRelativa1 = (double) (Double.parseDouble(texto1.get(termo).toString()) /documento1.getQtdPalavrasTotal());
			double frqRelativa2 = (double) (Double.parseDouble(texto2.get(termo).toString()) /documento2.getQtdPalavrasTotal());
			
//			System.out.println( termo + " Doc1 " + frqRelativa1 + " Doc2 " + frqRelativa2);
			somatorioGrauIgualdade += calculaGrauDeIgualdade( frqRelativa1, frqRelativa2) ;
		}

		return somatorioGrauIgualdade/( texto1.size() + texto2.size() - listTermosComuns.size());
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
