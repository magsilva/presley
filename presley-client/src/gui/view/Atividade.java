package gui.view;

import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

public class Atividade extends ViewPart {

	public Atividade() {
		// TODO Auto-generated constructor stub
	}

	public void createPartControl(Composite parent) {
		
		FillLayout fl = new FillLayout();
		fl.type = SWT.VERTICAL;
		parent.setLayout(null);	
		
		Button b1 = new Button(parent, SWT.RADIO); 
		b1.setText("Desenvolver");				
		b1.setSize(300, 25);

		Button b2 = new Button(parent, SWT.RADIO); 
		b2.setText("xxxxxxxxxxxx");
		b2.setLocation(0, 25);
		
		b2.setSize(300, 25);
		
		fl.spacing = 0;
		Label lb1 = new Label(parent, SWT.BORDER | SWT.CENTER); 		
		lb1.setText("Conhecimentos Envolvidos");
		
		lb1.setLocation(0, 55);
		lb1.setSize(180, 25);
		
	
		final Tree tree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		tree.setLocation(0, 80);
		tree.setSize(300, 100);
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("Oracle");	
		
		final TreeItem radio1 = new TreeItem(item, SWT.NONE);
		radio1.setText("PLSQL");

		final TreeItem radio2 = new TreeItem(item,  SWT.NONE);
	    radio2.setText("Views");	
	    
		final TreeItem radio3 = new TreeItem(item,  SWT.NONE);
	    radio3.setText("Data Blocks");	

		Label lb2 = new Label(parent, SWT.BORDER | SWT.CENTER); 		
		lb2.setText("Problemas Encontrados");
		lb2.setLocation(0, 185);
		lb2.setSize(180, 25);
		
		final Tree treeP = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		
		treeP.setLocation(0, 210);
		treeP.setSize(300, 100);
		TreeItem itemP = new TreeItem(treeP, SWT.NONE);
		itemP.setText("Views");	
		
		final TreeItem radio4 = new TreeItem(itemP, SWT.NONE);
		radio4.setText("Erro 1...");

		final TreeItem radio5 = new TreeItem(itemP,  SWT.NONE);
		radio5.setText("Erro 2 ...");	

	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
