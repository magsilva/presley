package beans;

import java.util.ArrayList;

public class ConhecimentoAtividade {
	
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
