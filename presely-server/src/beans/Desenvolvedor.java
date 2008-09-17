package beans;

import java.io.Serializable;
import java.util.ArrayList;

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
	private String localidade;
	private ArrayList<Conhecimento> listaConhecimento;
	
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
	public String getLocalidade() {
		return localidade;
	}
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	public ArrayList<Conhecimento> getListaConhecimento() {
		return listaConhecimento;
	}
	public void setListaConhecimento(ArrayList<Conhecimento> listaConhecimento) {
		this.listaConhecimento = listaConhecimento;
	}
	
}

