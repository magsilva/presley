package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Classe que representa os n�s da �rvore da classe presley.util.Tree.
 * 
 * @author JP
 *
 */
public class Item implements Serializable{

	private static final long serialVersionUID = 20L;
	private Conhecimento conhecimento;
	private ArrayList<Item> filhos;
	private Item pai;
	
	/**
	 * Construtor com o nome do n� como parametro
	 * @param nome � o nome deste n�
	 */
	
	public Item(Item pai, Conhecimento conhecimento){
		this.conhecimento=conhecimento;
		this.pai=pai;
		filhos = new ArrayList<Item>();
	}
	
	/**
	 * Retorna o nome deste n�
	 * @return � o nome deste n�
	 */
	public Conhecimento getConhecimento(){
		return conhecimento;
	}
	
	/**
	 * Informa se este n� possui filhos
	 * @return true se existem filhos ou false caso contr�rio
	 */
	public boolean temFilhos(){
		if (filhos.isEmpty())
			return false;
		return true;
	}
	
	/**
	 * Retorna o n� que � pai imediato (um n�vel a cima) deste n�
	 * @return o n� pai
	 */
	public Item getPai(){
		return pai;
	}
	
	/**
	 * Retorna o filho que tem como nome o indicado no parametro
	 * @param nome � o nome do n�
	 * @return o filho que tem o nome do parametro ou null se n�o existir
	 */
	public Item getFilho(Conhecimento conhecimento){
		Item filho = null;
		for (Item item : filhos) {
			if (item.getConhecimento().getNome().equals(conhecimento.getNome())  ) {
				filho = item;
				break;
			}
		}
		return filho;
	}
	
	/**
	 * Retorna todos os filhos imediados (um n�vel abaixo) deste n�
	 * @return um array contendo todos os n�s deste n�
	 */
	public ArrayList<Item> getFilhos(){
		if (filhos.isEmpty()) {
			return null;
		}
		return filhos;
	}
	
	/**
	 * Adiciona um novo filho deste n� da �rvore.
	 * @param nome � o nome deste n� da �rvore
	 * @return true se foi adicionado com sucesso e false caso contr�rio
	 */
	public boolean adicionaFilho(Conhecimento conhecimento){
		try{
			if (temFilhos()) {
				for (Item item : filhos) {
					if (item.getConhecimento().getNome().equals( conhecimento.getNome() )) {
						throw new Exception ("J� existe n� chamado ' "+conhecimento.getNome()+" '.");
					}
				}	
			}
			filhos.add(new Item(this,conhecimento));
		}catch(Exception e){
			System.err.println("ERRO Item: "+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Remove o filho que tem o nome indicado no par�metro;
	 * @param nome � o nome do n� da �rvore
	 * @return true se foi removido com sucesso e false caso contr�rio
	 */
	public boolean removeFilho(Conhecimento conhecimento){
		int index = 0;
		for (Item item : filhos) {
			
			if (item.getConhecimento().getNome().equals( conhecimento.getNome() )) {
				filhos.remove(index);
				return true;
			}
			index++;
		}
		return false;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
