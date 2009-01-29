package com.hukarz.presley.client.gui.wizard;


import java.sql.Date;
import java.util.ArrayList;

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

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.client.gui.view.Atividade;


public class AdicionaAtividadeWizardPage extends WizardPage {

	private Atividade atividade;
	private Text nomeAtividadeText;
	private Combo listaDesenvolvedores;
	private Combo listaSupervisores;
	private Text dataInicio;
	private Text dataFim;
	private Button naoConcluidoradioButton;
	private Button concluidoradioButton;
	private List grupoButoesStatus;
	
	
	private ArrayList<Desenvolvedor> desenvolvedores;

    public AdicionaAtividadeWizardPage(ISelection selection, Atividade atividade) {
        super("wizardPage");
        setTitle("Adiciona Atividade Wizard");
        setDescription("Adiciona uma nova Atividade.");
        this.atividade = atividade;
    }

    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }

    public String getNomeAtividade() {
        return nomeAtividadeText.getText();
    }
    
    public Desenvolvedor getDesenvolvedor(){
    	Desenvolvedor desenvolvedorRetorno = null; 
    	String selecao = listaDesenvolvedores.getText();
    	for (Desenvolvedor desenvolvedor : desenvolvedores) {
			if (desenvolvedor.getNome().equals(selecao)) {
				desenvolvedorRetorno = desenvolvedor;
				break;
			}
		}

    	return desenvolvedorRetorno;

    }
    
    public Desenvolvedor getNomeSupervisor(){
    	Desenvolvedor supervisorRetorno = null; 
    	String selecao = listaSupervisores.getText();
    	for (Desenvolvedor desenvolvedor : desenvolvedores) {
			if (desenvolvedor.getNome().equals(selecao)) {
				supervisorRetorno = desenvolvedor;
				break;
			}
		}

    	return supervisorRetorno;
    }
    
    public Date getDataInicio(){
    	String dataString = this.dataInicio.getText();
    	Date dataI = Date.valueOf(dataString);
    	return dataI;
    }
    
    public Date getDataFim(){
    	String dataString = this.dataFim.getText();
    	Date dataF = Date.valueOf(dataString);
    	return dataF;
    }
    
    public boolean getStatus(){
    	String selecao = this.grupoButoesStatus.getSelection()[0];
    	if (selecao.equals("Concluído"))
			return true;
		else
			return false;
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
        label.setText("Nome da Atividade:");

        nomeAtividadeText = new Text(
            controls,
            SWT.BORDER | SWT.SINGLE);
        GridData gd = new GridData(
            GridData.FILL_HORIZONTAL);
        nomeAtividadeText.setLayoutData(gd);
        nomeAtividadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelExplicativo = new Label(controls, SWT.NULL);
        labelExplicativo.setText("digite o nome da Atividade");

        Label labelDesenvolvedores =
            new Label(controls, SWT.NULL);
        labelDesenvolvedores.setText("Desenvolvedor:");

        try{
        desenvolvedores = this.atividade.getViewComunication().getListaDesenvolvedores();
        ArrayList<String> nomesDesenvolvedores = new ArrayList<String>();
        if (desenvolvedores!=null) {
			for (Desenvolvedor desenvolvedor : desenvolvedores) {
				nomesDesenvolvedores.add(desenvolvedor.getNome());
			}
		}
        String[] nomes = new String[nomesDesenvolvedores.size()];
        nomesDesenvolvedores.toArray(nomes);
        listaDesenvolvedores = new Combo(controls,
                SWT.BORDER | SWT.SINGLE);
        GridData gdDesenvolvedores = new GridData(
            GridData.FILL_HORIZONTAL);
        listaDesenvolvedores.setItems(nomes);
        listaDesenvolvedores.setText(nomes[0]);
        nomeAtividadeText.setLayoutData(gdDesenvolvedores);
        nomeAtividadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelDesenvolvedorExplicativo = new Label(controls, SWT.NULL);
        labelDesenvolvedorExplicativo.setText("escolha o desenvolvedor");
        
        Label labelSupervisor =
            new Label(controls, SWT.NULL);
        labelSupervisor.setText("Supervisor:");

        listaSupervisores = new Combo(controls,
                SWT.BORDER | SWT.SINGLE);
        GridData gdSupervisor = new GridData(
            GridData.FILL_HORIZONTAL);
        listaSupervisores.setItems(nomes);
        listaSupervisores.setText(nomes[0]);
        nomeAtividadeText.setLayoutData(gdSupervisor);
        nomeAtividadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });
        Label labelSupervisorExplicativo = new Label(controls, SWT.NULL);
        labelSupervisorExplicativo.setText("escolha o supervisor");
        

        Label labelDataInicio =
            new Label(controls, SWT.NULL);
        labelDataInicio.setText("Data Inicio:");

        dataInicio = new Text(controls,
                SWT.BORDER | SWT.SINGLE);
        GridData gdDataIni = new GridData(
            GridData.FILL_HORIZONTAL);
        nomeAtividadeText.setLayoutData(gdDataIni);
        nomeAtividadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelDataIExplicativo = new Label(controls, SWT.NULL);
        labelDataIExplicativo.setText("Formato: (AAAA-MM-DD)");
        
        Label labelDataFim =
            new Label(controls, SWT.NULL);
        labelDataFim.setText("Data Fim:");

        dataFim = new Text(controls,
                SWT.BORDER | SWT.SINGLE);
        GridData gdDataFim = new GridData(
            GridData.FILL_HORIZONTAL);
        nomeAtividadeText.setLayoutData(gdDataFim);
        nomeAtividadeText.addModifyListener(
            new ModifyListener() {
                public void modifyText(
                        ModifyEvent e) {
                    dialogChanged();
                }
             });

        Label labelDataFExplicativo = new Label(controls, SWT.NULL);
        labelDataFExplicativo.setText("Formato: (AAAA-MM-DD)");
        
        Label labelConcluida =
            new Label(controls, SWT.NULL);
        labelConcluida.setText("Status:");

        grupoButoesStatus =  new List(controls,SWT.BORDER | SWT.SINGLE);
        GridData gdGroup = new GridData(
            GridData.FILL_VERTICAL);
        grupoButoesStatus.setLayoutData(gdGroup);
        grupoButoesStatus.add("Concluído");
        grupoButoesStatus.add("Não Concluído");
        
        Label labelStatusExplicativo = new Label(controls, SWT.NULL);
        labelStatusExplicativo.setText("concluida ou não");

        }catch(Exception e){
        	e.printStackTrace();
        }

        setControl(controls);
    }


}
