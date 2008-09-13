package gui.wizard;

import java.awt.BorderLayout;
import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class RemoveAtividadeWizardPage extends WizardPage {

	private List listaAtividadesDisponiveis;
	private List listaAtividadesRemovidas;
	private Button remove;
	private Button restaura;
	private ArrayList<String> atividades;

    public RemoveAtividadeWizardPage(ISelection selection) {
        super("wizardPage");
        setTitle("Remove Atividade Wizard");
        setDescription("Selecione as atividades que serão removidas.");
    }

    public void setAtividades(ArrayList<String> atividades) {
		this.atividades = atividades;
	}



	private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public ArrayList<String> getAtividadeRemovidas() {
    	String[] nomes = listaAtividadesRemovidas.getItems();
    	ArrayList<String> atividadesRemovidas = new ArrayList<String>();
    	for (String string : nomes) {
			atividadesRemovidas.add(string);
		}
    	return atividadesRemovidas;
    }

    private void dialogChanged() {
		// TODO Auto-generated method stub
    }

    
    public void createControl(Composite parent) {
        Composite controls = new Composite(parent, SWT.NULL);
        FillLayout layoutFillHorizontal = new FillLayout();
        layoutFillHorizontal.type = SWT.HORIZONTAL;
        FillLayout layoutFillVertical = new FillLayout();
        layoutFillVertical.type = SWT.VERTICAL;
        GridLayout layout = new GridLayout();
        controls.setLayout(layout);
        layout.numColumns = 1;
        layout.verticalSpacing = 9;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Selecione as atividades:");
        
        Composite controlsListas = new Composite(controls, SWT.NULL);
        controlsListas.setLayout(layoutFillHorizontal);

        listaAtividadesDisponiveis = new List(controlsListas,SWT.BORDER | SWT.MULTI);
        for (String a : atividades) {
        	listaAtividadesDisponiveis.add(a);	
		}
        Composite controlsButtons = new Composite(controlsListas, SWT.NULL);
        controlsButtons.setLayout(layoutFillVertical);
        remove = new Button(controlsButtons,SWT.ICON);
        remove.setVisible(true);
        remove.setText("Remover >>");
        remove.addMouseListener(new MouseListener(){
        	
        	public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				String[] selecionados = listaAtividadesDisponiveis.getSelection();
				if (selecionados!=null){
					String[] removidos = listaAtividadesRemovidas.getItems();
					ArrayList<String> arrayRemovidos = new ArrayList<String>();
					for (String r : removidos) {
						arrayRemovidos.add(r);
					}
					for (String selecionado : selecionados) {
						listaAtividadesDisponiveis.remove(selecionado);
						if (!arrayRemovidos.contains(selecionado)) {
							listaAtividadesRemovidas.add(selecionado);		
						}
					}
				
				}else{
					
				}
					
				
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        
        restaura = new Button(controlsButtons,SWT.ICON);
        restaura.setVisible(true);
        restaura.setText("<< Restaurar");
        restaura.addMouseListener(new MouseListener(){
        	
        	public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				String[] restaurados = listaAtividadesRemovidas.getSelection();
				if (restaurados!=null){
					String[] rest = listaAtividadesDisponiveis.getItems();
					ArrayList<String> arrayRestaurados = new ArrayList<String>();
					for (String r : rest) {
						arrayRestaurados.add(r);
					}
					for (String selecionado : restaurados) {
						listaAtividadesRemovidas.remove(selecionado);
						if (!arrayRestaurados.contains(selecionado)) {
							listaAtividadesDisponiveis.add(selecionado);
						}
					}
				}else{
					
				}
				
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        listaAtividadesRemovidas = new List(controlsListas,SWT.BORDER | SWT.MULTI);
        
            
            setControl(controls);
    }
}