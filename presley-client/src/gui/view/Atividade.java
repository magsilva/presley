package gui.view;

import gui.action.RunAdicionaAtividadeWizardAction;
import gui.action.RunRemoveAtividadeWizardAction;
import gui.view.comunication.ViewComunication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
	private Label conhecimentoLabel, problemaLabel, contatosLabel;
	private Composite panel, panelConhecimento, panelProblema, panelContatos, parentComposite;
	private Button addAtividade, removeAtividade, removeAllAtividade, 
			buscaAtividade, encerraAtividade, associaConhecimento, desassociaConhecimento,
			buscaConhecimento, associaProblema, desassociaProblema, buscaProblema,
			buscaDesenvolvedor, qualificaDesenvolvedor;
	
	private RunAdicionaAtividadeWizardAction runAdicionaAtividade;
	private RunRemoveAtividadeWizardAction runRemoveAtividade;
	
	private final int larguraBotao = 20;
	private final int alturaBotao = 20;
	private final int posHorBotaoNivel1 = 4;
	private final int posHorBotaoNivel2 = 28;
	private final int posHorBotaoNivel3 = 52;
	private final int posHorBotaoNivel4 = 76;
	private final int posHorBotaoNivel5 = 100;
	private final int posVerBotaoNivel1 = 4;
	private final int posVerBotaoNivel2 = 113;
	private final int posVerBotaoNivel3 = 233;
	private final int posVerBotaoNivel4 = 352;
	private final int alturaPainelAtividade = 60;
	private final int alturaPainelConhecimentos = 70;
	private final int alturaPainelProblemas = 70;
	private final int alturaPainelContatos = 68;
	private final int posVerPainelAtividade = 25;
	private final int posVerPainelConhecimentos = 135;
	private final int posVerPainelProblemas = 255;
	private final int posVerPainelContatos = 373;
	private final int larguraJanela = 184;
	private final int distanciaPanelLabel = 5;
	private final int posHorPanel = 0;
	
	public Atividade()
	{
		this.viewComunication = new ViewComunication();
		this.viewComunication.teste(); //(HABILITAR PARA FINS DE TESTE)

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
					
					habilitaBotoesConhecimento();
				
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
	}
	
	private void desabilitaBotoesConhecimento(){
		associaConhecimento.setEnabled(false);
	    desassociaConhecimento.setEnabled(false);
	    buscaConhecimento.setEnabled(false);
	}
	
	private void habilitaBotoesConhecimento(){
		associaConhecimento.setEnabled(true);
	    desassociaConhecimento.setEnabled(true);
	    buscaConhecimento.setEnabled(true);		
	}
	
	private void desabilitaBotoesProblema(){
		associaProblema.setEnabled(false);
		desassociaProblema.setEnabled(false);
		buscaProblema.setEnabled(false);
	}
	
	private void habilitaBotoesProblema(){
		associaProblema.setEnabled(true);
		desassociaProblema.setEnabled(true);
		buscaProblema.setEnabled(true);
		buscaProblema.update();
	}
	
	private void desabilitaBotoesContato(){
		buscaDesenvolvedor.setEnabled(false);
		qualificaDesenvolvedor.setEnabled(false);	
	}
	
	private void habilitaBotoesContato(){
		buscaDesenvolvedor.setEnabled(true);
		qualificaDesenvolvedor.setEnabled(true);	
	}
		
	private void initComponents(Composite parent)
	{
		this.parentComposite = parent;
		
		buttons = new ArrayList<Button>();
		
		addAtividade = new Button(parentComposite, SWT.NONE);
		Image add = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/add.gif"));
		addAtividade.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
		addAtividade.setSize(larguraBotao, alturaBotao);
		addAtividade.setImage(add);
		addAtividade.setToolTipText("Adiciona nova atividade");
		addAtividade.setEnabled(true);
		addAtividade.addMouseListener(new MouseListener() {
			
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
		    	//viewComunication.sendPack(ati, 1);
										
				Button novoButao = new Button(panel, SWT.RADIO);
				novoButao.setText(novaAtividade);
				buttons.add(novoButao);
				int depoisButoes = 120;	
				
				final Tree conhecimentoTree = new Tree(panelConhecimento, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				conhecimentoTree.setLocation(0, 0);
				conhecimentoTree.setSize(larguraJanela, alturaPainelConhecimentos);
				conhecimentoTree.setVisible(false);	
				ArrayList<String> conhecimentos = viewComunication.getConhecimentosEnvolvidos(novaAtividade);
				if(conhecimentos != null)
					for(String c : conhecimentos)
					{
						TreeItem item = new TreeItem(conhecimentoTree, SWT.NONE);
						item.setText(c);
					}	
				conhecimentosTree.put(novaAtividade, conhecimentoTree);
				
				final Tree problemaTree = new Tree(panelProblema, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
				problemaTree.setLocation(0, 0);
				problemaTree.setSize(larguraJanela, alturaPainelProblemas);
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
		});
		
		removeAtividade = new Button(parentComposite, SWT.NONE);
		Image remove = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/remove.gif"));
		removeAtividade.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		removeAtividade.setSize(larguraBotao, alturaBotao);
		removeAtividade.setImage(remove);
		removeAtividade.setToolTipText("Remove atividade selecionada");
		removeAtividade.setEnabled(true);
		removeAtividade.addMouseListener(new MouseListener() {
		
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				runRemoveWizardAction();
				ArrayList<String> atividadesAtualizadas =  viewComunication.getAtividades();
				if (!atividadesAtualizadas.isEmpty()) {
					buttons = new ArrayList<Button>();
					panel.dispose();
					panel = new Composite(parentComposite, SWT.V_SCROLL | SWT.BORDER);
					panel.setLocation(posHorPanel, posVerPainelAtividade);
					panel.setSize(larguraJanela, alturaPainelAtividade);
					panel.setVisible(true);
					
					for(int i=0; i < atividadesAtualizadas.size(); i++)
					{
						final Button b = new Button(panel, SWT.RADIO);
						b.setText(atividadesAtualizadas.get(i));
						b.setLocation(0, i*25);
						b.setSize(200, 25);
						buttons.add(b);
						setListener(b);
					}
					int depoisButoes = 120;	
					
			    	//viewComunication.sendPack(ati, 1);
					
					atualizaPosicaoComponentes();

				}else{
					for (Button button : buttons) {
						button.dispose();
					}
					buttons.clear();
					
					for (Tree tree : conhecimentosTree.values()) {
						tree.dispose();
					}
					for (Tree tree : problemasTree.values()) {
						tree.dispose();
					}
					conhecimentosTree.clear();
					problemasTree.clear();
					
					desabilitaBotoesConhecimento();
					desabilitaBotoesProblema();
					desabilitaBotoesContato();
					
				}
			}
		
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
		});

		removeAllAtividade = new Button(parentComposite, SWT.NONE);
		Image removeAll = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/removeAll.gif"));
		removeAllAtividade.setLocation(posHorBotaoNivel3, posVerBotaoNivel1);
		removeAllAtividade.setSize(larguraBotao, alturaBotao);
		removeAllAtividade.setImage(removeAll);
		removeAllAtividade.setToolTipText("Remove todas as atividades");
		removeAllAtividade.setEnabled(true);
		
		buscaAtividade = new Button(parentComposite, SWT.NONE);
		Image busca = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/busca.gif"));
		buscaAtividade.setLocation(posHorBotaoNivel4, posVerBotaoNivel1);
		buscaAtividade.setSize(larguraBotao, alturaBotao);
		buscaAtividade.setImage(busca);
		buscaAtividade.setToolTipText("Busca atividades");
		buscaAtividade.setEnabled(true);
		
		encerraAtividade = new Button(parentComposite, SWT.NONE);
		Image encerra = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/encerra.gif"));
		encerraAtividade.setLocation(posHorBotaoNivel5, posVerBotaoNivel1);
		encerraAtividade.setSize(larguraBotao, alturaBotao);
		encerraAtividade.setImage(encerra);
		encerraAtividade.setToolTipText("Encerra atividade selecionada");
		encerraAtividade.setEnabled(true);
			
		panel = new Composite(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		panel.setLocation(posHorPanel, posVerPainelAtividade);
		panel.setSize(larguraJanela, alturaPainelAtividade);
		panel.setVisible(true);
		/*
		panel.getVerticalBar().addSelectionListener(
				SelectionListener listener = new SelectionAdapter(){
					
				});
		*/
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
		conhecimentoLabel.setLocation(0, posVerPainelAtividade + alturaPainelAtividade + distanciaPanelLabel);
		conhecimentoLabel.setSize(200, 20);	
		conhecimentoLabel.setVisible(true);
		conhecimentoLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),102,204,255));
		
		panelConhecimento = new Composite(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		panelConhecimento.setLocation(posHorPanel, posVerPainelConhecimentos);
		panelConhecimento.setSize(larguraJanela, alturaPainelConhecimentos);
		panelConhecimento.setVisible(true);
						
		conhecimentosTree = new HashMap<String, Tree>();
		
		associaConhecimento = new Button(parentComposite, SWT.NONE);
		Image ass = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/associaConh.gif"));
		associaConhecimento.setLocation(posHorBotaoNivel1, posVerBotaoNivel2);
		associaConhecimento.setSize(larguraBotao, alturaBotao);
		associaConhecimento.setImage(ass);
		associaConhecimento.setToolTipText("Associa novo conhecimento a atividade");
		associaConhecimento.setEnabled(false);
		
		desassociaConhecimento = new Button(parentComposite, SWT.NONE);
		Image desass = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/desassociaConh.gif"));
		desassociaConhecimento.setLocation(posHorBotaoNivel2, posVerBotaoNivel2);
		desassociaConhecimento.setSize(larguraBotao, alturaBotao);
		desassociaConhecimento.setImage(desass);
		desassociaConhecimento.setToolTipText("Desassocia o conhecimento da atividade");
		desassociaConhecimento.setEnabled(false);
		
		buscaConhecimento = new Button(parentComposite, SWT.NONE);
		Image buscaConh = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/buscaConh.gif"));
		buscaConhecimento.setLocation(posHorBotaoNivel3, posVerBotaoNivel2);
		buscaConhecimento.setSize(larguraBotao, alturaBotao);
		buscaConhecimento.setImage(buscaConh);
		buscaConhecimento.setToolTipText("Busca um conhecimento");
		buscaConhecimento.setEnabled(false);
		
		/*
		Tree tree = new Tree(panel, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
		tree.setLocation(0, 10);
		tree.setSize(200, 150);
		tree.setVisible(true);
		TreeItem item2 = new TreeItem(tree, SWT.NONE);
		item2.setText("teste");	
			
		*/
		
		for(String selection : viewComunication.getAtividades())
		{
			Tree conhecimentoTree = new Tree(panelConhecimento, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
			conhecimentoTree.setLocation(0,0);
			conhecimentoTree.setSize( this.larguraJanela , this.alturaPainelConhecimentos);
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
		problemaLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),255,102,51));
		problemaLabel.setLocation(0, posVerPainelConhecimentos + alturaPainelConhecimentos + distanciaPanelLabel);
		problemaLabel.setSize(200, 20);
		problemaLabel.setVisible(true);
		
		panelProblema = new Composite(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		panelProblema.setLocation(posHorPanel, posVerPainelProblemas);
		panelProblema.setSize(larguraJanela, alturaPainelProblemas);
		panelProblema.setVisible(true);
		
		problemasTree = new HashMap<String, Tree>();
		
		associaProblema = new Button(parentComposite, SWT.NONE);
		//Image ass = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/associaConh.gif"));
		associaProblema.setLocation(posHorBotaoNivel2, posVerBotaoNivel3);
		associaProblema.setSize(larguraBotao, alturaBotao);
		associaProblema.setImage(ass);
		associaProblema.setToolTipText("Associa novo problema a atividade");
		associaProblema.setEnabled(false);
		
		desassociaProblema = new Button(parentComposite, SWT.NONE);
		//Image ass = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/associaConh.gif"));
		desassociaProblema.setLocation(posHorBotaoNivel3, posVerBotaoNivel3);
		desassociaProblema.setSize(larguraBotao, alturaBotao);
		desassociaProblema.setImage(desass);
		desassociaProblema.setToolTipText("Desassocia problema de atividade");
		desassociaProblema.setEnabled(false);
		
		buscaProblema = new Button(parentComposite, SWT.NONE);
		//Image ass = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/associaConh.gif"));
		buscaProblema.setLocation(posHorBotaoNivel4, posVerBotaoNivel3);
		buscaProblema.setSize(larguraBotao, alturaBotao);
		buscaProblema.setImage(buscaConh);
		buscaProblema.setToolTipText("Busca problemas ja existentes");
		buscaProblema.setEnabled(false);
		
		buscaDesenvolvedor = new Button(parentComposite, SWT.NONE);
		Image buscaDes = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/buscaDes.gif"));
		buscaDesenvolvedor.setLocation(posHorBotaoNivel1, posVerBotaoNivel3);
		buscaDesenvolvedor.setSize(larguraBotao, alturaBotao);
		buscaDesenvolvedor.setImage(buscaDes);
		buscaDesenvolvedor.setToolTipText("Busca desenvolvedores para resolver esse problema");
		buscaDesenvolvedor.setEnabled(false);
		
		for(String selection : viewComunication.getAtividades())
		{
			Tree problemaTree = new Tree(panelProblema, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK | SWT.SCROLL_LINE);
			problemaTree.setLocation(0, 0);
			problemaTree.setSize(this.larguraJanela, this.alturaPainelProblemas);
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
		
		contatosLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		contatosLabel.setText("Contatos para os problemas");
		contatosLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),255,255,153));
		contatosLabel.setLocation(0, posVerPainelProblemas + alturaPainelProblemas + distanciaPanelLabel);
		contatosLabel.setSize(200, 20);
		contatosLabel.setVisible(true);
		
		panelContatos = new Composite(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		panelContatos.setLocation(posHorPanel, posVerPainelContatos);
		panelContatos.setSize(larguraJanela, alturaPainelContatos);
		panelContatos.setVisible(true);
		
		qualificaDesenvolvedor = new Button(parentComposite, SWT.NONE);
		Image ok = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/ok.gif"));
		qualificaDesenvolvedor .setLocation(posHorBotaoNivel1, posVerBotaoNivel4);
		qualificaDesenvolvedor .setSize(larguraBotao, alturaBotao);
		qualificaDesenvolvedor .setImage(ok);
		qualificaDesenvolvedor .setToolTipText("Qualifica resposta do usuario");
		qualificaDesenvolvedor.setEnabled(false);
	}

	
	public void adicionaAtividade(String atividade, ArrayList<String> conhecimentos,ArrayList<String> problemas){
		this.viewComunication.addAtividade(atividade, conhecimentos, problemas);
	}
	
	public void removeAtividade(String atividade){
		this.viewComunication.removeAtividade(atividade);
	}
	
	public ArrayList<String> getAtividades(){
		return this.viewComunication.getAtividades();
	}
	
	private void runAdicionaWizardAction(){
		this.runAdicionaAtividade = new RunAdicionaAtividadeWizardAction(this);
		this.runAdicionaAtividade.run(null);
	}
	
	private void runRemoveWizardAction(){
		this.runRemoveAtividade = new RunRemoveAtividadeWizardAction(this);
		this.runRemoveAtividade.run(null);
	}
}