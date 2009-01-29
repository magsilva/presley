package gui.action;

import gui.view.Dominio;
import gui.wizard.AdicionaAtividadeWizard;
import gui.wizard.AdicionaConhecimentoWizard;
import gui.wizard.AdicionaConhecimentoWizardPage;
import gui.wizard.LoginWizard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

public class RunAdicionaConhecimentoWizard extends Action implements
		IWorkbenchWindowActionDelegate {
	private Dominio dominio;

	public RunAdicionaConhecimentoWizard() {
		// TODO Auto-generated constructor stub
	}
	
	public RunAdicionaConhecimentoWizard(Dominio dominio) {
		this.dominio = dominio;
	}

	public RunAdicionaConhecimentoWizard(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunAdicionaConhecimentoWizard(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunAdicionaConhecimentoWizard(String text, int style) {
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
		AdicionaConhecimentoWizard wizard = new AdicionaConhecimentoWizard(this.dominio);
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

