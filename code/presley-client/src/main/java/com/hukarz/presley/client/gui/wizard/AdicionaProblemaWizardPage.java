package com.hukarz.presley.client.gui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.hukarz.presley.client.gui.view.MensagemAba;

public class AdicionaProblemaWizardPage extends WizardPage {

	private Combo elementosProjeto;
	private Text mensagemText, descricaoText;
	private int minimumWidth = 200;
	private int minimumHeight = 100;
	private Map<String, String> listaElementosProjeto = new HashMap<String, String>();
	private ArrayList<String> listaElementosSelecionados;
	private MensagemAba mensagemAba;
	private Button incluirExperimentoWDDS;
	private Button diretorioExperimentoWDDS;
	private String diretorio;
	
	public AdicionaProblemaWizardPage(MensagemAba mensagem) {
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
    
    public boolean executarExperimentoWDDS(){
    	return incluirExperimentoWDDS.getSelection();
    }

    public String getDiretorioArquivos(){
    	return diretorio;
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

 		listaElementosProjeto = mensagemAba.getDadosProjetoAtivo().getListaElementosProjeto();

 		//Preenche o combo
 		Object[] elementos = listaElementosProjeto.keySet().toArray() ;
 		Arrays.sort( elementos );
 		for (int i = 0; i < elementos.length; i++) {
 			elementosProjeto.add( listaElementosProjeto.get( (String) elementos[i] ) );
 		}
        
 		
 		incluirExperimentoWDDS = new Button(controls, SWT.CHECK);
 		incluirExperimentoWDDS.setText("Executar Experimento WDDS ?");
 	    GridData gdExecutarExperimento = new GridData(GridData.CENTER);
 	    incluirExperimentoWDDS.setLayoutData(gdExecutarExperimento);
 	    
        diretorioExperimentoWDDS = new Button(controls, SWT.TOGGLE );
        diretorioExperimentoWDDS.setText("");
 	    GridData gdDiretorioEscolhido = new GridData(GridData.FILL_HORIZONTAL);
 	    diretorioExperimentoWDDS.setLayoutData(gdDiretorioEscolhido);
 	    diretorioExperimentoWDDS.setEnabled(false);
 	    
 	    incluirExperimentoWDDS.addSelectionListener(
 	    		new SelectionListener(){

 	    			@Override
 	    			public void widgetDefaultSelected(SelectionEvent e) {
 	    				
 	    			}

 	    			@Override
 	    			public void widgetSelected(SelectionEvent e) {
 	    				if (incluirExperimentoWDDS.getSelection()){
 	 						Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
 	 						
 	 						DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
 	 						dialog.setFilterPath( "C://" );
 	 						diretorio = dialog.open();

 	 						diretorioExperimentoWDDS.setText("Diretório: " + diretorio);
 	    				} else 
 	    					diretorioExperimentoWDDS.setText(" ");
 	    			}

 	    		}
 	    );
 	    
 	    setControl(controls);

	}
	
    private void dialogChanged( ) {
		// TODO Auto-generated method stub
    }

}
