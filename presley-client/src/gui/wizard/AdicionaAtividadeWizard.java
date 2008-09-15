package gui.wizard;

import gui.view.Atividade;
import gui.view.comunication.ViewComunication;

import java.lang.reflect.InvocationTargetException;
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

public class AdicionaAtividadeWizard extends Wizard implements INewWizard {

	private AdicionaAtividadeWizardPage page;
	private AdicionaAtividadeWizardPage2 page2;
    private ISelection selection;
    private Atividade atividade;

    public AdicionaAtividadeWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AdicionaAtividadeWizard(Atividade a) {
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
    		
    		String atividade = page.getNomeAtividade();
    		ArrayList<String> conhecimentos = page2.getConhecimentos();
    		
    		/**MUDANCA*/
    		//conhecimentos.remove(this.atividade.getViewComunication().getOntologia().getRaiz().getNome());
    		ArrayList<String> problemas = new ArrayList<String>();
    		this.atividade.adicionaAtividade(atividade, conhecimentos, problemas);
	
    	}catch (Exception e) {
			// TODO: handle exception
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
        page=new AdicionaAtividadeWizardPage(selection);
        addPage(page);
        
        page2=new AdicionaAtividadeWizardPage2(selection,this.atividade);
        addPage(page2);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
