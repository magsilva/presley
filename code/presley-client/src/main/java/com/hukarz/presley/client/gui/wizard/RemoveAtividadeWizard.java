package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hukarz.presley.client.gui.view.Atividade;

public class RemoveAtividadeWizard extends Wizard implements INewWizard {
	private static final Logger logger = Logger.getLogger(RemoveAtividadeWizard.class);

	private RemoveAtividadeWizardPage page;
    private ISelection selection;
    private Atividade atividade;

    public RemoveAtividadeWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public RemoveAtividadeWizard(Atividade a) {
        this();
        this.atividade = a;
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
        //First save all the page data as variables.
    	try{
    		ArrayList<String> atividadeRemovidas = page.getAtividadeRemovidas();
    		for (String remover : atividadeRemovidas) {
    //			atividade.getViewComunication().removerAtividade(remover);
			}
	
    	}catch (Exception e) {
			// TODO: handle exception
    		logger.error(e.getMessage());
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
		// TODO Auto-generated method stub
		this.selection = selection;
	}
	
	public void addPages() {
        page=new RemoveAtividadeWizardPage(selection);
    //    page.setAtividades(this.atividade.getViewComunication().getAtividades());
        addPage(page);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
