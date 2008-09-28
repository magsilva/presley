package beans;

import java.io.Serializable;

public class ProblemaAtividade implements Serializable{
	private static final long serialVersionUID = 12L;
	private Problema problema;
	private TipoAtividade atividade;
	
	
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
	
	
}
