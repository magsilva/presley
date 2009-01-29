package com.hukarz.presley.client.gui.wizard;


import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.client.gui.view.Atividade;
import com.hukarz.presley.client.gui.view.MensagemAba;
import com.hukarz.presley.client.gui.view.comunication.ViewComunication;


public class AdicionaDesenvolvedorWizard extends Wizard implements INewWizard {

	private AdicionaDesenvolvedorWizardPage page;
	private AdicionaDesenvolvedorWizardPage2 page2;
	private ISelection selection;
    private MensagemAba mensagemAba;

    public AdicionaDesenvolvedorWizard() {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image =
            AbstractUIPlugin.
                imageDescriptorFromPlugin("Add",
                   "icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        
        
    }
    
    public AdicionaDesenvolvedorWizard(MensagemAba m) {
        this();
        this.mensagemAba = m;
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
		ArrayList<Conhecimento> listaConhecimentos = this.mensagemAba.getViewComunication().getListaConhecimentos();
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
		try {
			this.mensagemAba.getViewComunication().adicionaDesenvolvedor(novoDesenvolvedor);
		} catch (Exception e1) {

			MessageDialog.openError(this.getShell(), "ERROR", e1.getMessage());
			e1.printStackTrace();
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
        page=new AdicionaDesenvolvedorWizardPage(selection, this.mensagemAba);
        addPage(page);
        
        page2=new AdicionaDesenvolvedorWizardPage2(selection, this.mensagemAba);
        addPage(page2);
      }

	public MensagemAba getAtividade() {
		return mensagemAba;
	}
	
}
