package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;
import com.mysql.jdbc.PreparedStatement;

public class ServicoSolucaoImplDAO implements ServicoSolucao{

	public boolean atualizarStatusDaSolucao(int id, boolean status) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;
		
	}

	public boolean atualizarSolucao(Solucao solucao){
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {
		
			stm = conn.createStatement();
			
			String SQL =
			"UPDATE solucao SET "+
			" desenvolvedor_email='"+solucao.getDesenvolvedor().getEmail()+"'," +
			" problema_id= "+solucao.getProblema().getId()+","+
			" dataProposta= '"+solucao.getData()+"',"+
			" mensagem = '"+solucao.getMensagem()+"',"+
			" retornoSolucao= '"+solucao.getRetornoSolucao()+"',";
			
			if (solucao.getSolucaoResposta() == null)
				SQL += " id_solucaoResposta = 0" ;
			else	
				SQL += " id_solucaoResposta = "+solucao.getSolucaoResposta().getId() ;
			
			SQL += " WHERE id = "+solucao.getId()+";" ;
			
			System.out.println(SQL);
			stm.execute(SQL);
			
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public Solucao cadastrarSolucao(Solucao solucao) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {
		
			stm = conn.createStatement();

			String SQL = " INSERT INTO solucao( desenvolvedor_email, problema_id, dataProposta, mensagem, resolveu) " +
		     " VALUES(?,?,?,?,?);";
			
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL) ;
			pstmt.setString(1, solucao.getDesenvolvedor().getEmail());
			pstmt.setInt(2, solucao.getProblema().getId());
			pstmt.setDate(3, solucao.getData());
			pstmt.setString(4, solucao.getMensagem());
			pstmt.setInt(5, 0);
			pstmt.executeUpdate();
			
			SQL = "SELECT MAX(id) AS id FROM solucao WHERE desenvolvedor_email = '"+solucao.getDesenvolvedor().getEmail()+"'";
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				solucao.setId(rs.getInt(1));
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
				
		return solucao;
	}

	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(
			String emailDesenvolvedor) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
		return list;
	}

	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(
			Desenvolvedor desenvolvedor) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		try {
			
			stm = conn.createStatement();
		
			String SQL = 
				" SELECT id, desenvolvedor_email, problema_id, resolveu, dataProposta, "+
				" mensagem, retornoSolucao, id_solucaoResposta "+
				" FROM solucao "+
				" WHERE desenvolvedor_email = '"+desenvolvedor.getEmail()+"' AND "+
				" retornoSolucao <> '' ORDER BY id;";
							
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				Solucao s = new Solucao();
				
				s.setId(rs.getInt("id"));
				s.setDesenvolvedor(sd.getDesenvolvedor(rs.getString("desenvolvedor_email")));
				s.setProblema(sp.getProblema(rs.getInt("problema_id")));
				s.setAjudou(rs.getBoolean("resolveu"));
				s.setData(rs.getDate("dataProposta"));
				s.setMensagem(rs.getString("mensagem"));
				s.setRetornoSolucao(rs.getString("retornoSolucao"));
				
				list.add(s);				
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
	
	
	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(
			String emailDesenvolvedor) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
		return list;
	}

	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(
			String emailDesenvolvedor) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
		return list;
	}

	public boolean removerSolucao(int id) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public boolean solucaoExiste(int id) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public Solucao getSolucao(int id) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		try {
		
			stm = conn.createStatement();
		
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
	        	 stm.close();
	             //conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public ArrayList<Solucao> getSolucoesDoProblema(Problema problema) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		ArrayList<Solucao> list = new ArrayList<Solucao>();
		
		try {
		
			stm = conn.createStatement();
		
			String SQL = 
				" SELECT id, desenvolvedor_email, problema_id, resolveu, dataProposta, "+
				" mensagem, retornoSolucao, id_solucaoResposta "+
				" FROM solucao WHERE problema_id = "+problema.getId()+" "+
				" GROUP BY desenvolvedor_email"+
				" HAVING id = MIN(id);";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				Solucao s = new Solucao();
				
				s.setId(rs.getInt("id"));
				s.setDesenvolvedor(sd.getDesenvolvedor(rs.getString("desenvolvedor_email")));
				s.setProblema(sp.getProblema(rs.getInt("problema_id")));
				s.setAjudou(rs.getBoolean("resolveu"));
				s.setData(rs.getDate("dataProposta"));
				s.setMensagem(rs.getString("mensagem"));
				s.setRetornoSolucao(rs.getString("retornoSolucao"));
				
				if (rs.getInt("id_solucaoResposta") != 0)
					s.setSolucaoResposta( getRetornaFilhosSolucao( rs.getInt("id_solucaoResposta") ) ) ;
				
				list.add(s);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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

	private Solucao getRetornaFilhosSolucao(int idSolucaoPai ){
		Solucao retorno = null;
		
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoProblema sp = new ServicoProblemaImplDAO();
		
		try {
			stm = conn.createStatement();
		
			String SQL = 
				" SELECT id, desenvolvedor_email, problema_id, resolveu, dataProposta, "+
				" mensagem, retornoSolucao, id_solucaoResposta "+
				" FROM solucao WHERE id = "+ idSolucaoPai +";";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				retorno = new Solucao();
				
				retorno.setId(rs.getInt("id"));
				retorno.setDesenvolvedor(sd.getDesenvolvedor(rs.getString("desenvolvedor_email")));
				retorno.setProblema(sp.getProblema(rs.getInt("problema_id")));
				retorno.setAjudou(rs.getBoolean("resolveu"));
				retorno.setData(rs.getDate("dataProposta"));
				retorno.setMensagem(rs.getString("mensagem"));
				retorno.setRetornoSolucao(rs.getString("retornoSolucao"));
				
				if (rs.getInt("id_solucaoResposta") != 0)
					retorno.setSolucaoResposta( getRetornaFilhosSolucao( rs.getInt("id_solucaoResposta") ) ) ;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
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
		
		return retorno;
	}

}
