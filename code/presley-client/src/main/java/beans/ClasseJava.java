package beans;

import java.io.Serializable;

public class ClasseJava implements Serializable{
	
	private static final long serialVersionUID = 3L;
	private String nomeClasse;

	@Override
	public int hashCode() {
		return nomeClasse.hashCode();
	}

	@Override
	public boolean equals(Object arg0) {
		return this.nomeClasse == ((ClasseJava) arg0).getNomeClasse() ; 
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
