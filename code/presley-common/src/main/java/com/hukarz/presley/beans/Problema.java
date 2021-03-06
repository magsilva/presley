package com.hukarz.presley.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Amilcar Jr
 * Essa classe cont�m dados inerentes a um problema reportado por um desenvolvedor
 * a respeito de uma tipoAtividade.
 * 
 * �ltima modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class Problema implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private int id;
	private String descricao;
	private boolean resolvido;
	private ArrayList<Solucao> solucoes;
	private Date data;
	private String mensagem;
	private Map<ClasseJava, ArquivoJava> classesRelacionadas ;
	private Desenvolvedor desenvolvedorOrigem;
	private Conhecimento conhecimento;
	private Projeto projeto;
	private String numeroArquivoExperimento;
	private boolean temResposta = true;
	private boolean experimento = true;

	public Problema() {
		classesRelacionadas = new HashMap<ClasseJava, ArquivoJava>();
		mensagem = "";
		this.numeroArquivoExperimento = "";
	}
	
	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Desenvolvedor getDesenvolvedorOrigem() {
		return desenvolvedorOrigem;
	}

	public void setDesenvolvedorOrigem(Desenvolvedor desenvolvedorOrigem) {
		this.desenvolvedorOrigem = desenvolvedorOrigem;
	}

	public Map<ClasseJava, ArquivoJava> getClassesRelacionadas() {
		return classesRelacionadas;
	}

	public void setClassesRelacionadas(
			Map<ClasseJava, ArquivoJava> classesRelacionadas) {
		this.classesRelacionadas = classesRelacionadas;
	}

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

	public Conhecimento getConhecimento() {
		return conhecimento;
	}

	public String getNumeroArquivoExperimento() {
		return numeroArquivoExperimento;
	}

	public void setNumeroArquivoExperimento(String numeroArquivoExperimento) {
		this.numeroArquivoExperimento = numeroArquivoExperimento;
	}

	public void setConhecimento(Conhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}
	
	public boolean isTemResposta() {
		return temResposta;
	}

	public void setTemResposta(boolean temResposta) {
		this.temResposta = temResposta;
	}

	public boolean isExperimento() {
		return experimento;
	}

	public void setExperimento(boolean experimento) {
		this.experimento = experimento;
	}
}

