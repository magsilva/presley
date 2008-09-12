package beans;

import java.io.Serializable;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contém dados inerentes a um conhecimento.
 * 
 * Última modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class Conhecimento implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private String descricao;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}

