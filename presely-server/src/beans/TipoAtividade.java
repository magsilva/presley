package beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contém dados inerentes a uma atividade.
 * 
 * Última modificacao: 03/08/2008 por Amilcar Jr
 * 
 */

public class TipoAtividade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String descricao;
	private Desenvolvedor desenvolvedor;
	private Desenvolvedor supervisor;
	private int idPai;
	private Date dataInicio;
	private Date dataFinal;
	private boolean concluida;
	private ArrayList<Conhecimento> listaDeConhecimentosEnvolvidos;
	
	public TipoAtividade(String descricao, Desenvolvedor desenvolvedor, Desenvolvedor supervisor, 
			int idPai, Date dataInicio, Date dataFinal, boolean concluida, ArrayList<Conhecimento> listaConhecimentos) {
	
		setDescricao(descricao);
		setDesenvolvedor(desenvolvedor);
		setSupervisor(supervisor);
		setIdPai(idPai);
		setDataInicio(dataInicio);
		setDataFinal(dataFinal);
		setConcluida(concluida);
		setListaDeConhecimentosEnvolvidos(listaConhecimentos);

	}
	
	public TipoAtividade() {

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
	public Desenvolvedor getDesenvolvedor() {
		return desenvolvedor;
	}
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}
	public Desenvolvedor getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(Desenvolvedor supervisor) {
		this.supervisor = supervisor;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
	public boolean isConcluida() {
		return concluida;
	}
	public void setConcluida(boolean concluida) {
		this.concluida = concluida;
	}
	public ArrayList<Conhecimento> getListaDeConhecimentosEnvolvidos() {
		return listaDeConhecimentosEnvolvidos;
	}
	public void setListaDeConhecimentosEnvolvidos(
			ArrayList<Conhecimento> listaDeConhecimentosEnvolvidos) {
		this.listaDeConhecimentosEnvolvidos = listaDeConhecimentosEnvolvidos;
	}
	public int getIdPai() {
		return idPai;
	}
	public void setIdPai(int idPai) {
		this.idPai = idPai;
	}
	
}
