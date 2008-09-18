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

public class BuscaDesenvolvedoresWizard extends Wizard implements INewWizard {

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
    		System.out.println("grau de confiança: " + grauDeConfianca);
    		System.out.println("conhecimentos: " + conhecimentos.toString());
    		this.atividade.setDesenvolvedores(this.atividade.getViewComunication().buscaDesenvolvedores(conhecimentos, grauDeConfianca));
	
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
        page=new BuscaDesenvolvedoresWizardPage(selection, this.atividade);
        addPage(page);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
