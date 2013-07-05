package dados.cvs;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

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

	public void setFrom(String from, Connection conn) {
		
		String SQL = "SELECT email FROM desenvolvedor WHERE listaEmail LIKE '%" + from + "%'";

		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(SQL);
			if (rs.next())
				this.from = rs.getString("email");	
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	public Date getData() {
		return data;
	}

	public void setData(String linhaData) {
		//System.out.println(linhaData);
		
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

	public void setFromPorNome(String nome, Connection conn) {

		String SQL = "SELECT email FROM desenvolvedor WHERE nome ='" + nome.trim() + "'";

		Statement stm = null;
		try {
			stm = conn.createStatement();
			ResultSet rs = stm.executeQuery(SQL);
			if (rs.next())
				from = rs.getString("email");
			else 
				from = nome;
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	private static String retornarAssuntoResposta( String assuntoResposta ) {
		assuntoResposta = assuntoResposta.trim();
		if (assuntoResposta.startsWith("re") && assuntoResposta.indexOf(":") > -1 )
			assuntoResposta = assuntoResposta.replace("re: ", "") ;
			//assuntoResposta = assuntoResposta.substring( assuntoResposta.indexOf(":") +1 ).trim() ;
		
		return assuntoResposta;		
	}
	
	public static boolean adcionarEmail(ArrayList<Email> emails, Email emailIncluir){
		boolean retorno = false;
		String assuntoRespostaIncluir 	= "";
		String assuntoRespostaEmail 	= "";

		for (Email email : emails) {
			
			assuntoRespostaIncluir	= retornarAssuntoResposta( emailIncluir.getSubject() ) ;
			assuntoRespostaEmail	= retornarAssuntoResposta( email.getSubject() ) ;
			
			// -> No caso normal quando a resposta vem depois do 1� e-mail
			if ( emailIncluir.getInReplyTo().equals( email.messageID ) ||
					emailIncluir.getReferences().contains( email.messageID ) ||
					assuntoRespostaEmail.equals( assuntoRespostaIncluir )) {
				email.emailsFilho.add(emailIncluir);
				retorno = true;
				break;
			// Casos onde o e-mail inicial est� vindo depois da resposta	
			} else if ( email.getInReplyTo().equals( emailIncluir.messageID ) ||
					email.getReferences().contains( emailIncluir.messageID ) ||
					emailIncluir.getSubject().equals( assuntoRespostaEmail )) {
				emailIncluir.emailsFilho.add(email);
				emails.remove( emails.indexOf(email) );
				
				// -> Inicio da gabiarra
				ArrayList<Email> emailsExcluir = new ArrayList<Email>(); 
				for (Email email2 : emails) {
					assuntoRespostaEmail	= retornarAssuntoResposta( email2.getSubject() ) ;
					if ( email2.getInReplyTo().equals( emailIncluir.messageID ) ||
							email2.getReferences().contains( emailIncluir.messageID ) ||
							emailIncluir.getSubject().equals( assuntoRespostaEmail )) {
						emailIncluir.emailsFilho.add(email2);
						emailsExcluir.add( email2 );
					}	
				}
				
				for (Email email2 : emailsExcluir)
					emails.remove( emails.indexOf(email2) );
				// -> Fim
				
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
