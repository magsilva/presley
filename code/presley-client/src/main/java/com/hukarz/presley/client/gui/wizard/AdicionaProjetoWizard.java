package com.hukarz.presley.client.gui.wizard;

import java.rmi.RemoteException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.client.gui.view.MensagemAba;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;

public class AdicionaProjetoWizard extends Wizard implements INewWizard {
	private MensagemAba mensagemAba;
	private AdicionaProjetoWizardPage page;
	private ISelection selection;
	
	public AdicionaProjetoWizard(MensagemAba mensagemAba) {
		super();
		this.mensagemAba = mensagemAba;
	}

	public AdicionaProjetoWizard() {
		super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "/src/main/resources/icons/presley.gif");
        setDefaultPageImageDescriptor(image);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		Projeto projeto;
		if ( page.acionarCadastrarProjeto() ){
			projeto = page.cadastrarProjeto();
			
			try {
				mensagemAba.getCadastroProjeto().setProjeto(projeto);
				mensagemAba.getCadastroProjeto().criarProjeto();
			} catch (NomeInvalidoException e1) {
				MessageDialog.openError(this.getShell(), "ERROR", e1.getMessage());
				e1.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (ProjetoInexistenteException e) {
				e.printStackTrace();
			}
		} else {
			projeto = page.ativarProjeto();
			
			try {
				mensagemAba.getCadastroProjeto().setProjeto(projeto);
				mensagemAba.getCadastroProjeto().atualizarStatusProjeto();
			} catch (ProjetoInexistenteException e1) {
				MessageDialog.openError(this.getShell(), "ERROR", e1.getMessage());
				e1.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		this.selection = selection;
	}

	public void addPages() {
        page=new AdicionaProjetoWizardPage(selection, this.mensagemAba);
        addPage(page);
    }

	
}
