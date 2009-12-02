package com.hukarz.presley.beans;

import java.io.Serializable;

public class ClasseJava implements Serializable{
	
	private static final long serialVersionUID = 3L;
	private String nomeClasse;

	@Override
	public int hashCode() {
		return nomeClasse.hashCode();
	}

	// FIXME: implementar equals corretamente
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) {
			return false;
		}
		return this.nomeClasse.equals(((ClasseJava) arg0).getNomeClasse()); 
	}

	public ClasseJava(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}
	
}
