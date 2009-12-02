package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Classe que representa os nós da árvore da classe presley.util.Tree.
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
	 * Construtor com o nome do nó como parametro
	 * @param nome é o nome deste nó
	 */
	
	public Item(Item pai, Conhecimento conhecimento){
		this.conhecimento=conhecimento;
		this.pai=pai;
		filhos = new ArrayList<Item>();
	}
	
	/**
	 * Retorna o nome deste nó
	 * @return é o nome deste nó
	 */
	public Conhecimento getConhecimento(){
		return conhecimento;
	}
	
	/**
	 * Informa se este nó possui filhos
	 * @return true se existem filhos ou false caso contrário
	 */
	public boolean temFilhos(){
		if (filhos.isEmpty())
			return false;
		return true;
	}
	
	/**
	 * Retorna o nó que é pai imediato (um nível a cima) deste nó
	 * @return o nó pai
	 */
	public Item getPai(){
		return pai;
	}
	
	/**
	 * Retorna o filho que tem como nome o indicado no parametro
	 * @param nome é o nome do nó
	 * @return o filho que tem o nome do parametro ou null se não existir
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
	 * Retorna todos os filhos imediados (um nível abaixo) deste nó
	 * @return um array contendo todos os nós deste nó
	 */
	public ArrayList<Item> getFilhos(){
		if (filhos.isEmpty()) {
			return null;
		}
		return filhos;
	}
	
	/**
	 * Adiciona um novo filho deste nó da árvore.
	 * @param nome é o nome deste nó da árvore
	 * @return true se foi adicionado com sucesso e false caso contrário
	 */
	public boolean adicionaFilho(Conhecimento conhecimento){
		try{
			if (temFilhos()) {
				for (Item item : filhos) {
					if (item.getConhecimento().getNome().equals( conhecimento.getNome() )) {
						throw new Exception ("Já existe nó chamado ' "+conhecimento.getNome()+" '.");
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
	 * Remove o filho que tem o nome indicado no parâmetro;
	 * @param nome é o nome do nó da árvore
	 * @return true se foi removido com sucesso e false caso contrário
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
