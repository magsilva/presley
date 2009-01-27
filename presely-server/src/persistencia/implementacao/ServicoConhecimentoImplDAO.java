package persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import beans.Arquivo;
import beans.Conhecimento;
import beans.Desenvolvedor;

import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoArquivo;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;
import excessao.ConhecimentoInexistenteException;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contem a implementacao das operacoes para administrar um conhecimento.
 * 
 * �ltima modificacao: 16/09/2008 por RodrigoCMD
 */

public class ServicoConhecimentoImplDAO implements ServicoConhecimento{
	ServicoArquivo servicoArquivo= new ServicoArquivoImplDAO();
	ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();

	public boolean atualizarConhecimento(String nome, String novoNome,
			String descricao) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public boolean conhecimentoExiste(String nome) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public boolean criarConhecimento(String nome, String descricao) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();
			
			/*Gambiarra*/ descricao = "";

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public boolean removerConhecimento(String nome) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public Conhecimento getConhecimento(String nome) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = ""; 	
			if (nome != null) {
				SQL = " SELECT nome, descricao FROM conhecimento WHERE "+
				" nome = '"+nome+"';";
			} else {
				SQL = " SELECT nome, descricao FROM conhecimento;";
			}

			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				Conhecimento conhecimento = new Conhecimento();

				conhecimento.setNome(rs.getString("nome"));
				conhecimento.setDescricao(rs.getString("descricao"));
				nome = rs.getString("nome") ;
				
				SQL = " SELECT arquivo_id, arquivo_nome, endereco_servidor FROM conhecimento_has_arquivo" +
				" INNER JOIN arquivo ON id = arquivo_id" +
				" WHERE conhecimento_nome = '"+nome+"';";
				rs = stm.executeQuery(SQL);

				while (rs.next()) {
					Arquivo arquivo = new Arquivo( rs.getString("arquivo_nome") );
					arquivo.setEnderecoServidor(rs.getString("endereco_servidor"));
					conhecimento.adcionaArquivo( servicoArquivo.getArquivo(arquivo) ) ;
				}
					
				return conhecimento;

			}else{
				return null;
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} finally {
			try {
				stm.close();
				conn.close();	            
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}		
	}

	public boolean associaConhecimentos(String nomeConhecimentoPai,
			String nomeConhecimentoFilho) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public boolean desassociaConhecimentos(String nomeConhecimentoPai,
			String nomeConhecimentoFilho) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	public ArrayList<Conhecimento> getFilhos(String nomeConhecimentoPai)
	throws ConhecimentoInexistenteException {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();

		try {

			stm = conn.createStatement();
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
				stm.close();
				conn.close();	            
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return list;
	}

	public ArrayList<Conhecimento> getPais(String nomeConhecimentoFilho)
	throws ConhecimentoInexistenteException {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();

		try {

			stm = conn.createStatement();
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
				stm.close();
				conn.close();	            
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return list;
	}

	public ArrayList<Conhecimento> getListaConhecimento() {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();

		try {

			stm = conn.createStatement();
			String SQL = ""; 	

			SQL = " SELECT nome, descricao FROM conhecimento;";

			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){

				String nomeConhecimentoPai = rs.getString("nome");
				Conhecimento conhecimento = getConhecimento(nomeConhecimentoPai);
				
				System.out.println("Adicionando novo conhecimento a lista");
				
				list.add(conhecimento);
			}

		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			System.out.println("NullPointerException na classe ServicoConhecimentoImplDAO..");
			e.printStackTrace();
		} finally {
			try {
				stm.close();
				conn.close();	            
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return list;
	}

	public boolean associaArquivo(Conhecimento conhecimento, Arquivo arquivo) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " INSERT INTO conhecimento_has_arquivo (conhecimento_nome, arquivo_id)" +
			" VALUES ('"+conhecimento.getNome()+"',"+ arquivo.getId()+");";

			stm.execute(SQL);
		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return true;
	}

	
	public ArrayList<Desenvolvedor> getContribuintesConhecimento(Conhecimento conhecimento){
		// O ArrayList tem que permitir repeti��es
		ArrayList<Desenvolvedor> retorno = new ArrayList<Desenvolvedor>();
		
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = ""; 	

			// Retorna os desenvolvedores que j� tiveram problemas relacionados a esse conhecimento 
			SQL = "SELECT desenvolvedor_email FROM problema " +
 					" WHERE resolvido = 1 and conhecimento_nome = '"+ conhecimento.getNome() +"'";

			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){
				Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedor( rs.getString("desenvolvedor_email") ) ;
				retorno.add(desenvolvedor);
			}

			// Retorna os desenvolvedores com contribui��es aos problemas neste conhecimeto
			SQL = "SELECT s.desenvolvedor_email FROM problema p " +
					" INNER JOIN solucao s ON s.problema_id = p.id AND s.resolveu = 1 " +
					" WHERE p.conhecimento_nome = '"+ conhecimento.getNome() +"' " ;

			while (rs.next()){
				Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedor( rs.getString("desenvolvedor_email") ) ;
				retorno.add(desenvolvedor);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			System.out.println("NullPointerException na classe ServicoConhecimentoImplDAO..");
			e.printStackTrace();
		} finally {
			try {
				stm.close();
				conn.close();	            
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}

		return new ArrayList<Desenvolvedor>();
	}

}
