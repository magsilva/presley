package com.hukarz.presley.client.gui.action;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import com.hukarz.presley.client.gui.view.Atividade;
import com.hukarz.presley.client.gui.wizard.RemoveAtividadeWizard;

public class RunRemoveAtividadeWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private Atividade atividade;

	public RunRemoveAtividadeWizardAction() {
		// TODO Auto-generated constructor stub
	}
	
	public RunRemoveAtividadeWizardAction(Atividade a) {
		// TODO Auto-generated constructor stub
		this.atividade = a;
	}

	public RunRemoveAtividadeWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunRemoveAtividadeWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunRemoveAtividadeWizardAction(String text, int style) {
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
		RemoveAtividadeWizard wizard = new RemoveAtividadeWizard(this.atividade);
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		WizardDialog dialog = new WizardDialog(shell,wizard);
		dialog.create();
		dialog.open();
	

	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

}
