package persistencia.implementacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.TipoAtividade;
import beans.Conhecimento;
import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;

/**
 * 
 * @author Amilcar Jr
 * Essa classe contm a implementacao de operaes para 
 * administracao de atividades.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ServicoAtividadeImplDAO implements ServicoAtividade{

	public boolean atualizarStatusDaAtividade(int id,boolean terminada) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

		return true;

	}

	public int cadastrarAtividade(String emailDesenvolvedor,
			String emailGerente, String descricao, Date dataInicio, Date dataFim) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		int retorno = 0;
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " INSERT INTO atividade(desenvolvedor_email,gerente_email," +
			" descricao,dataInicio,dataFim,terminada) " +
			" VALUES('"+emailDesenvolvedor+"','"+emailGerente+
			" ','"+descricao+"','"+dataInicio+"','"+dataFim+"',0);";

			System.out.println(SQL);
			stm.execute(SQL);
			
			SQL = "SELECT id FROM atividade WHERE descricao = " + descricao + ";" ;
			
			
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			retorno = rs.getInt(1);
			

		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} finally {
			try {
				
				System.out.println("AQUI");
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}
		return retorno;

	}

	public TipoAtividade getAtividade(int id) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM atividade WHERE "+
			" id = "+id+";";


			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				TipoAtividade tipoAtividade = new TipoAtividade();

				tipoAtividade.setId(rs.getInt(1));
				tipoAtividade.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				tipoAtividade.setSupervisor(sd.getDesenvolvedor(rs.getString(3)));
				tipoAtividade.setIdPai(rs.getInt(4));
				tipoAtividade.setDescricao(rs.getString(5));
				tipoAtividade.setDataInicio(rs.getDate(6));
				tipoAtividade.setDataFinal(rs.getDate(7));
				tipoAtividade.setConcluida(rs.getBoolean(8));
				//tipoAtividade.setListaDeConhecimentosEnvolvidos(this.getConhecimentosEnvolvidosNaAtividade(rs.getInt(1)));

				return tipoAtividade;

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
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public ArrayList<Conhecimento> getConhecimentosEnvolvidosNaAtividade(
			int idAtividade) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		ArrayList<Conhecimento> list = new ArrayList<Conhecimento>();

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public ArrayList<TipoAtividade> getSubAtividades(int idPai) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();

		ArrayList<TipoAtividade> list = new ArrayList<TipoAtividade>();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM atividade WHERE "+
			" atividadePai = "+idPai+" ORDER BY desenvolvedor_email, id;";


			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);


			while (rs.next()){


				TipoAtividade tipoAtividade = new TipoAtividade();

				tipoAtividade.setId(rs.getInt(1));
				tipoAtividade.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				tipoAtividade.setSupervisor(sd.getDesenvolvedor(rs.getString(3)));
				tipoAtividade.setIdPai(rs.getInt(4));
				tipoAtividade.setDescricao(rs.getString(5));
				tipoAtividade.setDataInicio(rs.getDate(6));
				tipoAtividade.setDataFinal(rs.getDate(7));
				tipoAtividade.setConcluida(rs.getBoolean(8));
				//tipoAtividade.setListaDeConhecimentosEnvolvidos(sd.getConhecimentosDoDesenvolvedor(rs.getString(2)));


				list.add(tipoAtividade);

			}
			
			return list;

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

	public boolean removerAtividade(int id) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public boolean atividadeExiste(int id){

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public boolean adicionarConhecimentoAAtividade(int idAtividade,
			String nomeConhecimento) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

		stm = conn.createStatement();

			String SQL = " INSERT INTO atividade_has_conhecimento VALUES ("
				+idAtividade+",'" +nomeConhecimento+"');";

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
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

		return true;

	}

	public boolean removerConhecimentoDaAtividade(int idAtividade,
			String nomeConhecimento) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}


	}

	public boolean atividadeAssociadaAConhecimentoExiste(int idAtividade,
			String nomeConhecimento) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public boolean associarAtividades(int idSubAtividade, int idAtividadePai) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL;

			if (this.atividadeExiste(idSubAtividade) &&
					this.atividadeExiste(idAtividadePai)){


				SQL = " UPDATE atividade SET atividadePai = '"+idAtividadePai+
				"' WHERE id = '"+idSubAtividade+"';";

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
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}		
	}

	public boolean desassociarAtividades(int idSubAtividade, int idAtividadePai) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;;

		try {

			stm = conn.createStatement();

			String SQL;

			if (this.atividadeExiste(idSubAtividade) &&
					this.atividadeExiste(idAtividadePai)){


				SQL = " UPDATE atividade SET atividadePai = '0'"+
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
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public ArrayList<TipoAtividade> listarAtividades() {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();

		ArrayList<TipoAtividade> list = new ArrayList<TipoAtividade>();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM atividade";


			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){
				
				TipoAtividade tipoAtividade = new TipoAtividade();

				tipoAtividade.setId(rs.getInt(1));
				//tipoAtividade.setDesenvolvedor(sd.getDesenvolvedor(rs.getString(2)));
				//tipoAtividade.setSupervisor(sd.getDesenvolvedor(rs.getString(3)));
				tipoAtividade.setIdPai(rs.getInt(4));
				tipoAtividade.setDescricao(rs.getString(5));
				tipoAtividade.setDataInicio(rs.getDate(6));
				tipoAtividade.setDataFinal(rs.getDate(7));
				tipoAtividade.setConcluida(rs.getBoolean(8));
				//tipoAtividade.setListaDeConhecimentosEnvolvidos(sd.getConhecimentosDoDesenvolvedor(rs.getString(2)));

				list.add(tipoAtividade);

			}
			return list;


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
}

