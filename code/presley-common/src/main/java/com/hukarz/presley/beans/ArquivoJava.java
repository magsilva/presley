package com.hukarz.presley.beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ArquivoJava extends Arquivo {

	private static final long serialVersionUID = 1L;
	private Projeto projeto;
	
	public ArquivoJava(String nome, Projeto projeto) {
		super(nome);
		this.projeto = projeto;
	}

	@Override
	public String getTexto() throws IOException {
		String textoRetorno = "";

		//BufferedReader in = new BufferedReader(new FileReader( getEnderecoServidor() ));
	   URL url = new URL(getEnderecoServidor());
	   URLConnection conn = url.openConnection();
	   BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

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

	// -> Rotina para encontrar o endereço do arquivo no servidor SVN
	public void setEnderecoServidor(String enderecoArquivo) {
		String enderecoSVN = projeto.getEndereco_Servidor_Leitura() + 
				enderecoArquivo.substring(enderecoArquivo.indexOf(projeto.getNome()) + projeto.getNome().length() +1, enderecoArquivo.length());
		
		super.setEnderecoServidor(enderecoSVN);
		ajustarBarrasEndereco('\\', '/');
	}

	
}
