package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Representa uma estruta de dados do tipo árvore e serve para representar a estrutura da ontologia.
 * 
 * @author JP
 *
 */
public class Tree implements Serializable{
	private static final long serialVersionUID = 10L;
	private Item raiz;
	
	/**
	 * Construtor que inicializa uma arvore com uma raiz com nome do parametro
	 * @param raizConhecimento é o nome da raiz da arvore
	 */
	public Tree(TopicoConhecimento raizConhecimento){
		raiz = new Item(null,raizConhecimento);
	}
	
	/**
	 * Informa se o nó atual possui filhos 
	 * @return true se possuir filhos e false caso contrário
	 */
	public boolean temFilhos(){
		if (raiz.getFilhos().isEmpty())
			return false;
		return true;
	}
	
	/**
	 * Retorna o no raiz da arvore
	 * @return Tree é o nó raiz da arvore
	 */
	public Item getRaiz(){
		return raiz;
	}
	
	/**
	 * Retorna o filho com nome indicado
	 * @param nome é o nome do nó filho
	 * @return Item é o nó filho. É null se não existir nó com o nome indicado
	 */
	public Item getFilho(TopicoConhecimento conhecimento){
		Item filho = null;
		if (raiz.temFilhos()) {
			for (Item item : raiz.getFilhos()) {
				if (item.getConhecimento().getNome().equals( conhecimento.getNome() )) {
					filho = item;
					break;
				}
			}	
		}
		return filho;
	}
	
	
	/**
	 * Adiciona um novo filho deste nó da árvore.
	 * @param nome é o nome deste nó da árvore
	 * @return true se foi adicionado com sucesso e false caso contrário
	 */
	public boolean adicionaFilho(TopicoConhecimento conhecimento){
		try{
			if (raiz.temFilhos()) {
				for (Item item : raiz.getFilhos()) {
					if (item.getConhecimento().getNome().equals( conhecimento.getNome() )) {
						throw new Exception ("Já existe nó chamado ' "+ conhecimento.getNome() +" '.");
					}
				}	
			}
			raiz.adicionaFilho( conhecimento );
		}catch(Exception e){
			System.err.println("ERRO Tree: "+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Remove o filho que tem o nome indicado no parâmetro;
	 * @param nome é o nome do nó da árvore
	 * @return true se foi removido com sucesso e false caso contrário
	 */
	public boolean removeFilho(TopicoConhecimento conhecimento){
		int index = 0;
		if (raiz.temFilhos()) {
			for (Item item : raiz.getFilhos()) {
				index++;
				if (item.getConhecimento().getNome().equals( conhecimento.getNome() )) {
					raiz.removeFilho(conhecimento);
					return true;
				}
			}	
		}
		
		return false;
	}
	
	
	/**
	 * Localiza um ou mais filhos com o nome indicado. Percorre-se toda a arvore de forma recursiva em pré-ordem
	 * @param nome é o nome do filho que se deseja localizar
	 * @return a lista de filhos com o nome indicado que foram localizados ou null caso não encontre nenhum.
	 */
	public ArrayList<Item> localizaFilho(final TopicoConhecimento conhecimento){
		ArrayList<Item> filhosLocalizados = new ArrayList<Item>();
		
		if (raiz.getConhecimento().getNome().equals( conhecimento.getNome() ))  {
			filhosLocalizados.add(raiz);
		}
		
		if (raiz.getFilhos()==null) {
			return null;
		}
		
		for (Item item : raiz.getFilhos()) {
			localizaFilhoHelper(item, filhosLocalizados, conhecimento);
		}
		if (filhosLocalizados.isEmpty()) {
			return null;
		}
		return filhosLocalizados;
	}

	/**
	 * Método auxiliar do método anterior que localizar um nó que irá percorer a árvore de forma recursiva em pré-ordem.
	 * @param item é o nó atual da árvore modelo
	 * @param localizados é a lista de nós já localizados
	 * @param nome é o nome do nó que se deseja localizar
	 */
	private void localizaFilhoHelper(Item item, ArrayList<Item> localizados, final TopicoConhecimento conhecimento){
		if (item==null) {
			return;
		}
		//PROCESSAMENTO PRINCIPAL
		if (item.getConhecimento().getNome().equals( conhecimento.getNome() ))  {
			localizados.add(item);
		}
		
		ArrayList<Item> filhos = item.getFilhos();
		if (filhos!=null) {
			for (Item filho : filhos) {
				localizaFilhoHelper(filho, localizados, conhecimento);		
			}
		}else{
			localizaFilhoHelper(null, localizados, conhecimento);
		}
	}


}
