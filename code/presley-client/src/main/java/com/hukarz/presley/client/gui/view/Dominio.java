package com.hukarz.presley.client.gui.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

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
import ca.mcgill.cs.swevo.jayfx.model.FlyweightElementFactory;
import ca.mcgill.cs.swevo.jayfx.model.ICategories;
import ca.mcgill.cs.swevo.jayfx.model.IElement;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.client.gui.view.comunication.ViewComunication;


public class Dominio extends ViewPart {

	private Composite parentComposite;
	private ViewComunication viewComunication;
	private String ipServidor = "127.0.0.1";
	
	private Button incluirTopico, alterarTopico, incluirDocumentoBase;
	
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
	private Map<String, String> listaElementosProjeto;
	PresleyJayFX aDB;
	
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
		
    	com.hukarz.presley.beans.Tree conhecimentosModelo = getViewComunication().getOntologia();
    	treeConhecimentos = conhecimentosModelo.constroiArvoreGrafica(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
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
				
		    	com.hukarz.presley.beans.Tree conhecimentosModelo2 = getViewComunication().getOntologia();
		    	treeConhecimentos.removeAll();
		    	TreeItem[] itemCadatrado = conhecimentosModelo2.constroiArvoreGrafica(parentComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL).getItems();
				TreeItem[] item = new TreeItem[itemCadatrado.length];
				for (int i = 0; i < item.length; i++) {
					item[i] = new TreeItem(treeConhecimentos, SWT.NONE);
					item[i].setText(itemCadatrado[i].getText());
					item[i].setData(itemCadatrado[i].getData());
				}

			}

			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		alterarTopico = new Button(parentComposite, SWT.NONE);
		Image trocaMsg = new Image(alterarTopico.getDisplay(),this.getClass().getResourceAsStream("/src/main/resources/icons/trocaMsg.gif"));
		alterarTopico.setLocation(posHorBotaoNivel2, posVerBotaoNivel1);
		alterarTopico.setSize(larguraBotao, alturaBotao);
		alterarTopico.setImage(trocaMsg);
		alterarTopico.setToolTipText("Executar Experimento");
		alterarTopico.setVisible(true);
		alterarTopico.setEnabled(true);
		alterarTopico.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent e) {
				Projeto projetoAtivo;

				projetoAtivo = viewComunication.getProjetosAtivo().get(0); 
				// Objeto para o JayFX
				aDB = PresleyJayFX.obterInstancia( projetoAtivo );
				// Busca Todos os Elementos no projeto
				listaElementosProjeto = aDB.getTodasClassesMetodos();

				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
				dialog.setFilterPath( "C://" );
				String diretorio = dialog.open();
		 		
				executarExperimento(diretorio, projetoAtivo);		 		
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
				
				if ((treeConhecimentos.getSelection()[0].getData() instanceof Conhecimento) &&
					(treeConhecimentos.indexOf(treeConhecimentos.getSelection()[0]) != 0) ){

					Projeto projeto = viewComunication.getProjetosAtivo().get(0);
					
					Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					FileDialog dialog = new FileDialog(shell, SWT.OPEN);
					dialog.setFilterPath( "C://" );
					final String file = dialog.open();

					File arquivoSelecionado = new File(file);
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
						Conhecimento conhecimento = (Conhecimento) treeConhecimentos.getSelection()[0].getData();
						viewComunication.associaArquivo(conhecimento, arquivo);
						preencherListaDocumentosBase();
					} catch (Exception e) {
						e.printStackTrace();
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
		if (treeConhecimentos.getSelection()[0].getData() instanceof Conhecimento){
			Conhecimento conhecimentoSelecionado = (Conhecimento) treeConhecimentos.getSelection()[0].getData();
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
	
	
	private void executarExperimento(String diretorioArquivos, Projeto projetoAtivo){
		File diretorioCD = new File( diretorioArquivos );
		
		File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".pergunta");  
			}  
		}); 		
		
		try {
			for (int i = 0; i < listagemDiretorio.length; i++) {
				File file = new File( listagemDiretorio[i].getAbsolutePath() );
				FileReader fileReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fileReader);

        		Problema problema = new Problema();
        		problema.setResolvido(false);
        		problema.setData(new Date(System.currentTimeMillis()));
        		problema.setProjeto( projetoAtivo );
        		
        		// Desenvolvedor que enviou o problema
				String linha = reader.readLine();
        		problema.setDesenvolvedorOrigem( viewComunication.login(linha, "1") ) ;

        		// Descrição do problema
        		linha = reader.readLine();
        		problema.setDescricao( linha );
        		
        		// Corpo da Mensagem
        		String CorpoMensagem = "";
				while( (CorpoMensagem = reader.readLine()) != null ){
	        		problema.setMensagem( problema.getMensagem() + " " + CorpoMensagem );
				}

        		problema.setClassesRelacionadas( 
        				getClassesRelacionadas(problema.getDescricao() + " " + problema.getMensagem(), " ") ) ;
        		
        		//Adciona problema ao banco
        		viewComunication.adicionaProblema(problema);
			}
		} catch (Exception e) {

		}

	}

	
    /**
     * Metodo que retorna todas as classes e nomes de arquivos relacionados a mensagem 
     * esrita pelo desenvolvedor
     * @return <classe ou metodo,arquivo>
     * @throws ConversionException 
     * @throws ConversionException 
     */
    public Map<ClasseJava, ArquivoJava> getClassesRelacionadas( String texto, String separadorPalavras ) throws ConversionException {
    	Map<ClasseJava, ArquivoJava> retorno = new HashMap<ClasseJava, ArquivoJava>();
    	
    	StringTokenizer st = new StringTokenizer(texto, separadorPalavras);
    	
		while (st.hasMoreTokens()){   
			String palavra = st.nextToken();
			
			if (palavra.contains("."))
				getClassesRelacionadas(palavra,".");
			else if (listaElementosProjeto.values().contains(palavra)){
				for (String nomeClasse : listaElementosProjeto.keySet()) {
					if (listaElementosProjeto.get(nomeClasse).equals(palavra)){
						IElement elemento ;
						elemento = FlyweightElementFactory.getElement( ICategories.CLASS, nomeClasse );
						retorno.putAll( aDB.getElementoRelacionamento(elemento) );
								
						ClasseJava classe;   
						classe = new ClasseJava( elemento.getId() ); 
					
			    		ArquivoJava arquivo = new ArquivoJava(aDB.convertToJavaElement(elemento).getResource().getName(), aDB.getProjetoSelecionado());
			  	    		
			    		arquivo.setEnderecoServidor( aDB.convertToJavaElement(elemento).getPath().toString() ) ;
			    		retorno.put(classe, arquivo);
			    		break;
					}						
				}
			}
		}
    	
    	return retorno;    	
    }
	
}
