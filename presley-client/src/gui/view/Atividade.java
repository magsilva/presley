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

	private  Point location;
	private  Point size;
	private ArrayList<Button> buttons;
	private ArrayList<Point> locations;
	private ArrayList<Point> sizes;
	

	public Atividade()
	{
		ViewComunication.teste();		
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
		return location;
	}
	
	private Point getSize(COMPONENT component)
	{
		if(component == COMPONENT.BUTTON)
		{
			size.x = 300;
			size.y = 25;
		}
		else if(component == COMPONENT.LABEL)
		{
			size.x = 180;
			size.y = 25;
		}else if(component == COMPONENT.TREE)
		{
			size.x = 300;
			size.y = 150;
		}		
		
		location.y += size.y;
		
		return size;
	}

	private void initComponents(Composite parent)
	{
		locations = new ArrayList<Point>();
		sizes = new ArrayList<Point>();
		location = new Point(0,0);		
		size = new Point(0,0);
		buttons = new ArrayList<Button>();

		ArrayList<String> atividadesNomes = ViewComunication.getAtividades();
		
		for(String e : atividadesNomes)
		{
			final Button b = new Button(parent, SWT.RADIO);
			b.setText(e);
			b.setLocation(getNextLocation());
			b.setSize(getSize(COMPONENT.BUTTON));
			this.buttons.add(b);	
		}
		
		locations.add(getNextLocation());
		sizes.add(getSize(COMPONENT.LABEL));
		
		locations.add(getNextLocation());
		sizes.add(getSize(COMPONENT.TREE));
		
		locations.add(getNextLocation());
		sizes.add(getSize(COMPONENT.LABEL));
		
		locations.add(getNextLocation());
		sizes.add(getSize(COMPONENT.TREE));
		
		for(final Button b : buttons)
		{
			b.addMouseListener(new MouseListener(){
				public void mouseDoubleClick(MouseEvent e) {
					// TODO Auto-generated method stub					
				}
	
				public void mouseDown(MouseEvent e) {
					String selection = b.getText();
					Composite parent = b.getParent();						
					
					Label conhecimentoLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
					conhecimentoLabel.setText("Conhecimentos Envolvidos");
					conhecimentoLabel.setLocation(locations.get(0));
					conhecimentoLabel.setSize(sizes.get(0));		
					
					final Tree conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
					conhecimentoTree.setLocation(locations.get(1));
					conhecimentoTree.setSize(sizes.get(1));
					
						ArrayList<String> conhecimentos = ViewComunication.getConhecimentosEnvolvidos(selection);
						if(conhecimentos != null)
							for(String c : conhecimentos)
							{
								TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
								item.setText(c);
							}			

					
					Label problemaLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
					problemaLabel.setText("Problemas Encontrados");
					problemaLabel.setLocation(locations.get(2));
					problemaLabel.setSize(sizes.get(2));
					
					final Tree problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
					problemaTree.setLocation(locations.get(3));
					problemaTree.setSize(sizes.get(3));
					
						ArrayList<String> problemas = ViewComunication.getProblemas(selection);
						
						if(problemas != null)
							for(String c : problemas)
							{
								TreeItem item = new TreeItem(problemaTree, SWT.NONE);
								item.setText(c);
							}
						parent.update();
						parent.redraw();		
				}
				
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}				
			});
		}		
	}
}
