package gui.wizard;

import gui.view.Atividade;
import gui.view.comunication.ViewComunication;

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

import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.Problema;

public class BuscaDesenvolvedoresWizardPage extends WizardPage {

	private Atividade atividade;
	private Text grauDeConfianca;
	ViewComunication viewComunication;
	

    public BuscaDesenvolvedoresWizardPage(ISelection selection, Atividade atividade) {
        super("wizardPage");
        setTitle("Busca Desenvolvedores Wizard");
        setDescription("Indica os desenvolvedores que podem ajudar a resolver o problema.");
        this.atividade = atividade;
    }

    public int getGrauDeConfianca() {
        return Integer.parseInt(grauDeConfianca.getText());
    }
    
    
    public ArrayList<String> getConhecimentos(){
    	return null;
    	//return this.atividade.getConhecimentosDoProblema();
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
        layout.horizontalSpacing = 5;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Grau de confiança: ");

        grauDeConfianca = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(
            GridData.FILL_HORIZONTAL);
        grauDeConfianca.setLayoutData(gd);
        grauDeConfianca.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelExplicativo = new Label(controls, SWT.NULL);
        labelExplicativo.setText("digite o grau de confiança desejado (Entre 1 e 100)");

        setControl(controls);
    }


}
