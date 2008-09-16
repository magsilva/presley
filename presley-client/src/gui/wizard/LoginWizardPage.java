package gui.wizard;

import gui.view.Atividade;

import java.security.acl.Group;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import beans.Desenvolvedor;

public class LoginWizardPage extends WizardPage {

	private static final int RESET_ID = IDialogConstants.NO_TO_ALL_ID + 1;
	private Text usernameField;
	private Text passwordField;
	private Text ipField;
	
	private Atividade ativ;
	
	
	private ArrayList<Desenvolvedor> desenvolvedores;

    public LoginWizardPage(ISelection selection, Atividade atividade) {
        super("wizardPage");
        setTitle("LOGIN");
        setDescription("Forneca Login e Senhas.");
        this.ativ = atividade;
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getLogin(){
    	return usernameField.getText();
    }
    
    public String getSenha(){
    	return passwordField.getText();
    }
    
    public String getIP(){
    	return ipField.getText();
    }
    
    private void dialogChanged() {
		// TODO Auto-generated method stub
    }

    
    public void createControl(Composite parent) {
 
 	   
    	try{
    		Composite controls =
                new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            controls.setLayout(layout);
            layout.numColumns = 2;

     	    Label usernameLabel = new Label(controls, SWT.RIGHT);
     	    usernameLabel.setText("Usuario: ");

     	    usernameField = new Text(controls, SWT.SINGLE | SWT.BORDER);
     	    GridData data = new GridData(GridData.FILL_HORIZONTAL);
     	    usernameField.setLayoutData(data);

     	    Label passwordLabel = new Label(controls, SWT.RIGHT);
     	    passwordLabel.setText("Senha: ");

     	    passwordField = new Text(controls, SWT.SINGLE | SWT.PASSWORD | SWT.BORDER);
     	    data = new GridData(GridData.FILL_HORIZONTAL);
     	    passwordField.setLayoutData(data);
     	    
     	    Label ipLabel = new Label(controls, SWT.RIGHT);
     	    ipLabel.setText("IP: ");

     	    ipField = new Text(controls, SWT.SINGLE | SWT.BORDER);
     	    GridData dataIP = new GridData(GridData.FILL_HORIZONTAL);
     	    ipField.setLayoutData(dataIP);
     	    ipField.setMessage("150.165.130.2");
     	    ipField.setText("150.165.130.2");
            setControl(controls);
    	}catch(Exception e ){
    		e.printStackTrace();
    	}
 	    

    }


}
