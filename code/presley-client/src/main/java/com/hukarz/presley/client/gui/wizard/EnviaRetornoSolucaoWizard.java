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

import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class EnviaRetornoSolucaoWizard extends Wizard implements INewWizard {
	private static final Logger logger = Logger.getLogger(EnviaRetornoSolucaoWizard.class);
    private Solucao solucao;
    private EnviaRetornoSolucaoWizardPage page;
    private MensagemAba viewMensagem;
    private ISelection selection;
    
    public EnviaRetornoSolucaoWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("Add", "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
    }
    
    public EnviaRetornoSolucaoWizard(MensagemAba viewMensagem, Solucao solucao) {
        this();
        this.solucao = solucao;
        this.viewMensagem = viewMensagem;
    }

	@Override
	public boolean performFinish() {
    	try{
    		String resposta = page.getMensagemResposta();
    		solucao.setRetornoSolucao(resposta);
    		//Colocando no banco
    		viewMensagem.getViewComunication().atualizarSolucao(solucao);
    	}catch (Exception e) {
    		MessageDialog.openError(this.getShell(), "ERRO", e.getMessage());
    		logger.error(e.getMessage());
    		e.printStackTrace();
	}

	try {
	      getContainer().run(true, true, new IRunnableWithProgress() {
	         public void run(IProgressMonitor monitor)
	            throws InvocationTargetException, InterruptedException
	         {
	            performOperation(monitor);
	         }
	      });
	   }catch (InvocationTargetException e) {
		      return false;
	   }catch (InterruptedException e) {
	      // User canceled, so stop but don't close wizard.
	      return false;
	   }
        return true;
	}

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }
	
    private void performOperation(IProgressMonitor monitor) {

    }

    public void addPages() {
        page = new EnviaRetornoSolucaoWizardPage(selection, solucao);
        addPage(page);
    }
}
