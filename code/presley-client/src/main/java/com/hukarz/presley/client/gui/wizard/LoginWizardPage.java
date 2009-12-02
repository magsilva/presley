package com.hukarz.presley.client.gui.wizard;


import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hukarz.presley.client.gui.view.MensagemAba;


public class LoginWizardPage extends WizardPage {

	private Text usernameField;
	private Text passwordField;
	private Text ipField;
	
	
	
    public LoginWizardPage(ISelection selection, MensagemAba m) {
        super("wizardPage");
        setTitle("LOGIN");
        setDescription("Forneca Login e Senhas.");
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
     	    usernameField.setText("");
     	    usernameField.setLayoutData(data);

     	    final Label passwordLabel = new Label(controls, SWT.RIGHT);
     	    passwordLabel.setText("Senha: ");

     	    passwordField = new Text(controls, SWT.SINGLE 
     	    		| SWT.PASSWORD | SWT.BORDER);
     	    data = new GridData(GridData.FILL_HORIZONTAL);
     	    passwordField.setText("");
     	    passwordField.setLayoutData(data);
     	    
     	    Label ipLabel = new Label(controls, SWT.RIGHT);
     	    ipLabel.setText("IP: ");

     	    ipField = new Text(controls, SWT.SINGLE | SWT.BORDER);
     	    GridData dataIP = new GridData(GridData.FILL_HORIZONTAL);
     	    ipField.setLayoutData(dataIP);
     	    ipField.setText("150.165.130.2");
            setControl(controls);
    	}catch(Exception e ){
    		e.printStackTrace();
    	}
 	    

    }


}
