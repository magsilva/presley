package gui.wizard;

import gui.view.Atividade;
import gui.view.comunication.ViewComunication;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

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
import beans.Item;
import beans.TipoAtividade;
import beans.Tree;

public class AdicionaConhecimentoWizard extends Wizard implements INewWizard {

	private AdicionaConhecimentoWizardPage page;
	private AdicionaAtividadeWizardPage2 page2;
    private ISelection selection;
    private Atividade atividade;

    public AdicionaConhecimentoWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AdicionaConhecimentoWizard(Atividade a) {
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
    		//String nome = page.getName();
    		ArrayList<String> nomesNos = page.getConhecimentos();
    		String paiConhecimento = page.paiConhecimento();
    		Conhecimento pai = null;
    		ArrayList<Conhecimento> listaConhecimento = atividade.getViewComunication().getListaConhecimentos();
    		for (Conhecimento conhecimento : listaConhecimento) {
				if (conhecimento.getNome().equals(paiConhecimento)) {
					pai = conhecimento;
					break;
				}
			}
    		Conhecimento novoConhecimento = new Conhecimento();
    		for (String nome : nomesNos) {
    				novoConhecimento.setNome(nome);
					atividade.getViewComunication().adicionaConhecimento(novoConhecimento,pai);
			}
  
    		Tree myOntologia = atividade.getViewComunication().getOntologia();
    		ArrayList<Item> localizados = myOntologia.localizaFilho(paiConhecimento);
    		if (localizados!=null) {
    			for (Item conh : localizados) {
    					conh.adicionaFilho(novoConhecimento.getNome());
    			}
    		}
    		
    	   
    	}catch (Exception e) {
			// TODO: handle exception
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
        page=new AdicionaConhecimentoWizardPage(selection, this.atividade);
        addPage(page);
        
       
    }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
