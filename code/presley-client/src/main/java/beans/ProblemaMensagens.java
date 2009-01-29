package beans;

import java.util.ArrayList;

public class ProblemaMensagens {

	private String descricaoProblema;
	
	private ArrayList<String> mensagensPro;
	
	private int idProblema;
	
	public ProblemaMensagens() {
		this.descricaoProblema = "";
		this.mensagensPro = new ArrayList<String>();
	}
	
	public ProblemaMensagens(String descricaoPro, int idProblema) {
		this.descricaoProblema = descricaoPro;
		this.mensagensPro = new ArrayList<String>();
		this.idProblema = idProblema;
	}
	
	public void addMensagem(String msg) {
		if(msg != null) 
			mensagensPro.add(msg);
	}

	public String getDescricaoProblema() {
		return descricaoProblema;
	}

	public void setDescricaoProblema(String descricaoProblema) {
		this.descricaoProblema = descricaoProblema;
	}

	public ArrayList<String> getMensagensPro() {
		return mensagensPro;
	}

	public void setMensagensPro(ArrayList<String> mensagensPro) {
		this.mensagensPro = mensagensPro;
	}

	public int getIdProblema() {
		return idProblema;
	}

	public void setIdProblema(int idProblema) {
		this.idProblema = idProblema;
	}
	
}
