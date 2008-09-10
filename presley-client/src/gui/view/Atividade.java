package gui.view;

import gui.view.comunication.ViewComunication;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;


public class Atividade extends ViewPart {

	private ArrayList<Button> buttons;
	private Label conhecimentoLabel;
	private Tree conhecimentoTree;
	private Label problemaLabel;
	private Tree problemaTree;
	
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
		
		conhecimentoLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
		conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
		problemaLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
		problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
	
		for(final Button b : buttons)
		{
			b.addMouseListener(new MouseListener(){
				
				public void mouseDoubleClick(MouseEvent e) {
									
				}
	
				public void mouseDown(MouseEvent e) {
					String selection = ((Button)e.getSource()).getText();
					int depoisButoes = viewComunication.getAtividades().size()*25;		
										
					conhecimentoLabel.setText("Conhecimentos Envolvidos");
					conhecimentoLabel.setLocation(0, depoisButoes + 5);
					conhecimentoLabel.setSize(200, 20);	
									
					conhecimentoTree.setLocation(0, depoisButoes + 30);
					conhecimentoTree.setSize(200, 150);
					
					ArrayList<String> conhecimentos = viewComunication.getConhecimentosEnvolvidos(selection);
					
					conhecimentoTree.removeAll();
						if(conhecimentos != null)
							for(String c : conhecimentos)
							{
								TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
								item.setText(c);
							}				

					problemaLabel.setText("Problemas Encontrados");
					problemaLabel.setLocation(0, depoisButoes + 185);
					problemaLabel.setSize(200, 20);
					
					problemaTree.setLocation(0, depoisButoes + 210);
					problemaTree.setSize(200, 150);
					
						ArrayList<String> problemas = viewComunication.getProblemas(selection);
					
					problemaTree.removeAll();						
						if(problemas != null)
							for(String c : problemas)
							{
								TreeItem item = new TreeItem(problemaTree, SWT.NONE);
								item.setText(c);
							}
								
				}
				
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}				
			});
		}		
	}
}
