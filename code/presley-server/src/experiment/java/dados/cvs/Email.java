package dados.cvs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;

import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;

public class Email {
	private String messageID;
	private String inReplyTo, references;
	private String subject;
	private String mensagem;
	private ArrayList<Email> emailsFilho;
	private String from;
	private Date data;
	
	public Email() {
		emailsFilho = new ArrayList<Email>();
		messageID	= "";
		inReplyTo	= "";
		references	= "";
		subject		= "";
		mensagem	= "";
		from		= "";
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID.toLowerCase();
	}

	public String getInReplyTo() {
		return inReplyTo;
	}

	public void setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo.toLowerCase();
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references.toLowerCase();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject.toLowerCase();
	}	
	
	public ArrayList<Email> getEmailsFilho() {
		return emailsFilho;
	}
	
	public void setEmailsFilho(ArrayList<Email> emailsFilho) {
		this.emailsFilho = emailsFilho;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem.toLowerCase();
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Date getData() {
		return data;
	}

	public void setData(String linhaData) {
		StringTokenizer st = new StringTokenizer(linhaData);
		ArrayList<String> meses = new ArrayList<String>( );
		meses.add("jan");
		meses.add("feb");
		meses.add("mar");
		meses.add("apr");
		meses.add("may");
		meses.add("jun");
		meses.add("jul");
		meses.add("aug");
		meses.add("sep");
		meses.add("oct");
		meses.add("nov");
		meses.add("dec");


	 	int iDia = 0, iMes  = 0, iAno = 0;
		int qtdeValores = 1;
		/*
		 * 1 = Dia -- 2 = Mes -- 3 = Ano
		 * */
		
		while (st.hasMoreTokens() && qtdeValores < 4 ){
		 	String valor = st.nextToken();
		 	
		 	if ( qtdeValores == 1 ){
		 		try {
				 	iDia = Integer.parseInt( valor );
				} catch (Exception e) {
					continue;
				}
		 	} else if ( qtdeValores == 2 )
		 		iMes = meses.indexOf(valor) + 1;
		 	else if ( qtdeValores == 3 )
		 		iAno = Integer.parseInt( valor );
			
		 	qtdeValores++;
		}

		DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
		try {
			this.data = new Date(fmt.parse(iDia +"/"+ iMes +"/"+ iAno).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setFromPorNome(String nome) {

		String SQL = "SELECT email FROM desenvolvedor WHERE nome ='" + nome.trim() + "'";

		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(SQL);
			if (rs.next())
				from = rs.getString("email");	
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static boolean adcionarEmail(ArrayList<Email> emails, Email emailIncluir){
		boolean retorno = false;
		
		for (Email email : emails) {
			// -> No caso normal quando a resposta vem depois do 1º e-mail
			if ( emailIncluir.getInReplyTo().equals( email.messageID ) ||
					emailIncluir.getReferences().contains( email.messageID ) ||
					email.getSubject().equals(emailIncluir.getSubject().replaceFirst("re: ", ""))) {
				email.emailsFilho.add(emailIncluir);
				retorno = true;
				break;
			// Casos onde o e-mail inicial está vindo depois da resposta	
			} else if ( email.getInReplyTo().equals( emailIncluir.messageID ) ||
					email.getReferences().contains( emailIncluir.messageID ) ||
					emailIncluir.getSubject().equals(email.getSubject().replaceFirst("re: ", ""))) {
				emailIncluir.emailsFilho.add(email);
				emails.remove( emails.indexOf(email) );
				emails.add(emailIncluir);
				
				retorno = true;
				break;
			} else {
				retorno = adcionarEmail(email.getEmailsFilho(), emailIncluir);
				if (retorno)
					break;
			}
			
		}
		
		return retorno;
	}
	
}
