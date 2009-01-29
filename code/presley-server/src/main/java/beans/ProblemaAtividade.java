package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ProblemaAtividade implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
	private Problema problema;
	private TipoAtividade atividade;
	private ArrayList<Conhecimento> listaConhecimentos;
	
	
	public Problema getProblema() {
		return problema;
	}
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	public TipoAtividade getAtividade() {
		return atividade;
	}
	public void setAtividade(TipoAtividade atividade) {
		this.atividade = atividade;
	}
	public ArrayList<Conhecimento> getListaConhecimentos() {
		return listaConhecimentos;
	}
	public void setListaConhecimentos(ArrayList<Conhecimento> listaConhecimentos) {
		this.listaConhecimentos = listaConhecimentos;
	}
	
	
}
