package com.hukarz.presley.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ArquivoJava extends Arquivo {

	private static final long serialVersionUID = 1L;
	private Projeto projeto;
	
	public ArquivoJava(String nome, Projeto projeto) {
		super(nome);
		this.projeto = projeto;
	}

	@Override
	public String getTexto() throws IOException {
		StringBuilder textoRetorno = new StringBuilder();

		File file = new File(getEnderecoServidor());
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);

		String texto =  null;  
		boolean comentarioBloco = false ;
		while ((texto = reader.readLine()) != null) {  

			for(int x=0; x < texto.length();x++){
				if (x <= texto.length()-2 && texto.charAt(x)=='/' && texto.charAt(x+1)=='/') {
					textoRetorno.append( " " + texto.substring(x+2).trim() );
					break;
				}

				if (x <= texto.length()-2 && texto.charAt(x)=='/' && texto.charAt(x+1)=='*' ){
					textoRetorno.append(" "); 
					comentarioBloco = true ;
				}

				if (x <= texto.length()-2 && texto.charAt(x)=='*' && texto.charAt(x+1)=='/' )
					comentarioBloco = false ;

				if (comentarioBloco && texto.charAt(x)!='*' && texto.charAt(x)!='/' ){
					textoRetorno.append(texto.charAt(x)); 
				}
			}

		}
		
		return textoRetorno.toString();
	}

	public void setEnderecoServidor(String enderecoArquivo) {
		super.setEnderecoServidor(projeto.getEndereco_Servidor_Projeto() + 
				enderecoArquivo.replaceFirst("/"+projeto.getNome() + "/", ""));
		
		setEnderecoLog(enderecoArquivo);
	}

	// -> Rotina para encontrar o endereço do arquivo no servidor SVN
	public void setEnderecoLog(String enderecoLog) {
		super.setEnderecoLog( projeto.getDiretorio_Subversion() +
				enderecoLog.replaceFirst("/"+projeto.getNome() , "") )  ;
	}

}
