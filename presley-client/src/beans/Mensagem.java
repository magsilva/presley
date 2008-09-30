package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Mensagem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 133L;
	private Desenvolvedor desenvolvedorOrigem;
	private ArrayList<Desenvolvedor> desenvolvedoresDestino; 
	private Problema problema;
	private String texto;
	
	public Desenvolvedor getDesenvolvedorOrigem() {
		return desenvolvedorOrigem;
	}
	
	public void setDesenvolvedorOrigem(Desenvolvedor desenvolvedorOrigem) {
		this.desenvolvedorOrigem = desenvolvedorOrigem;
	}
	
	/*public ArrayList<Desenvolvedor> getDesenvolvedoresDestino() {
		return desenvolvedoresDestino;
	}*/
	
	public ArrayList<Desenvolvedor> getDesenvolvedoresDestino() {
		return this.desenvolvedoresDestino;
	}
	
	public void addDesenvolvedor(Desenvolvedor desenv) {
		if(desenv != null)
			desenvolvedoresDestino.add(desenv);
		else 
			; //EXCEÇÃO.
	}
	
	public void setDesenvolvedoresDestino(
			ArrayList<Desenvolvedor> desenvolvedoresDestino) {
		this.desenvolvedoresDestino = desenvolvedoresDestino;
	}
	
	public Problema getProblema() {
		return problema;
	}
	
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
	
}
