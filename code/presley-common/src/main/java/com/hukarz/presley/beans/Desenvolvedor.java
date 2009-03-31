package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contém dados inerentes a um desenvolvedor.
 * 
 * Última modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class Desenvolvedor implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private String nome;
	private String cvsNome;
	private HashMap<Conhecimento,Double> listaConhecimento;
	private String senha;
	
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
		return this.nome.equals(((Desenvolvedor)arg).nome);
	}
	
	@Override
	public int hashCode(){
		return this.email.hashCode();
	}
	
}

