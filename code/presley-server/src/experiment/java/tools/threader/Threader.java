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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.hukarz.presley.beans.Desenvolvedor;

public class Threader {
	private DB db;
	private Set<Desenvolvedor> developers;
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
	
			if (thread.getDesenvolvedor().getEmail().isEmpty() || thread.getSubject().contains("re:")) {
				continue;
			}
	
			ArrayList<Email> emailsExperimento = thread.retornarFilhosSemRepeticao();
	
			if (emailsExperimento.size() > 0){
				String nomeArquivo = base + System.currentTimeMillis()+ "_" + count ;
	
				PrintWriter arquivoQuestion = new PrintWriter(new 
						FileOutputStream(nomeArquivo + ".question"));
	
				arquivoQuestion.println(thread.getDesenvolvedor().getEmail());
				arquivoQuestion.println((new SimpleDateFormat("dd/MM/yyyy").format(thread.getData()))  );
				arquivoQuestion.println(thread.getSubject());
				arquivoQuestion.println(thread.getMensagem());
	
				arquivoQuestion.close();
	
				PrintWriter arquivoEmails = new PrintWriter(new FileOutputStream( nomeArquivo + ".emails"));
				for (Email emailArquivo : emailsExperimento) {
					arquivoEmails.println( emailArquivo.getDesenvolvedor().getEmail() );
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
			this.developers.add( email.getDesenvolvedor() );
			if (email.getEmailsFilho().size() > 0) {
				findChildrenDevelopers(email.getEmailsFilho());
			}
		}
	}
	
	public void findDevelopers() {
		this.developers = new HashSet<Desenvolvedor>();
		for (Email thread : this.threads) {
			this.developers.add( thread.getDesenvolvedor() );
			findChildrenDevelopers(thread.getEmailsFilho());
		}
	}

	public Collection<Desenvolvedor> getDevelopers() {
		Map<String, Desenvolvedor> emails = new HashMap<String, Desenvolvedor>();

		for (Email thread : this.threads) {
			String emailFrom = thread.getDesenvolvedor().getEmail();
			String nomeFrom  = thread.getDesenvolvedor().getNome();
			
			Desenvolvedor desenvolvedor = new Desenvolvedor();
			
			if (emails.get(nomeFrom)==null){
				desenvolvedor.setEmail(emailFrom);
				desenvolvedor.setNome(nomeFrom);
				desenvolvedor.setListaEmail("");
				emails.put(nomeFrom, desenvolvedor) ;
			} else {
				desenvolvedor = emails.get(nomeFrom);
				desenvolvedor.setListaEmail( emails.get(nomeFrom).getListaEmail() );
				if (!desenvolvedor.getListaEmail().contains(emailFrom)){
					desenvolvedor.setListaEmail( desenvolvedor.getListaEmail() + 
							" " + emailFrom);
					emails.put(nomeFrom, desenvolvedor) ;
				}
			}		
		}
		
		return emails.values();
	}

	public ArrayList<Email> getThreads() {
		return threads;
	}
	
	// TODO: trocar a implementa��o deste m�todo.
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
		boolean encontrouFrom		= false;
		
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
				encontrouFrom 		= false;
				
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
			else if ((!encontrouFrom) && linha.startsWith("from: ")){
				encontrouFrom = true;
				
				if (!linha.contains("@")){
					linha = linha + " "+ reader.readLine();					
				}
				linha = linha.replaceAll("from:", "");
				linha = retirarCaracteresExtras(linha);
				
				if (linha.contains("jira@apache.org")){
					linha = linha.replace("jira@apache.org", linha.trim() + "@presley" );
				}
				
				String emailFrom = "";
				String nomeFrom  = "";				
				
				StringTokenizer st = new StringTokenizer(linha);
				while (st.hasMoreTokens()) {
					String token = st.nextToken();

					if (token.contains("@")){
						emailFrom = retirarCaracteresExtras(token);
						nomeFrom  = retirarCaracteresExtras(nomeFrom);
					} else if (emailFrom.contains(","))
						nomeFrom = "";
					else 
						nomeFrom = nomeFrom + token + " ";
				}
	
				email.setDesenvolvedor(emailFrom, nomeFrom);	
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
