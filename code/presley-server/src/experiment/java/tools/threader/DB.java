package tools.threader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.server.core.PresleyProperties;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoProblemaImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoSolucaoImpl;

public class DB {

	private Connection connection;
	private ValidacaoProblemaImpl validacaoProblema;
	private ValidacaoSolucaoImpl  validacaoSolucao;

	public DB() {
		connection = MySQLConnectionFactory.open();
		validacaoSolucao = new ValidacaoSolucaoImpl();
		validacaoProblema = new ValidacaoProblemaImpl();
	}

	void cadastrarSolucoes(ArrayList<Email> children, Problema problema ) {
		for (Email email : children) {
			if (email.getDesenvolvedor().getEmail().isEmpty())
				continue;

			Desenvolvedor desenvolvedor = new Desenvolvedor();
			desenvolvedor.setEmail(email.getDesenvolvedor().getEmail());

			Solucao solucao = new Solucao();
			solucao.setAjudou(true);
			solucao.setProblema(problema);
			solucao.setData( email.getData() ) ;
			solucao.setMensagem(email.getMensagem());
			solucao.setDesenvolvedor(desenvolvedor);

			try {
				saveSolution(solucao);
				if (email.getEmailsFilho().size() > 0) {
					cadastrarSolucoes(email.getEmailsFilho(), problema);
				}
			} catch (ProblemaInexistenteException e) {
				e.printStackTrace();
			} catch (DesenvolvedorInexistenteException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public String getDeveloperEmail(String name) throws SQLException {
		Statement statement = null;
		statement = connection.createStatement();
		String query = "SELECT email FROM desenvolvedor WHERE nome ='" + name.trim() + "'";
		ResultSet rs = statement.executeQuery(query);

		if (rs.next()) {
			return rs.getString("email");
		}
		else {
			return null;
		}
	}

	public String getDeveloperEmailInTheListaEmail(String email) throws SQLException {
		Statement statement = connection.createStatement();
		String query = "SELECT email FROM desenvolvedor WHERE listaEmail LIKE '%" + email + "%'";
		ResultSet rs = statement.executeQuery(query);
		if (rs.next()) {
			return rs.getString("email");
		}
		else {
			return null;
		}
	}

	void saveDeveloper(Map<String, String> emails) {
		Statement statement = null;
		try {
			statement = this.connection.createStatement();

			Set<String> listaEmail = emails.keySet();

			for (String nome : listaEmail) {
				if (nome.length() > ThreaderGui.MAX_NAME_SIZE) {
					nome = nome.substring(0, ThreaderGui.MAX_NAME_SIZE);
				}

				String email  = emails.get(nome);

				if (email == null) {
					continue;
				}

				StringTokenizer st = new StringTokenizer(email);

				while (st.hasMoreTokens()){   
					String emailPrincipal = st.nextToken();
					String SQL = "SELECT email FROM desenvolvedor WHERE listaEmail LIKE '%"+ emailPrincipal +"%' or " +
					" nome = '"+ nome +"'"; 
					ResultSet rs = statement.executeQuery(SQL);

					if (emailPrincipal.length() > 50)
						continue;

					if (!rs.next()){
						// Se o nome não estiver vazio (Os grupos estão formados por nome)
						if (!nome.isEmpty()) {
							SQL = "INSERT INTO desenvolvedor (senha, cvsNome, nome, email, listaEmail) " +
							" VALUES ('9', '', '"+ nome +"', '"+ emailPrincipal +"', '"+ emailPrincipal +"')";
							statement.execute(SQL);
						} else {
							SQL = "INSERT INTO desenvolvedor (senha, cvsNome, nome, email, listaEmail) " +
							" VALUES ('9', '', '"+ nome +"', '"+ emailPrincipal +"', '"+ email +"')";
							statement.execute(SQL);
							break;
						}
					} 

				}
			}
			statement.close();
			this.connection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public Problema saveProblem(Problema problem) throws DescricaoInvalidaException, IOException, ProjetoInexistenteException, ConhecimentoNaoEncontradoException {
		return validacaoProblema.cadastrarProblema(problem);
	}

	public void saveSolution(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException {
		validacaoSolucao.cadastrarSolucao(solucao);
	}
	
	public void saveThreads(Threader threader) {
		Projeto projeto = new Projeto();
		PresleyProperties properties = PresleyProperties.getInstance();
		projeto.setNome(properties.getProperty("experiment.projectName"));
	
		for (Email email : threader.getThreads()) {
			if (email.getDesenvolvedor().getEmail().isEmpty()) {
				continue;
			}
	
			Desenvolvedor desenvolvedor = new Desenvolvedor(email.getDesenvolvedor().getEmail());
	
			// FIXME: criar construtor adequado em Problema para evitar este estilo de programação
			Problema problema = new Problema();
			problema.setDesenvolvedorOrigem(desenvolvedor);
			problema.setProjeto(projeto);
			problema.setData(email.getData()) ;
			problema.setDescricao(email.getSubject());
			problema.setMensagem(email.getMensagem());
			problema.setResolvido(true);
			problema.setTemResposta(false);
	
			try {
				problema = saveProblem(problema);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
	
			if (email.getEmailsFilho().size() > 0) {
				cadastrarSolucoes(email.getEmailsFilho(), problema);
			}
		}
	}

}
