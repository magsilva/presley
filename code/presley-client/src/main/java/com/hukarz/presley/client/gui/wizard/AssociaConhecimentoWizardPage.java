package com.hukarz.presley.client.gui.wizard;

import java.util.ArrayList;
import java.util.Hashtable;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.client.gui.view.Atividade;



public class AssociaConhecimentoWizardPage extends WizardPage {

	private Tree arvoreConhecimento;
	private Atividade atividade;
	private Hashtable<String,TreeItem> conhecimentosSelecionados;
	private ArrayList<Conhecimento> conhecimentos;
	private com.hukarz.presley.beans.Tree ontologia;

    public AssociaConhecimentoWizardPage(ISelection selection, Atividade atividade) {
        super("wizardPage");
        this.atividade=atividade;
        setTitle("Associa Conhecimento a Atividade Wizard");
        setDescription("Associa Conhecimento a Atividade.");
        conhecimentosSelecionados = new Hashtable<String,TreeItem>();
        ontologia = atividade.getViewComunication().getOntologia();
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    public ArrayList<Conhecimento> getConhecimentos(){
    	ArrayList<String> conhecimentosNomes = new ArrayList<String>();
    	//Atualiza a lista de Conhecimetos existentes
    	//Armazena os nomes dos conheciementos selecionados na arvore gr�fica
    	for (TreeItem conhecimento : conhecimentosSelecionados.values()) {
			conhecimentosNomes.add(conhecimento.getText());
		}
    	//Se a lista de nomes de conhecimentos for vazia, ent�o retorne null
    	if (conhecimentosNomes.isEmpty()) {
			return null;
		}
    	//Fazendo um mapeamento entre o nome dos conhecimentos e o conhecimento
    	//para agilizar a recupera��o mais a frente
    	Hashtable<String,Conhecimento> tabelaConhecimentos = new Hashtable<String, Conhecimento>();
    	
    	//Fazendo um mapeamento entre o nome dos conhecimentos e o conhecimento
    	//para agilizar a recupera��o mais a frente
    	for (Conhecimento conh : this.atividade.getViewComunication().getListaConhecimentos()) {
    		System.out.println(">>>>>>> CRIANDO TABELA >>>>>>>>>> " + conh.getNome());
    		tabelaConhecimentos.put(conh.getNome(), conh);
		}
    	
    	conhecimentos = new ArrayList<Conhecimento>();
    	
    	//Preenche a lista de conhecimentos para resposta
    	for (String nome : conhecimentosNomes) {
    		System.out.println(" >>>>>>>>>>>>>>>> KEY >>>>>>>>>>>> " + nome);
			Conhecimento conh1 = tabelaConhecimentos.get(nome);
			if (conh1!=null) {
				conhecimentos.add(conh1);
			}
		}
    	
    	return conhecimentos;
    }

    private void dialogChanged() {
		// TODO Auto-generated method stub

    }

    
    public void createControl(Composite parent) {
    	
        Composite controls =
            new Composite(parent, SWT.NULL);
        FillLayout layoutFillVertical = new FillLayout();
        layoutFillVertical.type = SWT.VERTICAL;
        
        controls.setLayout(layoutFillVertical);
        
        GridLayout layoutTopo = new GridLayout();
        layoutTopo.numColumns = 1;
        layoutTopo.verticalSpacing = 9;
        
        Composite controlsTopo =
            new Composite(controls, SWT.NULL);
        controlsTopo.setLayout(layoutTopo);

        Label label =
            new Label(controlsTopo, SWT.NULL);
        label.setText("Selecione os conhecimentos que ser�o associados:");
        
        try{
        	
        	final com.hukarz.presley.beans.Tree conhecimentosModelo = atividade.getViewComunication().getOntologia();
        	arvoreConhecimento = conhecimentosModelo.constroiArvoreGrafica(controls, SWT.BORDER | SWT.CHECK);
        	arvoreConhecimento.addListener(SWT.Selection, new Listener() {
			
				public void handleEvent(Event e) {
					// TODO Auto-generated method stub
					TreeItem atual=null;
					if (e.detail==SWT.CHECK) {
						atual = (TreeItem)e.item;
						if (atual.getChecked()) {
							//Verifica se � o raiz, se for, n�o inclui este na lista
							if (!atual.getText().equals(conhecimentosModelo.getRaiz().getConhecimento())) {
									conhecimentosSelecionados.put(atual.getText(), atual);	
									
							}	
						}
						/*for (TreeItem pai = atual.getParentItem(); pai!=null; pai = pai.getParentItem()) {
								pai.setChecked(true);	
								//Verifica se � o raiz, se for, n�o inclui este na lista
								if (!pai.getText().equals(conhecimentosModelo.getRaiz().getNome())) {
										conhecimentosSelecionados.put(pai.getText(), pai);
								}
						}
						*/
					}
				}
			
			});
            
            	
        }catch (Exception e) {
			// TODO: handle exception
        	System.out.println("ERRO ERRO: "+e.getMessage());
        	e.printStackTrace();
		}
        

        setControl(controls);
    }


}
