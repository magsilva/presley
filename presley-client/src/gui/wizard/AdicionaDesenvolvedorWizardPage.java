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

public class AdicionaDesenvolvedorWizardPage extends WizardPage {

	private Atividade atividade;
	private Text nomeDesenvolvedorText;
	private Text emailText;
	private Text senhaText;
	private Text localidadeText;
	private Text conhecimentoText;
	private Combo listaDesenvolvedores;
	private Combo listaSupervisores;
	private Button naoConcluidoradioButton;
	private Button concluidoradioButton;
	private List grupoButoesStatus;
	
	
	private ArrayList<Desenvolvedor> desenvolvedores;

    public AdicionaDesenvolvedorWizardPage(ISelection selection, Atividade atividade) {
        super("wizardPage");
        setTitle("Adiciona Desenvolvedor Wizard");
        setDescription("Adiciona um novo Desenvolvedor.");
        this.atividade = atividade;
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
    
    public String getConhecimentoDesenvolvedor() {
        return conhecimentoText.getText();
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
    
    public Desenvolvedor getNomeSupervisor(){
    	Desenvolvedor supervisorRetorno = null; 
    	String selecao = listaDesenvolvedores.getText();
    	for (Desenvolvedor desenvolvedor : desenvolvedores) {
			if (desenvolvedor.getNome().equals(selecao)) {
				supervisorRetorno = desenvolvedor;
				break;
			}
		}

    	return supervisorRetorno;
    }
    
        
    public boolean getStatus(){
    	String selecao = this.grupoButoesStatus.getSelection()[0];
    	if (selecao.equals("Concluído"))
			return true;
		else
			return false;
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
        layout.verticalSpacing = 5;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Nome:");

        nomeDesenvolvedorText = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(
            GridData.FILL_HORIZONTAL);
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

        Label label2 =
            new Label(controls, SWT.NULL);
        label2.setText("Email:");

        emailText = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd2 = new GridData(
            GridData.FILL_HORIZONTAL);
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
        
        
        Label label4 =
            new Label(controls, SWT.NULL);
        label4.setText("Localidade:");

        localidadeText = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd3 = new GridData(
            GridData.FILL_HORIZONTAL);
        localidadeText.setLayoutData(gd3);
        localidadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelExplicativo3 = new Label(controls, SWT.NULL);
        labelExplicativo3.setText("digite a localidade do Desenvolvedor");
        
        Label label3 =
            new Label(controls, SWT.NULL);
        label3.setText("Senha:");

        senhaText = new Text(controls, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
 	    senhaText.setLayoutData(data);

        Label labelExplicativo4 = new Label(controls, SWT.NULL);
        labelExplicativo4.setText("digite a senha do Desenvolvedor");
        
        setControl(controls);
    }


}
