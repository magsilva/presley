package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

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
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class EnviaRespostaWizard extends Wizard implements INewWizard {
	private static final Logger logger = Logger.getLogger(EnviaRespostaWizard.class);

	private EnviaRespostaWizardPage page;
	private ISelection selection;
    private Desenvolvedor desenvolvedorLogado;
    private Problema problema;
    private MensagemAba viewMensagem;
    private Solucao solucaoOrigem;
    
    public EnviaRespostaWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("Add", "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
    }
    
    public EnviaRespostaWizard(MensagemAba viewMensagem, Desenvolvedor desenvolvedorLogado, Problema problema, Solucao solucaoOrigem) {
        this();
        this.desenvolvedorLogado = desenvolvedorLogado;
        this.problema = problema;
        this.viewMensagem = viewMensagem;
        this.solucaoOrigem = solucaoOrigem;
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
        //First save all the page data as variables.

    	try{
    		String resposta = page.getDescricao();

    		Solucao solucao = new Solucao();
    		solucao.setDesenvolvedor( this.desenvolvedorLogado );
    		solucao.setProblema( problema );
    		solucao.setMensagem( resposta );
    		solucao.setData( new Date(System.currentTimeMillis()) ) ;
    		
    		//Colocando no banco
    		solucao = viewMensagem.getViewComunication().adicionaSolucao(solucao);
    		
    		if (solucaoOrigem != null){
    			solucaoOrigem.setSolucaoResposta(solucao);
    			viewMensagem.getViewComunication().atualizarSolucao(solucaoOrigem);
    		}

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
	
	public void addPages() {
        page = new EnviaRespostaWizardPage(selection, solucaoOrigem != null);
        addPage(page);
    }


	
}

