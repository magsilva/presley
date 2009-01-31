package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
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

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.client.gui.view.Atividade;


public class AssociaProblemaAtividadeWizard extends Wizard implements INewWizard {

	private static final Logger logger = Logger.getLogger(AssociaProblemaAtividadeWizard.class);
	private AssociaProblemaAtividadeWizardPage page;
	private AssociaProblemaAtividadeWizardPage2 page2;
    private ISelection selection;
    private Atividade atividade;
    private String nomeAtividade;

    public AssociaProblemaAtividadeWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AssociaProblemaAtividadeWizard(Atividade a, String nome) {
        this();
        this.atividade = a;
        this.nomeAtividade = nome;
        
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
        //First save all the page data as variables.

    	try{
    		
    		
    		String descricao = page.getDescricao();
    		String msg = page.getMensagem();
    		
    		ArrayList<Conhecimento> conhecimentos = page2.getConhecimentos();
    		
    		Problema problema = new Problema();
    		problema.setDescricao(descricao);
    		problema.setMensagem(msg);
    		problema.setData(new Date(System.currentTimeMillis()));
    		problema.setResolvido(false);
    		
    		TipoAtividade atividadeAssociada = null;
    		
    		ArrayList<TipoAtividade> listaAtividades = atividade.getViewComunication().buscaAtividades();
    		for (TipoAtividade tipoAtividade : listaAtividades) {
				if (tipoAtividade.getDescricao().equals(nomeAtividade)) {
					atividadeAssociada = tipoAtividade;
					problema.setAtividade(tipoAtividade);
				}
			}
  
    		//Associa os conhecimentos ao problema
    		//Essa lista de conhecimetos ser� enviada quando for buscar
    		//a lista de desenvolvedores que ajudar�o no problema
    		atividade.associaConhecimentosProblema(problema, conhecimentos);
    		
    		//Associa o problema a atividade no banco
    		for(Conhecimento c : conhecimentos) {
    			logger.info(c.getNome());
    		}
    		atividade.getViewComunication().associaProblemaAtividade(problema, atividadeAssociada, conhecimentos);
 
	
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
        page=new AssociaProblemaAtividadeWizardPage(selection, this.atividade, nomeAtividade);
        addPage(page);
        
        page2=new AssociaProblemaAtividadeWizardPage2(selection,this.atividade);
        addPage(page2);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
