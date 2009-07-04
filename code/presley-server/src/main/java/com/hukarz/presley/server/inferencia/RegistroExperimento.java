package com.hukarz.presley.server.inferencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoSolucaoImpl;

public class RegistroExperimento {
	private ArrayList<Desenvolvedor> listaDesenvolvedores; 
	private Map<Desenvolvedor, Integer> participacaoDesenvolvedor;
	private Problema problema;
	
	public RegistroExperimento(ArrayList<Desenvolvedor> listaDesenvolvedores,
			Map<Desenvolvedor, Integer> participacaoDesenvolvedor,
			Problema problema) {
		this.listaDesenvolvedores = listaDesenvolvedores;
		this.participacaoDesenvolvedor = participacaoDesenvolvedor;
		this.problema = problema;
	}

	public void gerarLog() {
		try {  			
			gerarArquivos();
			criarSolucoesValidas();
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (ProblemaInexistenteException e) {
			e.printStackTrace();
		} catch (DesenvolvedorInexistenteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	private int contarQuantidadeArquivos(final String extencao){
		File diretorioCD = new File( problema.getProjeto().getEndereco_Servidor_Gravacao() );

		File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith( extencao );  
			}  
		}); 
		
		return listagemDiretorio.length;
	}
	
	private void gerarArquivos() throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();

		String nomeArquivo = "000" + Integer.toString(contarQuantidadeArquivos(".recomendacoes") + 1);
		
		nomeArquivo = nomeArquivo.substring(nomeArquivo.length()-4);
		PrintWriter saidaRecomendacao = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + nomeArquivo + ".recomendacoes"));

		PrintWriter saidaPontuacao = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + nomeArquivo + ".extra"));
		for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {

			StringTokenizer st = new StringTokenizer( desenvolvedor.getListaEmail() );
			while (st.hasMoreTokens()){
				String email = st.nextToken();
				saidaRecomendacao.println( email );
			}

			saidaPontuacao.println( desenvolvedor.getEmail() + " - " + participacaoDesenvolvedor.get(desenvolvedor));
		}

		saidaRecomendacao.close();
		saidaPontuacao.close();
	}
	
	private void criarSolucoesValidas() throws ProblemaInexistenteException, DesenvolvedorInexistenteException, IOException{
		ValidacaoSolucaoImpl  validacaoSolucao = new ValidacaoSolucaoImpl();
		String conteudoEmail = "";

		Projeto projeto = problema.getProjeto();

		String nomeArquivoEmail = "000" + Integer.toString(contarQuantidadeArquivos(".recomendacoes"));
		nomeArquivoEmail = nomeArquivoEmail.substring(nomeArquivoEmail.length()-4);

		File file = new File( projeto.getEndereco_Servidor_Gravacao() + nomeArquivoEmail+ ".emails" );
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);

		String linha = "";
		while( (linha = reader.readLine()) != null ){
			conteudoEmail += linha + " ";
		}

		for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {

			StringTokenizer st = new StringTokenizer( desenvolvedor.getListaEmail() );
			while (st.hasMoreTokens()){
				String email = st.nextToken();
				if (conteudoEmail.contains(email)){
					Solucao solucao = new Solucao();
					solucao.setAjudou(true);
					solucao.setProblema(problema);
					solucao.setData( new Date(System.currentTimeMillis()) ) ;
					solucao.setMensagem("");
					solucao.setDesenvolvedor(desenvolvedor);

					validacaoSolucao.cadastrarSolucao(solucao);
					break;
				}
			} 
		}
	}
	
}
