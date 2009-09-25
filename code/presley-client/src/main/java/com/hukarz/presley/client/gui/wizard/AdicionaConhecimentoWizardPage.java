package com.hukarz.presley.client.gui.wizard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.client.gui.view.Dominio;


public class AdicionaConhecimentoWizardPage extends WizardPage {

	private static final Logger logger = Logger.getLogger(AdicionaConhecimentoWizardPage.class);
	private Dominio dominio;
	private Text nomeConhecimentoText;
	private Text descricaoConhecimentoText;
	private Button addConhecimentoButton;
	private Tree arvoreConhecimento;
	private ArrayList<TreeItem> conhecimentosSelecionados;
	private ArrayList<String> nomesNosAdicionado;
	private Map<Conhecimento, Conhecimento> conhecimentoFilhoPai;

    public AdicionaConhecimentoWizardPage(ISelection selection, Dominio dominio) {
        super("wizardPage");
        setTitle("Adiciona Conhecimento Wizard");
        setDescription("Adiciona uma novo Conhecimento.");
        this.dominio = dominio;
        conhecimentosSelecionados = new ArrayList<TreeItem>();
        nomesNosAdicionado = new ArrayList<String>();
        
        conhecimentoFilhoPai = new HashMap<Conhecimento, Conhecimento>();
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getNomeConhecimento() {
        return nomeConhecimentoText.getText();
    }
    
    public String getDescricaoConhecimento() {
        return descricaoConhecimentoText.getText();
    }
    
    public ArrayList<String> getConhecimentos(){
    	return nomesNosAdicionado;
    }
    
    public Map<Conhecimento, Conhecimento> conhecimentoFilhoPai(){
    	return conhecimentoFilhoPai;
    }

    
    private void dialogChanged() {
		// TODO Auto-generated method stub
    }

    
    public void createControl(Composite parent) {
    	Composite controls = new Composite(parent, SWT.NULL);
        FillLayout layoutFillVertical = new FillLayout();
        layoutFillVertical.type = SWT.VERTICAL;
        
        controls.setLayout(layoutFillVertical);
        
        GridLayout layoutTopo = new GridLayout();
        layoutTopo.numColumns = 3;
        layoutTopo.verticalSpacing = 5;

        
        Composite controlsTopo = new Composite(controls, SWT.NULL);
        controlsTopo.setLayout(layoutTopo);

        Label label = new Label(controlsTopo, SWT.NULL);
        label.setText("Nome do Conhecimento:");

        nomeConhecimentoText = new Text( controlsTopo, SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData( GridData.FILL_HORIZONTAL);
        nomeConhecimentoText.setLayoutData(gd);
        nomeConhecimentoText.addModifyListener( new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });
        
        addConhecimentoButton = new Button(controlsTopo,SWT.ICON);
        addConhecimentoButton.setVisible(true);
        addConhecimentoButton.setLayoutData(gd);
        addConhecimentoButton.setText("Adicionar");
        addConhecimentoButton.addMouseListener(new MouseListener(){
        	
        	public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				//Adiciona novo nó na arvore gráfica
				Conhecimento conhecimentoPai=null, conhecimentoFilho=null;
				TreeItem[] treeItem = arvoreConhecimento.getSelection();
				treeItem[0].setChecked(true);
				if (treeItem[0]!=null) {
					conhecimentoPai = new Conhecimento();
					conhecimentoPai.setNome( treeItem[0].getText() ) ;
				}
				
				if (treeItem!=null && treeItem[0]!=null) {
					TreeItem novoItem = new TreeItem(treeItem[0],treeItem[0].getStyle());
					conhecimentoFilho = new Conhecimento();
					conhecimentoFilho.setNome( nomeConhecimentoText.getText() );
					if (null != conhecimentoFilho.getNome() 
							&& !conhecimentoFilho.getNome().equals("")) {
						novoItem.setText(conhecimentoFilho.getNome());	
						nomesNosAdicionado.add(conhecimentoFilho.getNome());
						conhecimentoFilhoPai.put(conhecimentoFilho, conhecimentoPai);
					} else{
						return;	
					}
				}
				
				 
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        
        try{
        	arvoreConhecimento = dominio.getViewComunication().getArvoreGraficaDeConhecimentos(controls, SWT.BORDER | SWT.CHECK);
        	arvoreConhecimento.addListener(SWT.Selection, new Listener() {
			
				public void handleEvent(Event e) {
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
        	logger.error(e.getMessage());
        	e.printStackTrace();
		}

            
        setControl(controls);
    }


}
