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

import com.hukarz.presley.client.gui.view.Dominio;
import com.hukarz.presley.client.gui.wizard.AdicionaConhecimentoWizard;

public class RunAdicionaConhecimentoWizard extends Action implements
		IWorkbenchWindowActionDelegate {
	private Dominio dominio;

	public RunAdicionaConhecimentoWizard() {
	}
	
	public RunAdicionaConhecimentoWizard(Dominio dominio) {
		this.dominio = dominio;
	}

	public RunAdicionaConhecimentoWizard(String text) {
		super(text);
	}

	public RunAdicionaConhecimentoWizard(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RunAdicionaConhecimentoWizard(String text, int style) {
		super(text, style);
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow arg0) {
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
	}

}

