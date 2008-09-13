package gui.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
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

public class AdicionaAtividadeWizardPage extends WizardPage {

	private Text nomeAtividadeText;

    public AdicionaAtividadeWizardPage(ISelection selection) {
        super("wizardPage");
        setTitle("Adiciona Atividade Wizard");
        setDescription("Adiciona uma nova Atividade.");
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getNomeAtividade() {
        return nomeAtividadeText.getText();
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
        layout.verticalSpacing = 9;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Nome da Atividade:");

        nomeAtividadeText = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(
            GridData.FILL_HORIZONTAL);
        nomeAtividadeText.setLayoutData(gd);
        nomeAtividadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        label = new Label(controls, SWT.NULL);
        label.setText("digite o nome da Atividade");

        setControl(controls);
    }


}
