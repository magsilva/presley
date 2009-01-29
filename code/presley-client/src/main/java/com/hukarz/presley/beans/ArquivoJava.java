package com.hukarz.presley.beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArquivoJava extends Arquivo {

	private static final long serialVersionUID = 1L;
//	private ArrayList<Desenvolvedor> desenvolvedores = new ArrayList<Desenvolvedor>();
	
	public ArquivoJava(String nome) {
		super(nome);
	}

	@Override
	public String getTexto() throws IOException {
		String textoRetorno = "";

		BufferedReader in = new BufferedReader(new FileReader( getEnderecoServidor() ));
		String texto =  null;  
		boolean comentarioBloco = false ;
		while ((texto = in.readLine()) != null) {  

			for(int x=0; x < texto.length();x++){
				if (x <= texto.length()-2 && texto.charAt(x)=='/' && texto.charAt(x+1)=='/') {
					textoRetorno += " " + texto.substring(x+2).trim();
					break;
				}

				if (x <= texto.length()-2 && texto.charAt(x)=='/' && texto.charAt(x+1)=='*' ){
					textoRetorno += " "; 
					comentarioBloco = true ;
				}

				if (x <= texto.length()-2 && texto.charAt(x)=='*' && texto.charAt(x+1)=='/' )
					comentarioBloco = false ;

				if (comentarioBloco && texto.charAt(x)!='*' && texto.charAt(x)!='/' ){
					textoRetorno += texto.charAt(x); 
				}
			}

		}
		
		return textoRetorno;
	}

//	public ArrayList<Desenvolvedor> getDesenvolvedores() {
//		return desenvolvedores;
//	}
//
//	public void setDesenvolvedores(ArrayList<Desenvolvedor> desenvolvedores) {
//		this.desenvolvedores = desenvolvedores;
//	}
//
//	public void adcionaDesenvolvedor(Desenvolvedor desenvolvedor){
//	 	if (desenvolvedores.indexOf(desenvolvedor)==-1)
//	 		desenvolvedores.add(desenvolvedor);
//	}
	
}
