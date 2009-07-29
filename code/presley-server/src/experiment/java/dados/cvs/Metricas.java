package dados.cvs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;

public class Metricas {

	static String PATH = "C:/java/lucene/Experimento_Final/emails/";
	private File[] arquivosEmail;
	private int[] qtdeRecomendacoesCorretas;
	private int[] qtdeRecomendacoesFeitas;
	private int[] qtdeRespostas;
		
	
	/**
	 * 		
	 * @param args
	 * @throws DesenvolvedorInexistenteException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws DesenvolvedorInexistenteException, IOException {
		Metricas metricas = new Metricas();

		System.out.println("Precisão Total		= " + metricas.precisao(0));
		System.out.println("Abrangência Total	= " + metricas.abrangencia(0));
		
		for (int i = 1; i < 6; i++) {
			System.out.println("Quantidade  ("+ i +") = " + metricas.getQtdeRespostas(i));
			System.out.println("Precisão    ("+ i +") = " + metricas.precisao(i));
			System.out.println("Abrangência ("+ i +") = " + metricas.abrangencia(i));
		}

	}

	public Metricas() throws IOException {
		File diretorio = new File(PATH);
		qtdeRecomendacoesCorretas	= new int[6] ;
		qtdeRecomendacoesFeitas		= new int[6] ;
		qtdeRespostas				= new int[6] ;

		arquivosEmail = diretorio.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".emails");  
			}  
		});

		calcularValores();
	}

	public double precisao(int posicao) {
		return (double) qtdeRecomendacoesCorretas[posicao] / qtdeRecomendacoesFeitas[posicao];
	}

	public double abrangencia(int posicao) {
		return (double) qtdeRecomendacoesCorretas[posicao] / qtdeRespostas[posicao];
	}

	
	private void calcularValores() throws IOException{
		for (File arquivo : arquivosEmail) {
			int qtdeRegistros = 0 ;
			int posicao ; 
			FileReader fileReader = new FileReader(arquivo);
			BufferedReader reader = new BufferedReader(fileReader);
			
			while( reader.readLine() != null )
				qtdeRegistros += 1;
			
			if (qtdeRegistros == 0)
				continue;
			
			if (qtdeRegistros <= 5)
				posicao = qtdeRegistros ;
			else
				posicao = 5 ;
				
			qtdeRespostas[ posicao ] += qtdeRegistros ;
			qtdeRecomendacoesFeitas[ posicao ] += calcularQtdeRecomendacoes(arquivo);
			qtdeRecomendacoesCorretas[ posicao ] += calcularQtdeRecomendacoesCorretas(arquivo);
		
		}
		
		// Melhorar
		qtdeRespostas[0] = 0;
		for (int i = 1; i < 6; i++) {
			qtdeRespostas[0] += qtdeRespostas[i];
		}
		
		qtdeRecomendacoesFeitas[0] = 0;
		for (int i = 1; i < 6; i++) {
			qtdeRecomendacoesFeitas[0] += qtdeRecomendacoesFeitas[i];
		}
		
		qtdeRecomendacoesCorretas[0] = 0;
		for (int i = 1; i < 6; i++) {
			qtdeRecomendacoesCorretas[0] += qtdeRecomendacoesCorretas[i];
		}
	}


	private int calcularQtdeRecomendacoes(File arquivoEmail) throws IOException{
		FileReader fileReader = new FileReader( arquivoEmail.toString().replace(".emails", ".recomendations"));
		BufferedReader reader = new BufferedReader(fileReader);

		int qtdeRegistros = 0 ;

		while( reader.readLine() != null )
			qtdeRegistros += 1;

		return qtdeRegistros;
	}	
	
	private int calcularQtdeRecomendacoesCorretas(File arquivoEmail) throws IOException {
		int qtde = 0;
		FileReader fileReader = new FileReader( arquivoEmail.toString().replace(".emails", ".recomendations"));
		BufferedReader reader = new BufferedReader(fileReader);

		String emailRecomendado = "";
		while( (emailRecomendado = reader.readLine()) != null ){
			if (contemEmail(emailRecomendado, arquivoEmail)) {
				qtde += 1;
			}
		}

		return qtde;
	}
	
	private boolean contemEmail( String emailRecomendado, File enderecoArquivoExtra ) throws IOException{
		FileReader fileReader = new FileReader( enderecoArquivoExtra );
		BufferedReader reader = new BufferedReader(fileReader);
		
		String emailReal = "";
		while( (emailReal = reader.readLine()) != null ){
			if (emailRecomendado.contains(emailReal) ){
				return true;
			}
		}
		
		return false;
	}
	
	public int getQtdeRespostas(int posicao) {
		return qtdeRespostas[posicao];
	}

}
