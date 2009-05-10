package dados.cvs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class nomeDesenvolvedoresCVS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("C:/Java/lucene/Entradas/changeLog.txt");
		FileReader fileReader;
		ArrayList<String> nomesCVS = new ArrayList<String>(); 
		
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			
			String linhaArquivo = null;
			String texto = "";
			String nome = "";
			while( (linhaArquivo = reader.readLine()) != null ){
				if ( !(linhaArquivo.isEmpty())
						&& (linhaArquivo.charAt(0) == 'r') 
						&& ( linhaArquivo.indexOf(" | ") > -1 ) ){
					int posInicial = linhaArquivo.indexOf(" | ") + 3;
					texto = linhaArquivo.substring(posInicial);
					
					if (texto.indexOf(" | ") > -1){
						int posFinal = texto.indexOf(" | ");
						
						nome = texto.substring(0, posFinal) ;
						 
						if (nomesCVS.indexOf(nome) == -1)
							nomesCVS.add( texto.substring(0, posFinal) );
					}
				}
			}
			
			for (Iterator<String> iterator = nomesCVS.iterator(); iterator.hasNext();) {
				nome = iterator.next();
				
				System.out.println(nome);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
