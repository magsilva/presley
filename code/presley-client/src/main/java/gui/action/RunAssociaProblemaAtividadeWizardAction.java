package gui.action;

import gui.view.Atividade;
import gui.wizard.AdicionaAtividadeWizard;
import gui.wizard.AssociaProblemaAtividadeWizard;
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

import com.hukarz.presley.beans.TipoAtividade;


public class RunAssociaProblemaAtividadeWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private Atividade atividade;
	private String atividadeAssociada;

	public RunAssociaProblemaAtividadeWizardAction() {
		// TODO Auto-generated constructor stub
	}
	
	public RunAssociaProblemaAtividadeWizardAction(Atividade a, String atividadeAssociada) {
		// TODO Auto-generated constructor stub
		this.atividade = a;
		this.atividadeAssociada = atividadeAssociada;
	}

	public RunAssociaProblemaAtividadeWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunAssociaProblemaAtividadeWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunAssociaProblemaAtividadeWizardAction(String text, int style) {
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
		
		 
		AssociaProblemaAtividadeWizard wizard = new AssociaProblemaAtividadeWizard(this.atividade, this.atividadeAssociada);
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
