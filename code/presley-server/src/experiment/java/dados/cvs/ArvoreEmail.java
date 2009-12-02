package dados.cvs;

// http://www.javafree.org/artigo/4157/Introducao-ao-componente-JTree.html

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoProblemaImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoSolucaoImpl;

// TODO: Refatorar

public class ArvoreEmail extends JFrame {

	private ArrayList<Email> emails; 
	private ValidacaoSolucaoImpl  validacaoSolucao;
	private Connection dbConnection;

	private Logger logger;

	// GUI

	private JTextField  pathToMboxFilesTextField;

	private JPanel      topPannel;  
	private JPanel      bottomPanel;  

	private JButton addDevelopersButton;
	private JButton saveThreadsButton;
	private JButton buildThreadsButton;
	private JButton createQuestionFilesButton;


	public ArvoreEmail() {  	  
		super("Browser");  

		emails = null;
		validacaoSolucao = new ValidacaoSolucaoImpl();
		dbConnection = MySQLConnectionFactory.open();
		logger = Logger.getLogger(this.getClass());


		getContentPane().setLayout(new BorderLayout());  

		pathToMboxFilesTextField      = new JTextField(); 

		pathToMboxFilesTextField.setText("C:/Java/Math/Experimento/mbox_experimento/");

		topPannel = new JPanel(new BorderLayout());  
		bottomPanel = new JPanel(new GridLayout(1,1));

		addDevelopersButton = new JButton("Add Developers");
		addDevelopersButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addDevelopersButtonActionPerformed(evt);
			}
		});


		buildThreadsButton = new JButton("Build Threads");
		buildThreadsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				buildThreadsButtonActionPerformed(event);
			}
		});

		saveThreadsButton = new JButton("Save Threads");
		saveThreadsButton.setEnabled(false);
		saveThreadsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				saveThreadsButtonActionPerformed(event);
			}
		});

		createQuestionFilesButton = new JButton("Create .question");
		createQuestionFilesButton.setEnabled(false);
		createQuestionFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				createQuestionFilesButtonActionPerformed(event);
			}
		});


		topPannel.add(pathToMboxFilesTextField, BorderLayout.CENTER);

		bottomPanel.add(addDevelopersButton);
		bottomPanel.add(buildThreadsButton);
		bottomPanel.add(saveThreadsButton);
		bottomPanel.add(createQuestionFilesButton);

		getContentPane().add(topPannel,  BorderLayout.NORTH);  
		getContentPane().add(bottomPanel, BorderLayout.CENTER);  

		this.setSize(600, 150);  
		this.setVisible(true);  
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);  

	}  

	protected void createQuestionFilesButtonActionPerformed(ActionEvent event) {
		try {
			gerarArquivos(pathToMboxFilesTextField.getText(), this.emails);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void buildThreadsButtonActionPerformed(ActionEvent event) {
		buildThreads(pathToMboxFilesTextField.getText());
		saveThreadsButton.setEnabled(true);
		createQuestionFilesButton.setEnabled(true);
	}

	protected void addDevelopersButtonActionPerformed(java.awt.event.ActionEvent evt) {
		varreEmailDesenvolvedoresFrom(pathToMboxFilesTextField.getText());
	}

	protected void saveThreadsButtonActionPerformed(ActionEvent event) {
		saveThreads(this.emails);
	}

	/**
	 * Builds all the threads from mbox files
	 * @param directoryPath The path to mbox files
	 */
	public void buildThreads(String directoryPath) {
		File directory = new File(directoryPath);  
		File[] files = directory.listFiles();
		for (File file : files) {
			try {
				processMbox(file);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	private void processMbox(File file) throws FileNotFoundException, IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);


		logger.debug(file.toString());
		String linha = "";

		Email email = new Email();

		boolean encontrouMessageID	= false,
		encontrouSubject	= false,
		maisReferencias		= false,
		encontrouMensagem	= false,
		encontrouData		= false,
		proximaLinhaSubject = false;

		while( (linha = reader.readLine()) != null ){

			if ( linha.trim().equals("---------------------------------------------------------------------") ||
					linha.trim().equals("To unsubscribe, e-mail: dev-unsubscribe@commons.apache.org") ||
					linha.trim().equals("For additional commands, e-mail: dev-help@commons.apache.org"))
				continue;

			linha = linha.toLowerCase();
			linha = linha.replace('\t', ' ');

			if ( !email.getReferences().isEmpty() && maisReferencias )
				maisReferencias = !linha.contains(":"); 

			if (linha.startsWith("from ") && (linha.contains("@")) ){
				if (encontrouMessageID && encontrouSubject){
					if (!Email.adcionarEmail(emails, email)) 
						emails.add(email);
				}

				encontrouMessageID	= false;
				encontrouSubject	= false;
				encontrouMensagem	= false;
				maisReferencias		= false;
				encontrouData		= false;
				proximaLinhaSubject	= false;

				email = new Email();						   
			} else if ( (!encontrouMessageID && linha.startsWith("message-id: ")) ||
					(encontrouMessageID && email.getMessageID().isEmpty() ) ){ 
				encontrouMessageID = true;

				linha = linha.replaceAll("message-id: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setMessageID(linha);
			} else if (linha.startsWith("in-reply-to: ")){ 
				linha = linha.replaceAll("in-reply-to: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setInReplyTo(linha);
			} else if (linha.startsWith("from: ")){ 
				linha = linha.replaceAll("from:", "");
				linha = retirarCaracteresExtras(linha);
				StringTokenizer st = new StringTokenizer(linha);
				String emailFrom = "";
				while (st.hasMoreTokens())
					emailFrom = st.nextToken();

				if (!emailFrom.equals("jira@apache.org"))
					email.setFrom(emailFrom, dbConnection);
				else{
					email.setFromPorNome( linha.substring(0, linha.indexOf("jira")), dbConnection );
				}							   

			} else if (linha.startsWith("date: ") && !encontrouData){ 
				linha = linha.replaceAll("date: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setData(linha);
				encontrouData = true;
			} else if (linha.startsWith("references: ") || (maisReferencias) ){ 
				linha = linha.replaceAll("references: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setReferences( email.getReferences() + " " + linha);

				maisReferencias = true;
			} else if ((!encontrouSubject && linha.startsWith("subject: ")) ||
					(proximaLinhaSubject && linha.startsWith(" "))){
				encontrouSubject = true;
				proximaLinhaSubject = true;

				linha = linha.replaceAll("subject: ", "");
				email.setSubject( email.getSubject() + " " +linha);
			} else if (encontrouMessageID && encontrouSubject && linha.isEmpty() ){
				encontrouMensagem  = true;
			}

			if (encontrouSubject && proximaLinhaSubject && 
					!email.getSubject().isEmpty() && linha.contains(":") )
				proximaLinhaSubject = false;

			// Encontra a mensagem e a primeira linha da mensagem anterior
			if (encontrouMensagem && !linha.startsWith(">") ){
				email.setMensagem( email.getMensagem() + " " + linha );
			}
		}

		if (encontrouMessageID && encontrouSubject){
			if (!Email.adcionarEmail(emails, email)) 
				emails.add(email);

			encontrouMessageID	= false;
			encontrouSubject	= false;
			encontrouMensagem	= false;
			maisReferencias		= false;
			proximaLinhaSubject = false;
		}
	}


	public void saveThreads(ArrayList<Email> emails) {
		Projeto projeto = new Projeto();
		projeto.setNome("math");
		ValidacaoProblemaImpl validacaoProblema = new ValidacaoProblemaImpl();

		for (Email email : emails) {
			if (email.getFrom().isEmpty())
				continue;

			System.out.println( email.getMessageID() );

			Desenvolvedor desenvolvedor = new Desenvolvedor();
			desenvolvedor.setEmail(email.getFrom());

			Problema problema = new Problema();
			problema.setDesenvolvedorOrigem(desenvolvedor);
			problema.setProjeto(projeto);
			problema.setData( email.getData() ) ;
			problema.setDescricao(email.getSubject());
			problema.setMensagem(email.getMensagem());
			problema.setResolvido(true);
			problema.setTemResposta(false);

			try {
				problema = validacaoProblema.cadastrarProblema(problema);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}

			if (email.getEmailsFilho().size() > 0)
				cadastrarSolucoes(email.getEmailsFilho(), problema);

		}

	}

	private void cadastrarSolucoes(ArrayList<Email> emails, Problema problema ) {
		for (Email email : emails) {
			if (email.getFrom().isEmpty())
				continue;

			Desenvolvedor desenvolvedor = new Desenvolvedor();
			desenvolvedor.setEmail(email.getFrom());

			Solucao solucao = new Solucao();
			solucao.setAjudou(true);
			solucao.setProblema(problema);
			solucao.setData( email.getData() ) ;
			solucao.setMensagem(email.getMensagem());
			solucao.setDesenvolvedor(desenvolvedor);

			try {
				if (email.getEmailsFilho().size()==0){
					validacaoSolucao.cadastrarSolucao(solucao);
				} else {
					cadastrarSolucoes(email.getEmailsFilho(), problema);
					validacaoSolucao.cadastrarSolucao(solucao);
				}
			} catch (ProblemaInexistenteException e) {
				e.printStackTrace();
			} catch (DesenvolvedorInexistenteException e) {
				e.printStackTrace();
			}

		}
	}

	public ArrayList<Email> retornarFilhosSemRepeticao(Email email, String emailQuestion){
		ArrayList<Email> emailsResposta = email.getEmailsFilho();
		ArrayList<Email> emailsExperimento = new ArrayList<Email>();
		for (Email emailResposta : emailsResposta) {
			if ( !emailResposta.getFrom().equals(email.getFrom()) ){
				boolean achei = false;
				for (Email emailExperimento : emailsExperimento) {
					if ( emailResposta.getFrom().equals(emailExperimento.getFrom())){
						achei = true;
						break;						
					}
				}

				if (!achei)
					emailsExperimento.add(emailResposta);
			}
		}

		return emailsExperimento;
	}

	/**
	 * Gera .question
	 * @param base
	 * @param emails
	 * @throws FileNotFoundException
	 */
	public void gerarArquivos(String base, ArrayList<Email> emails) throws FileNotFoundException {
		int count = 1;

		for (Email email : emails) {

			if (email.getFrom().isEmpty() || email.getSubject().contains("re:"))
				continue;

			ArrayList<Email> emailsExperimento = retornarFilhosSemRepeticao(email, email.getFrom());

			if (emailsExperimento.size() > 0){
				String nomeArquivo = base + System.currentTimeMillis()+ "_" + count ;

				PrintWriter arquivoQuestion = new PrintWriter(new 
						FileOutputStream( nomeArquivo + ".question"));

				arquivoQuestion.println( email.getFrom() );
				arquivoQuestion.println( (new SimpleDateFormat("dd/MM/yyyy").format(email.getData()))  );
				arquivoQuestion.println( email.getSubject() );
				arquivoQuestion.println( email.getMensagem() );

				arquivoQuestion.close();

				PrintWriter arquivoEmails = new PrintWriter(new FileOutputStream( nomeArquivo + ".emails"));
				for (Email emailArquivo : emailsExperimento)
					arquivoEmails.println( emailArquivo.getFrom() );
				arquivoEmails.close();

				count += 1;
			}
		}
	}

	public void preencherArvore(ArrayList<Email> emails, DefaultMutableTreeNode no){
		for (Email email : emails) {
			DefaultMutableTreeNode assunto = new DefaultMutableTreeNode( email.getSubject() + " - " + email.getMessageID());
			if (email.getEmailsFilho().size()==0){
				no.add( assunto );
			} else {
				preencherArvore(email.getEmailsFilho(), assunto);
				no.add( assunto );
			}
		}
	}

	public void varreEmailDesenvolvedoresFrom(String base)  {  	        
		File diretorio = new File(base);  
		File[] conteudo = diretorio.listFiles();
		Map<String, String> emails = new HashMap<String, String>(); 

		try {
			// Retira dos arquivos os emails dos remententes
			for (int i=0; i < conteudo.length ; i++) {   

				File file = new File( conteudo[i].getAbsolutePath() );
				FileReader fileReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fileReader);

				Boolean encontrouFrom = true ; 
				String linha = "";

				while( (linha = reader.readLine()) != null ){
					if (encontrouFrom)
						encontrouFrom = !(linha.startsWith("From ") && (linha.contains("@")) );

					if ((!encontrouFrom) && linha.startsWith("From:") ){
						encontrouFrom = true;

						linha = linha.replaceAll("From:", "");

						String nome = "";

						StringTokenizer st = new StringTokenizer(linha);
						while (st.hasMoreTokens()){
							String email = st.nextToken();

							if (email.contains("@")){
								email	= retirarCaracteresExtras(email);
								nome	= retirarCaracteresExtras(nome);

								if (email.contains("jira@apache.org")){
									email = email.replace("jira@apache.org", nome.trim() + "@presley" );
								}

								if (emails.get(nome)==null){
									emails.put(nome, email) ;
								} else {
									String emailNaLista = emails.get(nome);
									if (!emailNaLista.contains(email)){
										emailNaLista += " " + email ;
										emails.put(nome, emailNaLista) ;
									}
								}									   

							} else if (email.contains(","))
								nome = "";
							else 
								nome = nome + email + " ";
						}
					}
				}
			} 


			Statement stm = null;

			try {
				stm = dbConnection.createStatement();

				Set<String> listaEmail = emails.keySet();
				for (Iterator<String> iterator = listaEmail.iterator(); iterator.hasNext();) {
					String nome = iterator.next();
					if (nome.length() > 99 )
						nome = nome.substring(0, 99);
					String email  = emails.get(nome);

					if (email == null)
						continue;

					StringTokenizer st = new StringTokenizer(email);

					while (st.hasMoreTokens()){   
						String emailPrincipal = st.nextToken();
						String SQL = "SELECT email FROM desenvolvedor WHERE listaEmail LIKE '%"+ emailPrincipal +"%' or " +
						" nome = '"+ nome +"'"; 
						ResultSet rs = stm.executeQuery(SQL);

						if (emailPrincipal.length() > 50)
							continue;

						if (!rs.next()){
							// Se o nome não estiver vazio (Os grupos estão formados por nome)

							if (!nome.isEmpty()){
								SQL = "INSERT INTO desenvolvedor (senha, cvsNome, nome, email, listaEmail) " +
								" VALUES ('9', '', '"+ nome +"', '"+ emailPrincipal +"', '"+ email +"')";

								stm.execute(SQL);
								break;
							} else {
								SQL = "INSERT INTO desenvolvedor (senha, cvsNome, nome, email, listaEmail) " +
								" VALUES ('9', '', '"+ nome +"', '"+ emailPrincipal +"', '"+ emailPrincipal +"')";

								stm.execute(SQL);
							}
						} 

					}

					DefaultMutableTreeNode arquivo = new DefaultMutableTreeNode( email + " - " + nome );
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					stm.close();
					dbConnection.close();
				} catch (SQLException onConClose) {
					System.out.println(" Houve erro no fechamento da conexão ");
					onConClose.printStackTrace();	             
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}  

	private String retirarCaracteresExtras(String palavra) {
		palavra = palavra.replace("JIRA", "");
		palavra = palavra.replace("<", "");
		palavra = palavra.replace(">", "");
		palavra = palavra.replace("(", "");
		palavra = palavra.replace(")", "");
		palavra = palavra.replace("[", "");
		palavra = palavra.replace("]", "");
		palavra = palavra.replace(",", "");
		palavra = palavra.replace(";", "");
		palavra = palavra.replace("'", "");
		palavra = palavra.replace("\"", "");

		return palavra;
	}

	public static void main(String args[]) {  
		new ArvoreEmail();
	}  

}