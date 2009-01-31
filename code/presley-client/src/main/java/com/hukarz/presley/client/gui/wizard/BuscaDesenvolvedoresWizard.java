package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

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

import com.hukarz.presley.client.gui.view.Atividade;


public class BuscaDesenvolvedoresWizard extends Wizard implements INewWizard {

	private static final Logger logger = Logger.getLogger(BuscaDesenvolvedoresWizard.class);
	private BuscaDesenvolvedoresWizardPage page;
    private ISelection selection;
    private Atividade atividade;

    public BuscaDesenvolvedoresWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public BuscaDesenvolvedoresWizard(Atividade a) {
        this();
        this.atividade = a;
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
        //First save all the page data as variables.
    	try{
    		ArrayList<String> conhecimentos = page.getConhecimentos();
    		int grauDeConfianca = page.getGrauDeConfianca();
    		logger.info("grau de confiança: " + grauDeConfianca);
    		logger.info("conhecimentos: " + conhecimentos.toString());
    		this.atividade.setDesenvolvedores(this.atividade.getViewComunication().buscaDesenvolvedores(conhecimentos, grauDeConfianca));
	
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
		   /*Preencher o panel com a lista*/
		   this.atividade.updateListaGraficaDesenvolvedores();
		   return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub
		this.selection = selection;
	}
	
	public void addPages() {
        page=new BuscaDesenvolvedoresWizardPage(selection, this.atividade);
        addPage(page);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
