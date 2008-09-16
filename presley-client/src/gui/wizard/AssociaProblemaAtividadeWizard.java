package gui.wizard;

import gui.view.Atividade;
import gui.view.comunication.ViewComunication;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.TipoAtividade;

public class AssociaProblemaAtividadeWizard extends Wizard implements INewWizard {

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
        
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
        //First save all the page data as variables.
		/*
    	try{
    		
    		String atividade = page.getNomeAtividade();
    		ArrayList<Conhecimento> conhecimentos = page2.getConhecimentos();
    		
    		Desenvolvedor desenvolvedor = page.getDesenvolvedor();
    		Desenvolvedor supervisor = page.getNomeSupervisor();
    		
    		Date dataInicio = page.getDataInicio();
    		Date dataFim = page.getDataFim();
    		
    		boolean status = page.getStatus();
    		
    		TipoAtividade novaAtividade = new TipoAtividade(atividade,desenvolvedor,supervisor,
    				0,dataInicio,dataFim,status,conhecimentos);
  
    		//Cria a atividade no banco
 //   		this.atividade.adicionaAtividade(novaAtividade);
	
    	}catch (Exception e) {
			// TODO: handle exception
    		System.out.println("ERRO ERRO:"+e.getMessage());
    		e.printStackTrace();
		}
		*/
    	
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
        //page=new AssociaProblemaAtividadeWizardPage(selection, this.atividade, this.atividade.);
        //addPage(page);
        
        //page2=new AssociaProblemaAtividadeWizardPage2(selection,this.atividade);
        //addPage(page2);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
