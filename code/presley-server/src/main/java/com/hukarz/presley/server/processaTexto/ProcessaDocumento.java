package com.hukarz.presley.server.processaTexto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.server.core.PresleyProperties;

public class ProcessaDocumento {
	
	public Arquivo getDocumentoProcessado(Arquivo arquivo) throws IOException {
		
		arquivo.setQtdPalavrasTotal( qtdPalavrasTotal( arquivo.getTexto() ) ) ;
		arquivo.setTexto( identificacaoDePalavras( arquivo.getTexto().toUpperCase() ) ) ;
		
		arquivo.setTermosSelecionados( retirarPalavrasMenosFrequente( calculaFrequencia( arquivo ), 0 )) ;

		return arquivo;
	}

	public Arquivo transformaTextoEmArquivo(String texto) throws IOException {
		Arquivo arquivo = new Arquivo("");
		arquivo.setQtdPalavrasTotal( qtdPalavrasTotal( texto ) ) ;
		arquivo.setTexto( identificacaoDePalavras( texto.toUpperCase() ) ) ;
		
		arquivo.setTermosSelecionados( retirarPalavrasMenosFrequente( calculaFrequencia( arquivo ), 0 )) ;

		return arquivo;
	}
	
	private int qtdPalavrasTotal(String texto) {
		StringTokenizer st = new StringTokenizer(texto);
		return st.countTokens() ;
	}
		
	private String identificacaoDePalavras(String texto){
		
		boolean primeiraPalavra = true;
		StringBuilder retorno = new StringBuilder();
		
		StringTokenizer st = new StringTokenizer(texto);
		
		while (st.hasMoreTokens()){   
			String palavra = st.nextToken();
					
			boolean bValida  = false;
			char caracter = ' ' ;

					
			palavra = retirarCaracterExtra(palavra) ;
			// Se for uma palavra
			for (int posicao = 0; posicao < palavra.length(); posicao++) {
				caracter = palavra.charAt(posicao);

				if ((ss1Palavra(caracter) != ' ') || 
					(ss2Palavra(caracter) != ' ') || 
					((!primeiraPalavra) && (ss3Palavra(caracter) != ' '))){
					bValida = true;
				} else {	
					bValida = false;
					break;
				}
			}
			
			// Se for um numero
			if (!bValida){
				boolean primeiroNumero = true;
				for (int posicao = 0; posicao < palavra.length(); posicao++) {
					caracter = palavra.charAt(posicao);

					if (((primeiroNumero) && (ss3Palavra(caracter)!= ' ')) ||
						((!primeiroNumero) && (ss3Palavra(caracter)!= ' ')) ) {
						bValida = true;
						primeiroNumero = false;
					} else if ( (caracter == '.') || (caracter == ',') ){
						bValida = true;
						primeiroNumero = true;
					} else {	
						bValida = false;
						break;
					}
				}
				
				if ((palavra.length()>1) && (primeiroNumero))
					bValida = false;
			}
			
			
			if (bValida){
				retorno.append( palavra + ' ' ) ;
			}
				
			
			primeiraPalavra = ((caracter == '.') || (caracter == '!') || (caracter == '?')) ;
		}
		
		return remocaoDePalavrasNegativas(retorno.toString());
	}

	private char ss1Palavra(char caracter){
		if	((caracter == 'A') ||(caracter == 'B') ||			
				(caracter == 'C') || (caracter == 'D') ||			
				(caracter == 'E') || (caracter == 'F') ||			
				(caracter == 'G') || (caracter == 'H') ||			
				(caracter == 'I') || (caracter == 'J') || 
				(caracter == 'K') || (caracter == 'L') ||	
				(caracter == 'M') || (caracter == 'N') ||	
				(caracter == 'O') || (caracter == 'P') ||	
				(caracter == 'Q') || (caracter == 'R') ||	
				(caracter == 'S') || (caracter == 'T') ||	
				(caracter == 'U') || (caracter == 'V') ||	
				(caracter == 'X') || (caracter == 'Y') ||	
				(caracter == 'W') || (caracter == 'Z')){
			return caracter;
		} else {
			return ' ';
		}
	}

	private char ss2Palavra(char caracter){
		if	((caracter == 'À') || (caracter == 'Á') || 
				(caracter == 'Â') || (caracter == 'Ã') ||
				(caracter == 'Ä') ||
				(caracter == 'É') || (caracter == 'Ê') ||
				(caracter == 'Ë') ||
				(caracter == 'Í') || (caracter == 'Î') ||
				(caracter == 'Ï') ||
				(caracter == 'Ô') || (caracter == 'Ó') ||
				(caracter == 'Ö') || (caracter == 'Õ') ||
				(caracter == 'Û') || (caracter == 'Ü') ||
				(caracter == 'Ç')){
			return caracter;
		} else {
			return ' ';
		}
	}

	private char ss3Palavra(char caracter){
		if	((caracter == '0') || (caracter == '1') ||			
		   (caracter == '2') ||	(caracter == '3') ||			
		   (caracter == '4') ||	(caracter == '5') ||			
		   (caracter == '6') ||	(caracter == '7') ||			
		   (caracter == '8') ||	(caracter == '9')){
			return caracter;
		} else {
			return ' ';
		}
	}

	private String retirarCaracterExtra(String palavra){
		
		for (int i = 0; i < palavra.length(); i++) {
			char caracter = palavra.charAt(i);
			if ((ss1Palavra(caracter) == ' ') && 
				(ss2Palavra(caracter) == ' ') && 
				(ss3Palavra(caracter) == ' '))
				palavra = palavra.substring(i+1, palavra.length()) ;
			else
				break;
		}
		
		for (int i = palavra.length()-1; i >= 0; i--) {
			char caracter = palavra.charAt(i);
			if ((ss1Palavra(caracter) == ' ') && 
				(ss2Palavra(caracter) == ' ') && 
				(ss3Palavra(caracter) == ' '))
				palavra = palavra.substring(0, i) ;
			else
				break;
		}
			
		return palavra ;
	}

	private String remocaoDePalavrasNegativas(String texto){
		
		String result = texto;
		PresleyProperties properties = PresleyProperties.getInstance();
		File diretorioCD = new File(properties.getProperty("stowords.directory"));   
		File[] listagemDiretorio = diretorioCD.listFiles(); 
		result = " " + result + " ";

		try {
			for (int i = 0; i < listagemDiretorio.length; i++) {  
				if (listagemDiretorio[i].isFile()){
					File file = new File( listagemDiretorio[i].getAbsolutePath() );
					FileReader fileReader = new FileReader(file);
					BufferedReader reader = new BufferedReader(fileReader);

					String palavra = "";

					while( (palavra = reader.readLine()) != null ){
						result = result.replaceAll(" " + palavra + " ", " ");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	private Map<String, Integer> calculaFrequencia(Arquivo arquivo) throws IOException{
		Map<String, Integer> m = new HashMap<String, Integer>();
		
		StringTokenizer st = new StringTokenizer( arquivo.getTexto() );
		while (st.hasMoreTokens()){   
			String palavra = st.nextToken();
			Integer freq = m.get(palavra); 
			m.put(palavra, (freq == null) ? 1 : freq + 1);
		}
		
		return m;
	}
	
	private Map<String, Integer> retirarPalavrasMenosFrequente(Map<String, Integer> tabelaPalavras, int frequenciaMinima){
		Map<String, Integer> retorno = new HashMap<String, Integer>();
		 
		for (Iterator<String> it = tabelaPalavras.keySet().iterator(); it.hasNext();) {
			String key = it.next();  
			Integer item = tabelaPalavras.get(key); 			
			
			if (item >= frequenciaMinima) {
				retorno.put(key.toString(), Integer.parseInt(item.toString()));
			}
		}
		
		return retorno;
	}

}
