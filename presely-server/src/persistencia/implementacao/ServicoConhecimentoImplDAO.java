package persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.Conhecimento;

import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoConhecimento;
import validacao.excessao.ConhecimentoInexistenteException;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contem a implementacao das operacoes para administrar um conhecimento.
 * 
 * Última modificacao: 16/09/2008 por RodrigoCMD
 */

public class ServicoConhecimentoImplDAO implements ServicoConhecimento{

	public boolean atualizarConhecimento(String nome, String novoNome,
			String descricao) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
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
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;
	}

	public boolean conhecimentoExiste(String nome) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
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
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
	}

	public boolean criarConhecimento(String nome, String descricao) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
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
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
		return true;
	}

	public boolean removerConhecimento(String nome) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
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
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
	}

	public Conhecimento getConhecimento(String nome) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
			String SQL = ""; 	
			if (nome != null) {
		
			SQL = " SELECT * FROM conhecimento WHERE "+
						 " nome = '"+nome+"';";
			} else {
				SQL = " SELECT * FROM conhecimento;";
			}
			
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
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }		
	}

	public boolean associaConhecimentos(String nomeConhecimentoPai,
			String nomeConhecimentoFilho) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO conhecimento_pai_filho " +
					     " VALUES('"+nomeConhecimentoPai+"','"+
						  nomeConhecimentoFilho+"');";
			
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

	public boolean desassociaConhecimentos(String nomeConhecimentoPai,
			String nomeConhecimentoFilho) {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " DELETE FROM conhecimento_pai_filho WHERE conhecimento_pai_nome = '" +
					     nomeConhecimentoPai+"' AND conhecimento_filho_nome = '"+
						 nomeConhecimentoFilho+"';";
			
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

	public ArrayList<Conhecimento> getFilhos(String nomeConhecimentoPai)
			throws ConhecimentoInexistenteException {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();
		
		try {
		
			Statement stm = conn.createStatement();
			String SQL = ""; 	
		
			SQL = " SELECT * FROM conhecimento_pai_filho WHERE "+
						 " conhecimento_pai_nome = '"+nomeConhecimentoPai+"';";
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				String nomeConhecimentoFilho = rs.getString(2);
				Conhecimento conhecimento = getConhecimento(nomeConhecimentoFilho);
				
				list.add(conhecimento);
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

	public ArrayList<Conhecimento> getPais(String nomeConhecimentoFilho)
			throws ConhecimentoInexistenteException {
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();
		
		try {
		
			Statement stm = conn.createStatement();
			String SQL = ""; 	
		
			SQL = " SELECT * FROM conhecimento_pai_filho WHERE "+
						 " conhecimento_filho_nome = '"+nomeConhecimentoFilho+"';";
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			while (rs.next()){
				
				String nomeConhecimentoPai = rs.getString(1);
				Conhecimento conhecimento = getConhecimento(nomeConhecimentoPai);
				
				list.add(conhecimento);
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

}
