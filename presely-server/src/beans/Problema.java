package beans;

import java.sql.Date;
import java.util.ArrayList;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contém dados inerentes a um problema reportado por um desenvolvedor
 * a respeito de uma atividade.
 * 
 * Última modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class Problema {

	private int id;
	private String descricao;
	private boolean resolvido;
	private ArrayList<Solucao> solucoes;
	private Atividade atividade;
	private Date data;
	private String mensagem;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public boolean isResolvido() {
		return resolvido;
	}
	public void setResolvido(boolean resolvido) {
		this.resolvido = resolvido;
	}
	public ArrayList<Solucao> getSolucoes() {
		return solucoes;
	}
	public void setSolucoes(ArrayList<Solucao> solucoes) {
		this.solucoes = solucoes;
	}
	public Atividade getAtividade() {
		return atividade;
	}
	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
}

