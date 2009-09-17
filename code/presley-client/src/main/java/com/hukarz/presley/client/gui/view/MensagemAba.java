package com.hukarz.presley.client.gui.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import ca.mcgill.cs.swevo.PresleyJayFX;
import ca.mcgill.cs.swevo.jayfx.JayFXException;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.client.gui.action.RunAdcionaProblemaWizardAction;
import com.hukarz.presley.client.gui.action.RunEnviaRespostaWizardAction;
import com.hukarz.presley.client.gui.action.RunEnviaRetornoSolucaoWizardAction;
import com.hukarz.presley.client.gui.view.comunication.ViewComunication;

public class MensagemAba extends ViewPart {
	private Composite parentComposite;
	private ViewComunication viewComunication;
	private String ipServidor = "127.0.0.1";

	private static Desenvolvedor desenvolvedorLogado = null;

	private boolean bLogin;

	private Button cadastroProjeto, login, logout, addUser, removeUser, 
			incluirProblema, excluirProblema, validarSolucao, encerrarProblema;

	private final int larguraBotao = 20;
	private final int alturaBotao = 20;

	private final int posDistancia = 22;
	
	private final int posVerBotaoNivel1 = 4;
	private final int posHorBotaoNivel1 = 4; 
	private final int posHorBotaoNivel2 = posHorBotaoNivel1 + posDistancia;
	private final int posHorBotaoNivel3 = posHorBotaoNivel2 + posDistancia;
	private final int posHorBotaoNivel4 = posHorBotaoNivel3 + posDistancia;
	private final int posHorBotaoNivel5 = posHorBotaoNivel4 + posDistancia;
	private final int posHorBotaoNivel6 = posHorBotaoNivel5 + posDistancia;
	private final int posHorBotaoNivel7 = posHorBotaoNivel6 + posDistancia;
	private final int posHorBotaoNivel8 = posHorBotaoNivel7 + posDistancia;

	private final int posVerPainelProblemas = 27;
	private final int posVerPainelLerMensagem = 250; // 230

	private final int alturaPainelProblemas = 200;
	private final int alturaPainelLerMensagem = 170; // 85

	private final int distanciaPanelLabel = 2;

	private final int larguraJanela = 200;

	private Tree treeProblemasEnviados = null, treeProblemasRecebidos = null;

	private Label mensagemRecebida;
	private Label lblGrupoBotoes1, lblGrupoBotoes2, lblGrupoBotoes3;

	private RunAdcionaProblemaWizardAction runAdcionaProblema;
	private RunEnviaRespostaWizardAction runEnviaResposta;
	private RunEnviaRetornoSolucaoWizardAction RunEnviaRetornoSolucao;

	private ArrayList<Mensagem> mensagensExibidas;
	private Timer timer;

	private Projeto projetoAtivo;
	private PresleyJayFX aDB;
	
	public MensagemAba() {
		this.viewComunication = new ViewComunication(ipServidor);
		bLogin = false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(null);
		initComponents(parent);		
	}

	public PresleyJayFX getDadosProjetoAtivo(){
		return aDB;
	}
	
	public ViewComunication getViewComunication() {
		return this.viewComunication;
	}

	private void initComponents(Composite parent) {
		
		this.parentComposite = parent;

		cadastroProjeto = new Button(parentComposite, SWT.NONE);
		Image log = new Image(cadastroProjeto.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/projeto.gif"));
		cadastroProjeto.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
		cadastroProjeto.setSize(larguraBotao, alturaBotao);
		cadastroProjeto.setImage(log);
		cadastroProjeto.setToolTipText("Projetos");
		cadastroProjeto.setEnabled(true);
		cadastroProjeto.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				runAdcionaProjetoWizardAction();
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		
		
		logout = new Button(parentComposite, SWT.NONE);
		Image logoff = new Image(logout.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/logout.gif"));
		logout.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		logout.setSize(larguraBotao, alturaBotao);
		logout.setImage(logoff);
		logout.setToolTipText("Logout");
		logout.setVisible(false);
		logout.setEnabled(false);
		logout.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				boolean answer = MessageDialog.openQuestion(incluirProblema
						.getShell(), "Confirmacao",
						"Tem certeza que deseja sair?");
				if (answer) {
					desabilitaBotoes();
					logout.setEnabled(false);
					logout.setVisible(false);
					login.setEnabled(true);
					login.setVisible(true);
				}
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		login = new Button(parentComposite, SWT.NONE);
		log = new Image(login.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/users.gif"));
		login.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		login.setSize(larguraBotao, alturaBotao);
		login.setImage(log);
		login.setToolTipText("Login");
		login.setEnabled(true);
		login.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
				if (aDB == null){
					projetoAtivo = viewComunication.getProjetoAtivo(); 
					// Objeto para o JayFX
					try {
						aDB = new PresleyJayFX( projetoAtivo );
					} catch (JayFXException e1) {
						e1.printStackTrace();
					}	
				}	
				
				// Exibe o wizard de login
				runLoginWizardAction();
				if (desenvolvedorLogado != null) {
					preenchelistaProblemasEnviados();
					preenchelistaProblemasRecebidos();
				}
			}

			public void mouseUp(MouseEvent e) {

			}

		});

		addUser = new Button(parentComposite, SWT.NONE);
		Image userAdd = new Image(addUser.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/addUser.gif"));
		addUser.setLocation(posHorBotaoNivel3, posVerBotaoNivel1);
		addUser.setSize(larguraBotao, alturaBotao);
		addUser.setImage(userAdd);
		addUser.setToolTipText("Adiciona novo desenvolvedor");
		addUser.setEnabled(true);
		addUser.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				// exibe o wizard de confirmacao para a adicao de desenvolvedor
				runAdicionaDesenvolvedorWizard();
			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		removeUser = new Button(parentComposite, SWT.NONE);
		Image userRemove = new Image(removeUser.getDisplay(),
				this.getClass().getResourceAsStream(
						"/src/main/resources/icons/removeUser.gif"));
		removeUser.setLocation(posHorBotaoNivel4, posVerBotaoNivel1);
		removeUser.setSize(larguraBotao, alturaBotao);
		removeUser.setImage(userRemove);
		removeUser.setToolTipText("Remove novo desenvolvedor");
		removeUser.setEnabled(false);
		removeUser.addMouseListener(new MouseListener() {

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

		treeProblemasEnviados = new Tree(parentComposite, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		treeProblemasEnviados.setLocation(0, posVerPainelProblemas); // posHorBotaoNivel1
		treeProblemasEnviados.setSize(larguraJanela, alturaPainelProblemas);
		treeProblemasEnviados.setVisible(true);
		treeProblemasEnviados.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				String titulo, descricao;

				if (treeProblemasEnviados.getSelection()[0].getData() instanceof Problema) {
					Problema problema = (Problema) treeProblemasEnviados
							.getSelection()[0].getData();
					titulo = problema.getDescricao();
					descricao = problema.getMensagem();
				} else {
					Solucao solucao = (Solucao) treeProblemasEnviados
							.getSelection()[0].getData();
					titulo = "Re: " + solucao.getProblema().getDescricao();
					descricao = solucao.getMensagem();
				}

				LerMensagem.setCarregaMensagem(titulo, descricao);
			}

		});

		treeProblemasEnviados.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				if ((treeProblemasEnviados.getSelectionCount() != 0)
						&& (treeProblemasEnviados.getSelection()[0].getData() instanceof Solucao)) {
					Solucao solucao = (Solucao) treeProblemasEnviados
							.getSelection()[0].getData();
					runEnviaRetornoSolucaoWizardAction(solucao);
					preenchelistaProblemasEnviados();
				}
			}

			public void mouseDown(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
			}

		});

		incluirProblema = new Button(parentComposite, SWT.NONE);
		Image obter = new Image(incluirProblema.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/add.gif"));
		incluirProblema.setLocation(posHorBotaoNivel5, posVerBotaoNivel1);
		incluirProblema.setSize(larguraBotao, alturaBotao);
		incluirProblema.setImage(obter);
		incluirProblema.setToolTipText("Inserir Problema");
		incluirProblema.setVisible(true);
		incluirProblema.setEnabled(true);
		incluirProblema.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				runAdcionaProblema();
				preenchelistaProblemasEnviados();
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		excluirProblema = new Button(parentComposite, SWT.NONE);
		Image trocaMsg = new Image(excluirProblema.getDisplay(), this
				.getClass().getResourceAsStream(
						"/src/main/resources/icons/remove.GIF"));
		excluirProblema.setLocation(posHorBotaoNivel6, posVerBotaoNivel1);
		excluirProblema.setSize(larguraBotao, alturaBotao);
		excluirProblema.setImage(trocaMsg);
		excluirProblema.setToolTipText("Excluir Problema");
		excluirProblema.setVisible(true);
		excluirProblema.setEnabled(true);
		excluirProblema.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				boolean excluir = (treeProblemasEnviados.getSelectionCount() != 0);

				if (!excluir)
					MessageDialog
							.openInformation(excluirProblema.getShell(),
									"Informação",
									"O problema deve ser selecionado antes de ser excluido");
				else
					excluir = MessageDialog
							.openQuestion(excluirProblema.getShell(),
									"Confirmacao",
									"Tem certeza que deseja excluir o Problema selecionado?");

				if (excluir) {
					// captura o problema selecionado
					Problema problemaSelecionado = (Problema) treeProblemasEnviados
							.getSelection()[0].getData();

					// realiza a remocao do Problema no servidor
					viewComunication.removerProblema(problemaSelecionado);
					preenchelistaProblemasEnviados();
				}
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		validarSolucao = new Button(parentComposite, SWT.NONE);
		Image ok = new Image(validarSolucao.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/ok.gif"));
		validarSolucao.setLocation(posHorBotaoNivel7, posVerBotaoNivel1);
		validarSolucao.setSize(larguraBotao, alturaBotao);
		validarSolucao.setImage(ok);
		validarSolucao.setToolTipText("Valida a resposta do usuario");
		validarSolucao.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {

				if ((treeProblemasEnviados.getSelectionCount() == 0)
						|| (treeProblemasEnviados.getSelection()[0].getData() instanceof Problema)) {
					MessageDialog
							.openInformation(validarSolucao.getShell(),
									"Informação",
									"A Solução deve ser selecionada antes de ser Aprovada");
				} else {
					Solucao solucao = (Solucao) treeProblemasEnviados
							.getSelection()[0].getData();
					solucao.setAjudou(true);
					viewComunication.atualizarStatusSolucao(solucao);

					MessageDialog.openInformation(validarSolucao.getShell(),
							"Informação", "Solução aprovada com sucesso.");
				}
			}

			public void mouseUp(MouseEvent e) {
			}

		});

		encerrarProblema = new Button(parentComposite, SWT.NONE);
		Image encerra = new Image(encerrarProblema.getDisplay(), this
				.getClass().getResourceAsStream(
						"/src/main/resources/icons/encerra.gif"));
		encerrarProblema.setLocation(posHorBotaoNivel8, posVerBotaoNivel1);
		encerrarProblema.setSize(larguraBotao, alturaBotao);
		encerrarProblema.setImage(encerra);
		encerrarProblema.setToolTipText("Encerra o Problema Selecionado");
		encerrarProblema.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
				if ((treeProblemasEnviados.getSelectionCount() == 0)
						|| (treeProblemasEnviados.getSelection()[0].getData() instanceof Solucao)) {
					MessageDialog
							.openInformation(validarSolucao.getShell(),
									"Informação",
									"O Problema deve ser selecionado antes de ser Concluido");
				} else {
					Problema problema = (Problema) treeProblemasEnviados
							.getSelection()[0].getData();
					problema.setResolvido(true);
					viewComunication.atualizarStatusProblema(problema);

					preenchelistaProblemasEnviados();
					MessageDialog.openInformation(validarSolucao.getShell(),
							"Informação", "Problema concluido com sucesso.");
				}
			}

			public void mouseUp(MouseEvent e) {
			}

		});

		mensagemRecebida = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		mensagemRecebida.setText("Problemas Recebidos");
		mensagemRecebida.setLocation(0, posVerPainelProblemas
				+ alturaPainelProblemas + distanciaPanelLabel);
		mensagemRecebida.setSize(200, 20);
		mensagemRecebida.setVisible(true);
		mensagemRecebida.setBackground(new Color(mensagemRecebida.getDisplay(),
				247, 193, 193));

		treeProblemasRecebidos = new Tree(parentComposite, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		treeProblemasRecebidos.setLocation(0, posVerPainelLerMensagem);
		treeProblemasRecebidos.setSize(larguraJanela, alturaPainelLerMensagem);
		treeProblemasRecebidos.setVisible(true);
		treeProblemasRecebidos.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e) {
				String titulo, descricao;

				if (treeProblemasRecebidos.getSelection()[0].getData() instanceof Problema) {
					Problema problema = (Problema) treeProblemasRecebidos.getSelection()[0].getData();
					titulo = problema.getDescricao();
					descricao = problema.getMensagem();
				} else {
					Solucao solucao = (Solucao) treeProblemasRecebidos.getSelection()[0].getData();
					titulo = "Re: " + solucao.getProblema().getDescricao();
					descricao = solucao.getMensagem();
				}

				LerMensagem.setCarregaMensagem(titulo, descricao);				
			}

		});

		treeProblemasRecebidos.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				if (treeProblemasRecebidos.getSelectionCount() != 0) {
					if (treeProblemasRecebidos.getSelection()[0].getData() instanceof Solucao) {
						Solucao solucao = (Solucao) treeProblemasRecebidos
								.getSelection()[0].getData();
						if ((solucao.getSolucaoResposta() == null)
								&& (!solucao.getSolucaoResposta().equals("")))
							runEnviaRespostaWizardAction(solucao.getProblema(),
									solucao);
					} else if (treeProblemasRecebidos.getSelection()[0]
							.getData() instanceof Problema) {
						Problema problema = (Problema) treeProblemasRecebidos
								.getSelection()[0].getData();
						runEnviaRespostaWizardAction(problema, null);
					}
					preenchelistaProblemasRecebidos();
				}
			}

			public void mouseDown(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
			}

		});

		lblGrupoBotoes3 = new Label(parentComposite, SWT.BORDER_DOT);
		lblGrupoBotoes3.setText("  ");
		lblGrupoBotoes3.setBackground(new Color(lblGrupoBotoes3.getDisplay(),
				204, 204, 204));
		lblGrupoBotoes3.setLocation(posHorBotaoNivel7 - 2, 2);
		lblGrupoBotoes3.setSize(50, 24);
		lblGrupoBotoes3.setVisible(true);

		lblGrupoBotoes2 = new Label(parentComposite, SWT.BORDER_DOT);
		lblGrupoBotoes2.setText("  ");
		lblGrupoBotoes2.setBackground(new Color(lblGrupoBotoes2.getDisplay(),
				51, 153, 204));
		lblGrupoBotoes2.setLocation(posHorBotaoNivel5 - 2, 2);
		lblGrupoBotoes2.setSize(50, 24);
		lblGrupoBotoes2.setVisible(true);

		lblGrupoBotoes1 = new Label(parentComposite, SWT.BORDER_DOT);
		lblGrupoBotoes1.setText("  ");
		lblGrupoBotoes1.setBackground(new Color(lblGrupoBotoes1.getDisplay(),
				51, 153, 0));
		lblGrupoBotoes1.setLocation(posHorBotaoNivel2 -2, 2);
		lblGrupoBotoes1.setSize(74, 24);
		lblGrupoBotoes1.setVisible(true);

		desabilitaBotoes();

		timer = new Timer();
		timer.schedule(new TimerTask() {
			private Runnable updateAction;

			public void run() {
				Display.getDefault().asyncExec(getUpdateAction());
			}

			private Runnable getUpdateAction() {
				if (updateAction == null) {
					updateAction = new Runnable() {

						public void run() {
							if (bLogin) {
								treeProblemasEnviados.removeAll();
								preenchelistaProblemasEnviados();
								preenchelistaProblemasRecebidos();
							}
						}
					};
				}
				return updateAction;
			}

		}, 0, 500 * 1000); // 1000 Representa 1 segundo

	}

	private void preenchelistaProblemasEnviados() {
		treeProblemasEnviados.removeAll();
		Image imgProblema = new Image(addUser.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/problema.gif"));

		ArrayList<Problema> problemasEncontrados = getViewComunication()
				.getProblemas(this.getDesenvolvedorLogado());

		TreeItem[] item = new TreeItem[problemasEncontrados.size()];

		int num = 0;
		for (Problema problema : problemasEncontrados) {
			// Cria o item de problema
			item[num] = new TreeItem(treeProblemasEnviados, SWT.NONE);
			item[num].setText(problema.getDescricao());
			item[num].setData(problema);
			item[num].setImage(imgProblema);

			preecheTreeItemSolucao(getViewComunication()
					.listarSolucoesDoProblema(problema), item[num], true);

			num++;
		}

	}

	private void preenchelistaProblemasRecebidos() {
		treeProblemasRecebidos.removeAll();
		Image imgProblema = new Image(addUser.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/problema.gif"));
		mensagensExibidas = viewComunication.obterMensagens(getDesenvolvedorLogado());

		if (mensagensExibidas != null) {
			TreeItem[] item = new TreeItem[mensagensExibidas.size()];

			int num = 0;
			for (Iterator<Mensagem> iteratorMensagem = mensagensExibidas
					.iterator(); iteratorMensagem.hasNext();) {
				Mensagem mensagem = iteratorMensagem.next();
				Problema problema = mensagem.getProblema();

				// Cria o item de problema
				item[num] = new TreeItem(treeProblemasRecebidos, SWT.NONE);
				item[num].setText(problema.getDescricao());
				item[num].setData(problema);
				item[num].setImage(imgProblema);

				preecheTreeItemSolucao(getViewComunication()
						.listarSolucoesDoProblema(problema), item[num], false);

				num++;
			}
		}

	}

	private void preecheTreeItemSolucao(ArrayList<Solucao> solucoes,
			TreeItem itemPai, boolean problemaEnviado) {
		TreeItem[] item = new TreeItem[solucoes.size()];
		Image imgSolucao = new Image(addUser.getDisplay(), this.getClass()
				.getResourceAsStream("/src/main/resources/icons/solucao.gif"));
		Image imgRespostaSolucao = new Image(addUser.getDisplay(), this
				.getClass().getResourceAsStream(
						"/src/main/resources/icons/respostaSolucao.gif"));

		int numItem = 0;
		for (Iterator<Solucao> iterator = solucoes.iterator(); iterator
				.hasNext();) {
			Solucao solucao = iterator.next();

			// Apenas preenche as Soluções do Desenvolvedor logado
			if (problemaEnviado
					|| solucao.getDesenvolvedor().getEmail().equals(
							getDesenvolvedorLogado().getEmail())) {
				item[numItem] = new TreeItem(itemPai, SWT.NONE);
				item[numItem].setText(solucao.getMensagem());
				item[numItem].setData(solucao);
				item[numItem].setImage(imgSolucao);

				if (solucao.getRetornoSolucao() != null) {
					TreeItem[] itemResposta = new TreeItem[solucoes.size()];
					itemResposta[numItem] = new TreeItem(item[numItem],
							SWT.NONE);
					itemResposta[numItem].setText(solucao.getRetornoSolucao());
					itemResposta[numItem].setData(solucao);
					itemResposta[numItem].setImage(imgRespostaSolucao);
				}

				if (solucao.getSolucaoResposta() != null) {
					ArrayList<Solucao> solucaoResposta = new ArrayList<Solucao>();
					solucaoResposta.add(solucao.getSolucaoResposta());
					preecheTreeItemSolucao(solucaoResposta, item[numItem],
							problemaEnviado);
				}
				numItem++;
			}

		}

	}

	private void runAdcionaProblema() {
		this.runAdcionaProblema = new RunAdcionaProblemaWizardAction(this);
		this.runAdcionaProblema.run(null);
	}

	private void runAdcionaProjetoWizardAction() {
		com.hukarz.presley.client.gui.action.RunAdcionaProjetoWizardAction runProjeto = new com.hukarz.presley.client.gui.action.RunAdcionaProjetoWizardAction(
				this);
		runProjeto.run(null);
	}
	
	private void runLoginWizardAction() {
		com.hukarz.presley.client.gui.action.RunLoginWizardAction runLogin = new com.hukarz.presley.client.gui.action.RunLoginWizardAction(
				this);
		runLogin.run(null);
	}

	public void setDesenvolvedorLogado(Desenvolvedor des) {
		this.desenvolvedorLogado = des;
	}

	public static Desenvolvedor getDesenvolvedorLogado() {
		return desenvolvedorLogado;
	}

	public void habilitaBotoes() {
		incluirProblema.setEnabled(true);
		excluirProblema.setEnabled(true);
		validarSolucao.setEnabled(true);
		encerrarProblema.setEnabled(true);
		bLogin = true;
	}

	public void desabilitaBotoes() {
		incluirProblema.setEnabled(false);
		excluirProblema.setEnabled(false);
		validarSolucao.setEnabled(false);
		encerrarProblema.setEnabled(false);
		bLogin = false;
	}

	public void desabilitaBotaoLogin() {
		login.setEnabled(false);
		login.setVisible(false);
		logout.setEnabled(true);
		logout.setVisible(true);
	}

	private void runAdicionaDesenvolvedorWizard() {
		com.hukarz.presley.client.gui.action.RunAdicionaDesenvolvedorWizardAction runAdicionaDesenvolvedor = new com.hukarz.presley.client.gui.action.RunAdicionaDesenvolvedorWizardAction(
				this);
		runAdicionaDesenvolvedor.run(null);
	}

	private void runEnviaRespostaWizardAction(Problema problema,
			Solucao solucaoOrigem) {
		this.runEnviaResposta = new RunEnviaRespostaWizardAction(this,
				problema, solucaoOrigem);
		this.runEnviaResposta.run(null);
	}

	private void runEnviaRetornoSolucaoWizardAction(Solucao solucao) {
		this.RunEnviaRetornoSolucao = new RunEnviaRetornoSolucaoWizardAction(
				this, solucao);
		this.RunEnviaRetornoSolucao.run(null);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
