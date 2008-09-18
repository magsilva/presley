package beans;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Representa uma estruta de dados do tipo �rvore e serve para representar a estrutura da ontologia.
 * 
 * @author JP
 *
 */
public class Tree {
	private Item raiz;
	
	/**
	 * Construtor que inicializa uma arvore com uma raiz com nome do parametro
	 * @param raizNome � o nome da raiz da arvore
	 */
	public Tree(String raizNome){
		raiz = new Item(null,raizNome);
	}
	
	/**
	 * Informa se o n� atual possui filhos 
	 * @return true se possuir filhos e false caso contr�rio
	 */
	public boolean temFilhos(){
		if (raiz.getFilhos().isEmpty())
			return false;
		return true;
	}
	
	/**
	 * Retorna o no raiz da arvore
	 * @return Tree � o n� raiz da arvore
	 */
	public Item getRaiz(){
		return raiz;
	}
	
	/**
	 * Retorna o filho com nome indicado
	 * @param nome � o nome do n� filho
	 * @return Item � o n� filho. � null se n�o existir n� com o nome indicado
	 */
	public Item getFilho(String nome){
		Item filho = null;
		if (raiz.temFilhos()) {
			for (Item item : raiz.getFilhos()) {
				if (item.getNome().equals(nome)) {
					filho = item;
					break;
				}
			}	
		}
		return filho;
	}
	
	
	/**
	 * Adiciona um novo filho deste n� da �rvore.
	 * @param nome � o nome deste n� da �rvore
	 * @return true se foi adicionado com sucesso e false caso contr�rio
	 */
	public boolean adicionaFilho(String nome){
		try{
			if (raiz.temFilhos()) {
				for (Item item : raiz.getFilhos()) {
					if (item.getNome().equals(nome)) {
						throw new Exception ("J� existe n� chamado ' "+nome+" '.");
					}
				}	
			}
			raiz.adicionaFilho(nome);
		}catch(Exception e){
			System.err.println("ERRO Tree: "+e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Remove o filho que tem o nome indicado no par�metro;
	 * @param nome � o nome do n� da �rvore
	 * @return true se foi removido com sucesso e false caso contr�rio
	 */
	public boolean removeFilho(String nome){
		int index = 0;
		if (raiz.temFilhos()) {
			for (Item item : raiz.getFilhos()) {
				index++;
				if (item.getNome().equals(nome)) {
					raiz.removeFilho(nome);
					return true;
				}
			}	
		}
		
		return false;
	}
	
	/**
	 * Constroi uma �rvore gr�fica do tipo org.eclipse.swt.widgets.Tree a partir da atual estrura da �rvore  
	 * @param parent � o Composite o qual essa arvore ser� filha 
	 * @param style � o estilo da �rvore gr�fica
	 * @return org.eclipse.swt.widgets.Tree � a arvore gr�fica usada na view utilizada nese plugin do eclipse
	 */
	public org.eclipse.swt.widgets.Tree constroiArvoreGrafica(Composite parent, int style){
		org.eclipse.swt.widgets.Tree treeGrafico = new org.eclipse.swt.widgets.Tree(parent,style);
		org.eclipse.swt.widgets.TreeItem treeItemGrafico = new TreeItem(treeGrafico,style);
		treeItemGrafico.setText(raiz.getNome());
		ArrayList<Item> filhosModelo = this.getRaiz().getFilhos();
		if (filhosModelo!=null) 
			for (Item filho : filhosModelo) {
				constroiArvoreGraficaHelper(treeItemGrafico, filho);	
			}
				
		return treeGrafico;
	}
	
	/**
	 * M�todo auxiliar do m�todo anterior que constr�i a arvore. Este percore a arvore de forma recursiva em pre-ordem.
	 * @param arvoreGrafica � a �rvore gr�fica que ser� constru�a
	 * @param arvoreModelo � o modelo de �rvore a partir do qual ser�constru�da a �rvore gr�fica
	 */
	private void constroiArvoreGraficaHelper(org.eclipse.swt.widgets.TreeItem arvoreGrafica, Item arvoreModelo){
		if (arvoreModelo==null) {
			return;
		}
		
		//PROCESSAMENTO
		org.eclipse.swt.widgets.TreeItem novoItemGrafico = new TreeItem(arvoreGrafica, arvoreGrafica.getStyle());
		novoItemGrafico.setText(arvoreModelo.getNome());
		
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
	 * Localiza um ou mais filhos com o nome indicado. Percorre-se toda a arvore de forma recursiva em pr�-ordem
	 * @param nome � o nome do filho que se deseja localizar
	 * @return a lista de filhos com o nome indicado que foram localizados ou null caso n�o encontre nenhum.
	 */
	public ArrayList<Item> localizaFilho(final String nome){
		ArrayList<Item> filhosLocalizados = new ArrayList<Item>();
		
		if (raiz.getNome().equals(nome)) {
			filhosLocalizados.add(raiz);
		}
		
		if (raiz.getFilhos()==null) {
			return null;
		}
		
		for (Item item : raiz.getFilhos()) {
			localizaFilhoHelper(item, filhosLocalizados, nome);
		}
		if (filhosLocalizados.isEmpty()) {
			return null;
		}
		return filhosLocalizados;
	}

	/**
	 * M�todo auxiliar do m�todo anterior que localizar um n� que ir� percorer a �rvore de forma recursiva em pr�-ordem.
	 * @param item � o n� atual da �rvore modelo
	 * @param localizados � a lista de n�s j� localizados
	 * @param nome � o nome do n� que se deseja localizar
	 */
	private void localizaFilhoHelper(Item item, ArrayList<Item> localizados, final String nome){
		if (item==null) {
			return;
		}
		//PROCESSAMENTO PRINCIPAL
		if (item.getNome().equals(nome)) {
			localizados.add(item);
		}
		
		ArrayList<Item> filhos = item.getFilhos();
		if (filhos!=null) {
			for (Item filho : filhos) {
				localizaFilhoHelper(filho, localizados, nome);		
			}
		}else{
			localizaFilhoHelper(null, localizados, nome);
		}
	}
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tree tree = new Tree("CONHECIMENTO");
		tree.adicionaFilho("Banco de Dados");
		tree.getFilho("Banco de Dados").adicionaFilho("MySQL");
		tree.getFilho("Banco de Dados").adicionaFilho("PostgresSQL");
		tree.adicionaFilho("LP");
		tree.getFilho("LP").adicionaFilho("JAVA");
		tree.getFilho("LP").adicionaFilho("C++");
		
				
		ArrayList<Item> localizados = tree.localizaFilho("C++");
		
		for (Item item2 : localizados) {
			item2.adicionaFilho("SmallTalk");
			System.out.println("Localizado: "+item2.getNome()+", Pai: "+item2.getPai().getNome());
		}
		if (localizados==null) {
			System.out.println("Nao Localizado. ");			
		}
		

	}

}
