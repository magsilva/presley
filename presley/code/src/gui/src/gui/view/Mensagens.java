package gui.view;

/* Desenvolvido por Leandro Carlos e Samara Martins */


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

public class Mensagens extends ViewPart {

	public Mensagens() {
		// TODO Auto-generated constructor stub
	}

	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

		final Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("Problema");	
		
		final TreeItem radio1 = new TreeItem(item, SWT.NONE);
		radio1.setText("Bope");

		final TreeItem radio2 = new TreeItem(item,  SWT.NONE);
	    radio2.setText("GigaBug");	

	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
