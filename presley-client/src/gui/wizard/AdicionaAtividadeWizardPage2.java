package gui.wizard;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class AdicionaAtividadeWizardPage2 extends WizardPage {

	private Text conhecimentoText;
	private Button addConhecimentoButton;
	private List lista;

    public AdicionaAtividadeWizardPage2(ISelection selection) {
        super("wizardPage");
        setTitle("Adiciona Atividade Wizard");
        setDescription("Adiciona conhecimentos associados a Atividade.");
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    public ArrayList<String> getConhecimentos(){
    	String[] arrays = lista.getItems();
    	ArrayList<String> conhecimentos = new ArrayList<String>();
    	if (arrays!=null){
    		for (String string : arrays) {
    			conhecimentos.add(string);
    		}
    		return conhecimentos;
    	}else{
    		return null;	
    	}
    }

    private void dialogChanged() {
		// TODO Auto-generated method stub

    }

    
    public void createControl(Composite parent) {
    	
        Composite controls =
            new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        controls.setLayout(layout);
        layout.numColumns = 3;
        layout.verticalSpacing = 9;

        Label label =
            new Label(controls, SWT.NULL);
        label.setText("Nome do Conhecimento:");

        conhecimentoText = new Text(
            controls,
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
        
        addConhecimentoButton = new Button(controls,SWT.ICON);
        addConhecimentoButton.setVisible(true);
        addConhecimentoButton.setLayoutData(gd);
        addConhecimentoButton.setText("Adicionar");
        addConhecimentoButton.addMouseListener(new MouseListener(){
        	
        	public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				String c = conhecimentoText.getText();
				System.out.println("Texto: "+c);
				lista.add(c);
			}
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
        });
        Label labelLista =
            new Label(controls, SWT.NULL);
        labelLista.setText("Conhecimentos Adicionados:");
        lista = new List(controls, SWT.V_SCROLL | SWT.BORDER);
		lista.setVisible(true);

        setControl(controls);
    }


}
