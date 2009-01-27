package beans;

import java.io.Serializable;

public class Projeto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nome;
	private boolean ativo;
	private String endereco_Servidor_Leitura;
	private String endereco_Servidor_Gravacao;
	
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
	public String getEndereco_Servidor_Leitura() {
		return endereco_Servidor_Leitura;
	}
	public void setEndereco_Servidor_Leitura(String endereco_Servidor_Leitura) {
		this.endereco_Servidor_Leitura = endereco_Servidor_Leitura;
	}
	public String getEndereco_Servidor_Gravacao() {
		return endereco_Servidor_Gravacao;
	}
	public void setEndereco_Servidor_Gravacao(String endereco_Servidor_Gravacao) {
		this.endereco_Servidor_Gravacao = endereco_Servidor_Gravacao;
	}
	
	
}
