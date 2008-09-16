package beans;

import java.util.ArrayList;

public class BuscaDesenvolvedores {
	private Problema problema;
	private ArrayList<Conhecimento> listaConhecimento;
	private int grauDeConfianca;
	
	
	public Problema getProblema() {
		return problema;
	}
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	public ArrayList<Conhecimento> getListaConhecimento() {
		return listaConhecimento;
	}
	public void setListaConhecimento(ArrayList<Conhecimento> listaConhecimento) {
		this.listaConhecimento = listaConhecimento;
	}
	public int getGrauDeConfianca() {
		return grauDeConfianca;
	}
	public void setGrauDeConfianca(int grauDeConfianca) {
		this.grauDeConfianca = grauDeConfianca;
	}

	
}
