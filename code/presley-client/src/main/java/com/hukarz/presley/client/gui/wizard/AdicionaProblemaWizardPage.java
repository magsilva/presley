package com.hukarz.presley.client.gui.wizard;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ca.mcgill.cs.swevo.jayfx.ConversionException;
import ca.mcgill.cs.swevo.jayfx.JayFX;
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
	private Map<String, String> listaElementosProjeto;
	private ArrayList<String> listaElementosSelecionados;
	private JayFX aDB;
    private MensagemAba mensagem;
	
	protected AdicionaProblemaWizardPage(MensagemAba mensagem) {
		super("wizardPage");
        setTitle("Adiciona Problema Wizard");
        setDescription("Adiciona um novo Problema");

        listaElementosSelecionados = new ArrayList<String>();
        
        // TODO: Extend JayFX correctly
        aDB = JayFX.obterInstancia( mensagem.getProjeto() );
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
    
    /**
     * Metodo que retorna todas as classes e nomes de arquivos relacionados a mensagem 
     * esrita pelo desenvolvedor
     * @return <classe ou metodo,arquivo>
     * @throws ConversionException 
     * @throws ConversionException 
     */
    public Map<ClasseJava, ArquivoJava> getClassesRelacionadas() throws ConversionException {
    	String mensagem = mensagemText.getText();
    	Map<ClasseJava, ArquivoJava> retorno = new HashMap<ClasseJava, ArquivoJava>();

    	for (Iterator<String> iterator = listaElementosSelecionados.iterator(); iterator.hasNext();) {
    		String elementoSelecionado = iterator.next();

    		if ( mensagem.indexOf(listaElementosProjeto.get(elementoSelecionado).toString()) != -1 ){
    			IElement elemento ; 
    			if ( elementoSelecionado.endsWith(")") ){
    				elemento = FlyweightElementFactory.getElement( ICategories.METHOD, elementoSelecionado );
    			} else {
    				elemento = FlyweightElementFactory.getElement( ICategories.CLASS, elementoSelecionado );
    			}
    			// TODO: Extend JayFX correctly
				retorno.putAll( aDB.getElementoRelacionamento(elemento) );
				
	    		ClasseJava classe   = new ClasseJava( elemento.getDeclaringClass().getId() ) ;
	    		ArquivoJava arquivo = new ArquivoJava( aDB.convertToJavaElement(elemento).getResource().getName() );
	    		
	    		retorno.put(classe, arquivo);
    		}
    	}

    	return retorno;    	
    }
    
    
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
        Composite controls = new Composite(parent, SWT.NULL);
        GridLayout layout  = new GridLayout();
        controls.setLayout(layout);
        
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

        // Busca Todos os Elementos no projeto
     // TODO: Extend JayFX correctly
        listaElementosProjeto = aDB.getTodasClassesMetodos();
        elementosProjeto = new Combo(controls, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        
        //Preenche o combo  
        Object[] elementos = listaElementosProjeto.keySet().toArray() ;
        Arrays.sort( elementos );
        for (int i = 0; i < elementos.length; i++) {
            elementosProjeto.add( listaElementosProjeto.get( (String) elementos[i] ) );
		}
        
        elementosProjeto.addSelectionListener(
        		new SelectionListener(){
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub
						
					}

					public void widgetSelected(SelectionEvent e) {
						if ( elementosProjeto.getText().indexOf("(") != -1 )
							mensagemText.insert( "Método " + elementosProjeto.getText() + " " ) ;
						else
							mensagemText.insert( "Classe " + elementosProjeto.getText() + " " ) ;
						
				        Object[] elementos = listaElementosProjeto.keySet().toArray() ;
				        Arrays.sort( elementos );
						listaElementosSelecionados.add( (String) elementos[ elementosProjeto.getSelectionIndex() ] ) ;
						mensagemText.setFocus();
					}
        		}
        );
        
        setControl(controls);
	}
	
    private void dialogChanged( ) {
		// TODO Auto-generated method stub
    }

}
