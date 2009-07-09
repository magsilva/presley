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

import com.hukarz.presley.client.gui.view.MensagemAba;
import com.hukarz.presley.client.gui.wizard.AdicionaProblemaWizard;

public class RunAdcionaProblemaWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private MensagemAba mensagem;

	public RunAdcionaProblemaWizardAction() {
		// TODO Auto-generated constructor stub
	}

	public RunAdcionaProblemaWizardAction(MensagemAba m) {
		mensagem = m;
	}

	public RunAdcionaProblemaWizardAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public RunAdcionaProblemaWizardAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public RunAdcionaProblemaWizardAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		AdicionaProblemaWizard wizard = new AdicionaProblemaWizard(mensagem);
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
