package com.hukarz.presley.server.inferencia;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;

public class RegistroExperimento {


	private Map<Desenvolvedor, Integer> participacaoDesenvolvedorArquivo;
	private Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento;
	private Problema problema;
	private ArrayList<Desenvolvedor> listaDesenvolvedores;
	private Map<Conhecimento, Double> grauSimilaridadeConhecimento;

	public void addSimilaridadeConhecimento(Conhecimento conhecimento, double valor) {
		Double valorAtual = grauSimilaridadeConhecimento.get(conhecimento);
		if (null == valorAtual) { 
			grauSimilaridadeConhecimento.put(conhecimento, valor);
		}
		else if (valor > valorAtual) {
			grauSimilaridadeConhecimento.put(conhecimento, valor);
		}
	}

	public void setListaDesenvolvedores(
			ArrayList<Desenvolvedor> listaDesenvolvedores) {
		this.listaDesenvolvedores = listaDesenvolvedores;
	}

	/**
	 * Singleton instance 
	 */
	private static RegistroExperimento instance = null; 



	/** 
	 * Private constructor to allow Singleton Pattern
	 */
	private RegistroExperimento() {
		participacaoDesenvolvedorArquivo = null;
		participacaoDesenvolvedorConhecimento = null;
		problema = null;
		listaDesenvolvedores = null;
		grauSimilaridadeConhecimento = new HashMap<Conhecimento, Double>();
	}

	public static RegistroExperimento getInstance() {
		if (null == instance) {
			instance = new RegistroExperimento();
		}
		return instance;
	}

	public void limpar() {
		participacaoDesenvolvedorArquivo = null;
		participacaoDesenvolvedorConhecimento = null;
		problema = null;
		listaDesenvolvedores = null;		
		grauSimilaridadeConhecimento = new HashMap<Conhecimento, Double>();
	}




	public void setParticipacaoDesenvolvedorArquivo(
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorArquivo) {
		this.participacaoDesenvolvedorArquivo = participacaoDesenvolvedorArquivo;
	}

	public void setParticipacaoDesenvolvedorConhecimento(
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento) {
		this.participacaoDesenvolvedorConhecimento = participacaoDesenvolvedorConhecimento;
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public void salvar() throws FileNotFoundException {
		gerarArquivosExtra();
		gerarArquivosRecomendations();
		gerarConhecimentosIdentificados();
	}

	private void gerarArquivosExtra() throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();

		PrintWriter saidaPontuacao = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".extra"));

		// Ajustar Codigo apos o teste ******************
		ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();  
		listaDesenvolvedores.addAll( participacaoDesenvolvedorArquivo.keySet() );

		ArrayList<Desenvolvedor> listaDesenvolvedoresConhecimento = new ArrayList<Desenvolvedor>();  
		listaDesenvolvedoresConhecimento.addAll( participacaoDesenvolvedorConhecimento.keySet() );

		for (Desenvolvedor desenvolvedor : listaDesenvolvedoresConhecimento) {
			if (listaDesenvolvedores.indexOf(desenvolvedor)==-1)
				listaDesenvolvedores.add(desenvolvedor);
		}
		// <-

		saidaPontuacao.println( problema.getDescricao() );

		for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {

			saidaPontuacao.println( desenvolvedor.getEmail() + "," + 
					participacaoDesenvolvedorArquivo.get(desenvolvedor) +"," +
					participacaoDesenvolvedorConhecimento.get(desenvolvedor));
		}

		saidaPontuacao.close();
	}

	private void gerarArquivosRecomendations() throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();

		PrintWriter saidaRecomendacao = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".recomendations"));

		for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {
			saidaRecomendacao.println( desenvolvedor.getEmail() );

			/*
			StringTokenizer st = new StringTokenizer( desenvolvedor.getListaEmail() );
			while (st.hasMoreTokens()){
				String email = st.nextToken();
				saidaRecomendacao.println( email );
			}
			 */
		}

		saidaRecomendacao.close();
	}
	
	
	private void gerarConhecimentosIdentificados() throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();

		PrintWriter saida = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".conhecimentos"));
		
		
		for (Conhecimento conhecimento : grauSimilaridadeConhecimento.keySet()) {
			double grau = grauSimilaridadeConhecimento.get(conhecimento);
			saida.println(grau + " " + conhecimento.getNome());
		}

		saida.close();
	}
	
	

	public void criarSolucoesValidas() throws ProblemaInexistenteException, DesenvolvedorInexistenteException, IOException{
		/*

		ValidacaoSolucaoImpl  validacaoSolucao = new ValidacaoSolucaoImpl();
		String conteudoEmail = "";

		Projeto projeto = problema.getProjeto();

		File file = new File( projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".emails" );

		try {
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
		} catch (FileNotFoundException e) {
			this.logger.info("Question " + problema.getNumeroArquivoExperimento() + " sem resposta.");
		}
		 */		
	}

}