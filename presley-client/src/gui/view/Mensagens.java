package gui.view;

/* Desenvolvido por Leandro Carlos, Samara Martins e Alysson Diniz */


import java.util.ArrayList;
import java.util.HashMap;
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

public class Mensagens extends ViewPart {

	private Composite parentComposite;
	private Button obterMensagens, enviarResposta;
	private final int larguraBotao = 20;
	private final int alturaBotao = 20;
	private final int posHorBotaoNivel1 = 4;
	private final int posVerBotaoNivel1 = 4;
	private final int posHorBotaoNivel2 = 28;
	
	public Mensagens() {
		// TODO Auto-generated constructor stub
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
				
				/*obter as mensagens do desenvolvedor logado*/
				//mensagens = viewComunication.obterMensagens(desenvolvedorLogado);
				
				/*obter os problemas associados as mensagens*/
				for(int i = 0; i < mensagens.size(); i++) {
					/*Pega o primeiro problema*/
					if(i == 0) {
						problemaAnterior = mensagens.get(i).getProblema();
						map.put(problemaAnterior.getDescricao(),mensagens.get(i).getTexto());
						continue;
					}
					/*Se eh o mesmo problema*/
					if(mensagens.get(i).getProblema().equals(problemaAnterior)){
						map.put(problemaAnterior.getDescricao(),mensagens.get(i).getTexto());
					}
					else {
						problemaAnterior = mensagens.get(i).getProblema();
						map.put(problemaAnterior.getDescricao(),mensagens.get(i).getTexto());
					}
				}
				
				/*No final, map deve conter uma lista de problemas e mensagens associadas*/
				
				/*Monta a arvore grafica em funcao de map*/
				final Tree tree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText("Problema");	
				
				final TreeItem radio1 = new TreeItem(item, SWT.NONE);
				radio1.setText("Bope");

				final TreeItem radio2 = new TreeItem(item,  SWT.NONE);
			    radio2.setText("GigaBug");
			    
			    tree.setLocation(posHorBotaoNivel1, 25);
				tree.setSize(1000, 100);
				tree.setVisible(true);
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
