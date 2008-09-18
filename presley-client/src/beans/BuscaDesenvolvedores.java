package beans;

import java.util.ArrayList;

public class BuscaDesenvolvedores {
	private Problema problema;
	private ArrayList<String> listaConhecimento;
	private int grauDeConfianca;
	
	
	public Problema getProblema() {
		return problema;
	}
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	public ArrayList<String> getListaConhecimento() {
		return listaConhecimento;
	}
	public void setListaConhecimento(ArrayList<String> listaConhecimento) {
		this.listaConhecimento = listaConhecimento;
	}
	public int getGrauDeConfianca() {
		return grauDeConfianca;
	}
	public void setGrauDeConfianca(int grauDeConfianca) {
		this.grauDeConfianca = grauDeConfianca;
	}

	
}
