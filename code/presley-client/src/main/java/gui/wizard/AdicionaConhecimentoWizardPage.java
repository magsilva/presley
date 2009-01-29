package gui.wizard;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gui.view.Dominio;

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


public class AdicionaConhecimentoWizardPage extends WizardPage {

	private Dominio dominio;
	private Text nomeConhecimentoText;
	private Text descricaoConhecimentoText;
	private Button addConhecimentoButton;
	private Tree arvoreConhecimento;
	private ArrayList<TreeItem> conhecimentosSelecionados;
	private com.hukarz.presley.beans.Tree ontologia;
	private ArrayList<String> nomesNosAdicionado;
	private String paiConhecimento;
	private Map<Conhecimento, Conhecimento> conhecimentoFilhoPai;

    public AdicionaConhecimentoWizardPage(ISelection selection, Dominio dominio) {
        super("wizardPage");
        setTitle("Adiciona Conhecimento Wizard");
        setDescription("Adiciona uma novo Conhecimento.");
        this.dominio = dominio;
        conhecimentosSelecionados = new ArrayList<TreeItem>();
        ontologia = dominio.getViewComunication().getOntologia();
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
					if (conhecimentoFilho.getNome()!=null||!conhecimentoFilho.getNome().equals("")) {
						novoItem.setText(conhecimentoFilho.getNome());	
						nomesNosAdicionado.add(conhecimentoFilho.getNome());
						conhecimentoFilhoPai.put(conhecimentoFilho, conhecimentoPai);
					}else{
						return;	
					}
				}
				
				 
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        
        try{
        	
        	com.hukarz.presley.beans.Tree conhecimentosModelo = dominio.getViewComunication().getOntologia();
        	arvoreConhecimento = conhecimentosModelo.constroiArvoreGrafica(controls, SWT.BORDER | SWT.CHECK);
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
        	System.out.println("ERRO ERRO: "+e.getMessage());
        	e.printStackTrace();
		}

            
        setControl(controls);
    }


}