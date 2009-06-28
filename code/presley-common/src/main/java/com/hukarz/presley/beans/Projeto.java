package com.hukarz.presley.beans;

import java.io.Serializable;

public class Projeto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome;
	private boolean ativo;
	private String diretorio_Subversion;
	private String endereco_Servidor_Gravacao;
	private String endereco_Log;
	private String endereco_Servidor_Projeto;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public String getDiretorio_Subversion() {
		return ajustar_Endereco( diretorio_Subversion );
	}
	
	private String ajustar_Endereco( String enderecoAtual ){
    	String endereco = "";
        for(int x=0; x< enderecoAtual.length();x++){
        	if (enderecoAtual.charAt(x)=='\\' )
        		endereco += "/";
        	else
        		endereco += enderecoAtual.charAt(x);
        }
		
		return endereco;
	}
	
	public void setDiretorio_Subversion(String diretorio_Subversion) {
		this.diretorio_Subversion = diretorio_Subversion;
	}
	public String getEndereco_Servidor_Gravacao() {
		return ajustar_Endereco( endereco_Servidor_Gravacao );
	}
	public void setEndereco_Servidor_Gravacao(String endereco_Servidor_Gravacao) {
		this.endereco_Servidor_Gravacao = endereco_Servidor_Gravacao;
	}
	
	public String getEndereco_Log() {
		return endereco_Log;
	}
	public void setEndereco_Log(String endereco_Log) {
		this.endereco_Log = endereco_Log;
	}
	
	public String getEndereco_Servidor_Projeto() {
		return endereco_Servidor_Projeto;
	}
	public void setEndereco_Servidor_Projeto(String endereco_Servidor_Projeto) {
		this.endereco_Servidor_Projeto = endereco_Servidor_Projeto;
	}
	
}
