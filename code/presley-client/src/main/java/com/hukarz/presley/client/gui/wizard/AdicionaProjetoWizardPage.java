package com.hukarz.presley.client.gui.wizard;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.client.gui.view.MensagemAba;

public class AdicionaProjetoWizardPage extends WizardPage {
	private Combo projetosExistentes;
	private Text nomeText, enderecoLeituraText, enderecoGravacaoText;
	private Button projetoAtivo;
	private MensagemAba mensagem;
	private TabFolder tabFolder;
	private ArrayList<Projeto> projetos; 

	public AdicionaProjetoWizardPage(ISelection selection, MensagemAba mensagem) {
		super("wizardPage");
        setTitle("Projeto Wizard");
        setDescription("Adiciona Projeto Ativo");
        this.mensagem = mensagem;
	}

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    private void dialogChanged() {
		// TODO Auto-generated method stub
    }

    public boolean acionarCadastrarProjeto(){
    	return (tabFolder.getSelectionIndex() == 0) ;
    }
    
    public Projeto cadastrarProjeto(){
    	Projeto projeto = new Projeto();
    	projeto.setAtivo(true);
    	projeto.setNome( nomeText.getText() );
    	projeto.setEndereco_Servidor_Gravacao( enderecoGravacaoText.getText() );
    	projeto.setEndereco_Log( enderecoLeituraText.getText() );
    	
    	return projeto;
    }
    
    public Projeto ativarProjeto(){
    	Projeto projeto = projetos.get( projetosExistentes.getSelectionIndex() );
    	projeto.setAtivo( projetoAtivo.getSelection() );
    	return projeto;
    }
    
	@Override
	public void createControl(Composite parent) {
        Composite controls = new Composite(parent, SWT.NULL);
        GridLayout layout  = new GridLayout();
        controls.setLayout(layout);

        // Exemplo tirado de http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/Createsatabbeddisplaywithfourtabsandafewcontrols.htm
        // Create the containing tab folder
        tabFolder = new TabFolder(controls, SWT.NONE);
        GridData gdDescricao = new GridData( GridData.FILL_BOTH);
        tabFolder.setLayoutData(gdDescricao);
        
        TabItem one = new TabItem(tabFolder, SWT.NONE);
        one.setText("Cadastro de Projetos");
        one.setToolTipText("Cadastro de Projetos");
        one.setControl(getTabOneControl(tabFolder));

        TabItem two = new TabItem(tabFolder, SWT.NONE);
        two.setText("Selecionar Projeto Ativo");
        two.setToolTipText("Selecionar Projeto Ativo");
        two.setControl(getTabTwoControl(tabFolder));
        
        
        setControl(controls);

	}

	  /**
	   * Gets the control for tab one
	   * 
	   * @param tabFolder the parent tab folder
	   * @return Control
	   */
	  private Control getTabOneControl(TabFolder tabFolder) {
	    Composite composite = new Composite(tabFolder, SWT.NULL);
	    GridLayout layout = new GridLayout();
	    composite.setLayout(layout);
	    layout.numColumns = 2;
	    
	    Label lblNomeProjeto = new Label(composite, SWT.NULL);
	    lblNomeProjeto.setText("Nome do Projeto: ");
		
		nomeText = new Text( composite, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData( GridData.FILL_HORIZONTAL);
        nomeText.setLayoutData(gd);
        nomeText.addModifyListener( new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        
	    Label lblLeitura = new Label(composite, SWT.NULL);
	    lblLeitura.setText("Endereço do Projeto: ");
        
        enderecoLeituraText = new Text( composite, SWT.BORDER | SWT.SINGLE);
        enderecoLeituraText.setLayoutData(gd);
        enderecoLeituraText.addModifyListener( new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        
	    Label lblGravacao = new Label(composite, SWT.NULL);
	    lblGravacao.setText("Endereço para Gravação: ");
        
        enderecoGravacaoText = new Text( composite, SWT.BORDER | SWT.SINGLE);
        enderecoGravacaoText.setLayoutData(gd);
        enderecoGravacaoText.addModifyListener( new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

	    return composite;
	  }
	  
	  /**
	   * Gets the control for tab two
	   * 
	   * @param tabFolder the parent tab folder
	   * @return Control
	   */
	  private Control getTabTwoControl(TabFolder tabFolder) {
		  Composite composite = new Composite(tabFolder, SWT.NULL);
		  GridLayout layout = new GridLayout();
		  composite.setLayout(layout);
		  layout.numColumns = 2;

		  Label lblProjetosExistentes = new Label(composite, SWT.NULL);
		  lblProjetosExistentes.setText("Nome do Projeto: ");

		  projetosExistentes = new Combo(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		  projetos =  mensagem.getViewComunication().getListaProjetos(null);

		  for (Iterator<Projeto> iterator = projetos.iterator(); iterator.hasNext();) {
			Projeto projeto = iterator.next();
			projetosExistentes.add( projeto.getNome() );
		  }
		  
		  projetosExistentes.addSelectionListener(
				  new SelectionListener(){

					  @Override
					  public void widgetDefaultSelected(SelectionEvent e) {

					  }

					  @Override
					  public void widgetSelected(SelectionEvent e) {
						  Projeto projeto = projetos.get( projetosExistentes.getSelectionIndex() );
						  projetoAtivo.setSelection( projeto.isAtivo() ) ;
					  }
				  }
		  );

		  Label lblEspaco = new Label(composite, SWT.NULL);
		  lblEspaco.setText("                 ");

		  projetoAtivo = new Button(composite, SWT.CHECK);
		  projetoAtivo.setText("Ativo");

		  return composite ;
	  }
}
