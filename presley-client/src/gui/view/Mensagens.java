package gui.view;

/* Desenvolvido por Leandro Carlos, Samara Martins e Alysson Diniz */


import gui.view.comunication.ViewComunication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import beans.Mensagem;
import beans.Problema;
import beans.ProblemaMensagens;

public class Mensagens extends ViewPart {

	private Composite parentComposite;
	private Button obterMensagens, enviarResposta;
	private final int larguraBotao = 20;
	private final int alturaBotao = 20;
	private final int posHorBotaoNivel1 = 4;
	private final int posVerBotaoNivel1 = 4;
	private final int posHorBotaoNivel2 = 28;
	private String ipServidor = "127.0.0.1";
	private ViewComunication viewComunication;
	private ArrayList<ProblemaMensagens> mensagensProblemas;

	public Mensagens() {
		this.viewComunication = new ViewComunication(ipServidor);
	}

	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		parent.setLayout(null);
		initComponents(parent);

	}
	
	private void initComponents(Composite parent)
	{
		this.parentComposite = parent;
		
		obterMensagens = new Button(parentComposite, SWT.NONE);
		Image obter = new Image(obterMensagens.getDisplay(),this.getClass().getResourceAsStream("/icons/add.gif"));
		obterMensagens.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
		obterMensagens.setSize(larguraBotao, alturaBotao);
		obterMensagens.setImage(obter);
		obterMensagens.setToolTipText("Obter mensagens");
		obterMensagens.setVisible(true);
		obterMensagens.setEnabled(true);
		obterMensagens.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent e) {
				String nomeProblema = "";
				ArrayList<String> mensagensDoProblema = null;
				ArrayList<Mensagem> mensagens = null;
				Problema problemaAnterior = null;
				Map map = new HashMap();
				ArrayList<String> nomeProblemas = new ArrayList<String>();
				mensagensProblemas = new ArrayList<ProblemaMensagens>();
				
				
				System.out.println("Antes de chamar o banco");
				/*obter as mensagens do desenvolvedor logado*/
				mensagens = viewComunication.obterMensagens(Atividade.getDesenvolvedorLogado());
				
				Iterator it = mensagens.iterator();
				while(it.hasNext()){	
					System.out.println("dentro do laco");
					Mensagem mensagem   = (Mensagem) it.next();
					String descricaoPro = mensagem.getProblema().getDescricao();
					
					
					if(nomeProblemas.contains(descricaoPro)) {
						System.out.println("ENTREI IF");
						Iterator ite = mensagensProblemas.iterator();
						
						/* GAMBIARRA!!!!! */
						while(ite.hasNext()) {
							ProblemaMensagens pro = (ProblemaMensagens) ite.next();
							
							if(descricaoPro.equals(pro.getDescricaoProblema())) {
								pro.addMensagem(mensagem.getTexto());
							}
						}
					}
					else {
						System.out.println("ENTREI ELSE");
						ProblemaMensagens proMsg = new ProblemaMensagens(descricaoPro, mensagem.getProblema().getId());
						proMsg.addMensagem(mensagem.getTexto());
						mensagensProblemas.add(proMsg);
						System.out.println("Adicionando: "+ descricaoPro + "ao nomeProblemas");
						nomeProblemas.add(descricaoPro);
					}
				}
				
				final Tree tree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
				tree.setLocation(posHorBotaoNivel1, 25);
				tree.setSize(1000, 100);
				tree.setVisible(true);
				
				TreeItem[] item = new TreeItem[mensagensProblemas.size()];
				
				int contador = 0;
				System.out.println("Antes do while");
				/*No final, map deve conter uma lista de problemas e mensagens associadas*/
				Iterator ite = mensagensProblemas.iterator();
				while(ite.hasNext()) {
					System.out.println("Estou no while");
					ProblemaMensagens proMsg = (ProblemaMensagens)ite.next();
					System.out.println("ID DO PROBLEMA: "+proMsg.getIdProblema());
					item[contador] = new TreeItem(tree, SWT.NONE);
					item[contador].setText(""+(proMsg.getDescricaoProblema()));	
					
					ArrayList<String> msgs = proMsg.getMensagensPro();
					Iterator iter = msgs.iterator();
					TreeItem[] radio = new TreeItem[msgs.size()];
					int count = 0;
					while(iter.hasNext()) {
						String msg = (String) iter.next();
						radio[count] = new TreeItem(item[contador], SWT.NONE);
						radio[count].setText(msg);
						count++;
					}
					
					contador++;
				}
				System.out.println("Depois do while");
				/*Monta a arvore grafica em funcao de map*/
				
				/*
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText("Problema");	
				
				final TreeItem radio1 = new TreeItem(item, SWT.NONE);
				radio1.setText("Bope");

				final TreeItem radio2 = new TreeItem(item,  SWT.NONE);
			    radio2.setText("GigaBug");
			    
			    tree.setLocation(posHorBotaoNivel1, 25);
				tree.setSize(1000, 100);
				tree.setVisible(true);*/
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		enviarResposta = new Button(parentComposite, SWT.NONE);
		Image trocaMsg = new Image(enviarResposta.getDisplay(),this.getClass().getResourceAsStream("/icons/trocaMsg.gif"));
		enviarResposta .setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		enviarResposta .setSize(larguraBotao, alturaBotao);
		enviarResposta .setImage(trocaMsg);
		enviarResposta .setToolTipText("Envia Resposta");
		enviarResposta .setVisible(true);
		enviarResposta .setEnabled(true);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}


}
