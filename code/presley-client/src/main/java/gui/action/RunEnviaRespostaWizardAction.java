package gui.action;

import gui.view.MensagemAba;
import gui.wizard.AdicionaAtividadeWizard;
import gui.wizard.AssociaProblemaAtividadeWizard;
import gui.wizard.EnviaRespostaWizard;
import gui.wizard.LoginWizard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.beans.TipoAtividade;


public class RunEnviaRespostaWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private MensagemAba viewMensagem;
    private Problema problema;
    private Solucao solucaoOrigem;

	public RunEnviaRespostaWizardAction() {
		// TODO Auto-generated constructor stub
	}
	
	public RunEnviaRespostaWizardAction(MensagemAba viewMensagem, Problema problema, Solucao solucaoOrigem) {
		this.viewMensagem = viewMensagem;
		this.problema = problema;
		this.solucaoOrigem = solucaoOrigem;
	}

	public RunEnviaRespostaWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunEnviaRespostaWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunEnviaRespostaWizardAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	public void run(IAction arg0) {
		EnviaRespostaWizard wizard = new EnviaRespostaWizard(this.viewMensagem, viewMensagem.getDesenvolvedorLogado(), problema, solucaoOrigem);
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog dialog = new WizardDialog(shell,wizard);
		dialog.create();
		dialog.open();
		dialog.close();
		wizard.dispose();
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

}
