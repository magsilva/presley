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

public class AssociaProblemaAtividadeWizardPage extends WizardPage {

	private Atividade atividade;
	private Text descricaoProblemaText;
	private Text mensagemText;
	private TipoAtividade tipoAtividadeAssociado;

    public AssociaProblemaAtividadeWizardPage(ISelection selection, Atividade atividade, TipoAtividade tipoAtividade) {
        super("wizardPage");
        setTitle("Associa Problema a Atividade Wizard");
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
        layout.verticalSpacing = 6;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Descrição do Problema:");

        descricaoProblemaText = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(
            GridData.FILL_HORIZONTAL);
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
                SWT.BORDER | SWT.SINGLE);
            GridData gdMens = new GridData(
                GridData.FILL_HORIZONTAL);
            mensagemText.setLayoutData(gdMens);
            mensagemText.addModifyListener(
                new ModifyListener() {
                    public void modifyText(
                            ModifyEvent e) {
                        dialogChanged();
                    }
                 });
        
        Label labelMensagemExplicativo = new Label(controls, SWT.NULL);
        labelMensagemExplicativo.setText("digite a mensagem que será enviada");
        
        Label labelAtividadeAssociada = new Label(controls, SWT.NULL);
        labelAtividadeAssociada.setText("Atividade associada: "+this.tipoAtividadeAssociado.getDescricao());

        
        
        setControl(controls);
    }


}
