package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

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
	private static int idConhecimentoGeral;
	private int id;
	private String nome;
	private String descricao;
	private ArrayList<Arquivo> arquivos ;
	
	public Conhecimento() {
		id = idConhecimentoGeral ; 
		arquivos = new ArrayList<Arquivo>();
		
		idConhecimentoGeral++;
	}
	
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
	
	public int hashCode() {
		return id;
	}
	
	public ArrayList<Arquivo> getArquivos() {
		return arquivos;
	}
	
	public void setArquivos(ArrayList<Arquivo> arquivos) {
		this.arquivos = arquivos;
	}
	
	public void adcionaArquivo( Arquivo arquivo ){
		arquivos.add(arquivo);		
	}
}

