package persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.TipoAtividade;
import beans.Conhecimento;
import beans.Desenvolvedor;
import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contem a implementacao das operacoes que um desenvolvedor
 * pode realizar sobre o sistema.
 * 
 * Última modificacao: 03/09/2008 por Amilcar Jr
 */

public class ServicoDesenvolvedorImplDAO implements ServicoDesenvolvedor{

	public boolean adicionarConhecimentoAoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO desenvolvedor_has_conhecimento VALUES ('"
						   +emailDesenvolvedor+"','" +nomeConhecimento+"');";
			
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

	public boolean atualizarDesenvolvedor(String email, String novoEmail, String nome,
			String localidade) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " UPDATE desenvolvedor SET email = '"+novoEmail+"',"+
						 " nome = '"+nome+"', localidade = '"+localidade+"'"+
						 " WHERE email = '"+email+"';";
			
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

	public boolean criarDesenvolvedor(String email, String nome,
			String localidade) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO desenvolvedor " +
					     " VALUES('"+email+"','"+
						  nome+"','"+localidade+"');";
			
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

	public boolean desenvolvedorExiste(String email) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM desenvolvedor WHERE "+
						 " email = '"+email+"';";
				
			
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

	public boolean removerConhecimentoDoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
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
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
		
	}
	
	public boolean conhecimentoDoDesenvolvedorExiste(String emailDesenvolvedor, String nomeConhecimento){
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM desenvolvedor_has_conhecimento WHERE "+
						 " desenvolvedor_email = '"+emailDesenvolvedor+"' " +
						 " AND conhecimento_nome = '"+nomeConhecimento+"';";
				
			
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

	public boolean removerDesenvolvedor(String email) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
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
	             conn.close();
	           } catch (SQLException onConClose) {
	             System.out.println(" Houve erro no fechamento da conexão ");
	             onConClose.printStackTrace();	             
	           }
	         }
				
	}

	public Desenvolvedor getDesenvolvedor(String email) {
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM desenvolvedor WHERE "+
						 " email = '"+email+"';";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				
				Desenvolvedor desenvolvedor = new Desenvolvedor();
				
				desenvolvedor.setEmail(rs.getString(1));
				desenvolvedor.setNome(rs.getString(2));
				desenvolvedor.setLocalidade(rs.getString(3));
				desenvolvedor.setListaConhecimento(this.getConhecimentosDoDesenvolvedor(rs.getString(1)));
				
				return desenvolvedor;
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

	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();
		
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM desenvolvedor_has_conhecimento WHERE "+
						 " desenvolvedor_email = '"+email+"' ORDER BY conhecimento_nome;";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			
			while (rs.next()){
				
				//pegar chave do conhecimento
				String nomeConhecimento = rs.getString(2);
				ServicoConhecimento sc = new ServicoConhecimentoImplDAO();
				
				Conhecimento conhecimento = sc.getConhecimento(nomeConhecimento);
								
				list.add(conhecimento);
				
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

	public ArrayList<TipoAtividade> getAtividadesDoDesenvolvedor(String email) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ArrayList<TipoAtividade> list = new ArrayList<TipoAtividade>();
		
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
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
				tipoAtividade.setListaDeConhecimentosEnvolvidos(this.getConhecimentosDoDesenvolvedor(rs.getString(2)));
				
				
				list.add(tipoAtividade);
				
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
