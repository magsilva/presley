package com.hukarz.presley.client.gui.wizard;


import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class AdicionaDesenvolvedorWizardPage extends WizardPage {

	private MensagemAba mensagemAba;
	private Text nomeDesenvolvedorText;
	private Text emailText;
	private Text senhaText;
	private Text cvsIdentificacaoText;
	private Combo listaDesenvolvedores;
	
	
	private ArrayList<Desenvolvedor> desenvolvedores;

    public AdicionaDesenvolvedorWizardPage(ISelection selection, MensagemAba m) {
        super("wizardPage");
        setTitle("Adiciona Desenvolvedor Wizard");
        setDescription("Adiciona um novo Desenvolvedor.");
        this.mensagemAba = m;
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getNomeDesenvolvedor() {
        return nomeDesenvolvedorText.getText();
    }
    
    public String getEmailDesenvolvedor() {
        return emailText.getText();
    }
    
    public String getSenhaDesenvolvedor() {
        return senhaText.getText();
    }
    
    public String getIdentificacaoCVS() {
        return cvsIdentificacaoText.getText();
    }
    
    public Desenvolvedor getDesenvolvedor(){
    	Desenvolvedor desenvolvedorRetorno = null; 
    	String selecao = listaDesenvolvedores.getText();
    	for (Desenvolvedor desenvolvedor : desenvolvedores) {
			if (desenvolvedor.getNome().equals(selecao)) {
				desenvolvedorRetorno = desenvolvedor;
				break;
			}
		}

    	return desenvolvedorRetorno;

    }
    
       
   private void dialogChanged() {
		// TODO Auto-generated method stub
    }

    
    public void createControl(Composite parent) {
    	 Composite controls	= new Composite(parent, SWT.NULL);
         GridLayout layout	= new GridLayout();
         controls.setLayout(layout);
         layout.numColumns	= 3;
         layout.verticalSpacing = 5;

        Label label = new Label(controls, SWT.NULL);
        label.setText("Nome:");

        nomeDesenvolvedorText = new Text( controls, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        nomeDesenvolvedorText.setLayoutData(gd);
        nomeDesenvolvedorText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelExplicativo = new Label(controls, SWT.NULL);
        labelExplicativo.setText("digite o nome do Desenvolvedor");

        Label label2 = new Label(controls, SWT.NULL);
        label2.setText("Email:");

        emailText = new Text( controls, SWT.BORDER | SWT.SINGLE);
        GridData gd2 = new GridData(GridData.FILL_HORIZONTAL);
        emailText.setLayoutData(gd2);
        emailText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelExplicativo2 = new Label(controls, SWT.NULL);
        labelExplicativo2.setText("digite o email do Desenvolvedor");
        
        
        Label label4 = new Label(controls, SWT.NULL);
        label4.setText("Identificação no CVS:");

        cvsIdentificacaoText = new Text(controls, SWT.BORDER | SWT.SINGLE);
        GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
        cvsIdentificacaoText.setLayoutData(gd3);
        cvsIdentificacaoText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelExplicativo3 = new Label(controls, SWT.NULL);
        labelExplicativo3.setText("digite a Identificação no CVS");
        
        Label label3 = new Label(controls, SWT.NULL);
        label3.setText("Senha:");

        senhaText		= new Text(controls, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
        GridData data	= new GridData(GridData.FILL_HORIZONTAL);
 	    senhaText.setLayoutData(data);

        Label labelExplicativo4 = new Label(controls, SWT.NULL);
        labelExplicativo4.setText("digite a senha do Desenvolvedor");
        
        setControl(controls);
    }


}
