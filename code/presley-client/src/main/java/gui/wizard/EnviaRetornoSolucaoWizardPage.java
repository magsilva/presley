package gui.wizard;

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

import com.hukarz.presley.beans.Solucao;


public class EnviaRetornoSolucaoWizardPage extends WizardPage {
	private Text mensagemRespostaText;
    private Solucao solucao;

    public EnviaRetornoSolucaoWizardPage(ISelection selection, Solucao solucao) {
        super("wizardPage");
        setTitle("Envia Resposta Wizard");
        setDescription("Envia uma Resposta para a solução do usuario.");
        this.solucao = solucao;
    }

    public String getMensagemResposta() {
        return mensagemRespostaText.getText();
    }

	public void createControl(Composite parent) {
        Composite controls = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        controls.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 2;

        Label label = new Label(controls, SWT.NULL);
        label.setText("Mensagem de resposta:");

        mensagemRespostaText = new Text( controls, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gd = new GridData( GridData.FILL_BOTH);
        mensagemRespostaText.setLayoutData(gd);
        mensagemRespostaText.setText( solucao.getRetornoSolucao() );
        mensagemRespostaText.addModifyListener(
                new ModifyListener() {
                    public void modifyText(
                            ModifyEvent e) {
                        dialogChanged();
                    }
                 });
        
        setControl(controls);

	}
	
    private void dialogChanged() { }

}