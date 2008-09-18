package gui.action;

import gui.view.Atividade;
import gui.wizard.AdicionaAtividadeWizard;
import gui.wizard.AdicionaDesenvolvedorWizard;
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

public class RunAdicionaDesenvolvedorWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private Atividade atividade;

	public RunAdicionaDesenvolvedorWizardAction() {
		// TODO Auto-generated constructor stub
	}
	
	public RunAdicionaDesenvolvedorWizardAction(Atividade a) {
		// TODO Auto-generated constructor stub
		this.atividade = a;
	}

	public RunAdicionaDesenvolvedorWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunAdicionaDesenvolvedorWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunAdicionaDesenvolvedorWizardAction(String text, int style) {
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
		AdicionaDesenvolvedorWizard wizard = new AdicionaDesenvolvedorWizard(this.atividade);
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
