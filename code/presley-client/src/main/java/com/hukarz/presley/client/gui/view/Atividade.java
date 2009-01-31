package com.hukarz.presley.client.gui.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.part.ViewPart;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.ProblemaAtividade;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.client.gui.action.RunAdicionaAtividadeWizardAction;
import com.hukarz.presley.client.gui.action.RunAdicionaDesenvolvedorWizardAction;
import com.hukarz.presley.client.gui.action.RunAssociaProblemaAtividadeWizardAction;
import com.hukarz.presley.client.gui.action.RunBuscaDesenvolvedorWizardAction;
import com.hukarz.presley.client.gui.action.RunRemoveAtividadeWizardAction;
import com.hukarz.presley.client.gui.view.comunication.ViewComunication;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;

public class Atividade extends ViewPart {

	private static final Logger logger = Logger.getLogger(Atividade.class);

	private ArrayList<Button> buttons;
	private HashMap<String, List> conhecimentosList;
	private HashMap<String, List> problemasList;
	private ViewComunication viewComunication;
	private List listaAtividades, listaConhecimentos, listaProblemas,
	listaDesenvolvedores;
	private Label conhecimentoLabel, problemaLabel, contatosLabel;
	private Composite parentComposite;
	private Button login, logout, addAtividade, addUser, removeUser,
	addConhecimento, removeConhecimento, removeAtividade,
	encerraAtividade, associaConhecimento, desassociaConhecimento,
	associaProblema, desassociaProblema, buscaDesenvolvedor,
	enviaMensagem;

	private ArrayList<Desenvolvedor> desenvolvedoresHabilitados;

	private RunAdicionaAtividadeWizardAction runAdicionaAtividade;
	private RunRemoveAtividadeWizardAction runRemoveAtividade;
	private RunAssociaProblemaAtividadeWizardAction runAssociaProblema;
	private RunBuscaDesenvolvedorWizardAction runBuscaDesenvolvedor;
	private RunAdicionaDesenvolvedorWizardAction runAdicionaDesenvolvedor;

	private static Desenvolvedor desenvolvedorLogado = null;
	private String atividadeSelecionada = null;
	private String problemaSelecionado = null;
	private Hashtable<String, ArrayList<Conhecimento>> problemaAssociadoConhecimentos = new Hashtable<String, ArrayList<Conhecimento>>();

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
	private final int larguraJanela = 195;
	private final int distanciaPanelLabel = 5;
	private final int posHorPanel = 0;

	private String ipServidor = "127.0.0.1";

	public Atividade() {
		this.viewComunication = new ViewComunication(ipServidor);

	}

	public void createPartControl(Composite parent) {
		parent.setLayout(null);
		initComponents(parent);
	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setDesenvolvedorLogado(Desenvolvedor des) {
		this.desenvolvedorLogado = des;
	}

	public void updateListaGraficaDesenvolvedores() {
		for (int index = 0; index < desenvolvedoresHabilitados.size(); index++) {
			listaDesenvolvedores.add(desenvolvedoresHabilitados.get(index)
					.getEmail());
		}
	}

	public static Desenvolvedor getDesenvolvedorLogado() {
		return desenvolvedorLogado;
	}

	public void habilitaBotoes() {
		addAtividade.setEnabled(true);
		removeAtividade.setEnabled(true);
		encerraAtividade.setEnabled(true);
		associaConhecimento.setEnabled(true);
		desassociaConhecimento.setEnabled(true);
		associaProblema.setEnabled(true);
		desassociaProblema.setEnabled(true);
		buscaDesenvolvedor.setEnabled(true);
		addConhecimento.setEnabled(true);
		removeConhecimento.setEnabled(true);
		removeUser.setEnabled(true);
		enviaMensagem.setEnabled(true);
	}

	public void desabilitaBotoes() {
		addAtividade.setEnabled(false);
		removeAtividade.setEnabled(false);
		encerraAtividade.setEnabled(false);
		associaConhecimento.setEnabled(false);
		desassociaConhecimento.setEnabled(false);
		associaProblema.setEnabled(false);
		desassociaProblema.setEnabled(false);
		buscaDesenvolvedor.setEnabled(false);
		addConhecimento.setEnabled(false);
		removeConhecimento.setEnabled(false);
		removeUser.setEnabled(false);
		enviaMensagem.setEnabled(false);
	}

	public void desabilitaBotaoLogin() {
		login.setEnabled(false);
		login.setVisible(false);
		logout.setEnabled(true);
		logout.setVisible(true);
	}

	private void initComponents(Composite parent) {
		this.parentComposite = parent;

		buttons = new ArrayList<Button>();

		logout = new Button(parentComposite, SWT.NONE);
		Image logoff = new Image(logout.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/logout.gif"));
		logout.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
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
				boolean answer = MessageDialog.openQuestion(removeAtividade
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
		Image log = new Image(login.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/users.gif"));
		login.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
		login.setSize(larguraBotao, alturaBotao);
		login.setImage(log);
		login.setToolTipText("Login");
		login.setEnabled(true);
		login.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				// Exibe o wizard de login
				runLoginWizardAction();

			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		addAtividade = new Button(parentComposite, SWT.NONE);
		Image add = new Image(addAtividade.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/add.gif"));
		addAtividade.setLocation(posHorBotaoNivel4, posVerBotaoNivel1);
		addAtividade.setSize(larguraBotao, alturaBotao);
		addAtividade.setImage(add);
		addAtividade.setToolTipText("Adiciona nova atividade");
		addAtividade.setEnabled(false);
		addAtividade.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			public void mouseDown(MouseEvent e) {
				String ultimaAtividadeAdicionada = null;
				String novaAtividade = null;

				// armazena a ultima atividade incluida
				ArrayList<String> atividadesAnteriores = viewComunication.getAtividades();
				if (!atividadesAnteriores.isEmpty()) {
					ultimaAtividadeAdicionada = atividadesAnteriores
					.get(atividadesAnteriores.size() - 1);
				}

				// exibe o wizard para adicao de nova atividade
				runAdicionaWizardAction();

				// captura o nome da atividade inserido pelo usuario
				ArrayList<String> atividadesPosteriores = viewComunication
				.getAtividades();
				if (atividadesPosteriores != null
						&& !atividadesPosteriores.isEmpty()) {
					novaAtividade = atividadesPosteriores
					.get(atividadesPosteriores.size() - 1);
				}

				// viewComunication.buscaAtividades();

				// ação de adicionar atividade foi cancelada
				if (ultimaAtividadeAdicionada != null
						&& ultimaAtividadeAdicionada.equals(novaAtividade)) {

				} else {
					// adiciona o item na lista de atividades
					listaAtividades.add(novaAtividade);

					// captura os conhecimentos envolvidos
					ArrayList<Conhecimento> conhecimentos = viewComunication
					.getConhecimentosEnvolvidos(novaAtividade);

					// limpa as listas de conhecimentos e problemas
					listaConhecimentos.removeAll();
					listaProblemas.removeAll();

					// cria estruturas para armazenar os conhecimentos e
					// problemas
					ArrayList<String> conh = new ArrayList<String>();

					// Preenche a lista de conhecimentos
					if (conhecimentos != null) {
						for (Conhecimento c : conhecimentos) {
							conh.add(c.getNome());
							listaConhecimentos.add(c.getNome());
						}
					}

					// Seleciona o item recem adicionado
					listaAtividades.select(listaAtividades
							.indexOf(novaAtividade));
					atividadeSelecionada = novaAtividade;

					// atualiza a vizualizacao
					listaAtividades.getParent().redraw();
					listaAtividades.getParent().update();
					listaConhecimentos.getParent().redraw();
					listaConhecimentos.getParent().update();

				}
			}

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
		});

		addUser = new Button(parentComposite, SWT.NONE);
		Image userAdd = new Image(addUser.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/addUser.gif"));
		addUser.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
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
		Image userRemove = new Image(removeUser.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/removeUser.gif"));
		removeUser.setLocation(posHorBotaoNivel3, posVerBotaoNivel1);
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

		removeAtividade = new Button(parentComposite, SWT.NONE);
		Image remove = new Image(addAtividade.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/remove.gif"));
		removeAtividade.setLocation(posHorBotaoNivel5, posVerBotaoNivel1);
		removeAtividade.setSize(larguraBotao, alturaBotao);
		removeAtividade.setImage(remove);
		removeAtividade.setToolTipText("Remove atividade selecionada");
		removeAtividade.setEnabled(false);
		removeAtividade.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {

				boolean answer = MessageDialog
				.openQuestion(removeAtividade.getShell(),
						"Confirmacao",
				"Tem certeza que deseja excluir a atividade selecionada?");
				if (answer) {
					// captura o id da atividade selecionada
					int idAtividade = listaAtividades.getSelectionIndex();
					String nomeAtividade = listaAtividades.getItem(idAtividade);
					TipoAtividade atividadeLocalizada = null;

					ArrayList<TipoAtividade> atividades = viewComunication.buscaAtividades();
					logger.info("Dados da atividade a ser removida: " + 
							atividadeLocalizada.getDescricao() + "\n" +
							atividadeLocalizada.getId());
					if (atividades != null) {
						for (TipoAtividade tipoAtividade : atividades) {
							if (tipoAtividade.getDescricao().equals(
									nomeAtividade)) {
								atividadeLocalizada = tipoAtividade;
							}
						}
					} else {
						logger.info("Nao achou a lista de atividade");
					}



					// realiza a remocao da atividade no servidor
					viewComunication.removerAtividade(atividadeLocalizada);

					// realiza a remocao da atividade na lista grafica
					listaAtividades.remove(idAtividade);

					// limpa as listas de problemas e conhecimentos
					listaConhecimentos.removeAll();
					listaProblemas.removeAll();

					// atualiza as listas
					listaAtividades.getParent().redraw();
					listaAtividades.getParent().update();
					listaConhecimentos.getParent().redraw();
					listaConhecimentos.getParent().update();
					listaProblemas.getParent().redraw();
					listaProblemas.getParent().update();
				}
			}

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		encerraAtividade = new Button(parentComposite, SWT.NONE);
		Image encerra = new Image(addAtividade.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/encerra.gif"));
		encerraAtividade.setLocation(posHorBotaoNivel6, posVerBotaoNivel1);
		encerraAtividade.setSize(larguraBotao, alturaBotao);
		encerraAtividade.setImage(encerra);
		encerraAtividade.setToolTipText("Encerra atividade selecionada");
		encerraAtividade.setEnabled(false);
		encerraAtividade.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				boolean answer = MessageDialog
				.openQuestion(removeAtividade.getShell(),
						"Confirmacao",
				"Tem certeza que deseja encerrar a atividade selecionada?");
				if (answer) {
					// TODO implementar o encerramento
				}

			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		listaAtividades = new List(parentComposite, SWT.SINGLE | SWT.BORDER);
		listaAtividades.setLocation(posHorPanel, posVerPainelAtividade);
		listaAtividades.setSize(larguraJanela, alturaPainelAtividade);
		listaAtividades.setVisible(true);
		ArrayList<String> atividadesNomes = viewComunication.getAtividades();

		if (atividadesNomes != null) {
			for (int i = 0; i < atividadesNomes.size(); i++) {
				listaAtividades.add(atividadesNomes.get(i));

			}
			listaAtividades.addMouseListener(new MouseListener() {

				public void mouseDoubleClick(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

				public void mouseDown(MouseEvent arg0) {

					// Captura atividade selecionada
					int index = listaAtividades.getSelectionIndex();
					String atividade = listaAtividades.getItem(index);

					atividadeSelecionada = atividade;

					// Retorna os problemas associados
					ArrayList<Problema> problemas = viewComunication.getProblemas(atividade);
					ArrayList<Problema> problemasAssociados = new ArrayList<Problema>();
					for (Problema problema : problemas) {
						if (problema.getAtividade().getDescricao().equals(
								atividadeSelecionada)) {
							problemasAssociados.add(problema);
						}
					}

					// Captura os conhecimentos associados
					ArrayList<Conhecimento> conhecimentos = viewComunication
					.getConhecimentosEnvolvidos(atividade);

					// Limpa a lista de problemas
					listaProblemas.removeAll();

					// limpa a lista de conhecimentos
					listaConhecimentos.removeAll();

					// Adiciona os problemas a arvore
					if (problemasAssociados != null) {
						for (int i = 0; i < problemasAssociados.size(); i++) {
							listaProblemas.add(problemasAssociados.get(i)
									.getDescricao());
							listaProblemas.getParent().redraw();
							listaProblemas.getParent().update();
						}
					}

					// Adiciona os conhecimentos a lista
					if (conhecimentos != null) {
						for (int i = 0; i < conhecimentos.size(); i++) {
							listaConhecimentos.add(conhecimentos.get(i)
									.getNome());
							listaConhecimentos.getParent().redraw();
							listaConhecimentos.getParent().update();
						}

					}

				}

				public void mouseUp(MouseEvent arg0) {
					// TODO Auto-generated method stub

				}

			});

		}

		listaDesenvolvedores = new List(parentComposite, SWT.V_SCROLL
				| SWT.BORDER);
		listaDesenvolvedores.setLocation(posHorPanel, posVerPainelContatos);
		listaDesenvolvedores.setSize(larguraJanela, alturaPainelContatos);
		listaDesenvolvedores.setVisible(true);
		listaDesenvolvedores.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {

				// Captura os desenvolvedores selecionados
				String[] nomesDesenvolvedoresSelecionados = listaDesenvolvedores
				.getSelection();

			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		conhecimentoLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		conhecimentoLabel.setText("Conhecimentos Envolvidos");
		conhecimentoLabel.setLocation(0, posVerPainelAtividade
				+ alturaPainelAtividade + distanciaPanelLabel);
		conhecimentoLabel.setSize(200, 20);
		conhecimentoLabel.setVisible(true);
		conhecimentoLabel.setBackground(new Color(conhecimentoLabel
				.getDisplay(), 102, 204, 255));

		listaConhecimentos = new List(parentComposite, SWT.V_SCROLL
				| SWT.BORDER);
		listaConhecimentos.setLocation(posHorPanel, posVerPainelConhecimentos);
		listaConhecimentos.setSize(larguraJanela, alturaPainelConhecimentos);
		listaConhecimentos.setVisible(true);

		conhecimentosList = new HashMap<String, List>();

		addConhecimento = new Button(parentComposite, SWT.NONE);
		addConhecimento.setLocation(posHorBotaoNivel3, posVerBotaoNivel2);
		addConhecimento.setSize(larguraBotao, alturaBotao);
		addConhecimento.setImage(add);
		addConhecimento.setToolTipText("Cadastra um novo conhecimento");
		addConhecimento.setEnabled(true);
		addConhecimento.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				Conhecimento ultimoConhecimentoAdicionado = null;
				Conhecimento novoConhecimento = null;

				// armazena o ultimo conhecimento incluido
				ArrayList<Conhecimento> conhecimentosAnteriores = viewComunication
				.getListaConhecimentos();
				if (conhecimentosAnteriores != null
						&& !conhecimentosAnteriores.isEmpty()) {
					ultimoConhecimentoAdicionado = conhecimentosAnteriores
					.get(conhecimentosAnteriores.size() - 1);
				}

				// exibe o wizard para adicao de novo conhecimento
				runAdicionaConhecimentoWizardAction();

				// captura o nome da atividade inserido pelo usuario
				ArrayList<Conhecimento> conhecimentosPosteriores = viewComunication
				.getListaConhecimentos();
				if (conhecimentosPosteriores != null
						&& !conhecimentosPosteriores.isEmpty()) {
					novoConhecimento = conhecimentosPosteriores
					.get(conhecimentosPosteriores.size() - 1);
				}

			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		removeConhecimento = new Button(parentComposite, SWT.NONE);
		removeConhecimento.setLocation(posHorBotaoNivel4, posVerBotaoNivel2);
		removeConhecimento.setSize(larguraBotao, alturaBotao);
		removeConhecimento.setImage(remove);
		removeConhecimento.setToolTipText("Remove o conhecimento selecionado");
		removeConhecimento.setEnabled(false);
		removeConhecimento.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {

				boolean answer = MessageDialog
				.openQuestion(removeAtividade.getShell(),
						"Confirmacao",
				"Tem certeza que deseja excluir o conhecimento selecionado?");

				if (answer) {

					// captura o id do conhecimento selecionado
					int idConh = listaConhecimentos.getSelectionIndex();

					// captura o nome do conhecimento
					String nomeConh = listaConhecimentos.getItem(idConh);
					Conhecimento conhLocalizado = null;

					/* Encontra o conhecimento selecionado no banco */
					ArrayList<Conhecimento> conhecimentos = viewComunication.getListaConhecimentos();
					logger.info("Lista de Conhecimentos : " + conhecimentos.get(0).getNome());
					logger.info(conhecimentos.get(1).getNome());

					for (Conhecimento conhecimento : conhecimentos) {
						if (conhecimento.getNome().equals(nomeConh)) {
							conhLocalizado = conhecimento;
						}
					}

					logger.info("Dados do conhecimento a ser removido: " + conhLocalizado.getDescricao());

					/* Testar se o conhecimento é pai de alguem */
					try {
						if (viewComunication.possuiFilhos(conhLocalizado)) {
							logger.info("Soh eh possivel excluir conhecimentos folhas!");
						} else {
							// realiza a remocao da atividade no servidor
							viewComunication
							.removerConhecimento(conhLocalizado);

							// realiza a remocao da atividade na lista grafica
							listaConhecimentos.remove(idConh);
						}
					} catch (ConhecimentoInexistenteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		associaConhecimento = new Button(parentComposite, SWT.NONE);
		Image ass = new Image(addAtividade.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/associaConh.gif"));
		associaConhecimento.setLocation(posHorBotaoNivel1, posVerBotaoNivel2);
		associaConhecimento.setSize(larguraBotao, alturaBotao);
		associaConhecimento.setImage(ass);
		associaConhecimento
		.setToolTipText("Associa novo conhecimento a atividade");
		associaConhecimento.setEnabled(false);
		associaConhecimento.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				Conhecimento novoConhecimento = null;
				Conhecimento ultimoCohecimentoAdicionado = null;

				// armazena a ultima atividade incluida
				ArrayList<Conhecimento> conhecimentosAnteriores = viewComunication
				.getConhecimentosEnvolvidos(atividadeSelecionada);
				if (conhecimentosAnteriores != null
						&& !conhecimentosAnteriores.isEmpty()) {
					ultimoCohecimentoAdicionado = conhecimentosAnteriores
					.get(conhecimentosAnteriores.size() - 1);
				}

				// exibe o wizard para adicao de novo conhecimento
				runAssociaConhecimentoAtividade();

				// captura os nomes dos conhecimentos selecionados pelo usuario
				ArrayList<Conhecimento> conhecimentosPosteriores = viewComunication
				.getConhecimentosEnvolvidos(atividadeSelecionada);
				if (conhecimentosPosteriores != null
						&& !conhecimentosPosteriores.isEmpty()) {
					novoConhecimento = conhecimentosPosteriores
					.get(conhecimentosPosteriores.size() - 1);
					listaConhecimentos.add(novoConhecimento.getNome());
				}

			}

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		desassociaConhecimento = new Button(parentComposite, SWT.NONE);
		Image desass = new Image(addAtividade.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/desassociaConh.gif"));
		desassociaConhecimento
		.setLocation(posHorBotaoNivel2, posVerBotaoNivel2);
		desassociaConhecimento.setSize(larguraBotao, alturaBotao);
		desassociaConhecimento.setImage(desass);
		desassociaConhecimento
		.setToolTipText("Desassocia o conhecimento da atividade");
		desassociaConhecimento.setEnabled(false);
		desassociaConhecimento.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {

				// recupera o id do conhecimento selecionado na na lista da
				// interface
				int idConhecimento = listaConhecimentos.getSelectionIndex();
				String nomeConhecimento = listaConhecimentos
				.getItem(idConhecimento);
				Conhecimento conhecimentoDesassociado = null;
				TipoAtividade atividadeDesassociada = null;
				ArrayList<TipoAtividade> atividade = null;

				boolean answer = MessageDialog.openQuestion(removeAtividade
						.getShell(), "Confirmacao",
						"Tem certeza que desassociar o conhecimento "
						+ nomeConhecimento
						+ " da atividade selecionada?");

				if (answer) {
					atividade = viewComunication.buscaAtividades();

					for (TipoAtividade a : atividade) {
						if (a.getDescricao().equals(atividadeSelecionada))
							atividadeDesassociada = a;
					}
					// recupera todos os cohecimentos envolvidos na ativadade
					ArrayList<Conhecimento> conhecimentosPosteriores = viewComunication
					.getConhecimentosEnvolvidos(atividadeDesassociada
							.getDescricao());

					for (Conhecimento c : conhecimentosPosteriores) {
						if (c.getNome().equals(nomeConhecimento))
							conhecimentoDesassociado = c;
					}
					ArrayList<Conhecimento> desassociaConhecimento = new ArrayList<Conhecimento>();

					desassociaConhecimento.add(conhecimentoDesassociado);

					try {
						viewComunication.desassociaConhecimentoAtividade(
								desassociaConhecimento, atividadeDesassociada);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					listaConhecimentos.remove(idConhecimento);
				}
			}

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		ArrayList<String> nomesAtividades = viewComunication.getAtividades();
		if (nomesAtividades != null) {
			for (String selection : nomesAtividades) {
				ArrayList<Conhecimento> conhecimentos = viewComunication
				.getConhecimentosEnvolvidos(selection);
				if (conhecimentos != null) {
					for (Conhecimento conhecimento : conhecimentos)
						listaConhecimentos.add(conhecimento.getNome());
					conhecimentosList.put(selection, listaConhecimentos);
				}

			}

		}

		problemaLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		problemaLabel.setText("Problemas Encontrados");
		problemaLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),
				255, 102, 51));
		problemaLabel.setLocation(0, posVerPainelConhecimentos
				+ alturaPainelConhecimentos + distanciaPanelLabel);
		problemaLabel.setSize(200, 20);
		problemaLabel.setVisible(true);

		listaProblemas = new List(parentComposite, SWT.BORDER | SWT.V_SCROLL);
		listaProblemas.setLocation(posHorPanel, posVerPainelProblemas);
		listaProblemas.setSize(larguraJanela, alturaPainelProblemas);
		listaProblemas.setVisible(true);

		problemasList = new HashMap<String, List>();

		associaProblema = new Button(parentComposite, SWT.NONE);
		// Image ass = new
		// Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream
		// ("/icons/associaConh.gif"));
		associaProblema.setLocation(posHorBotaoNivel2, posVerBotaoNivel3);
		associaProblema.setSize(larguraBotao, alturaBotao);
		associaProblema.setImage(ass);
		associaProblema.setToolTipText("Associa novo problema a atividade");
		associaProblema.setEnabled(false);
		associaProblema.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				runAssociaProblemaAtividadeWizardAction();

				// limpando a lista de problemas para incluir o novo
				listaProblemas.removeAll();

				// Adiciona o problema criado e associado a atividade na lista
				// de problemas
				ArrayList<Problema> problemasAssocidos = null;
				problemasAssocidos = getViewComunication().getProblemas(atividadeSelecionada);
				for (Problema problema : problemasAssocidos) {
					listaProblemas.add(problema.getDescricao());
				}

			}

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

		desassociaProblema = new Button(parentComposite, SWT.NONE);
		// Image ass = new
		// Image(addAtividade.getDisplay(),this.getClass().getResourceAsStream
		// ("/icons/associaConh.gif"));
		desassociaProblema.setLocation(posHorBotaoNivel3, posVerBotaoNivel3);
		desassociaProblema.setSize(larguraBotao, alturaBotao);
		desassociaProblema.setImage(desass);
		desassociaProblema.setToolTipText("Desassocia problema de atividade");
		desassociaProblema.setEnabled(false);
		desassociaProblema.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent e) {
				boolean answer = MessageDialog
				.openQuestion(removeAtividade.getShell(),
						"Confirmacao",
				"Tem certeza que deseja desassociar o problema da atividade selecionada?");

				if (answer) {
					Problema problemaDesassociado = null;
					TipoAtividade atividadeDesassociada = null;
					ArrayList<Problema> listaProblemasAtividade = null;
					ProblemaAtividade probAtividade = new ProblemaAtividade();
					ArrayList<TipoAtividade> allAtividades = null;

					/* Captura o id do problema selecionado */
					int idProblema = listaProblemas.getSelectionIndex();

					/* Captura o nome do problema selecionado */
					String nomeProblema = listaProblemas.getItem(idProblema);

					/* Captura o id da atividade selecionada */
					int idAtividade = listaAtividades.getSelectionIndex();

					/* Captura o nome da atividade selecionada */
					String nomeAtividade = listaConhecimentos
					.getItem(idAtividade);

					/* Captura a lista de problemas associados a atividade */
					listaProblemasAtividade = viewComunication.getProblemas(nomeAtividade);

					/* Captura a lista de atividades */
					allAtividades = viewComunication.buscaAtividades();

					/*
					 * Captura a atividade da qual o problema deve ser
					 * desassociado
					 */
					for (TipoAtividade a : allAtividades) {
						if (a.getDescricao().equals(nomeAtividade)) {
							atividadeDesassociada = a;
						}
					}

					logger.info("Antes do for");
					/* Captura o problema a ser desassociado */
					for (Problema a : listaProblemasAtividade) {
						logger.info(nomeProblema);
						if (a.getDescricao().equals(nomeProblema)) {
							logger.info("cmd");
							problemaDesassociado = a;
							break;
						}
					}

					logger.info("2x0" + problemaDesassociado.getDescricao());
					/* Exclui do banco */
					try {
						probAtividade.setAtividade(atividadeDesassociada);
						probAtividade.setProblema(problemaDesassociado);
						viewComunication
						.desassociaProblemaAtividade(problemaDesassociado);
						listaProblemas.remove(idProblema);
						logger.info("Terminou!");
					} catch (Exception excep) {
						excep.printStackTrace();
					}
				}
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});

		buscaDesenvolvedor = new Button(parentComposite, SWT.NONE);
		Image buscaDes = new Image(addAtividade.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/buscaDes.gif"));
		buscaDesenvolvedor.setLocation(posHorBotaoNivel1, posVerBotaoNivel3);
		buscaDesenvolvedor.setSize(larguraBotao, alturaBotao);
		buscaDesenvolvedor.setImage(buscaDes);
		buscaDesenvolvedor
		.setToolTipText("Busca desenvolvedores para resolver esse problema");
		buscaDesenvolvedor.setEnabled(false);
		buscaDesenvolvedor.addMouseListener(new MouseListener() {

			public void mouseUp(MouseEvent arg0) {

			}

			public void mouseDown(MouseEvent arg0) {

				problemaSelecionado = listaProblemas.getItem(listaProblemas
						.getSelectionIndex());
				runBuscaDesenvolvedorWizardAction();

				ArrayList<String> desenvolvedoresNomes = new ArrayList<String>();
				for (Desenvolvedor d : desenvolvedoresHabilitados)
					desenvolvedoresNomes.add(d.getNome());

				if (desenvolvedoresNomes != null) {
					for (String nome : desenvolvedoresNomes) {
						listaDesenvolvedores.add(nome);
					}
				}
				// ArrayList<Problema> problemasAssocidos;
				// problemasAssocidos =
				// getViewComunication().getProblemas(atividadeSelecionada);
				// if (problemasAssocidos!=null) {
				// for (Problema problema : problemasAssocidos) {
				// listaProblemas.add(problema.getDescricao());
				// }
				// }
				// Adiciona o problema criado e associado a atividade na lista
				// de problemas

			}

			public void mouseDoubleClick(MouseEvent arg0) {

			}

		});

		contatosLabel = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		contatosLabel.setText("Contatos para os problemas");
		contatosLabel.setBackground(new Color(conhecimentoLabel.getDisplay(),
				255, 255, 153));
		contatosLabel.setLocation(0, posVerPainelProblemas
				+ alturaPainelProblemas + distanciaPanelLabel);
		contatosLabel.setSize(200, 20);
		contatosLabel.setVisible(true);

		enviaMensagem = new Button(parentComposite, SWT.NONE);
		Image enviaMens = new Image(enviaMensagem.getDisplay(), this.getClass()
				.getResourceAsStream("/icons/trocaMsg.gif"));
		enviaMensagem.setLocation(posHorBotaoNivel1, posVerBotaoNivel4);
		enviaMensagem.setSize(larguraBotao, alturaBotao);
		enviaMensagem.setImage(enviaMens);
		enviaMensagem.setToolTipText("Envia mensagem para usuario selecionado");
		enviaMensagem.setEnabled(false);
		enviaMensagem.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {

			}

			public void mouseDown(MouseEvent e) {
				Problema problemaAssociado = null;
				ArrayList<Desenvolvedor> desenvolvedorDestino = new ArrayList<Desenvolvedor>();

				/* Captura o id do desenvolvedor que devera receber a mensagem */
				int idDesenvolvedor = listaDesenvolvedores.getSelectionIndex();

				/* Captura o nome do desenvolvedor que devera receber a mensagem */
				String emailDesenvolvedorDestino = listaDesenvolvedores
				.getItem(idDesenvolvedor);

				/* Captura o id do problema ao qual sera associado a mensagem */
				int idProblema = listaProblemas.getSelectionIndex();

				/* Captura a lista de Desenvolvedores */
				ArrayList<Desenvolvedor> allDesenvolvedores = viewComunication.getListaDesenvolvedores();

				/* Captura a atividade da qual o problema deve ser desassociado */
				for (Desenvolvedor a : allDesenvolvedores) {
					if (a.getEmail().equals(emailDesenvolvedorDestino))
						desenvolvedorDestino.add(a);
				}

				/* Captura o nome do problema ao qual sera associado a mensagem */
				String nomeProblema = listaProblemas.getItem(idProblema);

				/* Captura a lista de problemas */
				ArrayList<Problema> allProblemas = viewComunication
				.getListaProblemas();

				/* Captura a atividade da qual o problema deve ser desassociado */
				for (Problema a : allProblemas) {
					if (a.getDescricao().equals(nomeProblema)) {
						problemaAssociado = a;
					}
				}

				/* Captura a mensagem a ser enviada */
				String mensagem = problemaAssociado.getMensagem();

				ArrayList<Desenvolvedor> desenvolvedoresOrigem = viewComunication
				.getListaDesenvolvedores();
				logger.info("Email do desenvolvedor logado: "
						+ desenvolvedoresOrigem.get(1).getEmail());
				logger.info("Email do desenvolvedor destino: "
						+ desenvolvedorDestino.get(0).getEmail());
				logger.info("Problema Associado: "
						+ problemaAssociado.getDescricao());
				logger.info("Mensagem: " + mensagem);

				viewComunication.enviarMensagem(desenvolvedorLogado,
						desenvolvedorDestino, problemaAssociado, mensagem);
			}

			public void mouseUp(MouseEvent e) {

			}

		});
	}

	public void associaConhecimentosProblema(Problema problema,
			ArrayList<Conhecimento> conhecimentos) {
		this.problemaAssociadoConhecimentos.put(problema.getDescricao(),
				conhecimentos);
	}

	public ArrayList<String> getAtividades() {
		return this.viewComunication.getAtividades();
	}

	private void runAdicionaWizardAction() {
		this.runAdicionaAtividade = new RunAdicionaAtividadeWizardAction(this);
		this.runAdicionaAtividade.run(null);
	}

	private void runAssociaProblemaAtividadeWizardAction() {
		this.runAssociaProblema = new RunAssociaProblemaAtividadeWizardAction(
				this, atividadeSelecionada);
		this.runAssociaProblema.run(null);
	}

	private void runBuscaDesenvolvedorWizardAction() {
		this.runBuscaDesenvolvedor = new RunBuscaDesenvolvedorWizardAction(this);
		this.runBuscaDesenvolvedor.run(null);
	}

	private void runLoginWizardAction() {
		com.hukarz.presley.client.gui.action.RunLoginWizardAction runLogin = new com.hukarz.presley.client.gui.action.RunLoginWizardAction(
				this);
		runLogin.run(null);

	}

	private void runAdicionaConhecimentoWizardAction() {
		// gui.action.RunAdicionaConhecimentoWizard runLogin = new
		// gui.action.RunAdicionaConhecimentoWizard(this);
		// runLogin.run(null);
	}

	private void runAssociaConhecimentoAtividade() {
		com.hukarz.presley.client.gui.action.RunAssociaConhecimentoAtividadeWizardAction runLogin = new com.hukarz.presley.client.gui.action.RunAssociaConhecimentoAtividadeWizardAction(
				this);
		runLogin.run(null);
	}

	private void runAdicionaDesenvolvedorWizard() {
		// gui.action.RunAdicionaDesenvolvedorWizardAction
		// runAdicionaDesenvolvedor = new
		// gui.action.RunAdicionaDesenvolvedorWizardAction(this);
		// runAdicionaDesenvolvedor.run(null);
	}

	public ViewComunication getViewComunication() {
		return this.viewComunication;
	}

	public String getAtividadeSelecionada() {
		return this.atividadeSelecionada;
	}

	public ArrayList<String> getConhecimentosDoProblema() {
		ArrayList<String> conhecimentosDoProblema = new ArrayList<String>();
		conhecimentosDoProblema = viewComunication
		.getConhecimentosAssociados(problemaSelecionado);
		/*
		 * if(!problemaAssociadoConhecimentos.isEmpty()){
		 * System.out.println("problema selecionado: " + problemaSelecionado);
		 * ArrayList<Conhecimento> conhecimentos =
		 * problemaAssociadoConhecimentos.get(problemaSelecionado);
		 * 
		 * 
		 * 
		 * System.out.println("Nome do conhecimento: "+conhecimentos.get(0).getNome
		 * ()); for(Conhecimento c : conhecimentos){
		 * conhecimentosDoProblema.add(c.getNome()); } } else
		 * System.out.println("hashtable de problemas e conhecimentos é null");
		 */

		return conhecimentosDoProblema;
	}

	public ArrayList<Desenvolvedor> getDesenvolvedores() {
		return desenvolvedoresHabilitados;
	}

	public void setDesenvolvedores(ArrayList<Desenvolvedor> desenvolvedores) {
		desenvolvedoresHabilitados = new ArrayList<Desenvolvedor>();
		this.desenvolvedoresHabilitados.addAll(desenvolvedores);
	}
}
