package gui.wizard;

import gui.view.Atividade;
import gui.view.comunication.ViewComunication;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

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

import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.TipoAtividade;

public class AdicionaDesenvolvedorWizard extends Wizard implements INewWizard {

	private AdicionaDesenvolvedorWizardPage page;
	private ISelection selection;
    private Atividade atividade;

    public AdicionaDesenvolvedorWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AdicionaDesenvolvedorWizard(Atividade a) {
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
    		/*String atividade = page.getNomeDesenvolvedor();
    		
    		Desenvolvedor desenvolvedor = page.getDesenvolvedor();
    		Desenvolvedor supervisor = page.getNomeSupervisor();
    		
    		    		
    		TipoAtividade novaAtividade = new TipoAtividade(atividade,desenvolvedor,supervisor,
    				0,dataInicio,dataFim,status,conhecimentos);
  
    		//Cria a atividade no banco
    		//this.atividade.getViewComunication().adicionaAtividade(novaAtividade);
	*/
    		Desenvolvedor desenvolvedor = page.getDesenvolvedor();
    		this.atividade.getViewComunication().adicionaDesenvolvedor(desenvolvedor);
    		
    	}catch (Exception e) {
    		
    		MessageDialog.openError(this.getShell(), "ERRO", e.getMessage());
    		System.out.println("ERRO ERRO:"+e.getMessage());
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
        page=new AdicionaDesenvolvedorWizardPage(selection, this.atividade);
        addPage(page);
        
      }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
