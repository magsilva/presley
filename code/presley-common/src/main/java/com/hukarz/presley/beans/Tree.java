package com.hukarz.presley.beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

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
	public Tree(Conhecimento raizConhecimento){
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
	public Item getFilho(Conhecimento conhecimento){
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
	public boolean adicionaFilho(Conhecimento conhecimento){
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
	public boolean removeFilho(Conhecimento conhecimento){
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
	 * Constroi uma árvore gráfica do tipo org.eclipse.swt.widgets.Tree a partir da atual estrura da árvore  
	 * @param parent é o Composite o qual essa arvore será filha 
	 * @param style é o estilo da árvore gráfica
	 * @return org.eclipse.swt.widgets.Tree é a arvore gráfica usada na view utilizada nese plugin do eclipse
	 */
	public org.eclipse.swt.widgets.Tree constroiArvoreGrafica(Composite parent, int style){
		org.eclipse.swt.widgets.Tree treeGrafico = new org.eclipse.swt.widgets.Tree(parent,style);
		org.eclipse.swt.widgets.TreeItem treeItemGrafico = new TreeItem(treeGrafico,style);
		treeItemGrafico.setText( raiz.getConhecimento().getNome() );
		ArrayList<Item> filhosModelo = this.getRaiz().getFilhos();
		if (filhosModelo!=null) 
			for (Item filho : filhosModelo) {
				constroiArvoreGraficaHelper(treeItemGrafico, filho);	
			}
				
		return treeGrafico;
	}
	
	/**
	 * Método auxiliar do método anterior que constrói a arvore. Este percore a arvore de forma recursiva em pre-ordem.
	 * @param arvoreGrafica é a árvore gráfica que será construía
	 * @param arvoreModelo é o modelo de árvore a partir do qual ser´construída a árvore gráfica
	 */
	private void constroiArvoreGraficaHelper(org.eclipse.swt.widgets.TreeItem arvoreGrafica, Item arvoreModelo){
		if (arvoreModelo==null) {
			return;
		}
		
		//PROCESSAMENTO
		org.eclipse.swt.widgets.TreeItem novoItemGrafico = new TreeItem(arvoreGrafica, arvoreGrafica.getStyle());
		novoItemGrafico.setData( arvoreModelo.getConhecimento() );
		novoItemGrafico.setText(arvoreModelo.getConhecimento().getNome());
		
		ArrayList<Item> filhos = arvoreModelo.getFilhos(); 
		if (filhos==null) {
			constroiArvoreGraficaHelper(novoItemGrafico, null);//Percore da Esquerda para Direita
		}else{
			for (Item item : filhos) {
				constroiArvoreGraficaHelper(novoItemGrafico, item);//Percore da Esquerda para Direita	
			}	
		}
	}
	
	/**
	 * Localiza um ou mais filhos com o nome indicado. Percorre-se toda a arvore de forma recursiva em pré-ordem
	 * @param nome é o nome do filho que se deseja localizar
	 * @return a lista de filhos com o nome indicado que foram localizados ou null caso não encontre nenhum.
	 */
	public ArrayList<Item> localizaFilho(final Conhecimento conhecimento){
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
	private void localizaFilhoHelper(Item item, ArrayList<Item> localizados, final Conhecimento conhecimento){
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
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Conhecimento conhecimento = new Conhecimento();
		conhecimento.setNome("CONHECIMENTO");
		Tree tree = new Tree( conhecimento );
		
//		tree.adicionaFilho("Banco de Dados");
//		tree.getFilho("Banco de Dados").adicionaFilho("MySQL");
//		tree.getFilho("Banco de Dados").adicionaFilho("PostgresSQL");
//		tree.adicionaFilho("LP");
//		tree.getFilho("LP").adicionaFilho("JAVA");
//		tree.getFilho("LP").adicionaFilho("C++");
//		
//		Item item = tree.getFilho("LP");
//				
//		ArrayList<Item> localizados = tree.localizaFilho("C++");
//		
//		for (Item item2 : localizados) {
//			item2.adicionaFilho("SmallTalk");
//		}
//		if (localizados==null) {
//		}
		

	}

}
