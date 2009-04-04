package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;


public class ServicoProjetoImplDAO implements ServicoProjeto {

	@Override
	public ArrayList<Projeto> getProjetosAtivo() {
		Connection conn = MySQLConnectionFactory.open();
		ArrayList<Projeto> projetos = new ArrayList<Projeto>();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " SELECT nome, ativo, endereco_Servidor_Leitura, endereco_Servidor_Gravacao FROM projeto WHERE ativo = 1";

			// System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while(rs.next()) {
				Projeto projeto = new Projeto();

				projeto.setAtivo(true);
				projeto.setNome(rs.getString("nome"));
				projeto.setEndereco_Servidor_Gravacao( rs.getString("endereco_Servidor_Gravacao") ) ;
				projeto.setEndereco_Servidor_Leitura( rs.getString("endereco_Servidor_Leitura") ) ;
			
				projetos.add(projeto);
			}

			return projetos;
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public boolean criarProjeto(Projeto projeto) {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = "UPDATE projeto SET ativo ='0';";
			stm.execute(SQL);

			SQL = " INSERT INTO projeto (nome, ativo, endereco_Servidor_Leitura, endereco_Servidor_Gravacao) " +
					" VALUES ('"+ projeto.getNome() +"','1','"+ projeto.getEndereco_Servidor_Leitura() +"','"+ projeto.getEndereco_Servidor_Gravacao() +"');";

			//System.out.println(SQL);
			stm.execute(SQL);
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	@Override
	public boolean removerProjeto(Projeto projeto) {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;

		try {
			stm = conn.createStatement();
			String SQL = " DELETE FROM projeto WHERE nome = '"+ projeto.getNome() +"';";
			stm.execute(SQL);
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	@Override
	public boolean atualizarStatusProjeto(Projeto projeto) {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;

		try {
			stm = conn.createStatement();
			String SQL = "UPDATE projeto SET ativo =" ;
			if (projeto.isAtivo())
				SQL += "'1'";
			else
				SQL += "'0'";
				
			SQL += " WHERE nome = '"+ projeto.getNome() +"';";
			System.out.println(SQL);
			stm.execute(SQL);
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public boolean projetoExiste(Projeto projeto) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = "SELECT * FROM projeto WHERE nome = '"+projeto.getNome()+"';";

			System.out.println(SQL);
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
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public ArrayList<Projeto> getProjetos() {
		Connection conn = MySQLConnectionFactory.open();
		ArrayList<Projeto> projetos = new ArrayList<Projeto>();
		Statement stm = null;
	

		try {
			stm = conn.createStatement();

			String sql = "SELECT nome, ativo, endereco_Servidor_Leitura, endereco_Servidor_Gravacao" +
					" FROM projeto ORDER BY nome";
			ResultSet rs = stm.executeQuery(sql);
			System.out.println(sql);
						
			while(rs.next()) {
				Projeto projeto = new Projeto();
				
				projeto.setNome( rs.getString("nome") );
				projeto.setEndereco_Servidor_Gravacao( rs.getString("endereco_Servidor_Gravacao") ) ;
				projeto.setEndereco_Servidor_Leitura( rs.getString("endereco_Servidor_Leitura") );
				projeto.setAtivo( rs.getBoolean("ativo") );
				
				projetos.add(projeto);
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	
				return null;
			}
		}
		return projetos;
	}

	@Override
	public Projeto getProjeto(String nome) {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = "SELECT * FROM projeto WHERE nome = '"+nome+"';";

			Projeto projeto = new Projeto();
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){
				projeto.setAtivo( rs.getBoolean("ativo") );
				projeto.setEndereco_Servidor_Gravacao( rs.getString("endereco_Servidor_Gravacao") );
				projeto.setEndereco_Servidor_Leitura( rs.getString("endereco_Servidor_Leitura") );
				projeto.setNome( rs.getString("nome") );
			}

			return projeto;
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}	
	}
	
}
