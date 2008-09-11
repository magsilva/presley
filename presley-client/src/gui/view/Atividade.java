package gui.view;

import gui.view.comunication.ViewComunication;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.jdt.internal.ui.refactoring.MessageWizardPage;
import org.eclipse.jdt.internal.ui.refactoring.TextInputWizardPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.MessageLine;
import org.eclipse.ui.internal.dialogs.ImportWizard;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.core.internal.runtime.Messages;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.refresh.IRefreshMonitor;
import org.eclipse.core.resources.refresh.IRefreshResult;
import org.eclipse.core.resources.refresh.RefreshProvider;


public class Atividade extends ViewPart {

	private ArrayList<Button> buttons;
	private HashMap<String,Tree> conhecimentosTree;
	private HashMap<String,Tree> problemasTree;
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
		
		final Button novaAtividade = new Button(parent, SWT.ICON);
		novaAtividade.setLocation(0,0);
		novaAtividade.setSize(16, 16);
	//	Image novo = new Image(novaAtividade.getDisplay(), "C:/Documents and Settings/alyson/Desktop/project_client/src/gui/view/add.gif");
	//	novaAtividade.setImage(novo);
		

	//	novaAtividade.setImage(new Image(null, "icons/novo.gif"));
		
		novaAtividade.addMouseListener(
				new MouseListener()
				{
					public void mouseDoubleClick(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					public void mouseDown(MouseEvent arg0) {
									
							//colocar a tela para captura das atividades
					}

					public void mouseUp(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
				}
				);
		
		for(int i=0; i < atividadesNomes.size(); i++)
		{
			final Button b = new Button(parent, SWT.RADIO);
			b.setText(atividadesNomes.get(i));
			b.setLocation(0, i*25 + 20);
			b.setSize(200, 25);
			this.buttons.add(b);	
		}
		int depoisButoes = viewComunication.getAtividades().size()*25 + 20;		
		
		final Label conhecimentoLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
		conhecimentoLabel.setText("Conhecimentos Envolvidos");
		conhecimentoLabel.setLocation(0, depoisButoes + 5);
		conhecimentoLabel.setSize(200, 20);	
		conhecimentoLabel.setVisible(false);
						
		conhecimentosTree = new HashMap<String, Tree>();
		
		for(String selection : viewComunication.getAtividades())
		{
			final Tree conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
			conhecimentoTree.setLocation(0, depoisButoes + 30);
			conhecimentoTree.setSize(200, 150);
			conhecimentoTree.setVisible(false);
				ArrayList<String> conhecimentos = viewComunication.getConhecimentosEnvolvidos(selection);
				if(conhecimentos != null)
					for(String c : conhecimentos)
					{
						TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
						item.setText(c);
					}	
				
				conhecimentosTree.put(selection, conhecimentoTree);
		}
		
		final Label problemaLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
		problemaLabel.setText("Problemas Encontrados");
		problemaLabel.setLocation(0, depoisButoes + 185);
		problemaLabel.setSize(200, 20);
		problemaLabel.setVisible(false);
		
		problemasTree = new HashMap<String, Tree>();
		
		for(String selection : viewComunication.getAtividades())
		{
			final Tree problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
			problemaTree.setLocation(0, depoisButoes + 210);
			problemaTree.setSize(200, 150);
			problemaTree.setVisible(false);
			problemaTree.setEnabled(false);
			
				ArrayList<String> problemas = viewComunication.getProblemas(selection);
				
				if(problemas != null)
					for(String c : problemas)
					{
						TreeItem item = new TreeItem(problemaTree, SWT.NONE);
						item.setText(c);
					}
				
				problemasTree.put(selection, problemaTree);
		}
			
	
		for(final Button b : buttons)
		{
			b.addMouseListener(new MouseListener(){
				
				public void mouseDoubleClick(MouseEvent e) {
									
				}
	
				public void mouseDown(MouseEvent e) {
					Set<String> keys = conhecimentosTree.keySet();
				
						for(String key : keys)
						{
							conhecimentosTree.get(key).setVisible(false);
							conhecimentosTree.get(key).setEnabled(false);
							
							problemasTree.get(key).setVisible(false);
							problemasTree.get(key).setEnabled(false);
						}
						
						problemaLabel.setVisible(true);
						conhecimentoLabel.setVisible(true);
						
						conhecimentosTree.get( b.getText()).setVisible(true);
						conhecimentosTree.get( b.getText()).setEnabled(true);
						
						problemasTree.get( b.getText()).setVisible(true);
						problemasTree.get( b.getText()).setEnabled(true);
					
						b.getParent().redraw();
				}
				
				public void mouseUp(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}				
			});
		}		
	}
}