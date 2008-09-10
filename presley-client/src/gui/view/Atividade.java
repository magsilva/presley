package gui.view;

import gui.view.comunication.ViewComunication;

import java.util.ArrayList;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
	LABEL,
	BUTTON,
	TREE
}

public class Atividade extends ViewPart {

	private ArrayList<Button> buttons;
	private ViewComunication viewComunication;

	public Atividade()
	{
		this.viewComunication = new ViewComunication();
		viewComunication.teste();		
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
	
	private void initComponents(Composite parent)
	{
		buttons = new ArrayList<Button>();

		ArrayList<String> atividadesNomes = viewComunication.getAtividades();
		
		for(int i=0; i < atividadesNomes.size(); i++)
		{
			final Button b = new Button(parent, SWT.RADIO);
			b.setText(atividadesNomes.get(i));
			b.setLocation(0, i*25);
			b.setSize(200, 25);
			this.buttons.add(b);	
		}
	
		for(final Button b : buttons)
		{
			b.addMouseListener(new MouseListener(){
				
				public void mouseDoubleClick(MouseEvent e) {
									
				}
	
				public void mouseDown(MouseEvent e) {
					String selection = b.getText();
					Composite parent = b.getParent();
					int depoisButoes = viewComunication.getAtividades().size()*25;		
					
					System.out.println(selection);
					
					parent.setRedraw(false);
											
					Label conhecimentoLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
					conhecimentoLabel.setText("Conhecimentos Envolvidos");
					conhecimentoLabel.setLocation(0, depoisButoes + 5);
					conhecimentoLabel.setSize(200, 20);	
									
					final Tree conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
					conhecimentoTree.setLocation(0, depoisButoes + 30);
					conhecimentoTree.setSize(200, 150);
					
						ArrayList<String> conhecimentos = viewComunication.getConhecimentosEnvolvidos(selection);
						if(conhecimentos != null)
							for(String c : conhecimentos)
							{
								TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
								item.setText(c);
							}				

					Label problemaLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
					problemaLabel.setText("Problemas Encontrados");
					problemaLabel.setLocation(0, depoisButoes + 185);
					problemaLabel.setSize(200, 25);
					
					final Tree problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
					problemaTree.setLocation(0, depoisButoes + 210);
					problemaTree.setSize(200, 150);
					
						ArrayList<String> problemas = viewComunication.getProblemas(selection);
						
						if(problemas != null)
							for(String c : problemas)
							{
								TreeItem item = new TreeItem(problemaTree, SWT.NONE);
								item.setText(c);
							}
											
						parent.update();
						parent.setRedraw(true);
						parent.redraw();		
				}
				
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}				
			});
		}		
	}
}
