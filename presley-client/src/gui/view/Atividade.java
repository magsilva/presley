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

enum COMPONENT {
	SPACE,
	LABEL,
	BUTTON,
	TREE
}

public class Atividade extends ViewPart {

	private Point location;
	private Point size;
	private String selection;
	
	public Atividade() {
		location = new Point(0,0);
		size = new Point(0,0);
		selection = "";
	}
	
	
	
	public void createPartControl(Composite parent) 
	{		
		parent.setLayout(null);
		initComponents(parent);
	}

/*	public void createPartControl(Composite parent) {
		
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
*/
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	private Point getNextLocation()
	{
		return this.location;
	}
	
	private Point getSize(COMPONENT component)
	{
		if(component == COMPONENT.BUTTON)
		{
			this.size.x = 300;
			this.size.y = 25;
		}
		else if(component == COMPONENT.LABEL)
		{
			this.size.x = 180;
			this.size.y = 25;
		}else if(component == COMPONENT.SPACE)
		{
			this.size.x = 0;
			this.size.y = 5;
		}else if(component == COMPONENT.TREE)
		{
			this.size.x = 300;
			this.size.y = 150;
		}		
		
		this.location.y += this.size.y;
		
		return this.size;
	}

	private void initComponents(Composite parent)
	{
		this.location = new Point(0,0);		
		this.size = new Point(0,0);

		ArrayList<String> atividadesNomes = ViewComunication.getAtividades();
		
		if(atividadesNomes.size() > 0)
			this.selection = atividadesNomes.get(0);
		
		for(String e : atividadesNomes)
		{
			Button b = new Button(parent, SWT.RADIO);
			b.setText(e);
			b.setLocation(this.getNextLocation());
			b.setSize(this.getSize(COMPONENT.BUTTON));
		}
		
		this.getSize(COMPONENT.SPACE);	
		
		Label conhecimentoLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
		conhecimentoLabel.setText("Conhecimentos Envolvidos");
		conhecimentoLabel.setLocation(this.getNextLocation());
		conhecimentoLabel.setSize(this.getSize(COMPONENT.LABEL));		
		
		final Tree conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		conhecimentoTree.setLocation(this.getNextLocation());
		conhecimentoTree.setSize(this.getSize(COMPONENT.TREE));
		
			ArrayList<String> conhecimentos = ViewComunication.getConhecimentosEnvolvidos(this.selection);
				for(String c : conhecimentos)
				{
					TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
					item.setText(c);
				}			

		this.getSize(COMPONENT.SPACE);	
		
		Label problemaLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
		problemaLabel.setText("Problemas Encontrados");
		problemaLabel.setLocation(this.getNextLocation());
		problemaLabel.setSize(this.getSize(COMPONENT.LABEL));
		
		final Tree problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
		problemaTree.setLocation(this.getNextLocation());
		problemaTree.setSize(this.getSize(COMPONENT.TREE));
		
			ArrayList<String> conhecimentos = ViewComunication.getProblemas(this.selection);
				for(String c : conhecimentos)
				{
					TreeItem item = new TreeItem(problemaTree, SWT.NONE);
					item.setText(c);
				}			
	}
}
