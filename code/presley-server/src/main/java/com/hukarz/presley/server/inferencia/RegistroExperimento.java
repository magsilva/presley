package com.hukarz.presley.server.inferencia;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;

// TODO: mover esta classe pra outro pacote
public class RegistroExperimento {

	private Map<Desenvolvedor, Integer> participacaoDesenvolvedorArquivo;
	private Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento;
	private Problema problema;
	private ArrayList<Desenvolvedor> listaDesenvolvedores;
	private Map<Conhecimento, Double> grauSimilaridadeConhecimento;
	private Map<Integer, Conhecimento> idArquivoPorConhecimento;
	
	/**
	 * Singleton instance 
	 */
	private static RegistroExperimento instance = null;
	
	/** 
	 * Private constructor to allow Singleton Pattern
	 */
	private RegistroExperimento() {
		limpar();
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
		idArquivoPorConhecimento = null;
	}
	
	public void addSimilaridadeConhecimento(Integer idArquivo, double valor) {
		Conhecimento conhecimentoArquivo = idArquivoPorConhecimento.get(idArquivo);
		
		Double valorAtual = null;
		
		Conhecimento conhecimento = new Conhecimento();
		for (Iterator<Conhecimento> it = grauSimilaridadeConhecimento.keySet().iterator(); it.hasNext();) {
			conhecimento = it.next();
			if (conhecimentoArquivo.getNome().equals(conhecimento.getNome())){
				valorAtual = grauSimilaridadeConhecimento.get(conhecimento);
				break;
			}
		}
		
		if (null == valorAtual) { 
			grauSimilaridadeConhecimento.put(conhecimentoArquivo, valor);
		} else if (valor > valorAtual) {
			grauSimilaridadeConhecimento.remove(conhecimento);
			grauSimilaridadeConhecimento.put(conhecimentoArquivo, valor);
		}
	}

	public void setListaDesenvolvedores(
			ArrayList<Desenvolvedor> listaDesenvolvedores) {
		this.listaDesenvolvedores = listaDesenvolvedores;
	}

	public void setParticipacaoDesenvolvedorArquivo(
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorArquivo) {
		this.participacaoDesenvolvedorArquivo = participacaoDesenvolvedorArquivo;
	}

	public void setParticipacaoDesenvolvedorConhecimento(
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento) {
		this.participacaoDesenvolvedorConhecimento = participacaoDesenvolvedorConhecimento;
	}

	public void setIdArquivoPorConhecimento(
			Map<Integer, Conhecimento> arquivoPorConhecimento) {
		this.idArquivoPorConhecimento = arquivoPorConhecimento;
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public void salvar() throws FileNotFoundException {
		if (problema.isExperimento()){
			gerarArquivosExtra();
			gerarArquivosRecomendations();
			gerarConhecimentosIdentificados();
		}
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
		}

		saidaRecomendacao.close();
	}
	
	
	private void gerarConhecimentosIdentificados() throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();

		PrintWriter saida = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".conhecimentos"));

		for (Iterator<Conhecimento> it = grauSimilaridadeConhecimento.keySet().iterator(); it.hasNext();) {
			Conhecimento conhecimento = it.next();
			double grau = grauSimilaridadeConhecimento.get(conhecimento);
			saida.println( String.valueOf( grau ).replace('.', ',') + "\t" + 
							conhecimento.getNome());
		}

		saida.close();
	}
	

}