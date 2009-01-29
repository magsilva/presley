package gui.action;

import gui.view.MensagemAba;
import gui.wizard.EnviaRetornoSolucaoWizard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.hukarz.presley.beans.Solucao;


public class RunEnviaRetornoSolucaoWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
    private Solucao solucao;
	private MensagemAba viewMensagem;

	public RunEnviaRetornoSolucaoWizardAction(MensagemAba viewMensagem, Solucao solucao) {
        this.solucao = solucao;
        this.viewMensagem = viewMensagem;
    }

	public void dispose() {
		// TODO Auto-generated method stub
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
	}

	public void run(IAction action) {
		EnviaRetornoSolucaoWizard wizard = new EnviaRetornoSolucaoWizard(viewMensagem, solucao);
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog dialog = new WizardDialog(shell,wizard);
		dialog.create();
		dialog.open();
		dialog.close();
		wizard.dispose();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
	}

}
