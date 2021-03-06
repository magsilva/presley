package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Amilcar Jr
 * Essa classe cont�m dados inerentes a um desenvolvedor.
 * 
 * �ltima modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class Desenvolvedor implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3338357327771483149L;

	/**
	 * 
	 */
	public Desenvolvedor() {
		super();
	}
	/**
	 * @param email
	 */
	public Desenvolvedor(String email) {
		super();
		this.email = email;
	}
	/**
	 * 
	 */
	private String email;
	private String nome;
	private String cvsNome;
	private HashMap<Conhecimento,Double> listaConhecimento;
	private String senha;
	private String listaEmail;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCVSNome() {
		return cvsNome;
	}
	public void setCVSNome(String cvsNome) {
		this.cvsNome = cvsNome;
	}
	public HashMap<Conhecimento,Double> getListaConhecimento() {
		return listaConhecimento;
	}
	public void setListaConhecimento(HashMap<Conhecimento,Double> listaConhecimentosDesenvolvedor) {
		this.listaConhecimento = listaConhecimentosDesenvolvedor;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getSenha() {
		return senha;
	}
	
	@Override
	public boolean equals(Object arg){
		if (null == arg) {
			return false;
		}
		return this.nome.equals(((Desenvolvedor)arg).nome);
	}
	
	@Override
	public int hashCode(){
		return this.email.hashCode();
	}
	public void setListaEmail(String listaEmail) {
		this.listaEmail = listaEmail;
	}
	public String getListaEmail() {
		return listaEmail;
	}
	
}

