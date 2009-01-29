package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ConhecimentoAtividade implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 17L;
	private ArrayList<Conhecimento> conhecimento;
	private TipoAtividade Atividade;
	
	
	public ArrayList<Conhecimento> getConhecimentos() {
		return conhecimento;
	}
	public void setConhecimento(ArrayList<Conhecimento> conhecimento) {
		this.conhecimento = conhecimento;
	}
	public TipoAtividade getAtividade() {
		return Atividade;
	}
	public void setAtividade(TipoAtividade atividade) {
		Atividade = atividade;
	}

}
