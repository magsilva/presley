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

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TipoAtividade;


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
    		ArrayList<Conhecimento> conhecimentos = page2.getConhecimentos();
    		
    		Desenvolvedor desenvolvedor = page.getDesenvolvedor();
    		Desenvolvedor supervisor = page.getNomeSupervisor();
    		
    		Date dataInicio = null;
			Date dataFim = null;
			try {
				dataInicio = page.getDataInicio();
				dataFim = page.getDataFim();
			} catch (RuntimeException e) {
				MessageDialog.openError(this.getShell(), "ERRO", "Formato data inválida.");
				return false;
			}
    		
    		boolean status = page.getStatus();
    		
    		TipoAtividade novaAtividade = new TipoAtividade(atividade,desenvolvedor,supervisor,
    				0,dataInicio,dataFim,status,conhecimentos);
  
    		//Cria a atividade no banco
    		this.atividade.getViewComunication().adicionaAtividade(novaAtividade);
	
    	}catch (Exception e) {
    		MessageDialog.openError(this.getShell(), "ERRO", e.getMessage());
    		System.out.println("ERRO ERRO:"+e.getMessage());
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
        page=new AdicionaAtividadeWizardPage(selection, this.atividade);
        addPage(page);
        
        page2=new AdicionaAtividadeWizardPage2(selection,this.atividade);
        addPage(page2);
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
