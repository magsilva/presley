package tools.threader;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.hukarz.presley.beans.Desenvolvedor;

public class ThreaderGui extends JFrame {

	private Threader threader;
	
	static final int MAX_NAME_SIZE = 99;
	
	// GUI

	private JButton buildThreadsButton;
	private JButton createQuestionFilesButton;  
	private JButton findDevelopersButton;
	private JButton saveThreadsButton;
	
	private JTextField pathToMboxFilesTextField;
	
	private JPanel topPannel;
	private JPanel bottomPanel;
	
	public ThreaderGui() {  	  
		super("Browser");  

		this.threader = new Threader();

		// GUI

		pathToMboxFilesTextField      = new JTextField();
		pathToMboxFilesTextField.setText("path to mbox files...");

		topPannel = new JPanel(new BorderLayout());  
		bottomPanel = new JPanel(new GridLayout(1,1));

		
		buildThreadsButton = new JButton("Build Threads");
		buildThreadsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				buildThreadsButtonActionPerformed();
			}
		});
		
		findDevelopersButton = new JButton("Find Developers");
		findDevelopersButton.setEnabled(false);
		findDevelopersButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				findDevelopersButtonActionPerformed();
			}
		});

		saveThreadsButton = new JButton("Save Threads");
		saveThreadsButton.setEnabled(false);
		saveThreadsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveThreadsButtonActionPerformed();
			}
		});

		createQuestionFilesButton = new JButton("Create .question");
		createQuestionFilesButton.setEnabled(false);
		createQuestionFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				createQuestionFilesButtonActionPerformed();
			}
		});

		topPannel.add(pathToMboxFilesTextField, BorderLayout.CENTER);

		bottomPanel.add(buildThreadsButton);
		bottomPanel.add(findDevelopersButton);
		bottomPanel.add(saveThreadsButton);
		bottomPanel.add(createQuestionFilesButton);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(topPannel,  BorderLayout.NORTH);  
		getContentPane().add(bottomPanel, BorderLayout.CENTER);  

		this.setSize(550, 95);  
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	protected void buildThreadsButtonActionPerformed() {
		threader.buildThreads(this.pathToMboxFilesTextField.getText());
		this.saveThreadsButton.setEnabled(true);
		this.createQuestionFilesButton.setEnabled(true);
		this.findDevelopersButton.setEnabled(true);
		
		int userOption = JOptionPane.showConfirmDialog(null, 
				"Exibir threads no console?", "Exibir threads no console?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	
		if (JOptionPane.YES_OPTION == userOption) {
			this.showThreads();
		}
		
	}	

	protected void createQuestionFilesButtonActionPerformed() {
		try {
			threader.createDotQuestionFiles(pathToMboxFilesTextField.getText());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void display() {
		this.setVisible(true);
	}

	protected void findDevelopersButtonActionPerformed() {
		threader.findDevelopers();
		
		int userOption = JOptionPane.showConfirmDialog(null, 
				"Exibir desenvolvedores no console?", "Exibir desenvolvedores no console?", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	
		if (JOptionPane.YES_OPTION == userOption) {
			this.showDevelopers();
		} else {
			this.saveDevelopers();
		}
	}

	protected void saveThreadsButtonActionPerformed() {
		threader.saveThreads();
	}

	/**
	 * Show each child from the thread
	 * @param children Emails within a thread
	 * @param level Identation level
	 */
	private void showChildren(ArrayList<Email> children, int level) {
		StringBuilder identation = new StringBuilder();
		for (int i = 0; i < level; i++) {
			identation.append("\t");
		}

		for (Email email : children) {
			System.out.println(identation.toString() + email.getSubject());
			System.out.println(identation.toString() + email.getDesenvolvedor().getEmail());
			if (email.getEmailsFilho().size() > 0) {
				showChildren(email.getEmailsFilho(), level + 1);
			}
		}
	}

	/**
	 * Show all developers in the console 
	 */
	void showDevelopers() {
		Collection<Desenvolvedor> developers = threader.getDevelopers();
		for (Desenvolvedor desenvolvedor : developers) {
			System.out.println("E-mail: " + desenvolvedor.getEmail() + 
					" Nome: " + desenvolvedor.getNome());
		}
	}

	/**
	 * Save all developers in the Database 
	 * @throws SQLException 
	 */
	void saveDevelopers() {
		Collection<Desenvolvedor> developers = threader.getDevelopers();
		for (Desenvolvedor desenvolvedor : developers)
			threader.saveDeveloper(desenvolvedor);
	}
	
	/**
	 * Show all threads in the console 
	 */
	void showThreads() {
		ArrayList<Email> threads = threader.getThreads();
		for (Email email : threads) {
			System.out.println(email.getSubject());
			System.out.println(email.getDesenvolvedor().getEmail());
			showChildren(email.getEmailsFilho(), 1);
		}
	}

	public static void main(String args[]) {  
		ThreaderGui arvoreEmail = new ThreaderGui();
		arvoreEmail.display();
	}  

}