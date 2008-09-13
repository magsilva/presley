package gui.view;

import gui.action.RunAdicionaAtividadeWizardAction;
import gui.view.comunication.ViewComunication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import beans.Desenvolvedor;


public class Atividade extends ViewPart {

	private ArrayList<Button> buttons;
	private HashMap<String,Tree> conhecimentosTree;
	private HashMap<String,Tree> problemasTree;
	private ViewComunication viewComunication;
	private Label conhecimentoLabel;
	private Label problemaLabel;
	private Composite panel;
	private Composite parentComposite;
	private RunAdicionaAtividadeWizardAction runAdicionaAtividade;
	
	public Atividade()
	{
		this.viewComunication = new ViewComunication();
		//this.viewComunication.teste();

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
				try{
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
					button.getParent().update();
					
				}catch(Exception e1){
					System.out.println("ERRO ERRO :   "+e1.getMessage());
				}
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
		
	private void initComponents(Composite parent)
	{
		this.parentComposite = parent;
		
		buttons = new ArrayList<Button>();
		
		Button evento = new Button(parentComposite, SWT.NONE);
		evento.setLocation(4, 4);
		evento.setSize(16, 16);
		
		MouseListener mouselistener = new MouseListener() {
			
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				runAdicionaWizardAction();
				String novaAtividade =  viewComunication.getAtividades().get(viewComunication.getAtividades().size()-1);	
		    	Desenvolvedor des = new Desenvolvedor();
		    	des.setEmail("coelhao@vai.pro.japao");
		    	beans.TipoAtividade ati = new beans.TipoAtividade(novaAtividade, des, des, 0,  new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), false, null);
		    	viewComunication.sendPack(ati, 1);
										
				Button novoButao = new Button(panel, SWT.RADIO);
				novoButao.setText(novaAtividade);
				buttons.add(novoButao);
				int depoisButoes = 120;	
				
				final Tree conhecimentoTree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				conhecimentoTree.setLocation(0, depoisButoes + 30);
				conhecimentoTree.setSize(200, 150);
				conhecimentoTree.setVisible(false);	
				ArrayList<String> conhecimentos = viewComunication.getConhecimentosEnvolvidos(novaAtividade);
				if(conhecimentos != null)
					for(String c : conhecimentos)
					{
						TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
						item.setText(c);
					}	
				conhecimentosTree.put(novaAtividade, conhecimentoTree);
				
				final Tree problemaTree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				problemaTree.setLocation(0, depoisButoes + 210);
				problemaTree.setSize(200, 150);
				problemaTree.setVisible(false);		
				ArrayList<String> problemas = viewComunication.getProblemas(novaAtividade);
				
				if(problemas != null)
					for(String c : problemas)
					{
						TreeItem item = new TreeItem(problemaTree, SWT.NONE);
						item.setText(c);
					}
				problemasTree.put(novaAtividade, problemaTree);
				
				setListener(novoButao);		
				atualizaPosicaoComponentes();	
				
			}
		
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
		};
		
		evento.addMouseListener(mouselistener);
			
		panel = new Composite(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		panel.setLocation(0, 25);
		panel.setSize(200, 100);
		panel.setVisible(true);
				
		ArrayList<String> atividadesNomes = viewComunication.getAtividades();
			
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
	
		conhecimentoLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		conhecimentoLabel.setText("Conhecimentos Envolvidos");
		conhecimentoLabel.setLocation(0, depoisButoes + 5);
		conhecimentoLabel.setSize(200, 20);	
		conhecimentoLabel.setVisible(false);
						
		conhecimentosTree = new HashMap<String, Tree>();
		
		for(String selection : viewComunication.getAtividades())
		{
			Tree conhecimentoTree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
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
		
		problemaLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		problemaLabel.setText("Problemas Encontrados");
		problemaLabel.setLocation(0, depoisButoes + 185);
		problemaLabel.setSize(200, 20);
		problemaLabel.setVisible(false);
		
		problemasTree = new HashMap<String, Tree>();
		
		for(String selection : viewComunication.getAtividades())
		{
			Tree problemaTree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
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
	
	public void adicionaAtividade(String atividade, ArrayList<String> conhecimentos,ArrayList<String> problemas){
		this.viewComunication.addAtividade(atividade, conhecimentos, problemas);
	}
	
	private void runAdicionaWizardAction(){
		this.runAdicionaAtividade = new RunAdicionaAtividadeWizardAction(this);
		this.runAdicionaAtividade.run(null);
	}
}