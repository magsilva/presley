package com.hukarz.presley.client.gui.wizard;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Item;
import com.hukarz.presley.client.gui.view.MensagemAba;


public class AdicionaDesenvolvedorWizardPage2 extends WizardPage {

	private static final Logger logger = Logger.getLogger(AdicionaDesenvolvedorWizardPage2.class);
	private Tree arvoreConhecimento;
	private MensagemAba mensagemAba;
	private Hashtable<String,TreeItem> conhecimentosSelecionados;
	private ArrayList<Conhecimento> conhecimentos;
	private com.hukarz.presley.beans.Tree ontologia;
	private ArrayList<Item> itens;
	private Combo grauConhecimento;
	private Button addConhecimentoButton;
	private AdicionaDesenvolvedorWizardPage page;
	private AdicionaDesenvolvedorWizardPage2 page2;
	private ArrayList<String> itensConhecimentos;
	private ArrayList<Double> listaGraus;
	

    public AdicionaDesenvolvedorWizardPage2(ISelection selection, MensagemAba m) {
        super("wizardPage");
        this.mensagemAba = m;
        setTitle("Adiciona Desenvolvedor Wizard");
        setDescription("Adiciona Desenvolvedor");
        conhecimentosSelecionados = new Hashtable<String,TreeItem>();
        ontologia = mensagemAba.getViewComunication().getOntologia();
        itensConhecimentos = new ArrayList<String>();
        listaGraus = new ArrayList<Double>();
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    public int getGrauConhecimento() {
        return grauConhecimento.getSelectionIndex()+1;
    }
    
      
    public ArrayList<String> pegaConhecimentos(){
    	return itensConhecimentos;
    }
    
    public ArrayList<Double> pegaGraus(){
    	
    	return listaGraus;
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
    	for (Conhecimento conh : this.mensagemAba.getViewComunication().getListaConhecimentos()) {
			tabelaConhecimentos.put(conh.getNome(), conh);
		}
    	
    	conhecimentos = new ArrayList<Conhecimento>();
    	//Preenche a lista de conhecimentos para resposta
    	for (String nome : conhecimentosNomes) {
			Conhecimento conh1 = tabelaConhecimentos.get(nome);
			if (conh1!=null) {
				conhecimentos.add(conh1);
			}
		}
    	
    	return conhecimentos;
    }

    public void createControl(Composite parent) {
    	
    	 Composite controls = new Composite(parent, SWT.NULL);
         FillLayout layoutFillVertical = new FillLayout();
         layoutFillVertical.type = SWT.VERTICAL;
         
         controls.setLayout(layoutFillVertical);
         
         GridLayout layoutTopo = new GridLayout();
         layoutTopo.numColumns = 3;
         layoutTopo.verticalSpacing = 9;
          
         Composite controls2 =
             new Composite(controls, SWT.NULL);
         controls2.setLayout(layoutTopo);
         
        
        Label label2 =
            new Label(controls2, SWT.NULL);
        label2.setText("Grau de Conhecimento:");

        grauConhecimento = new Combo(controls2, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd3 = new GridData(GridData.FILL_HORIZONTAL);
        grauConhecimento.setLayoutData(gd3);
        grauConhecimento.setEnabled(false);
        grauConhecimento.add( "Regular" );
        grauConhecimento.add( "Bom" );
        grauConhecimento.add( "Excelente" );
        
        addConhecimentoButton = new Button(controls2,SWT.ICON);
        addConhecimentoButton.setVisible(true);
        addConhecimentoButton.setLayoutData(gd3);
        addConhecimentoButton.setText("Adicionar");             
        addConhecimentoButton.setEnabled(false);
        
        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Selecione os conhecimentos que ser�o validados:");
        
        try{
        	
        	final com.hukarz.presley.beans.Tree conhecimentosModelo = mensagemAba.getViewComunication().getOntologia();
        	arvoreConhecimento = conhecimentosModelo.constroiArvoreGrafica(controls, SWT.BORDER );
        	arvoreConhecimento.addListener(SWT.Selection, new Listener() {
			
				public void handleEvent(Event e) {
					TreeItem atual = (TreeItem)e.item;
					boolean temFilhos = true;
					String recebeNome = null;
					
					// TODO Auto-generated method stub
					//Verifica se � o raiz, se for, n�o inclui este na lista
					if (!atual.getText().equals(conhecimentosModelo.getRaiz().getConhecimento())) {
														
						itens = mensagemAba.getViewComunication().getOntologia().localizaFilho( (Conhecimento) atual.getData());
						
						for(int i = 0; i < itens.size(); i++){
							
							temFilhos = itens.get(i).temFilhos();
						
							
						}
						
						if (temFilhos) {
							grauConhecimento.setEnabled(false);
							addConhecimentoButton.setEnabled(false);
						} 
						else {
							grauConhecimento.setEnabled(true);
							addConhecimentoButton.setEnabled(true);
						}
					}
			
				}
			});
        				
            
        	addConhecimentoButton.addMouseListener(new MouseListener(){
            	
            	public void mouseDoubleClick(MouseEvent arg0) {
    				// TODO Auto-generated method stub
    				
    			}
    			public void mouseDown(MouseEvent e) {
    				// TODO Auto-generated method stub
    				//Adiciona novo n� na arvore gr�fica
    				
    				TreeItem atual=null;
    				TreeItem[] selecoes=null;
					selecoes = arvoreConhecimento.getSelection();
					atual = (TreeItem)selecoes[0];
					String recebeNome = atual.getText();
					int grauTexto =  getGrauConhecimento();
					Double grauDouble = new Double(grauTexto);
						
					itensConhecimentos.add(recebeNome);
					listaGraus.add(grauDouble);
    				
    			}
    			public void mouseUp(MouseEvent arg0) {
    				// TODO Auto-generated method stub
    				
    			}
    			
            });
        	
        	
            	
        }catch (Exception e) {
			// TODO: handle exception
        	logger.error(e.getMessage());
        	e.printStackTrace();
		}
        
        
             
       
        setControl(controls);
    }


}
