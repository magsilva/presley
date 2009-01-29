package gui.action;

import gui.view.Atividade;
import gui.wizard.AssociaProblemaAtividadeWizard;
import gui.wizard.BuscaDesenvolvedoresWizard;
//import gui.wizard.BuscaDesenvolvedoresWizard;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import beans.BuscaDesenvolvedores;

public class RunBuscaDesenvolvedorWizardAction extends Action implements
IWorkbenchWindowActionDelegate{
	
	private Atividade atividade;

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public RunBuscaDesenvolvedorWizardAction(Atividade a) {
		// TODO Auto-generated constructor stub
		this.atividade = a;
	}
	
	public RunBuscaDesenvolvedorWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunBuscaDesenvolvedorWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunBuscaDesenvolvedorWizardAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {
		
		BuscaDesenvolvedoresWizard wizard = new BuscaDesenvolvedoresWizard(this.atividade);
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
