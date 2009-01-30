package com.hukarz.presley.client.gui.wizard;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hukarz.presley.client.gui.view.Atividade;


public class AssociaProblemaAtividadeWizardPage extends WizardPage {

	private Atividade atividade;
	private Text descricaoProblemaText;
	private Text mensagemText;
	private String tipoAtividadeAssociado;

    public AssociaProblemaAtividadeWizardPage(ISelection selection, Atividade atividade, String tipoAtividade) {
        super("wizardPage");
        setTitle("Associa Problema à Atividade Wizard");
        setDescription("Associa problema a uma Atividade.");
        this.atividade = atividade;
        this.tipoAtividadeAssociado = tipoAtividade;
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getDescricao() {
        return descricaoProblemaText.getText();
    }
    
    public String getMensagem() {
        return mensagemText.getText();
    }
    
    private void dialogChanged() {
		// TODO Auto-generated method stub
    }

    public void createControl(Composite parent) {
        Composite controls =
            new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        controls.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 2;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Descrição do Problema:");

        descricaoProblemaText = new Text(
            controls,
            SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gd = new GridData(
            GridData.FILL_BOTH);
        descricaoProblemaText.setLayoutData(gd);
        descricaoProblemaText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });
        

        Label labelExplicativo = new Label(controls, SWT.NULL);
        labelExplicativo.setText("digite a descrição do problema");

        Label labelMensagem =
            new Label(controls, SWT.NULL);
        labelMensagem.setText("Mensagem:");
        
        mensagemText = new Text(
                controls,
                SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
            GridData gdMens = new GridData(
                GridData.FILL_BOTH);
            mensagemText.setLayoutData(gdMens);
            mensagemText.addModifyListener(
                new ModifyListener() {
                    public void modifyText(
                            ModifyEvent e) {
                        dialogChanged();
                    }
                 });
        
        Label labelMensagemExplicativo = new Label(controls, SWT.NULL);
        labelMensagemExplicativo.setText(" mensagem que será enviada");
        
        Label labelAtividadeAssociada1 = new Label(controls, SWT.NULL);
        labelAtividadeAssociada1.setText("Atividade associada: ");
        
        Text labelAtividadeAssociada2 = new Text(controls, SWT.NULL);
        labelAtividadeAssociada2.setText(this.tipoAtividadeAssociado);
        labelAtividadeAssociada2.setEditable(false);
    

        
        
        setControl(controls);
    }


}
