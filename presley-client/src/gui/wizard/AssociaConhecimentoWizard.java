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

public class AssociaConhecimentoWizard extends Wizard implements INewWizard {

	private AssociaConhecimentoWizardPage page;
	//private AdicionaAtividadeWizardPage2 page2;
    private ISelection selection;
    private Atividade atividade;
    private String nomeAtividadeSelecionada;

    public AssociaConhecimentoWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("associaConh.GIF",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AssociaConhecimentoWizard(Atividade a) {
        this();
        this.atividade = a;
        this.nomeAtividadeSelecionada = this.atividade.getAtividadeSelecionada();
    }
    
    private void performOperation(IProgressMonitor monitor) {
    	
    }


	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
        //First save all the page data as variables.
    	try{
    		TipoAtividade atividadeAssociada = null;
    		ArrayList<Conhecimento> conhecimentos = page.getConhecimentos();
    		ArrayList<TipoAtividade> atividades = this.atividade.getViewComunication().buscaAtividades();
    		for (TipoAtividade tipoAtividade : atividades) {
				if (tipoAtividade.getDescricao().equals(nomeAtividadeSelecionada)) {
					atividadeAssociada=tipoAtividade;
					break;
				}
			}
    		
    		//Cria a atividade no banco
    		this.atividade.getViewComunication().associaConhecimentoAtividade(conhecimentos, atividadeAssociada);
	
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
        page=new AssociaConhecimentoWizardPage(selection, this.atividade);
        addPage(page);
        
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}