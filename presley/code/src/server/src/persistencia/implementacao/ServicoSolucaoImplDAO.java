package persistencia.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.Desenvolvedor;
import beans.Problema;
import beans.Solucao;
import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoDesenvolvedor;
import persistencia.interfaces.ServicoProblema;
import persistencia.interfaces.ServicoSolucao;

public class ServicoSolucaoImplDAO implements ServicoSolucao{

	public boolean atualizarStatusDaSolução(int id, boolean status) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL;
			
			if(status){
			 SQL = " UPDATE solucao SET resolveu = 1"+
				   " WHERE id = "+id+";";
			}else{
			 SQL = " UPDATE solucao SET resolveu = 0"+
				   " WHERE id = "+id+";";
			}
			System.out.println(SQL);
			stm.execute(SQL);
			
		} catch (SQLException e) {
			e.printStackTrace();
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

	public boolean cadastrarSolucao(String emailDesenvolvedor, int idProblema,
			Date dataDaProposta, String mensagem) {

		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO solucao(desenvolvedor_email,problema_id,dataProposta,mensagem,resolveu) " +
					     " VALUES('"+emailDesenvolvedor+"',"+
						   idProblema+",'"+dataDaProposta+"','"+mensagem+"',0);";
			
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

	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(
			String emailDesenvolvedor) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM solucao WHERE "+
						 " desenvolvedor_email = '"+emailDesenvolvedor+"'"+
						 " AND resolveu = 1 ORDER BY id;";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				Solucao s = new Solucao();
				
				s.setId(rs.getInt(1));
				s.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				s.setProblema(sp.getProblema(rs.getInt(3)));
				s.setAjudou(rs.getBoolean(4));
				s.setData(rs.getDate(5));
				s.setMensagem(rs.getString(6));
				
				list.add(s);
				
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
		return list;
	}

	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(
			String emailDesenvolvedor) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM solucao WHERE "+
						 " desenvolvedor_email = '"+emailDesenvolvedor+"' ORDER BY id;";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				Solucao s = new Solucao();
				
				s.setId(rs.getInt(1));
				s.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				s.setProblema(sp.getProblema(rs.getInt(3)));
				s.setAjudou(rs.getBoolean(4));
				s.setData(rs.getDate(5));
				s.setMensagem(rs.getString(6));
				
				list.add(s);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
		return list;
	}

	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(
			String emailDesenvolvedor) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM solucao WHERE "+
						 " desenvolvedor_email = '"+emailDesenvolvedor+"'"+
						 " AND resolveu = 0 ORDER BY id;";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				Solucao s = new Solucao();
				
				s.setId(rs.getInt(1));
				s.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				s.setProblema(sp.getProblema(rs.getInt(3)));
				s.setAjudou(rs.getBoolean(4));
				s.setData(rs.getDate(5));
				s.setMensagem(rs.getString(6));
				
				list.add(s);
				
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
		return list;
	}

	public boolean removerSolucao(int id) {
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			if (this.solucaoExiste(id)){
				String SQL = " DELETE FROM solucao WHERE " +
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

	public boolean solucaoExiste(int id) {
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM solucao WHERE "+
						 " id = "+id+";";
				
			
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

	public Solucao getSolucao(int id) {
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM solucao WHERE "+
						 " id = "+id+";";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				
				Solucao s = new Solucao();
				
				s.setId(rs.getInt(1));
				s.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				s.setProblema(sp.getProblema(rs.getInt(3)));
				s.setAjudou(rs.getBoolean(4));
				s.setData(rs.getDate(5));
				s.setMensagem(rs.getString(6));
				
				return s;
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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
