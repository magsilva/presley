package beans;

import java.util.ArrayList;
/**
 * Classe que representa os nós da árvore da classe presley.util.Tree.
 * 
 * @author JP
 *
 */
public class Item {

	private String nome;
	private ArrayList<Item> filhos;
	private Item pai;
	
	/**
	 * Construtor com o nome do nó como parametro
	 * @param nome é o nome deste nó
	 */
	
	public Item(Item pai, String nome){
		this.nome=nome;
		this.pai=pai;
		filhos = new ArrayList<Item>();
	}
	
	/**
	 * Retorna o nome deste nó
	 * @return é o nome deste nó
	 */
	public String getNome(){
		return nome;
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
	public Item getFilho(String nome){
		Item filho = null;
		for (Item item : filhos) {
			if (item.getNome().equals(nome)) {
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
	public boolean adicionaFilho(String nome){
		try{
			if (temFilhos()) {
				for (Item item : filhos) {
					if (item.getNome().equals(nome)) {
						throw new Exception ("Já existe nó chamado ' "+nome+" '.");
					}
				}	
			}
			filhos.add(new Item(this,nome));
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
	public boolean removeFilho(String nome){
		int index = 0;
		for (Item item : filhos) {
			index++;
			if (item.getNome().equals(nome)) {
				filhos.remove(index);
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
