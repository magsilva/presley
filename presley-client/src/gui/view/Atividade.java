package gui.view;

import gui.action.RunAdicionaAtividadeWizardAction;
import gui.action.RunRemoveAtividadeWizardAction;
import gui.view.comunication.ViewComunication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.Problema;
import beans.TipoAtividade;


public class Atividade extends ViewPart {

	private ArrayList<Button> buttons;
	private HashMap<String,List> conhecimentosList;
	private HashMap<String,List> problemasList;
	private ViewComunication viewComunication;
	private List listaAtividades, listaConhecimentos, listaProblemas; 
	private Label conhecimentoLabel, problemaLabel, contatosLabel;
	private Composite panelContatos, parentComposite;
	private Button login,logout, addAtividade, addConhecimento,removeConhecimento, removeAtividade, removeAllAtividade, 
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
	private final int posHorBotaoNivel6 = 124;
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

	}	
	
	public void createPartControl(Composite parent) 
	{		
		parent.setLayout(null);
		initComponents(parent);
	}
		
	private void initComponents(Composite parent)
	{
		this.parentComposite = parent;
		
		buttons = new ArrayList<Button>();
		
		
		login = new Button(parentComposite, SWT.NONE);
		Image log = new Image(login.getDisplay(),this.getClass().getResourceAsStream("/icons/users.gif"));
		login.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
		login.setSize(larguraBotao, alturaBotao);
		login.setImage(log);
		login.setToolTipText("Login");
		login.setEnabled(true);
		login.addDisposeListener(new DisposeListener(){

			public void widgetDisposed(DisposeEvent e) {
				//Habilita o botao de logout
				logout = new Button(parentComposite, SWT.NONE);
				Image logoff = new Image(logout.getDisplay(),this.getClass().getResourceAsStream("/icons/logout.gif"));
				logout.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
				logout.setSize(larguraBotao, alturaBotao);
				logout.setImage(logoff);
				logout.setToolTipText("Logout");
				logout.setEnabled(true);
				
				//Habilita os outros botoes
				addAtividade.setEnabled(true);
				removeAtividade.setEnabled(true);
				removeAllAtividade.setEnabled(true);
				buscaAtividade.setEnabled(true);
				encerraAtividade.setEnabled(true);
				associaConhecimento.setEnabled(true);
				desassociaConhecimento.setEnabled(true);
				buscaConhecimento.setEnabled(true);
				associaProblema.setEnabled(true);
				desassociaProblema.setEnabled(true);
				buscaProblema.setEnabled(true);
				buscaDesenvolvedor.setEnabled(true);
				qualificaDesenvolvedor.setEnabled(true);
				addConhecimento.setEnabled(true);
				removeConhecimento.setEnabled(true);
				
			}
			
		});
		login.addMouseListener(new MouseListener() {
			
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent e) {
				//Exibe o wizard de login
				//runLoginWizardAction();
				
				//Desabilita o botao de login
				login.dispose();
				
				ArrayList<TipoAtividade> atividades = viewComunication.getAtividades();
				
				for(int i=0; i < atividades.size(); i++)
				{
					listaAtividades.add((atividades.get(i)).getDescricao());
					
				}
				
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		addAtividade = new Button(parentComposite, SWT.NONE);
		Image add = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/add.gif"));
		addAtividade.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		addAtividade.setSize(larguraBotao, alturaBotao);
		addAtividade.setImage(add);
		addAtividade.setToolTipText("Adiciona nova atividade");
		addAtividade.setEnabled(false);
		addAtividade.addMouseListener(new MouseListener() {
			
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		
			public void mouseDown(MouseEvent e) {
				
				// exibe o wizard para adicao de nova atividade
				runAdicionaWizardAction();
				
				//cadastra a atividade no servidor
				//viewComunication.adicionaAtividade(atividade);
				
				//captura o nome da atividade inserido pelo usuario	???????		
				TipoAtividade novaAtividade =  viewComunication.getAtividades().get(viewComunication.getAtividades().size()-1);
				System.out.println("Nova Atividade = " + novaAtividade.getDescricao());
				
				//adiciona o item na lista de atividades
				listaAtividades.add(novaAtividade.getDescricao());

				//captura os conhecimentos envolvidos
				ArrayList<Conhecimento> conhecimentos = viewComunication.getConhecimentosEnvolvidos(novaAtividade);

				//limpa as listas de conhecimentos e problemas
				listaConhecimentos.removeAll();
				listaProblemas.removeAll();

				//cria estruturas para armazenar os conhecimentos e problemas
				ArrayList<String> conh = new ArrayList<String>();
				ArrayList<String> prob = new ArrayList<String>();


				//Preenche a lista de conhecimentos
				if(conhecimentos != null)
					for(Conhecimento c : conhecimentos)
					{
						conh.add(c.getNome());
						listaConhecimentos.add(c.getNome());
					}	

				//Seleciona o item recem adicionado
				listaAtividades.select(listaAtividades.indexOf(novaAtividade.getDescricao()));

				//atualiza a vizualizacao
				listaAtividades.getParent().redraw();
				listaAtividades.getParent().update();
				listaConhecimentos.getParent().redraw();
				listaConhecimentos.getParent().update();		
			}
		
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		removeAtividade = new Button(parentComposite, SWT.NONE);
		Image remove = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/remove.gif"));
		removeAtividade.setLocation(posHorBotaoNivel3, posVerBotaoNivel1);
		removeAtividade.setSize(larguraBotao, alturaBotao);
		removeAtividade.setImage(remove);
		removeAtividade.setToolTipText("Remove atividade selecionada");
		removeAtividade.setEnabled(false);
		removeAtividade.addMouseListener(new MouseListener() {
		
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
			public void mouseDown(MouseEvent arg0) {
				
				//captura o id da atividade selecionada
				int idAtividade = listaAtividades.getSelectionIndex();
				
				//exibe o wizard de confirmacao para a retirada da atividade
				//runRemoveWizardAction();
				
				//realiza a remocao da atividade no servidor
				viewComunication.removerAtividade(listaAtividades.getItem(idAtividade));
				
				//realiza a remocao da atividade na lista
				listaAtividades.remove(idAtividade);
				
				//limpa as listas de problemas e conhecimentos
				listaConhecimentos.removeAll();
				listaProblemas.removeAll();
				
				//atualiza as listas
				listaAtividades.getParent().redraw();
				listaAtividades.getParent().update();
				listaConhecimentos.getParent().redraw();
				listaConhecimentos.getParent().update();
				listaProblemas.getParent().redraw();
				listaProblemas.getParent().update();
			}
		
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
		
			}
		
		});

		removeAllAtividade = new Button(parentComposite, SWT.NONE);
		Image removeAll = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/removeAll.gif"));
		removeAllAtividade.setLocation(posHorBotaoNivel4, posVerBotaoNivel1);
		removeAllAtividade.setSize(larguraBotao, alturaBotao);
		removeAllAtividade.setImage(removeAll);
		removeAllAtividade.setToolTipText("Remove todas as atividades");
		removeAllAtividade.setEnabled(false);
		removeAllAtividade.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent arg0) {
				// exibe o wizard para confirmacao de exclusao
				//runRemoveAllWizardAction();
				
			
				//realiza a exclusao das atividades
				//viewComunication.sendPack(new Atividade(), 1);
				
				//limpa as listas
				listaAtividades.removeAll();
				listaConhecimentos.removeAll();
				listaProblemas.removeAll();
				
				//atualiza a vizualizacao
				listaAtividades.getParent().redraw();
				listaAtividades.getParent().update();
				listaConhecimentos.getParent().redraw();
				listaConhecimentos.getParent().update();
				listaProblemas.getParent().redraw();
				listaProblemas.getParent().update();
				
			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		buscaAtividade = new Button(parentComposite, SWT.NONE);
		Image busca = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/busca.gif"));
		buscaAtividade.setLocation(posHorBotaoNivel5, posVerBotaoNivel1);
		buscaAtividade.setSize(larguraBotao, alturaBotao);
		buscaAtividade.setImage(busca);
		buscaAtividade.setToolTipText("Busca atividades");
		buscaAtividade.setEnabled(false);
		
		encerraAtividade = new Button(parentComposite, SWT.NONE);
		Image encerra = new Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream("/icons/encerra.gif"));
		encerraAtividade.setLocation(posHorBotaoNivel6, posVerBotaoNivel1);
		encerraAtividade.setSize(larguraBotao, alturaBotao);
		encerraAtividade.setImage(encerra);
		encerraAtividade.setToolTipText("Encerra atividade selecionada");
		encerraAtividade.setEnabled(false);
			
		listaAtividades = new List(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		listaAtividades.setLocation(posHorPanel, posVerPainelAtividade);
		listaAtividades.setSize(larguraJanela, alturaPainelAtividade);
		listaAtividades.setVisible(true);
		listaAtividades.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent arg0) {

				//Captura atividade selecionada
				String descricaoAtividade = listaAtividades.getItem(listaAtividades.getSelectionIndex());
				
				//atividade = viewComunication.getAtividade(descricaoAtividade);
				TipoAtividade atividade = new TipoAtividade();		
				
				//Captura os problemas associados
				ArrayList<Problema> problemas = viewComunication.getProblemas(atividade);
				
				//Captura os conhecimentos associados
				ArrayList<Conhecimento> conhecimentos = viewComunication.getConhecimentosEnvolvidos(atividade);
				
				//Limpa a lista de problemas
				listaProblemas.removeAll();
				
				//limpa a lista de conhecimentos
				listaConhecimentos.removeAll();
				
				//Adiciona os problemas a arvore
				for(int i=0; i < problemas.size(); i++)
				{
					listaProblemas.add(problemas.get(i).getDescricao());
					listaProblemas.getParent().redraw();
					listaProblemas.getParent().update();
				}
				
				//Adiciona os conhecimentos a lista
				for(int i=0; i < conhecimentos.size(); i++)
				{
					listaConhecimentos.add(conhecimentos.get(i).getNome());
					listaConhecimentos.getParent().redraw();
					listaConhecimentos.getParent().update();
				}
				
			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		
		ArrayList<TipoAtividade> atividades = viewComunication.getAtividades();
			
		for(int i=0; i < atividades.size(); i++)
		{
			listaAtividades.add(atividades.get(i).getDescricao());
			
		}
	
		conhecimentoLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		conhecimentoLabel.setText("Conhecimentos Envolvidos");
		conhecimentoLabel.setLocation(0, posVerPainelAtividade + alturaPainelAtividade + distanciaPanelLabel);
		conhecimentoLabel.setSize(200, 20);	
		conhecimentoLabel.setVisible(true);
		conhecimentoLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),102,204,255));
		
		listaConhecimentos = new List(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		listaConhecimentos.setLocation(posHorPanel, posVerPainelConhecimentos);
		listaConhecimentos.setSize(larguraJanela, alturaPainelConhecimentos);
		listaConhecimentos.setVisible(true);
	
		
		conhecimentosList = new HashMap<String, List>();
		
		addConhecimento = new Button(parentComposite, SWT.NONE);
		addConhecimento.setLocation(posHorBotaoNivel4, posVerBotaoNivel2);
		addConhecimento.setSize(larguraBotao, alturaBotao);
		addConhecimento.setImage(add);
		addConhecimento.setToolTipText("Cadastra um novo conhecimento");
		addConhecimento.setEnabled(false);
		
		removeConhecimento = new Button(parentComposite, SWT.NONE);
		removeConhecimento.setLocation(posHorBotaoNivel5, posVerBotaoNivel2);
		removeConhecimento.setSize(larguraBotao, alturaBotao);
		removeConhecimento.setImage(remove);
		removeConhecimento.setToolTipText("Remove o conhecimento selecionado");
		removeConhecimento.setEnabled(false);
		
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
		for(String selection : viewComunication.getAtividades())
		{
			ArrayList<String> conhecimentosModelo = viewComunication.getConhecimentosEnvolvidos(selection);
			for (String string : conhecimentosModelo)
				listaConhecimentos.add(string);
			conhecimentosList.put(selection, listaConhecimentos);
		}
		*/
		problemaLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		problemaLabel.setText("Problemas Encontrados");
		problemaLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),255,102,51));
		problemaLabel.setLocation(0, posVerPainelConhecimentos + alturaPainelConhecimentos + distanciaPanelLabel);
		problemaLabel.setSize(200, 20);
		problemaLabel.setVisible(true);
		
		listaProblemas = new List(parentComposite, SWT.BORDER);
		listaProblemas.setLocation(posHorPanel, posVerPainelProblemas);
		listaProblemas.setSize(larguraJanela, alturaPainelProblemas);
		listaProblemas.setVisible(true);
		
		problemasList = new HashMap<String, List>();
		
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
		/*
		for(String selection : viewComunication.getAtividades())
		{
				ArrayList<String> problemas = viewComunication.getProblemas(selection);
				
				if(problemas != null)
					for(String c : problemas)
					{
						listaProblemas.add(c);
					}
				
				problemasList.put(selection, listaProblemas);
		}
		*/
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

	
	
	private void runAdicionaWizardAction(){
		this.runAdicionaAtividade = new RunAdicionaAtividadeWizardAction(this);
		this.runAdicionaAtividade.run(null);
	}
	
	public ViewComunication getViewComunication() {
		return this.viewComunication;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
}