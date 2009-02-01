package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class LoginWizard extends Wizard implements INewWizard {
	private static final Logger logger = Logger.getLogger(AdicionaConhecimentoWizard.class);

	private LoginWizardPage page;
	private ISelection selection;
	private MensagemAba mensagem;

	public LoginWizard() {
		super();
		setNeedsProgressMonitor(true);
		ImageDescriptor image =
			AbstractUIPlugin.imageDescriptorFromPlugin("Add", "/src/main/resources/icons/presley.gif");
		setDefaultPageImageDescriptor(image);


	}

	public LoginWizard(MensagemAba m) {
		this();
		this.mensagem = m;
	}

	private void performOperation(IProgressMonitor monitor) {

	}


	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		//First save all the page data as variables.

		try{
			String login = page.getLogin();
			String senha = page.getSenha();
			String ip = page.getIP();

			Desenvolvedor des = this.mensagem.getViewComunication().login(login, senha);

			if (des== null) {
				return false;
			}
			else {
				this.mensagem.setDesenvolvedorLogado(des);
				this.mensagem.habilitaBotoes();
				this.mensagem.desabilitaBotaoLogin();
			}

		}catch (Exception e) {
			MessageDialog.openError(this.getShell(), "Erro", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(final IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
					performOperation(monitor);
				}
			});
		}
		catch (InvocationTargetException e) {
			return false;
		}
		catch (InterruptedException e) {
			// User canceled, so stop but don't close wizard.
			return false;
		}
		return true;
	}


	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		this.selection = selection;
	}

	public void addPages() {
		page = new LoginWizardPage(selection, this.mensagem);
		addPage(page);

	}

	public MensagemAba getMensagem() {
		return mensagem;
	}

}
