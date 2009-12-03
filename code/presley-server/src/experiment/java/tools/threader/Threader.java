package tools.threader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Threader {
	private DB db;
	private Set<String> developers;

	private ArrayList<Email> threads;
	public Threader() {
		this.db = new DB();
		this.threads = null;
		this.developers = null;
	}
	
	/**
	 * Builds threads in memory
	 * @param directoryPath Path to directory containing the mbox files
	 */
	public void buildThreads(String directoryPath) {
		this.threads = new ArrayList<Email>();
	
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
	
	/**
	 * Create .question files
	 */
	public void createDotQuestionFiles(String base) throws FileNotFoundException {
		int count = 1;
	
		for (Email thread : this.threads) {
	
			if (thread.getFrom().isEmpty() || thread.getSubject().contains("re:")) {
				continue;
			}
	
			ArrayList<Email> emailsExperimento = thread.retornarFilhosSemRepeticao();
	
			if (emailsExperimento.size() > 0){
				String nomeArquivo = base + System.currentTimeMillis()+ "_" + count ;
	
				PrintWriter arquivoQuestion = new PrintWriter(new 
						FileOutputStream(nomeArquivo + ".question"));
	
				arquivoQuestion.println(thread.getFrom());
				arquivoQuestion.println((new SimpleDateFormat("dd/MM/yyyy").format(thread.getData()))  );
				arquivoQuestion.println(thread.getSubject());
				arquivoQuestion.println(thread.getMensagem());
	
				arquivoQuestion.close();
	
				PrintWriter arquivoEmails = new PrintWriter(new FileOutputStream( nomeArquivo + ".emails"));
				for (Email emailArquivo : emailsExperimento) {
					arquivoEmails.println( emailArquivo.getFrom() );
				}
				arquivoEmails.close();
	
				count += 1;
			}
		}
	}

	/**
	 * Show each child from the thread
	 * @param children Emails within a thread
	 */
	private void findChildrenDevelopers(ArrayList<Email> children) {
		for (Email email : children) {
			this.developers.add(email.getFrom());
			if (email.getEmailsFilho().size() > 0) {
				findChildrenDevelopers(email.getEmailsFilho());
			}
		}
	}
	
	public void findDevelopers() {
		this.developers = new HashSet<String>();
		for (Email thread : this.threads) {
			this.developers.add(thread.getFrom());
			findChildrenDevelopers(thread.getEmailsFilho());
		}
	}

	public Set<String> getDevelopers() {
		return developers;
	}

	public ArrayList<Email> getThreads() {
		return threads;
	}
	
	// TODO: trocar a implementação deste método.
	protected void processMbox(File file) throws FileNotFoundException, IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(fileReader);
		String LINE_DASHES = "---------------------------------------------------------------------";
		String LINE_UNSUBSCRIBE_MESSAGE = "To unsubscribe, e-mail: dev-unsubscribe@commons.apache.org";
		String LINE_ADDITIONAL_COMMANDS = "For additional commands, e-mail: dev-help@commons.apache.org"; 
		String linha = "";
	
		Email email = new Email();
	
		boolean encontrouMessageID	= false;
		boolean encontrouSubject	= false;
		boolean maisReferencias		= false;
		boolean encontrouMensagem	= false;
		boolean encontrouData		= false;
		boolean proximaLinhaSubject = false;
	
		while( (linha = reader.readLine()) != null ){
			linha = linha.trim();
	
			if ( linha.equals(LINE_DASHES) || linha.equals(LINE_UNSUBSCRIBE_MESSAGE) 
					|| linha.equals(LINE_ADDITIONAL_COMMANDS)) {
				continue;
			}
	
			linha = linha.toLowerCase();
			linha = linha.replace('\t', ' ');
	
			if ( !email.getReferences().isEmpty() && maisReferencias) {
				maisReferencias = !linha.contains(":"); 
			}
	
			if (linha.startsWith("from ") && (linha.contains("@")) ) {
				if (encontrouMessageID && encontrouSubject) {
					if (!email.adicionar(this.threads)) 
						this.threads.add(email);
				}
	
				encontrouMessageID	= false;
				encontrouSubject	= false;
				encontrouMensagem	= false;
				maisReferencias		= false;
				encontrouData		= false;
				proximaLinhaSubject	= false;
	
				email = new Email();						   
			} 
			else if ((!encontrouMessageID && linha.startsWith("message-id: ")) 
					|| (encontrouMessageID && email.getMessageID().isEmpty() ) ) { 
				encontrouMessageID = true;
				linha = linha.replaceAll("message-id: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setMessageID(linha);
			} 
			else if (linha.startsWith("in-reply-to: ")) { 
				linha = linha.replaceAll("in-reply-to: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setInReplyTo(linha);
			} 
			else if (linha.startsWith("from: ")){ 
				linha = linha.replaceAll("from:", "");
				linha = retirarCaracteresExtras(linha);
				StringTokenizer st = new StringTokenizer(linha);
				String emailFrom = "";
				while (st.hasMoreTokens()) {
					emailFrom = st.nextToken();
				}
	
				// FIXME: Fazer esta troca depois de processar todo o arquivo
				try {
					// FIXME: tornar jira@apache.org uma variável para cada projeto
					if (emailFrom.equals("jira@apache.org")) {
						String name = linha.substring(0, linha.indexOf("jira"));
						email.setFrom(this.db.getDeveloperEmail(name));
					}
					else {
						email.setFrom(this.db.getDeveloperEmailInTheListaEmail(emailFrom));
					}	
				}
				catch (SQLException sqlException) {
					sqlException.printStackTrace();
				}
	
			} 
			else if (linha.startsWith("date: ") && !encontrouData){ 
				linha = linha.replaceAll("date: ", "");
				linha = retirarCaracteresExtras(linha);
				email.setData(linha);
				encontrouData = true;
			} 
			else if (linha.startsWith("references: ") || (maisReferencias) ){ 
				linha = linha.replaceAll("references: ", "");
				linha = this.retirarCaracteresExtras(linha);
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
	
		if (encontrouMessageID && encontrouSubject) {
			if (!email.adicionar(this.threads)) { 
				this.threads.add(email);
			}
			encontrouMessageID	= false;
			encontrouSubject	= false;
			encontrouMensagem	= false;
			maisReferencias		= false;
			proximaLinhaSubject = false;
		}
	}

	String retirarCaracteresExtras(String palavra) {
		String result = palavra;
		result = result.replace("JIRA", "");
		result = result.replace("<", "");
		result = result.replace(">", "");
		result = result.replace("(", "");
		result = result.replace(")", "");
		result = result.replace("[", "");
		result = result.replace("]", "");
		result = result.replace(",", "");
		result = result.replace(";", "");
		result = result.replace("'", "");
		result = result.replace("\"", "");
		return result;
	}

	public void saveThreads() {
		this.db.saveThreads(this);
	}
	
}
