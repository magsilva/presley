package gui.wizard;

import gui.view.Atividade;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

import util.Item;

public class AdicionaAtividadeWizardPage2 extends WizardPage {

	private Text conhecimentoText;
	private Button addConhecimentoButton;
	private Tree arvoreConhecimento;
	private Atividade atividade;
	private ArrayList<TreeItem> conhecimentosSelecionados;
	private util.Tree ontologia;

    public AdicionaAtividadeWizardPage2(ISelection selection, Atividade atividade) {
        super("wizardPage");
        this.atividade=atividade;
        setTitle("Adiciona Atividade Wizard");
        setDescription("Adiciona conhecimentos associados a Atividade.");
        conhecimentosSelecionados = new ArrayList<TreeItem>();
        ontologia = atividade.getViewComunication().getOntologia();
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
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
        layoutTopo.verticalSpacing = 9;
        
        Composite controlsTopo =
            new Composite(controls, SWT.NULL);
        controlsTopo.setLayout(layoutTopo);

        Label label =
            new Label(controlsTopo, SWT.NULL);
        label.setText("Nome do Conhecimento:");

        conhecimentoText = new Text(
            controlsTopo,
            SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(
            GridData.FILL_HORIZONTAL);
        conhecimentoText.setLayoutData(gd);
        conhecimentoText.addModifyListener(
            new ModifyListener() {
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
				String nome=null;
				TreeItem[] treeItem = arvoreConhecimento.getSelection();
				treeItem[0].setChecked(true);
				if (treeItem!=null&&treeItem[0]!=null) {
					TreeItem novoItem = new TreeItem(treeItem[0],treeItem[0].getStyle());
					nome = conhecimentoText.getText();
					if (nome!=null||!nome.equals("")) {
						novoItem.setText(nome);	
					}else{
						return;	
					}
				}
				
				//Adiciona novo nó na Ontologia
				ArrayList<Item> itens = ontologia.localizaFilho(treeItem[0].getText());
				for (Item item : itens) {
					if (item.getPai()==null&&treeItem[0].getParentItem()==null) {
						atividade.getViewComunication().adicionaConhecimento(itens.get(0), nome);
					}else{
						if (!(item.getPai()==null||treeItem[0].getParentItem()==null)) {
							if (item.getPai().getNome().equals(treeItem[0].getParentItem().getText())) {
								atividade.getViewComunication().adicionaConhecimento(itens.get(0), nome);		
							}		
						}
						
					}
				}
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        try{
        	
        	util.Tree conhecimentosModelo = atividade.getViewComunication().getOntologia();
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
        	System.out.println("ERRO ERRO: "+e.getMessage());
        	e.printStackTrace();
		}
        

        setControl(controls);
    }


}
