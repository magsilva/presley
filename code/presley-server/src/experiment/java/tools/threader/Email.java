package tools.threader;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.hukarz.presley.beans.Desenvolvedor;

public class Email {
	private Date data;
	private ArrayList<Email> emailsFilho;
	private Desenvolvedor desenvolvedor;
	private String inReplyTo, references;
	private String mensagem;
	private String messageID;
	
	private String subject;
	
	public Email() {
		emailsFilho = new ArrayList<Email>();
		messageID	= "";
		inReplyTo	= "";
		references	= "";
		subject		= "";
		mensagem	= "";
		desenvolvedor = new Desenvolvedor();
	}

	public boolean adicionar(ArrayList<Email> emails) {
		boolean retorno = false;
		String assuntoRespostaIncluir 	= "";
		String assuntoRespostaEmail 	= "";

		for (Email email : emails) {
			
			assuntoRespostaIncluir	= retornarAssuntoResposta(this.subject) ;
			assuntoRespostaEmail	= retornarAssuntoResposta(email.getSubject()) ;
			
			// -> No caso normal quando a resposta vem depois do 1º e-mail
			if ( this.getInReplyTo().equals( email.messageID ) ||
					this.getReferences().contains( email.messageID ) ||
					assuntoRespostaEmail.equals( assuntoRespostaIncluir )) {
				email.emailsFilho.add(this);
				retorno = true;
				break;
			// Casos onde o e-mail inicial está vindo depois da resposta	
			} 
			else if (email.getInReplyTo().equals( this.messageID ) ||
					email.getReferences().contains( this.messageID ) ||
					this.subject.equals(assuntoRespostaEmail)) {
				this.emailsFilho.add(email);
				emails.remove( emails.indexOf(email) );
				
				// -> Inicio da gabiarra
				ArrayList<Email> emailsExcluir = new ArrayList<Email>(); 
				for (Email email2 : emails) {
					assuntoRespostaEmail	= retornarAssuntoResposta( email2.getSubject() ) ;
					if ( email2.getInReplyTo().equals( this.messageID ) ||
							email2.getReferences().contains( this.messageID ) ||
							this.getSubject().equals( assuntoRespostaEmail )) {
						this.emailsFilho.add(email2);
						emailsExcluir.add( email2 );
					}	
				}
				
				for (Email email2 : emailsExcluir) {
					emails.remove( emails.indexOf(email2) );
				}
				// -> Fim
				
				emails.add(this);
				
				retorno = true;
				break;
			} 
			else {
				retorno = adicionar(email.getEmailsFilho());
				if (retorno)
					break;
			}
			
		}
		
		return retorno;
	}

	public Date getData() {
		return data;
	}

	public ArrayList<Email> getEmailsFilho() {
		return emailsFilho;
	}

	public Desenvolvedor getDesenvolvedor() {
		return desenvolvedor;
	}

	public String getInReplyTo() {
		return inReplyTo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String getMessageID() {
		return messageID;
	}

	public String getReferences() {
		return references;
	}	
	
	public String getSubject() {
		return subject;
	}
	
	public ArrayList<Email> retornarFilhosSemRepeticao() {
		ArrayList<Email> emailsResposta = getEmailsFilho();
		ArrayList<Email> emailsExperimento = new ArrayList<Email>();
		for (Email emailResposta : emailsResposta) {
			if ( !emailResposta.getDesenvolvedor().getEmail().equals( desenvolvedor.getEmail() ) ){
				boolean achei = false;
				
				for (Email emailExperimento : emailsExperimento) {
					if ( emailResposta.getDesenvolvedor().getEmail().equals(
							emailExperimento.getDesenvolvedor().getEmail())){
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

	public void setEmailsFilho(ArrayList<Email> emailsFilho) {
		this.emailsFilho = emailsFilho;
	}
	
	public void setDesenvolvedor(final String from, final String nome) {
		if (null != from) {
			this.desenvolvedor.setEmail( from );
			this.desenvolvedor.setNome( nome );
		}				
	}

	public void setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo.toLowerCase();
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem.toLowerCase();
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID.toLowerCase();
	}
	
	public void setReferences(String references) {
		this.references = references.toLowerCase();
	}
	
	public void setSubject(String subject) {
		this.subject = subject.toLowerCase();
	}

	private static String retornarAssuntoResposta( String assuntoResposta ) {
		assuntoResposta = assuntoResposta.trim();
		if (assuntoResposta.startsWith("re") && assuntoResposta.indexOf(":") > -1 )
			assuntoResposta = assuntoResposta.replace("re: ", "") ;
			//assuntoResposta = assuntoResposta.substring( assuntoResposta.indexOf(":") +1 ).trim() ;
		
		return assuntoResposta;		
	}
	
}
