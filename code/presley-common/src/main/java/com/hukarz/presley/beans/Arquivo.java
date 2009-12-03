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
	private String nome;
	private String enderecoServidor;
	private String texto;
	private int qtdPalavrasTotal;
	private Map<String, Integer> termosSelecionados;
	private String enderecoLog;

	public Arquivo(String nome) {
		this.nome = nome;
		this.texto = "";
		this.termosSelecionados = new HashMap<String, Integer>();
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

	public String getEnderecoServidor() {
		return enderecoServidor;
	}

	public void setEnderecoServidor(String enderecoServidor) {
		this.enderecoServidor = enderecoServidor;
	}

	public String getTexto() throws IOException{
		// FIXME: getter não altera o estado do objeto
		if (texto.equals("")){
			File file = new File(this.enderecoServidor);
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line = reader.readLine()) != null) {
				sb.append(line + " ");
			}

			fileReader.close();
			reader.close();

			this.texto = sb.toString();
		} 
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public boolean localizarEndereco(String diretorioProjeto){
		boolean achou = false;
		File baseFolder = new File(diretorioProjeto);  
		File[] files = baseFolder.listFiles();
		
		for (int i = 0; i < files.length; i++) {  
			File file = files[i];

			if (file.getPath().indexOf(".") == -1) {
				localizarEndereco(file.getPath());
			}

			if (file.getPath().endsWith(nome)) {  
				enderecoServidor = file.getPath();
				ajustarBarrasEndereco('\\', '/');
				achou = true;
				break;
			}  
		}		

		return achou;
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

	public void ajustarBarrasEndereco(char barraOld, char barraNew) {
		String endereco = enderecoServidor ;
		enderecoServidor = "";

		for(int x = 0; x < endereco.length(); x++){
			if (endereco.charAt(x) == barraOld) {
				enderecoServidor += barraNew;
			}
			else {
				enderecoServidor += endereco.charAt(x);
			}
		}
	}

	public String getEnderecoLog() {
		return enderecoLog;
	}

	public void setEnderecoLog(String enderecoLog) {
		this.enderecoLog = enderecoLog;
	}	

}
