package com.hukarz.presley.client.gui.view;

/* Desenvolvido por Leandro Carlos, Samara Martins e Alysson Diniz */



import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.ProblemaMensagens;
import com.hukarz.presley.client.gui.action.RunEnviaRespostaWizardAction;
import com.hukarz.presley.client.gui.view.comunication.ViewComunication;


public class Mensagens extends ViewPart {

	private Composite parentComposite;
	private Button obterMensagens, enviarResposta, qualificaDesenvolvedor;
	private final int larguraBotao = 20;
	private final int alturaBotao = 20;
	private final int posHorBotaoNivel1 = 4;
	private final int posVerBotaoNivel1 = 4;
	private final int posHorBotaoNivel2 = 28;
	private final int posHorBotaoNivel3 = 52;
	private String ipServidor = "127.0.0.1";
	private ViewComunication viewComunication;
	private ArrayList<ProblemaMensagens> mensagensProblemas;
	private RunEnviaRespostaWizardAction runEnviaResposta;
	private Tree tree = null;

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
				ArrayList<Mensagem> mensagens = null;
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
				
				tree = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK);
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
		enviarResposta.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent e) {
				runEnviaRespostaWizardAction(getViewMensagens());
				
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	
		qualificaDesenvolvedor = new Button(parentComposite, SWT.NONE);
		Image ok = new Image(qualificaDesenvolvedor.getDisplay(),this.getClass().getResourceAsStream("/icons/ok.gif"));
		qualificaDesenvolvedor.setLocation(posHorBotaoNivel3, posVerBotaoNivel1);
		qualificaDesenvolvedor.setSize(larguraBotao, alturaBotao);
		qualificaDesenvolvedor.setImage(ok);
		qualificaDesenvolvedor.setToolTipText("Qualifica resposta do usuario");
		qualificaDesenvolvedor.setEnabled(true);
		qualificaDesenvolvedor.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				Mensagem msgSelecionada = getMensagem();
				viewComunication.qualificaDesenvolvedor(msgSelecionada.getDesenvolvedorOrigem(), msgSelecionada.getProblema(), true);
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	public Mensagens getViewMensagens() {
		return this;
	}
	
	public Desenvolvedor getDesenvolvedorLogado(){
		return Atividade.getDesenvolvedorLogado();
	}
	
	public Mensagem getMensagem(){
		Mensagem mensagemSelecionada = null;
		ArrayList<Mensagem> mensagens = viewComunication.obterMensagens(this.getDesenvolvedorLogado());
		TreeItem[] mensagensSelecionadas = tree.getSelection();
		for(int i =0; i < mensagens.size(); i++) {
			if(mensagens.get(i).getTexto().equals(mensagensSelecionadas[0].getText())){
				System.out.println("ACHOU A MENSAGEM!");
				mensagemSelecionada = mensagens.get(i);
			}
		}
		return mensagemSelecionada;
	}
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	private void runEnviaRespostaWizardAction(Mensagens viewMensagens){
//		this.runEnviaResposta = new RunEnviaRespostaWizardAction(viewMensagens);
//		this.runEnviaResposta.run(null);
	}

	public ViewComunication getViewComunication() {
		return viewComunication;
	}


}
