package com.hukarz.presley.beans;

import java.io.Serializable;

public class QualificacaoDesenvolvedor implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7L;
	private Desenvolvedor desenvolvedor;
	private boolean foiUtil;
	private Problema problema;
	
	public Desenvolvedor getDesenvolvedor() {
		return desenvolvedor;
	}
	public void setDesenvolvedor(Desenvolvedor desenvolvedor) {
		this.desenvolvedor = desenvolvedor;
	}
	public boolean isFoiUtil() {
		return foiUtil;
	}
	public void setFoiUtil(boolean foiUtil) {
		this.foiUtil = foiUtil;
	}
	public Problema getProblema() {
		return problema;
	}
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	
}
