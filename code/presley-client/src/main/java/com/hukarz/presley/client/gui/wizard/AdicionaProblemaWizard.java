package com.hukarz.presley.client.gui.wizard;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class AdicionaProblemaWizard extends Wizard implements INewWizard {
	private static final Logger logger = Logger.getLogger(AdicionaProblemaWizard.class);
	
	private AdicionaProblemaWizardPage page;
    private MensagemAba mensagemAba;

	public AdicionaProblemaWizard(MensagemAba m) {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("Add", "/src/main/resources/icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        mensagemAba = m;
	}

	@Override
	public boolean performFinish() {
		cadastrarProblema();

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
			return false;
		}
		
		return true;
	}

	private void cadastrarProblema() {
		try{
			Problema problema = new Problema();
			problema.setDescricao("");
			problema.setMensagem( page.getMensagem() );
			problema.setDescricao( page.getDescricao() );
			problema.setData(new Date(System.currentTimeMillis()));
			problema.setClassesRelacionadas( 
					mensagemAba.getDadosProjetoAtivo().getClassesRelacionadas(page.getDescricao() + " " + page.getMensagem(), " ") ) ;
			problema.setDesenvolvedorOrigem( MensagemAba.getDesenvolvedorLogado() ) ;
			problema.setResolvido(false);
			problema.setExperimento(false);
			
			problema.setProjeto( mensagemAba.getCadastroProjeto().getProjetoAtivo() );

			//Adciona problema ao banco
			mensagemAba.getMensagemProblema().setProblema(problema);
			mensagemAba.getMensagemProblema().cadastrarProblema();

		}catch (Exception e) {
			MessageDialog.openError(this.getShell(), "ERRO", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
    private void performOperation(IProgressMonitor monitor) {
    	
    }
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	public void addPages() {
        page = new AdicionaProblemaWizardPage(mensagemAba);
        addPage(page);
    }
	
}
