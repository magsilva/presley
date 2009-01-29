package beans;

import java.io.Serializable;

public class DadosAutenticacao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	private String user;
	private String passwd;
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	

}
