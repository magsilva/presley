package persistencia.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.Atividade;
import beans.Conhecimento;
import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contém a implementacao de operações para 
 * administracao de atividades.
 * 
 * Última modificacao: 03/09/2008 por Amilcar Jr
 */

public class ServicoAtividadeImplDAO implements ServicoAtividade{

	public boolean atualizarStatusDaAtividade(int id,boolean terminada) {

		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL;
			
			if(terminada){
			 SQL = " UPDATE atividade SET terminada = 1"+
				   " WHERE id = "+id+";";
			}else{
			 SQL = " UPDATE atividade SET terminada = 0"+
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

	public boolean cadastrarAtividade(String emailDesenvolvedor,
			String emailGerente, String descricao, Date dataInicio, Date dataFim) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO atividade(desenvolvedor_email,gerente_email," +
						 " descricao,dataInicio,dataFim,terminada) " +
					     " VALUES('"+emailDesenvolvedor+"','"+emailGerente+
						 " ','"+descricao+"','"+dataInicio+"','"+dataFim+"',0);";
			
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

	public Atividade getAtividade(int id) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM atividade WHERE "+
						 " id = "+id+";";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				
				Atividade atividade = new Atividade();
				
				atividade.setId(rs.getInt(1));
				atividade.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				atividade.setSupervisor(sd.getDesenvolvedor(rs.getString(3)));
				atividade.setIdPai(rs.getInt(4));
				atividade.setDescricao(rs.getString(5));
				atividade.setDataInicio(rs.getDate(6));
				atividade.setDataFinal(rs.getDate(7));
				atividade.setConcluida(rs.getBoolean(8));
				atividade.setListaDeConhecimentosEnvolvidos(this.getConhecimentosEnvolvidosNaAtividade(rs.getInt(1)));
				
				return atividade;
				
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

	public ArrayList<Conhecimento> getConhecimentosEnvolvidosNaAtividade(
			int idAtividade) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();
		
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM atividade_has_conhecimento WHERE "+
						 " atividade_id = "+idAtividade+" " +
						 " ORDER BY conhecimento_nome;";
				
			
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

	public ArrayList<Atividade> getSubAtividades(int idPai) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		
		ArrayList<Atividade> list = new ArrayList<Atividade>();
		
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM atividade WHERE "+
						 " atividadePai = "+idPai+" ORDER BY desenvolvedor_email, id;";
				
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			
			while (rs.next()){
											
				
				Atividade atividade = new Atividade();
				
				atividade.setId(rs.getInt(1));
				atividade.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				atividade.setSupervisor(sd.getDesenvolvedor(rs.getString(3)));
				atividade.setIdPai(rs.getInt(4));
				atividade.setDescricao(rs.getString(5));
				atividade.setDataInicio(rs.getDate(6));
				atividade.setDataFinal(rs.getDate(7));
				atividade.setConcluida(rs.getBoolean(8));
				atividade.setListaDeConhecimentosEnvolvidos(sd.getConhecimentosDoDesenvolvedor(rs.getString(2)));
				
				
				list.add(atividade);
				
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

	public boolean removerAtividade(int id) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			if (this.atividadeExiste(id)){
				String SQL = " DELETE FROM atividade WHERE " +
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

	public boolean atividadeExiste(int id){
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM atividade WHERE "+
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
	
	public boolean adicionarConhecimentoAAtividade(int idAtividade,
			String nomeConhecimento) {
		
	MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " INSERT INTO atividade_has_conhecimento VALUES ("
						   +idAtividade+",'" +nomeConhecimento+"');";
			
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

	public boolean removerConhecimentoDaAtividade(int idAtividade,
			String nomeConhecimento) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			if (this.atividadeAssociadaAConhecimentoExiste(idAtividade, nomeConhecimento)){
				String SQL = " DELETE FROM atividade_has_conhecimento "+
							 " WHERE atividade_id = "+idAtividade+
							 " AND conhecimento_nome = '"+nomeConhecimento+"';";
				
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

	public boolean atividadeAssociadaAConhecimentoExiste(int idAtividade,
			String nomeConhecimento) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL = " SELECT * FROM atividade_has_conhecimento WHERE "+
						 " atividade_id = "+idAtividade+" " +
						 " AND conhecimento_nome = '"+nomeConhecimento+"' ORDER BY atividade_id;";
				
			
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

	public boolean associarAtividades(int idSubAtividade, int idAtividadePai) {
		
		MySQLConnectionFactory factory = new MySQLConnectionFactory();
		
		Connection conn = factory.getConnection();
		
		try {
		
			Statement stm = conn.createStatement();
		
			String SQL;
			
			if (this.atividadeExiste(idSubAtividade) &&
				this.atividadeExiste(idAtividadePai)){
				
			
				SQL = " UPDATE atividade SET atividadePai = '"+idAtividadePai+"'"+
				      " WHERE id = "+idSubAtividade+";";
				
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

}
