package persistencia.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoProblema;
import beans.Problema;

public class ServicoProblemaImplDAO implements ServicoProblema{

	public boolean atualizarStatusDoProblema(int id, boolean status) {

		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL;
			
			if(status){
			 SQL = " UPDATE problema SET resolvido = 1"+
				   " WHERE id = "+id+";";
			}else{
			 SQL = " UPDATE problema SET resolvido = 0"+
				   " WHERE id = "+id+";";
			}
			System.out.println(SQL);
			stm.execute(SQL);
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;

	}

	public boolean cadastrarProblema(int idAtividade, String descricao,
			Date dataDoRelato, String mensagem) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO problema(atividade_id,descricao,dataRelato,mensagem) " +
					     " VALUES("+idAtividade+",'"+
						   descricao+"','"+dataDoRelato+"','"+mensagem+"');";
			
			System.out.println(SQL);
			stm.execute(SQL);
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;
		
	}

	public ArrayList<Problema> listarProblemasDaAtividade(int idAtividade) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		ArrayList<Problema> list = new ArrayList<Problema>();
		ServicoAtividade sa = new ServicoAtividadeImplDAO();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM problema WHERE "+
						 " atividade_id = "+idAtividade+" ORDER BY atividade_id;";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				Problema p = new Problema();
				
				p.setId(rs.getInt(1));
				p.setAtividade(sa.getAtividade(rs.getInt(2)));
				p.setDescricao(rs.getString(3));
				p.setResolvido(rs.getBoolean(4));
				p.setData(rs.getDate(5));
				p.setMensagem(rs.getString(6));
				
				list.add(p);
				
			}
			return list;
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
		
	}

	public boolean removerProblema(int id) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			if (this.problemaExiste(id)){
				String SQL = " DELETE FROM problema WHERE " +
							 " id = "+id+";";
				
				System.out.println(SQL);
				stm.execute(SQL);
				return true;
			
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public boolean problemaExiste(int id) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM problema WHERE "+
						 " id = "+id+" ORDER BY id;";
				
			
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
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public Problema getProblema(int id) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		ServicoAtividade sa = new ServicoAtividadeImplDAO();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM problema WHERE "+
						 " id = "+id+";";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				
				Problema p = new Problema();
				
				p.setId(rs.getInt(1));
				p.setAtividade(sa.getAtividade(rs.getInt(2)));
				p.setDescricao(rs.getString(3));
				p.setResolvido(rs.getBoolean(4));
				p.setData(rs.getDate(5));
				p.setMensagem(rs.getString(6));
				
				return p;
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}
	
	public ArrayList<Problema> getListaProblemas() {
		Connection conn = MySQLConnectionFactory.getConnection();

		ArrayList<Problema> list = new ArrayList<Problema>();
		ServicoAtividade sa = new ServicoAtividadeImplDAO();

		try {

			Statement stm = conn.createStatement();

			String SQL = " SELECT * FROM problema";


			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){

				Problema p = new Problema();

				p.setId(rs.getInt(1));
				p.setAtividade(sa.getAtividade(rs.getInt(2)));
				p.setDescricao(rs.getString(3));
				p.setResolvido(rs.getBoolean(4));
				p.setData(rs.getDate(5));
				p.setMensagem(rs.getString(6));

				list.add(p);

			}
			return list;
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

}
