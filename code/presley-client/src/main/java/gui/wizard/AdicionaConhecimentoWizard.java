package gui.wizard;

import gui.view.Dominio;
import gui.view.comunication.ViewComunication;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
import com.hukarz.presley.beans.Item;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.beans.Tree;


public class AdicionaConhecimentoWizard extends Wizard implements INewWizard {

	private AdicionaConhecimentoWizardPage page;
	private AdicionaAtividadeWizardPage2 page2;
    private ISelection selection;
    private Dominio dominio;

    public AdicionaConhecimentoWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
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
    		Map<Conhecimento, Conhecimento> conhecimentoFilhoPai = page.conhecimentoFilhoPai();
    		
    		Set<Conhecimento> conhecimentosFilho = conhecimentoFilhoPai.keySet(); 
    		ArrayList<Conhecimento> listaConhecimento = dominio.getViewComunication().getListaConhecimentos();
    		
    		for (Iterator<Conhecimento> iterator = conhecimentosFilho.iterator(); iterator.hasNext();) {
    			Conhecimento filho = iterator.next();
				
	    		Conhecimento pai = null;
   				Conhecimento paiConhecimento = conhecimentoFilhoPai.get( filho );
   				if (paiConhecimento != null){
   		    		for (Conhecimento conhecimento : listaConhecimento) {
   						if (conhecimento.getNome().equals(paiConhecimento.getNome())) {
   							pai = conhecimento;
   							break;
   						}
   					}
   				}
   				
				dominio.getViewComunication().adicionaConhecimento(filho,pai);
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
        page=new AdicionaConhecimentoWizardPage(selection, this.dominio);
        addPage(page);
    }

	public Dominio getDominio() {
		return dominio;
	}
	
}
