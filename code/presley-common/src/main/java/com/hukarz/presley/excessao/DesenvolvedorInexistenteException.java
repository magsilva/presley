package com.hukarz.presley.excessao;

public class DesenvolvedorInexistenteException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DesenvolvedorInexistenteException(){
		super("Não há desenvolvedores aptos a esse conhecimento!");
	}
	
}
