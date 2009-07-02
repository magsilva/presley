package com.hukarz.presley.client.gui.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.PlatformUI;

import ca.mcgill.cs.swevo.PresleyJayFX;
import ca.mcgill.cs.swevo.jayfx.ConversionException;
import ca.mcgill.cs.swevo.jayfx.model.ClassElement;
import ca.mcgill.cs.swevo.jayfx.model.FlyweightElementFactory;
import ca.mcgill.cs.swevo.jayfx.model.ICategories;
import ca.mcgill.cs.swevo.jayfx.model.IElement;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.client.gui.view.MensagemAba;

public class AdicionaProblemaWizardPage extends WizardPage {

	private Combo elementosProjeto;
	private Text mensagemText, descricaoText;
	private int minimumWidth = 200;
	private int minimumHeight = 100;
	private Map<String, String> listaElementosProjeto = new HashMap<String, String>();
	private ArrayList<String> listaElementosSelecionados;
	private PresleyJayFX aDB;
	private MensagemAba mensagemAba;
	private Button incluirArquivoExperimento;
	private Label diretorioEscolhido;
	private String diretorio;
	
	protected AdicionaProblemaWizardPage(MensagemAba mensagem) {
		super("wizardPage");
        setTitle("Adiciona Problema Wizard");
        setDescription("Adiciona um novo Problema");
        this.mensagemAba = mensagem;
        
        listaElementosSelecionados = new ArrayList<String>();
	}
	
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getMensagem() {
        return mensagemText.getText();
    }

    public String getDescricao() {
        return descricaoText.getText();
    }
    
    public boolean executarExperimento(){
    	return incluirArquivoExperimento.getSelection();
    }
    
    public String getDiretorioArquivos(){
    	return diretorio;
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
			
			if (palavra.contains(".") && !palavra.equals("."))
				palavra = palavra.substring(0, palavra.indexOf(".") );
			
			if (listaElementosProjeto.values().contains(palavra)){
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
    
    
	public void createControl(Composite parent) {
        Composite controls = new Composite(parent, SWT.NULL);
        GridLayout layout  = new GridLayout();
        controls.setLayout(layout); //layout
        
        layout.numColumns  = 2;
        layout.verticalSpacing = 3;

 	    Label labelDescricao = new Label(controls, SWT.NULL);
 	    labelDescricao.setText("Descrição:");
 	    descricaoText = new Text(controls, SWT.MULTI | SWT.BORDER);
 	    GridData gdDescricao = new GridData(GridData.FILL_HORIZONTAL);
 	    descricaoText.setLayoutData(gdDescricao);
        descricaoText.addModifyListener(
        		new ModifyListener() {
        			public void modifyText(
        					ModifyEvent e) {
        				dialogChanged();
        			}
        		});
        
        Label labelMensagem = new Label(controls, SWT.NULL);
        labelMensagem.setText("Mensagem:");
        
        mensagemText = new Text(controls, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gdMens = new GridData(GridData.FILL_BOTH);
        gdMens.minimumWidth = minimumWidth;
        gdMens.minimumHeight = minimumHeight;
        mensagemText.setLayoutData(gdMens);
        mensagemText.addModifyListener(
        		new ModifyListener() {
        			public void modifyText(
        					ModifyEvent e) {
        				dialogChanged();
        			}
        		});
        
        
        Label label = new Label(controls, SWT.NULL);
        label.setText("Elementos no Projeto");

        elementosProjeto = new Combo(controls, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
 	    GridData gdElementos = new GridData(GridData.FILL_HORIZONTAL);
        elementosProjeto.setLayoutData(gdElementos);
        
        elementosProjeto.addSelectionListener(
        		new SelectionListener(){
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}

					public void widgetSelected(SelectionEvent e) {
						if ( elementosProjeto.getText().indexOf("(") != -1 )
							mensagemText.insert( " " + elementosProjeto.getText() + " " ) ;
						else
							mensagemText.insert( " " + elementosProjeto.getText() + " " ) ;
						
				        Object[] elementos = listaElementosProjeto.keySet().toArray() ;
				        Arrays.sort( elementos );
						listaElementosSelecionados.add( (String) elementos[ elementosProjeto.getSelectionIndex() ] ) ;
						mensagemText.setFocus();
					}
        		}
        );

 		aDB = mensagemAba.getDadosProjetoAtivo();
 		listaElementosProjeto = mensagemAba.getElementosProjeto();

 		//Preenche o combo
 		Object[] elementos = listaElementosProjeto.keySet().toArray() ;
 		Arrays.sort( elementos );
 		for (int i = 0; i < elementos.length; i++) {
 			elementosProjeto.add( listaElementosProjeto.get( (String) elementos[i] ) );
 		}
        
 		
 		incluirArquivoExperimento = new Button(controls, SWT.CHECK);
 		incluirArquivoExperimento.setText("Executar Experimento ?");
 	    GridData gdExecutarExperimento = new GridData(GridData.CENTER);
 	    incluirArquivoExperimento.setLayoutData(gdExecutarExperimento);
 	    
        diretorioEscolhido = new Label(controls, SWT.NULL);
        diretorioEscolhido.setText("");
 	    
 	    incluirArquivoExperimento.addSelectionListener(
 	    		new SelectionListener(){

 	    			@Override
 	    			public void widgetDefaultSelected(SelectionEvent e) {
 	    				
 	    			}

 	    			@Override
 	    			public void widgetSelected(SelectionEvent e) {
 	    				if (incluirArquivoExperimento.getSelection()){
 	 						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
 	 						
 	 						DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
 	 						dialog.setFilterPath( "C://" );
 	 						diretorio = dialog.open();

 	 						diretorioEscolhido.setText("Executar no Diretório " + diretorio);
 	    				} else 
 	    					diretorioEscolhido.setText(" ");
 	    			}

 	    		}
 	    );

 	    setControl(controls);

	}
	
    private void dialogChanged( ) {
		// TODO Auto-generated method stub
    }

}
