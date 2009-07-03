package com.hukarz.presley.client.gui.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;

import org.apache.log4j.Logger;
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

import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class AdicionaProblemaWizard extends Wizard implements INewWizard {
	private static final Logger logger = Logger.getLogger(AdicionaProblemaWizard.class);
	
	private AdicionaProblemaWizardPage page;
    private ISelection selection;
    private MensagemAba mensagem;

	public AdicionaProblemaWizard(MensagemAba m) {
        super();
        setNeedsProgressMonitor(true);
        ImageDescriptor image = AbstractUIPlugin.imageDescriptorFromPlugin("Add", "/src/main/resources/icons/presley.gif");
        setDefaultPageImageDescriptor(image);
        mensagem = m;
	}

	@Override
	public boolean performFinish() {
		if (page.executarExperimentoWDDS()){
			executarExperimento();
		} else {
			cadastrarProblema();
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
					page.getClassesRelacionadas(page.getDescricao() + " " + page.getMensagem(), " ") ) ;
			problema.setDesenvolvedorOrigem( MensagemAba.getDesenvolvedorLogado() ) ;
			problema.setResolvido(false);
			problema.setProjeto( mensagem.getViewComunication().getProjetoAtivo());

			//Adciona problema ao banco
			mensagem.getViewComunication().adicionaProblema(problema);

		}catch (Exception e) {
			MessageDialog.openError(this.getShell(), "ERRO", e.getMessage());
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void executarExperimento(){
		File diretorioCD = new File( page.getDiretorioArquivos() );
		
		File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".pergunta");  
			}  
		}); 		
		
		try {
			for (int i = 0; i < listagemDiretorio.length; i++) {
				File file = new File( listagemDiretorio[i].getAbsolutePath() );
				FileReader fileReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fileReader);

        		Problema problema = new Problema();
        		problema.setResolvido(false);
        		problema.setData(new Date(System.currentTimeMillis()));
        		problema.setProjeto( mensagem.getViewComunication().getProjetoAtivo() );
        		
        		// Desenvolvedor que enviou o problema
				String linha = reader.readLine();
        		problema.setDesenvolvedorOrigem( mensagem.getViewComunication().login(linha, "1") ) ;

        		// Descrição do problema
        		linha = reader.readLine();
        		problema.setDescricao( linha );
        		
        		// Corpo da Mensagem
        		String CorpoMensagem = "";
				while( (CorpoMensagem = reader.readLine()) != null ){
	        		problema.setMensagem( problema.getMensagem() + " " + CorpoMensagem );
				}

        		problema.setClassesRelacionadas( 
        				page.getClassesRelacionadas(problema.getDescricao() + " " + problema.getMensagem(), " ") ) ;
        		
        		//Adciona problema ao banco
        		mensagem.getViewComunication().adicionaProblema(problema);
			}
		} catch (Exception e) {

		}

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
