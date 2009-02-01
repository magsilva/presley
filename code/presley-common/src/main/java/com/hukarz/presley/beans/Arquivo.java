package com.hukarz.presley.beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Arquivo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String nome, enderecoServidor, texto ;
	private int qtdPalavrasTotal;
	private Map<String, Integer> termosSelecionados = new HashMap<String, Integer>();
	
	public Arquivo(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome, boolean localizaEndereco, Projeto projeto) {
		this.nome = nome;
		if (localizaEndereco)
			localizaEndereco( projeto.getEndereco_Servidor_Leitura() );
	}

	public String getEnderecoServidor() {
		return enderecoServidor;
	}

	public void setEnderecoServidor(String enderecoServidor) {
		this.enderecoServidor = enderecoServidor;
	}

	public String getTexto() throws IOException{
		if (texto.equals("")){
			File file = new File(getEnderecoServidor());
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			
			String textoTemp = null;
			String textoArq = "";
			while( (textoTemp = reader.readLine()) != null )
				textoArq += textoTemp + " ";
				
			fileReader.close();
			reader.close();
			
			setTexto(textoArq) ;
			
			return textoArq;
		} else
			return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public void localizaEndereco(String diretorioProjeto){
		// diret�rio que ser� listado.  
		File baseFolder = new File(diretorioProjeto);  
		  
		// obtem a lista de arquivos  
		File[] files = baseFolder.listFiles();  
		for (int i = 0; i < files.length; i++) {  
		    File file = files[i];
		    
		    if (file.getPath().indexOf( "." ) == -1)
		    	localizaEndereco(file.getPath());

		    if (file.getPath().endsWith( nome )) {  
//		        enderecoServidor = file.getPath().replaceAll( "\\" , "/"); Dar erro
	        	enderecoServidor = "";
	        	String endereco = file.getPath();
		        for(int x=0; x< endereco.length();x++){
		        	if (endereco.charAt(x)=='\\' )
		        		enderecoServidor += "/";
		        	else
		        		enderecoServidor += endereco.charAt(x);
		        }
		        		
		        break;
		    }  
		}		
	}

	public void adicionaTermo(String palavra, Integer quantidade){
		termosSelecionados.put(palavra, quantidade);
	}
	
	public Map<String, Integer> getTermosSelecionados() {
		return termosSelecionados;
	}
	
	public void setTermosSelecionados(Map<String, Integer> termosSelecionados) {
		this.termosSelecionados = termosSelecionados;
	}

	public int getQtdPalavrasTotal() {
		return qtdPalavrasTotal;
	}
	
	public void setQtdPalavrasTotal(int qtdPalavrasTotal) {
		this.qtdPalavrasTotal = qtdPalavrasTotal;
	}
	
}