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
import com.hukarz.presley.client.gui.wizard.AdicionaDesenvolvedorWizard;
import com.hukarz.presley.client.gui.wizard.AdicionaProjetoWizard;

public class RunAdicionaDesenvolvedorWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private MensagemAba mensagemAba;

	public RunAdicionaDesenvolvedorWizardAction() {
		// TODO Auto-generated constructor stub
	}
	
	public RunAdicionaDesenvolvedorWizardAction(MensagemAba m) {
		this.mensagemAba = m;
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
		AdicionaDesenvolvedorWizard wizard = new AdicionaDesenvolvedorWizard(this.mensagemAba);
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
