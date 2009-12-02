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

import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.client.gui.view.MensagemAba;
import com.hukarz.presley.client.gui.wizard.EnviaRespostaWizard;


public class RunEnviaRespostaWizardAction extends Action implements
		IWorkbenchWindowActionDelegate {
	private MensagemAba viewMensagem;
    private Problema problema;
    private Solucao solucaoOrigem;

	public RunEnviaRespostaWizardAction() {
	}
	
	public RunEnviaRespostaWizardAction(MensagemAba viewMensagem, Problema problema, Solucao solucaoOrigem) {
		this.viewMensagem = viewMensagem;
		this.problema = problema;
		this.solucaoOrigem = solucaoOrigem;
	}

	public RunEnviaRespostaWizardAction(String text) {
		super(text);
	}

	public RunEnviaRespostaWizardAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public RunEnviaRespostaWizardAction(String text, int style) {
		super(text, style);
	}

	public void dispose() {
	}

	public void init(IWorkbenchWindow arg0) {
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
	}

}
