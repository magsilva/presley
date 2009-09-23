package com.hukarz.presley.client.gui.component;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import com.hukarz.presley.beans.Item;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;

public class ArvoreGraficaDeConhecimentos {
	/**
	 * Retorna a ontolgia dos conhecimentos.
	 * @return Tree � a arvore de conhecimentos.
	 * @throws ConhecimentoInexistenteException 
	 */
	public org.eclipse.swt.widgets.Tree getArvoreGraficaDeConhecimentos(
			Tree arvoreConhecimentos, Composite parent, int style) throws ConhecimentoInexistenteException{
		Item raiz = arvoreConhecimentos.getRaiz();
		
		org.eclipse.swt.widgets.Tree treeGrafico = new org.eclipse.swt.widgets.Tree(parent,style);
		org.eclipse.swt.widgets.TreeItem treeItemGrafico = new TreeItem(treeGrafico,style);
		treeItemGrafico.setText( raiz.getConhecimento().getNome() );
		ArrayList<Item> filhosModelo = raiz.getFilhos();
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

}
