package gui.view;

import gui.view.comunication.ViewComunication;

import java.awt.Event;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.commands.KeyConfigurationEvent;
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class Atividade extends ViewPart {

	private ArrayList<Button> buttons;
	private HashMap<String,Tree> conhecimentosTree;
	private HashMap<String,Tree> problemasTree;
	private ViewComunication viewComunication;
	private Label conhecimentoLabel;
	private Label problemaLabel;
	private Composite panel;
	private Text textAtividade;
	private boolean textoHabilitado = false;
	
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
	
	private void setListener(final Button button)
	{
		button.addMouseListener(new MouseListener(){
			
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
					
					conhecimentosTree.get( button.getText()).setVisible(true);
					conhecimentosTree.get( button.getText()).setEnabled(true);
					
					problemasTree.get( button.getText()).setVisible(true);
					problemasTree.get( button.getText()).setEnabled(true);
				
					button.getParent().redraw();
			}
			
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}				
		});
	}
	
	private void atualizaPosicaoComponentes()
	{

				for(int i=0; i < this.buttons.size(); i++)
				{
					buttons.get(i).setLocation(0, i*25);
					buttons.get(i).setSize(200, 25);
					this.buttons.get(i).getParent().redraw();
				}		
			
		panel.update();
		panel.redraw();
		panel.getParent().update();
		panel.getParent().redraw();
	}
		
	private void initComponents(final Composite parent)
	{
		buttons = new ArrayList<Button>();
		
		Button evento = new Button(parent, SWT.NONE);
		evento.setLocation(4, 4);
		evento.setSize(16, 16);
		
		evento.addMouseListener(
				new MouseListener(){

					public void mouseDoubleClick(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					public void mouseDown(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					public void mouseUp(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
			
		panel = new Composite(parent, SWT.V_SCROLL | SWT.BORDER);
		panel.setLocation(0, 25);
		panel.setSize(200, 100);
		panel.setVisible(true);
				
		ArrayList<String> atividadesNomes = viewComunication.getAtividades();
				
		textAtividade = new Text(parent, SWT.BORDER);
		textAtividade.setSize(200, 25);
		textAtividade.setLocation(25, 0);
		textAtividade.setVisible(true);
		textAtividade.setTextLimit(200);
		
	    FocusListener focusListener = new FocusListener() {
	        public void focusGained(FocusEvent e) {
	        	/*Text t = (Text) e.widget;
	        	String novaAtividade = t.getText();						
				
				viewComunication.addAtividade(novaAtividade, null, null);						
				textAtividade.setText("");
										
				final Button novoButao = new Button(panel, SWT.RADIO);
				novoButao.setText(novaAtividade);
				buttons.add(novoButao);
				
				final Tree conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				conhecimentoTree.setVisible(false);						
				conhecimentosTree.put(novaAtividade, conhecimentoTree);
				
				final Tree problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				problemaTree.setVisible(false);						
				problemasTree.put(novaAtividade, conhecimentoTree);
				
				setListener(novoButao);		
				atualizaPosicaoComponentes();	*/
	        }
	        public void focusLost(FocusEvent e) {
	        	
	        	Text t = (Text) e.widget;
	        	String novaAtividade =  t.getText();								
				
				viewComunication.addAtividade(novaAtividade, null, null);						
				textAtividade.setText("");
										
				final Button novoButao = new Button(panel, SWT.RADIO);
				novoButao.setText(novaAtividade);
				buttons.add(novoButao);
				
				final Tree conhecimentoTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				conhecimentoTree.setVisible(false);						
				conhecimentosTree.put(novaAtividade, conhecimentoTree);
				
				final Tree problemaTree = new Tree(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				problemaTree.setVisible(false);						
				problemasTree.put(novaAtividade, conhecimentoTree);
				
				setListener(novoButao);		
				atualizaPosicaoComponentes();	
	            }
	          
	        };
	        
	   textAtividade.addFocusListener(focusListener);		
			
		for(int i=0; i < atividadesNomes.size(); i++)
		{
			final Button b = new Button(panel, SWT.RADIO);
			b.setText(atividadesNomes.get(i));
			b.setLocation(0, i*25);
			b.setSize(200, 25);
			this.buttons.add(b);
			setListener(b);
		}
		int depoisButoes = 120;	
	
		conhecimentoLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
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
		
		problemaLabel = new Label(parent, SWT.BORDER | SWT.CENTER);
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
	}
}