package gui.wizard;

import gui.view.Atividade;
import gui.view.comunication.ViewComunication;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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

public class AdicionaDesenvolvedorWizard extends Wizard implements INewWizard {

	private AdicionaDesenvolvedorWizardPage page;
	private AdicionaDesenvolvedorWizardPage2 page2;
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
    	  
		String nome = page.getNomeDesenvolvedor();
		String email = page.getEmailDesenvolvedor();
		String local = page.getLocalidadeDesenvolvedor();
		String senha = page.getSenhaDesenvolvedor();
		ArrayList<Double> graus = page2.pegaGraus();
		ArrayList<String> conhecimentos = page2.pegaConhecimentos();
		ArrayList<Conhecimento> listaConhecimentos = this.atividade.getViewComunication().getListaConhecimentos();
		ArrayList<Conhecimento> listaConhecimentosSelecionados = new ArrayList<Conhecimento>();
		HashMap<Conhecimento,Double> mapConhecimentoGrau = new HashMap<Conhecimento, Double>();
		for (String nomeConhecimento : conhecimentos) {
			for (Conhecimento conhecimento : listaConhecimentos) {
				if(conhecimento.getNome().equals(nomeConhecimento)){
					mapConhecimentoGrau.put(conhecimento, graus.get(conhecimentos.indexOf(nomeConhecimento)));
				}
			}
		}
		
		Desenvolvedor novoDesenvolvedor = new Desenvolvedor();
		novoDesenvolvedor.setEmail(email);
		novoDesenvolvedor.setListaConhecimento(mapConhecimentoGrau);
		novoDesenvolvedor.setLocalidade(local);
		novoDesenvolvedor.setNome(nome);
		novoDesenvolvedor.setSenha(senha);
		this.atividade.getViewComunication().adicionaDesenvolvedor(novoDesenvolvedor);
		
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
        
        page2=new AdicionaDesenvolvedorWizardPage2(selection, this.atividade);
        addPage(page2);
      }

	public Atividade getAtividade() {
		return atividade;
	}
	
}
