package persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.Conhecimento;

import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoConhecimento;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contem a implementacao das operacoes para administrar um conhecimento.
 * 
 * �ltima modificacao: 03/09/2008 por Amilcar Jr
 */

public class ServicoConhecimentoImplDAO implements ServicoConhecimento{

	public boolean atualizarConhecimento(String nome, String novoNome,
			String descricao) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " UPDATE conhecimento SET nome = '"+novoNome+"',"+
						 " descricao = '"+descricao+"' "+
						 " WHERE nome = '"+nome+"';";
			
			System.out.println(SQL);
			stm.execute(SQL);
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conex�o ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;
	}

	public boolean conhecimentoExiste(String nome) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM conhecimento WHERE "+
						 " nome = '"+nome+"';";
				
			
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
	             System.out.println(" Houve erro no fechamento da conex�o ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public boolean criarConhecimento(String nome, String descricao) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO conhecimento " +
					     " VALUES('"+nome+"','"+
						  descricao+"');";
			
			System.out.println(SQL);
			stm.execute(SQL);
			
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
	         try {
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conex�o ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;
	}

	public boolean removerConhecimento(String nome) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			if (this.conhecimentoExiste(nome)){
				String SQL = " DELETE FROM conhecimento WHERE " +
							 " nome = '"+nome+"';";
				
				System.out.println(SQL);
				stm.execute(SQL);
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
	             System.out.println(" Houve erro no fechamento da conex�o ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
	}

	public Conhecimento getConhecimento(String nome) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM conhecimento WHERE "+
						 " nome = '"+nome+"';";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				
				Conhecimento conhecimento = new Conhecimento();
				
				conhecimento.setNome(rs.getString(1));
				conhecimento.setDescricao(rs.getString(2));
				
				return conhecimento;
				
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
	             System.out.println(" Houve erro no fechamento da conex�o ");
	             onConClose.printStackTrace();	             
	           }
	         }
		
	}

}
