package gui.wizard;

import gui.view.Atividade;

import java.security.acl.Group;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import beans.Desenvolvedor;
import beans.TipoAtividade;

public class EnviaRespostaWizardPage extends WizardPage {

	private Text descricaoProblemaText;

    public EnviaRespostaWizardPage(ISelection selection) {
        super("wizardPage");
        setTitle("Envia Resposta Wizard");
        setDescription("Envia uma Resposta para a mensagem do usuario.");
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getDescricao() {
        return descricaoProblemaText.getText();
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
        label.setText("Mensagem de resposta:");

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
        
        /*
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
    

  		*/      
        
        setControl(controls);
    }


}
