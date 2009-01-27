package gui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

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

import gui.view.Atividade;
import gui.view.MensagemAba;
import beans.Problema;

public class AdicionaProblemaWizard extends Wizard implements INewWizard {
	private AdicionaProblemaWizardPage page;
    private ISelection selection;
    private MensagemAba mensagem;

	public AdicionaProblemaWizard(MensagemAba m) {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("Add", "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        mensagem = m;
	}

	@Override
	public boolean performFinish() {
    	try{
    		Problema problema = new Problema();
    		problema.setDescricao("");
    		problema.setMensagem( page.getMensagem() );
    		problema.setDescricao( page.getDescricao() );
    		problema.setData(new Date(System.currentTimeMillis()));
    		problema.setClassesRelacionadas( page.getClassesRelacionadas() ) ;
    		problema.setDesenvolvedorOrigem( MensagemAba.getDesenvolvedorLogado() ) ;
    		problema.setResolvido(false);
    		
    		//Adciona problema ao banco
    		mensagem.getViewComunication().adicionaProblema(problema);
    		
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

    private void performOperation(IProgressMonitor monitor) {
    	
    }
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	public void addPages() {
        page = new AdicionaProblemaWizardPage(mensagem);
        addPage(page);
    }
	
}
