package com.hukarz.presley.client.gui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hukarz.presley.client.gui.view.MensagemAba;

public class AdicionaProblemaWizardPage extends WizardPage {

	private Text mensagemText, descricaoText;
	private int minimumWidth = 200;
	private int minimumHeight = 100;
	private MensagemAba mensagemAba;
	private String diretorio;
	
	public AdicionaProblemaWizardPage(MensagemAba mensagem) {
		super("wizardPage");
        setTitle("Adiciona Problema Wizard");
        setDescription("Adiciona um novo Problema");
        this.mensagemAba = mensagem;
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

 		
 	    setControl(controls);

	}
	
    private void dialogChanged( ) {
		// TODO Auto-generated method stub
    }

}
