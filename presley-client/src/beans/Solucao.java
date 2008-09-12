package beans;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author Amilcar Jr
 * Essa classe cont�m dados inerentes a uma solucao proposta
 * por um desenvolvedor.
 * 
 * �ltima modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class Solucao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Problema problema;
	private Desenvolvedor desenvolvedor;
	private Date data;
	private String mensagem;
	private boolean ajudou;
	
	public boolean isAjudou() {
		return ajudou;
	}
	public void setAjudou(boolean ajudou) {
		this.ajudou = ajudou;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Problema getProblema() {
		return problema;
	}
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	public Desenvolvedor getDesenvolvedor() {
		return desenvolvedor;
	}
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
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
