package com.hukarz.presley.client.gui.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ca.mcgill.cs.swevo.PresleyJayFX;
import ca.mcgill.cs.swevo.jayfx.ConversionException;
import ca.mcgill.cs.swevo.jayfx.JayFXException;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.client.gui.view.comunication.ViewComunication;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;


public class Dominio extends ViewPart {

	private Composite parentComposite;
	private ViewComunication viewComunication;
	private String ipServidor = "127.0.0.1";
	
	private Button incluirTopico, executarExperimento, incluirDocumentoBase;
	
	private final int larguraBotao = 20;
	private final int alturaBotao = 20;

	private final int posVerBotaoNivel1 = 4;
	private final int posVerBotaoNivel2 = 250;
	private final int posHorBotaoNivel1 = 4;
	private final int posHorBotaoNivel2 = 28;

	private final int posVerPainelTopicosDominio = 27;
	private final int posVerPainelDocumentoBase = 272; // 250
	
	private final int alturaPainelTopicosDominio = 200; // 100;
	private final int alturaPainelDocumentoBase = 150;
	
	private final int distanciaPanelLabel = 2;
	
	private final int larguraJanela = 200;
	
	private Tree treeConhecimentos = null;
	
	private Label documentosBase;
	private List listaDocumentosBase; 
	private PresleyJayFX aDB;
	private Projeto projetoAtivo;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public Dominio() {
		this.viewComunication = new ViewComunication(ipServidor);
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(null);
		initComponents(parent);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	private void initComponents(Composite parent)
	{
		this.parentComposite = parent;
		
    	try {
			treeConhecimentos = getViewComunication().getArvoreGraficaDeConhecimentos(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		} catch (ConhecimentoInexistenteException e2) {
			e2.printStackTrace();
		}
		//treeConhecimentos = new Tree(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		treeConhecimentos.setLocation(0, posVerPainelTopicosDominio);
		treeConhecimentos.setSize(larguraJanela, alturaPainelTopicosDominio);
		treeConhecimentos.setVisible(true);
		treeConhecimentos.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				preencherListaDocumentosBase();
			}
		});

		incluirTopico = new Button(parentComposite, SWT.NONE);
		Image obter = new Image(incluirTopico.getDisplay(),this.getClass().getResourceAsStream("/src/main/resources/icons/add.gif"));
		incluirTopico.setLocation(posHorBotaoNivel1, posVerBotaoNivel1);
		incluirTopico.setSize(larguraBotao, alturaBotao);
		incluirTopico.setImage(obter);
		incluirTopico.setToolTipText("Inserir Tópico");
		incluirTopico.setVisible(true);
		incluirTopico.setEnabled(true);
		incluirTopico.addMouseListener(new MouseListener() {

			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent e) {

				// exibe o wizard para adicao de novo conhecimento
				runAdicionaConhecimentoWizardAction();
				
		    	treeConhecimentos.removeAll();
				try {
			    	TreeItem[] itemCadatrado;
					itemCadatrado = getViewComunication().getArvoreGraficaDeConhecimentos(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL).getItems();
					TreeItem[] item = new TreeItem[itemCadatrado.length];
					for (int i = 0; i < item.length; i++) {
						item[i] = new TreeItem(treeConhecimentos, SWT.NONE);
						item[i].setText(itemCadatrado[i].getText());
						item[i].setData(itemCadatrado[i].getData());
					}
				} catch (ConhecimentoInexistenteException e1) {
					e1.printStackTrace();
				}

			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		executarExperimento = new Button(parentComposite, SWT.NONE);
		Image trocaMsg = new Image(executarExperimento.getDisplay(),this.getClass().getResourceAsStream("/src/main/resources/icons/trocaMsg.gif"));
		executarExperimento.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		executarExperimento.setSize(larguraBotao, alturaBotao);
		executarExperimento.setImage(trocaMsg);
		executarExperimento.setToolTipText("Executar Experimento");
		executarExperimento.setVisible(true);
		executarExperimento.setEnabled(true);
		executarExperimento.addMouseListener(new MouseListener(){

			private Logger logger = Logger.getLogger(this.getClass());

			public void mouseDoubleClick(MouseEvent e) {
				
				this.logger.debug("clique duplo");

				projetoAtivo = viewComunication.getProjetoAtivo();
				
				// Objeto para o JayFX
				try {
					if (aDB == null)
						aDB = new PresleyJayFX( projetoAtivo );
				} catch (JayFXException e1) {
					System.exit(1);
				}
				
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog.setFilterPath( "C://" );
				String diretorio = dialog.open();
				
				try {
					executarExperimento(diretorio, projetoAtivo);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
/*
				try {
					ajustarArquivosQuestion(diretorio);
					ajustarArquivosEmails();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
*/								
			}

			public void mouseDown(MouseEvent e) {
//				runEnviaRespostaWizardAction(getViewMensagens());
				
			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		documentosBase = new Label(parentComposite, SWT.BORDER | SWT.CENTER);
		documentosBase.setText("Documentos Base");
		documentosBase.setLocation(0, posVerPainelTopicosDominio + alturaPainelTopicosDominio + distanciaPanelLabel);
		documentosBase.setSize(200, 20);	
		documentosBase.setVisible(true);
		documentosBase.setBackground(new Color(documentosBase.getDisplay(),102,204,255));

		
		listaDocumentosBase = new List(parentComposite, SWT.V_SCROLL | SWT.BORDER);
		listaDocumentosBase.setLocation(0, posVerPainelDocumentoBase); 
		listaDocumentosBase.setSize(larguraJanela, alturaPainelDocumentoBase);
		listaDocumentosBase.setVisible(true);


		incluirDocumentoBase = new Button(parentComposite, SWT.NONE);
		Image add = new Image(incluirDocumentoBase.getDisplay(),this.getClass().getResourceAsStream("/src/main/resources/icons/add.gif"));
		incluirDocumentoBase.setLocation(posHorBotaoNivel1, posVerBotaoNivel2);
		incluirDocumentoBase.setSize(larguraBotao, alturaBotao);
		incluirDocumentoBase.setImage(add);
		incluirDocumentoBase.setToolTipText("Inserir Documento Base");
		incluirDocumentoBase.setEnabled(true);
		incluirDocumentoBase.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0) {
				
				if ((treeConhecimentos.getSelection()[0].getData() instanceof TopicoConhecimento) &&
					(treeConhecimentos.indexOf(treeConhecimentos.getSelection()[0]) != 0) ){

					Projeto projeto = viewComunication.getProjetoAtivo();
					
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					/*
					FileDialog dialog = new FileDialog(shell, SWT.OPEN);
					dialog.setFilterPath( "C://" );
					final String file = dialog.open();
					 */
					
					DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
					dialog.setFilterPath( "C://" );
					String diretorio = dialog.open();
					File diretorioCD = new File( diretorio );
					File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
						public boolean accept(File d, String name) {  
							return name.toLowerCase().endsWith(".txt");  
						}  
					}); 		
					
					
					for (int i = 0; i < listagemDiretorio.length; i++) {
						File arquivoSelecionado = new File( listagemDiretorio[i].getAbsolutePath() );
						File arquivoNovo = new File(projeto.getEndereco_Servidor_Gravacao()+ arquivoSelecionado.getName());
						
						try {
							arquivoNovo.createNewFile();
							copyFile(arquivoSelecionado, arquivoNovo);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						Arquivo arquivo = new Arquivo( arquivoSelecionado.getName() );
						arquivo.setEnderecoServidor( projeto.getEndereco_Servidor_Gravacao()+ arquivoSelecionado.getName() ) ;
						
						try {
							TopicoConhecimento conhecimento = (TopicoConhecimento) treeConhecimentos.getSelection()[0].getData();
							viewComunication.associaArquivo(conhecimento, arquivo);
							preencherListaDocumentosBase();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}


			}
			
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

		});

	}
	
	public ViewComunication getViewComunication(){
		return this.viewComunication;
	}

	private void preencherListaDocumentosBase() {
		listaDocumentosBase.removeAll();
		if (treeConhecimentos.getSelection()[0].getData() instanceof TopicoConhecimento){
			TopicoConhecimento conhecimentoSelecionado = (TopicoConhecimento) treeConhecimentos.getSelection()[0].getData();
			ArrayList<Arquivo> arquivosDoConhecimento = conhecimentoSelecionado.getArquivos();
			for (Iterator<Arquivo> iterator = arquivosDoConhecimento.iterator(); iterator.hasNext();) {
				Arquivo arquivo = iterator.next();
				listaDocumentosBase.add( arquivo.getNome() ) ;
			}
		}
	}
	
	private void runAdicionaConhecimentoWizardAction() {
		com.hukarz.presley.client.gui.action.RunAdicionaConhecimentoWizard runLogin = new com.hukarz.presley.client.gui.action.RunAdicionaConhecimentoWizard(this);
		runLogin.run(null);
	}

	// Copies src file to dst file.
	// If the dst file does not exist, it is created
	private void copyFile(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}	
	
	
	private void executarExperimento(String diretorioArquivos, Projeto projetoAtivo) throws FileNotFoundException{
		
		File diretorioCD = new File( diretorioArquivos );
//		DateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");  
		PrintWriter saidaTempoProcessamento = new PrintWriter(new 
				FileOutputStream("C:/TempoProcessamento.txt"));
		
		DateFormat formataHora = new SimpleDateFormat("HH:mm:ss.SSS"); 
		
		File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".question");  
			}  
		}); 		
		
		File file = null;
		try {
			for (int i = 0; i < listagemDiretorio.length; i++) {
				file = new File( listagemDiretorio[i].getAbsolutePath() );
				
				System.out.println("Processando arquivo " + file.getName());
				saidaTempoProcessamento.println( "Processando arquivo " + file.getName() );

				saidaTempoProcessamento.println( "Inicio >>>> " + formataHora.format(System.currentTimeMillis()) );
				
				FileReader fileReader;
				
				fileReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fileReader);

				int posicaoDoPonto = file.getName().indexOf('.');       		        		
        		String numeroArquivoExperimento = file.getName().substring(0, posicaoDoPonto);				
				
        		Problema problema = new Problema();
        		problema.setNumeroArquivoExperimento(numeroArquivoExperimento);
        		problema.setResolvido(false);
        		problema.setProjeto( projetoAtivo );
        		
        		// Desenvolvedor que enviou o problema
				String linha = reader.readLine();
			
				if (linha.contains("jira@apache.org")) {
					String nome = extractNomeJira( linha );
					
					problema.setDesenvolvedorOrigem( viewComunication.getDesenvolvedorPorNome(nome) ) ;
				}
				else {
					String email = extractEmail(linha);
					problema.setDesenvolvedorOrigem( viewComunication.login(email, "1") ) ;
				}	
				
        		// Data do envio
        		linha = reader.readLine();
        		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");  
        		problema.setData( new java.sql.Date( sdf.parse(linha).getTime() ));
        		
        		// Descrição do problema
        		linha = reader.readLine();
        		problema.setDescricao( linha );
        		
        		// Corpo da Mensagem
        		StringBuilder corpoDaMensagem = new StringBuilder();
				while( (linha = reader.readLine()) != null ){
	        		corpoDaMensagem.append(linha + " ");
				}
				problema.setMensagem(corpoDaMensagem.toString());

				//System.out.println("Obtendo classes relacionadas...");
        		problema.setClassesRelacionadas( 
        			 aDB.getClassesRelacionadas(problema.getDescricao() + " " + problema.getMensagem(), " ") ) ;
        		
        		File arq = new File(projetoAtivo.getEndereco_Servidor_Gravacao() + numeroArquivoExperimento + ".emails");  
        		problema.setTemResposta( arq.exists() );
        		
        		//Adciona problema ao banco
        		//System.out.println("Classes relacionadas obtidas");
        		viewComunication.adicionaProblema(problema);
				saidaTempoProcessamento.println( "Fim    >>>> " + formataHora.format(System.currentTimeMillis()) );
        		
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Não foi possível ler do arquivo " + file.getName());
			e.printStackTrace();
		} catch (DesenvolvedorInexistenteException e) {
			e.printStackTrace();
		} catch (ConversionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		saidaTempoProcessamento.close();

	}

	private String extractEmail(String fromHeader) {
		
		if (fromHeader.contains("<")) {
			int beginIndex = fromHeader.indexOf('<') + 1;
			int endIndex = fromHeader.indexOf('>');
			return fromHeader.substring(beginIndex, endIndex);
		}
		else {
			return fromHeader;
		}
	}
	
	private String extractNomeJira(String fromHeader){
		int endIndex = fromHeader.indexOf("jira@apache.org");
		String nome = fromHeader.substring(0, endIndex);
		nome = nome.replace("<", "").replace(">", "");
		nome = nome.replace("\"", "");
		nome = nome.replace("(", "").replace(")", "");
		nome = nome.replace("JIRA", "").trim();
		
		return nome;
	}
	
}
