package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contem a implementacao das operacoes que um desenvolvedor
 * pode realizar sobre o sistema.
 * 
 * Última modificacao: 17/09/2008 por RodrigoCMD
 */

public class ServicoDesenvolvedorImplDAO implements ServicoDesenvolvedor{

	public boolean adicionarConhecimentoAoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento, double grau, int qntResposta) {
		if(qntResposta <= 0)
			qntResposta = 1;
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " INSERT INTO desenvolvedor_has_conhecimento VALUES ('"
				+emailDesenvolvedor+"','" +nomeConhecimento+"','" +grau+"','" +qntResposta+
				"');";

			System.out.println(SQL);
			stm.execute(SQL);

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public boolean atualizarDesenvolvedor(String email, String novoEmail, String nome,
			String cvsNome, String senha) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {

			stm = conn.createStatement();

			String SQL = " UPDATE desenvolvedor SET email = '"+novoEmail+"',"+
			" nome = '"+nome+"', cvsNome = '"+cvsNome+"', senha = '"+senha+
			"' WHERE email = '"+email+"';";

			System.out.println(SQL);
			stm.execute(SQL);

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;

	}

	public boolean criarDesenvolvedor(String email, String nome,
			String cvsNome, String senha) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {

			stm = conn.createStatement();

			String SQL = " INSERT INTO desenvolvedor " +
			" VALUES('"+email+"','"+
			nome+"','"+cvsNome+"','"+senha+"');";

			System.out.println(SQL);
			stm.execute(SQL);

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public boolean desenvolvedorExiste(String email) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM desenvolvedor WHERE "+
			" email = '"+email+"';";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){
				return true;
			}else{
				return false;
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public boolean removerConhecimentoDoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			if (this.conhecimentoDoDesenvolvedorExiste(emailDesenvolvedor, nomeConhecimento)){
				String SQL = " DELETE FROM desenvolvedor_has_conhecimento "+
				" WHERE desenvolvedor_email = '"+emailDesenvolvedor+"'"+
				" AND conhecimento_nome = '"+nomeConhecimento+"';";

				System.out.println(SQL);
				stm.execute(SQL);
				return true;
			}
			else return false;

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public boolean conhecimentoDoDesenvolvedorExiste(String emailDesenvolvedor, String nomeConhecimento){

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM desenvolvedor_has_conhecimento WHERE "+
			" desenvolvedor_email = '"+emailDesenvolvedor+"' " +
			" AND conhecimento_nome = '"+nomeConhecimento+"';";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){
				return true;
			}else{
				return false;
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}


	}

	public boolean removerDesenvolvedor(String email) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {

			stm = conn.createStatement();

			if (this.desenvolvedorExiste(email)){

				String SQL = " DELETE FROM desenvolvedor WHERE " +
				" email = '"+email+"';";

				System.out.println(SQL);
				stm.execute(SQL);
				return true;

			}else {
				return false;
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public Desenvolvedor getDesenvolvedor(String email) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
			
		try {

			stm = conn.createStatement();

			String SQL = " SELECT email, nome, cvsNome FROM desenvolvedor WHERE "+
			" email = '"+email+"';";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				Desenvolvedor desenvolvedor = new Desenvolvedor();

				desenvolvedor.setEmail(rs.getString("email"));
				desenvolvedor.setNome(rs.getString("nome"));
				desenvolvedor.setCVSNome(rs.getString("cvsNome"));
				desenvolvedor.setListaConhecimento(this.getConhecimentosDoDesenvolvedor(rs.getString("email"), 1));
				desenvolvedor.setSenha("");

				return desenvolvedor;
			}else{
				return null;
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public Desenvolvedor getDesenvolvedorCVS(String cvsNome) throws DesenvolvedorInexistenteException {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
			
		try {

			stm = conn.createStatement();

			String SQL = " SELECT email, nome, cvsNome FROM desenvolvedor WHERE "+
			" cvsNome = '"+cvsNome+"';";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				Desenvolvedor desenvolvedor = new Desenvolvedor();

				desenvolvedor.setEmail(rs.getString("email"));
				desenvolvedor.setNome(rs.getString("nome"));
				desenvolvedor.setCVSNome(rs.getString("cvsNome"));
				desenvolvedor.setListaConhecimento(this.getConhecimentosDoDesenvolvedor(rs.getString("email"), 1));
				desenvolvedor.setSenha("");

				return desenvolvedor;
			}else{
				throw new DesenvolvedorInexistenteException();
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}
	
	public int getQntResposta(String email, String conhecimento){
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try{
			
			stm = conn.createStatement();
			String sql = "select qtd_resposta from " +
						 "desenvolvedor_has_conhecimento where " +
						 "desenvolvedor_email = '" + email + "' and conhecimento_nome = '" + conhecimento+"';";
			
			System.out.println("getQTDADE!!: "+sql);
			ResultSet rs = stm.executeQuery(sql);
			System.out.println("PASSOU DO EXECUTEQUERY");
			int teste = rs.getInt(1);
			System.out.println(teste);
			return teste;
				
		} catch(SQLException e){
			System.out.println("Bugou o getQtdadeRespostas");
			e.printStackTrace();
			return -1;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}
	
	public int getGrau(String email, String conhecimento){
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try{
			
			stm = conn.createStatement();
			String sql = "select grau from " +
						 "desenvolvedor_has_conhecimento where " +
						 "desenvolvedor_email = '" + email + "' and conhecimento_nome = " + conhecimento;
			
			ResultSet rs = stm.executeQuery(sql);
			
			return rs.getInt(1);
				
		} catch(SQLException e){
			return -1;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}
	
	public boolean updateQntResposta(String email, String conhecimento, int quantidade){
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();		
		
		Statement stm = null;
		
		try{
			
			stm = conn.createStatement();
			
			String sql = "update desenvolvedor_has_conhecimento set qtd_resposta = " +quantidade+
			 " where desenvolvedor_email = '" + email+ "' and conhecimento_nome = '" + conhecimento + "'";

			stm.execute(sql);
			
			return true;
				
		} catch(SQLException e){
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}
	
	public boolean updateGrau(String email, String conhecimento, int grau){
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try{
			
			stm = conn.createStatement();
			
			String sql = "update desenvolvedor_has_conhecimento set grau = " +grau+
			 " where desenvolvedor_email = '" + email+ "' and conhecimento_nome = '" + conhecimento + "'";

			stm.execute(sql);
			
			return true;
				
		} catch(SQLException e){
			return false;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	// Modificado por Francisco - 16/09 - 18:50
	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email) {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();
		HashMap<Conhecimento, Double> map = new HashMap<Conhecimento, Double>();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM desenvolvedor_has_conhecimento WHERE "+
			" desenvolvedor_email = '"+email+"' ORDER BY conhecimento_nome;";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);


			while (rs.next()){

				//pegar chave do conhecimento
				String nomeConhecimento = rs.getString(2);
				Double grau = new Double(rs.getInt(3));
				
				ServicoConhecimento sc = new ServicoConhecimentoImplDAO();

				Conhecimento conhecimento = sc.getConhecimento(nomeConhecimento);

				//list.add(conhecimento);
				map.put(conhecimento, grau);

			}
			//return list;

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
		
		Set<Entry<Conhecimento, Double>> set = map.entrySet();
		
		for(Entry<Conhecimento, Double> c : set)
			list.add(c.getKey());
		return list;
	}
	
	public HashMap<Conhecimento, Double> getConhecimentosDoDesenvolvedor(String email, int x) {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		HashMap<Conhecimento, Double> map = new HashMap<Conhecimento, Double>();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM desenvolvedor_has_conhecimento WHERE "+
			" desenvolvedor_email = '"+email+"' ORDER BY conhecimento_nome;";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);


			while (rs.next()){

				//pegar chave do conhecimento
				String nomeConhecimento = rs.getString(2);
				Double grau = new Double(rs.getInt(3));
				
				ServicoConhecimento sc = new ServicoConhecimentoImplDAO();

				Conhecimento conhecimento = sc.getConhecimento(nomeConhecimento);

				//list.add(conhecimento);
				map.put(conhecimento, grau);

			}
			//return list;

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
		
		return map;
	}

	// Modificado por Francisco - 16/09 -- 18:53
	public ArrayList<TipoAtividade> getAtividadesDoDesenvolvedor(String email) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<TipoAtividade> list = new ArrayList<TipoAtividade>();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM atividade WHERE "+
			" desenvolvedor_email = '"+email+"' ORDER BY id;";


			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);


			while (rs.next()){
				TipoAtividade tipoAtividade = new TipoAtividade();

				tipoAtividade.setId(rs.getInt(1));
				tipoAtividade.setDesenvolvedor(this.getDesenvolvedor(rs.getString(2)));
				tipoAtividade.setSupervisor(this.getDesenvolvedor(rs.getString(3)));
				tipoAtividade.setIdPai(rs.getInt(4));
				tipoAtividade.setDescricao(rs.getString(5));
				tipoAtividade.setDataInicio(rs.getDate(6));
				tipoAtividade.setDataFinal(rs.getDate(7));
				tipoAtividade.setConcluida(rs.getBoolean(8));
				//tipoAtividade.setListaDeConhecimentosEnvolvidos(this.getConhecimentosDoDesenvolvedor(rs.getString(2)));


				list.add(tipoAtividade);

			}
			return list;


		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public ArrayList<Desenvolvedor> getTodosDesenvolvedores() {

		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Desenvolvedor> list = new ArrayList<Desenvolvedor>();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM desenvolvedor;";

			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){

				Desenvolvedor desenvolvedor = new Desenvolvedor();

				desenvolvedor.setEmail(rs.getString(1));
				desenvolvedor.setNome(rs.getString(2));
				desenvolvedor.setCVSNome(rs.getString(3));
				//desenvolvedor.setListaConhecimento(this.getConhecimentosDoDesenvolvedor(rs.getString(1), 1));				

				list.add(desenvolvedor);
			}
			return list;

		} catch (Exception e) {
			return null;
		} finally {
			/*try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}*/
		}
	}

	public Desenvolvedor autenticaDesenvolvedor(String email, String senha) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM desenvolvedor WHERE "+
			" email = '"+email+"' AND senha = '"+senha+"';";


			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				Desenvolvedor desenvolvedor = new Desenvolvedor();

				desenvolvedor.setEmail(rs.getString(1));
				desenvolvedor.setNome(rs.getString(2));
				desenvolvedor.setCVSNome(rs.getString(3));
				//desenvolvedor.setListaConhecimento(this.getConhecimentosDoDesenvolvedor(rs.getString(1), 1));

				return desenvolvedor;
			}else{
				return null;
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	
	public ArrayList<Desenvolvedor> getDesenvolvedoresPorConhecimento(Conhecimento conhecimento) {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Desenvolvedor> list = new ArrayList<Desenvolvedor>();

		try {
			stm = conn.createStatement();

			String SQL = 
				"SELECT DS.email, DS.nome, DS.cvsNome, DC.desenvolvedor_email, DC.conhecimento_nome, DC.grau" +
				" FROM desenvolvedor_has_conhecimento AS DC" +
				" INNER JOIN desenvolvedor AS DS ON DS.email = DC.desenvolvedor_email" +
				" WHERE conhecimento_nome = '"+ conhecimento.getNome() +"'";
				
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){
				Desenvolvedor desenvolvedor = new Desenvolvedor();

				desenvolvedor.setEmail(rs.getString("email"));
				desenvolvedor.setNome(rs.getString("nome"));
				desenvolvedor.setCVSNome(rs.getString("cvsNome"));
				desenvolvedor.setListaConhecimento(this.getConhecimentosDoDesenvolvedor(rs.getString("email"), 1));
				desenvolvedor.setSenha("");

				list.add(desenvolvedor);
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
		
		return list;
	}
	
}
