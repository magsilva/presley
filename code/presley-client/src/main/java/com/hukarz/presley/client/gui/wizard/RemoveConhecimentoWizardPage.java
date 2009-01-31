package com.hukarz.presley.client.gui.wizard;


import java.util.ArrayList;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Item;
import com.hukarz.presley.client.gui.view.Atividade;



public class RemoveConhecimentoWizardPage extends WizardPage {
	private static final Logger logger = Logger.getLogger(RemoveConhecimentoWizardPage.class);

	private Atividade atividade;
	private Text descricaoConhecimentoText;
	private Button remConhecimentoButton;
	private Tree arvoreConhecimento;
	private ArrayList<TreeItem> conhecimentosSelecionados;
	private com.hukarz.presley.beans.Tree ontologia;
	
	


    public RemoveConhecimentoWizardPage(ISelection selection, Atividade atividade) {
        super("wizardPage");
        setTitle("Remover Conhecimento Wizard");
        setDescription("Remover um Conhecimento.");
        this.atividade = atividade;
        conhecimentosSelecionados = new ArrayList<TreeItem>();
        ontologia = atividade.getViewComunication().getOntologia();

    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

     
    public String getDescricaoConhecimento() {
        return descricaoConhecimentoText.getText();
    }
    
    public ArrayList<String> getConhecimentos(){
    	ArrayList<String> conhecimentosNomes = new ArrayList<String>();
    	for (TreeItem conhecimento : conhecimentosSelecionados) {
			conhecimentosNomes.add(conhecimento.getText());
		}
    	
    	if (conhecimentosNomes.isEmpty()) {
			return null;
		}
    	
    	return conhecimentosNomes;
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
        layoutTopo.numColumns = 3;
        layoutTopo.verticalSpacing = 5;

        
        Composite controlsTopo =
            new Composite(controls, SWT.NULL);
        controlsTopo.setLayout(layoutTopo);

        GridData gd = new GridData(
                GridData.FILL_HORIZONTAL);
        
        remConhecimentoButton = new Button(controlsTopo,SWT.ICON);
        remConhecimentoButton.setVisible(true);
        remConhecimentoButton.setLayoutData(gd);
        remConhecimentoButton.setText("Remover");
        remConhecimentoButton.addMouseListener(new MouseListener(){
        	
        	public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				//Remove nó na arvore gráfica
				TreeItem[] treeItem = arvoreConhecimento.getSelection();
											
				//Remove nó na Ontologia
				ArrayList<Item> itens = ontologia.localizaFilho( (Conhecimento) treeItem[0].getData());
				for (Item item : itens) {
					item.getPai().removeFilho( (Conhecimento) treeItem[0].getData());
				} 
				
				treeItem[0].dispose();
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        try{
        	
        	com.hukarz.presley.beans.Tree conhecimentosModelo = atividade.getViewComunication().getOntologia();
        	arvoreConhecimento = conhecimentosModelo.constroiArvoreGrafica(controls, SWT.BORDER | SWT.CHECK);
        	arvoreConhecimento.addListener(SWT.Selection, new Listener() {
			
				public void handleEvent(Event e) {
					// TODO Auto-generated method stub
					TreeItem atual=null;
					if (e.detail==SWT.CHECK) {
						atual = (TreeItem)e.item;
						if (atual.getChecked()) {
							conhecimentosSelecionados.add(atual);	
						}
						for (TreeItem pai = atual.getParentItem(); pai!=null; pai = pai.getParentItem()) {
								pai.setChecked(true);	
								conhecimentosSelecionados.add(pai);
						}
					}
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
