package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.client.gui.view.Dominio;
import com.hukarz.presley.interfaces.Conhecimento;


public class AdicionaConhecimentoWizard extends Wizard implements INewWizard {

	private static final Logger logger = Logger.getLogger(AdicionaConhecimentoWizard.class);
	private AdicionaConhecimentoWizardPage page;
//	private AdicionaAtividadeWizardPage2 page2;
    private ISelection selection;
    private Dominio dominio;

    public AdicionaConhecimentoWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "/src/main/resources/icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AdicionaConhecimentoWizard(Dominio dominio) {
        this();
        this.dominio = dominio;
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
        //First save all the page data as variables.
    	try{
			Conhecimento conhecimento = dominio.getConhecimento();
    		Map<TopicoConhecimento, TopicoConhecimento> conhecimentoFilhoPai = page.conhecimentoFilhoPai();
    		
    		Set<TopicoConhecimento> conhecimentosFilho = conhecimentoFilhoPai.keySet();
    		
    		ArrayList<TopicoConhecimento> listaConhecimento = conhecimento.getListaConhecimento();
    		
    		for (Iterator<TopicoConhecimento> iterator = conhecimentosFilho.iterator(); iterator.hasNext();) {
    			TopicoConhecimento filho = iterator.next();
				
    			TopicoConhecimento pai = null;
    			TopicoConhecimento paiConhecimento = conhecimentoFilhoPai.get( filho );
   				if (paiConhecimento != null){
   		    		for (TopicoConhecimento topicoConhecimento : listaConhecimento) {
   						if (topicoConhecimento.getNome().equals(paiConhecimento.getNome())) {
   							pai = topicoConhecimento;
   							break;
   						}
   					}
   				}
   				
   				conhecimento.setConhecimento(filho);
   				conhecimento.criarConhecimento();
   				conhecimento.associaConhecimentos(pai);
			}

    	   
    	}catch (Exception e) {
			// TODO: handle exception
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
		// TODO Auto-generated method stub
		this.selection = selection;
	}
	
	public void addPages() {
        page=new AdicionaConhecimentoWizardPage(selection, this.dominio);
        addPage(page);
    }

	public Dominio getDominio() {
		return dominio;
	}
	
}
