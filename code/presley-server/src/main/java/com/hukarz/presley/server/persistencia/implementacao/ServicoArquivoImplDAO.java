package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;

public class ServicoArquivoImplDAO implements ServicoArquivo {

	@Override
	public boolean arquivoExiste(Arquivo arquivo) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {
			stm = conn.createStatement();

			String SQL = " SELECT id FROM arquivo "+
			" WHERE arquivo_nome = '"+arquivo.getNome()+"' and "+
			" endereco_servidor = '"+arquivo.getEnderecoServidor()+"';";

			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			return rs.next();
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

	@Override
	public boolean atualizarArquivo(Arquivo arquivoAnterior, Arquivo arquivoNovo) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " UPDATE arquivo SET arquivo_nome = '"+arquivoNovo.getNome()+"',"+
			" endereco_servidor = '"+arquivoNovo.getEnderecoServidor()+"', endereco_log = '"+arquivoNovo.getEnderecoLog() +"'"+
			" WHERE id = "+arquivoAnterior.getId()+";";

			//System.out.println(SQL);
			stm.execute(SQL);

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

		return true;
	}

	@Override
	public boolean criarArquivo(Arquivo arquivo) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		try {
			
			stm = conn.createStatement();
			
			String SQL = " INSERT INTO arquivo (arquivo_nome, endereco_servidor, quantidadePalavras, endereco_log)" +
			" VALUES('"+arquivo.getNome()+"','"+arquivo.getEnderecoServidor()+"', "+arquivo.getQtdPalavrasTotal()+", '"+arquivo.getEnderecoLog()+"');";

			//System.out.println(SQL);
			stm.execute(SQL);

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

		return true;
	}

	@Override
	public Arquivo getArquivo(Arquivo arquivo) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = ""; 	
			if (arquivo != null) {
				SQL = " SELECT id, arquivo_nome, endereco_servidor, quantidadePalavras, endereco_log FROM arquivo "+
				" WHERE arquivo_nome = '"+ arquivo.getNome() +"' AND endereco_servidor = '"+ arquivo.getEnderecoServidor() +"' ;";
			} else {
				SQL = " SELECT id, arquivo_nome, endereco_servidor, quantidadePalavras, endereco_log FROM arquivo;";
			}

			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){
				Arquivo arquivoRetorno = new Arquivo( rs.getString("arquivo_nome") );

				arquivoRetorno.setId( rs.getInt("id"));
				arquivoRetorno.setEnderecoServidor(rs.getString("endereco_servidor"));
				arquivoRetorno.setQtdPalavrasTotal(rs.getInt("quantidadePalavras"));
				arquivoRetorno.setEnderecoLog(rs.getString("endereco_log"));
				
				SQL = "SELECT AP.quantidade, P.palavra " +
						" FROM arquivo_has_palavras AS AP" +
						" INNER JOIN palavra AS P ON P.id = AP.palavra_id " +
						" WHERE AP.arquivo_id ="+ arquivoRetorno.getId()+";";
				rs = stm.executeQuery(SQL);
				
				while (rs.next()){
					arquivoRetorno.adicionaTermo(rs.getString("palavra"), rs.getInt("quantidade"));
				}
				
				return arquivoRetorno;
			}else{
				return null;
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
	}

	@Override
	public boolean removerArquivo(Arquivo arquivo) {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			if (this.arquivoExiste(arquivo)){
				String SQL = " DELETE FROM arquivo WHERE id = "+arquivo.getId()+";";

				//System.out.println(SQL);
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
				//conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

	}

	public Arquivo associaPalavrasArquivo(Arquivo arquivo, Map<String, Integer> termosSelecionados){

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " DELETE FROM arquivo_has_palavras " +
						 " WHERE arquivo_id = "+arquivo.getId()+";";

			// System.out.println(SQL);
			stm.execute(SQL);

			Set<String> palavras = termosSelecionados.keySet();
			for (Iterator<String> iterator = palavras.iterator(); iterator.hasNext();) {
				String palavra = iterator.next();
				
				SQL = " INSERT INTO arquivo_has_palavras (arquivo_id, palavra_id, quantidade)" +
				 " VALUES ("+ arquivo.getId()+","+retornarIdPalavra(palavra)+","+  termosSelecionados.get(palavra) +");";

				stm.execute(SQL);
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

		
		arquivo.setTermosSelecionados(termosSelecionados);
		return arquivo;
	}
	
	private int retornarIdPalavra(String palavra) throws SQLException {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;
		int id = 0 ;
		
		stm = conn.createStatement();
		String SQL = "SELECT id FROM palavra WHERE palavra = '"+palavra+"';";
		ResultSet rs = stm.executeQuery(SQL);
		
		if (! rs.next() ){
			SQL = "INSERT INTO palavra (palavra) VALUES ('"+palavra+"');" ;
			stm.execute(SQL);
			
			SQL = "SELECT id FROM palavra WHERE palavra = '"+palavra+"';";
			rs = stm.executeQuery(SQL);
			rs.next();
		}
		
		id = rs.getInt("id");
		stm.close();
		//conn.close();
		
		return id;
	}

	@Override
	public int getQuantidadeArquivosCadastrados() {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;
		int qtde = 0 ;
		
		try {
			stm = conn.createStatement();
			String SQL = "SELECT COUNT(*) AS qtde FROM arquivo a;";
			ResultSet rs = stm.executeQuery(SQL);
			
			if ( rs.next() ){
				qtde = rs.getInt("qtde");
			}
			
			stm.close();
			//conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return qtde;
	}

	@Override
	public int getQuantidadeArquivosComTermo(String termo) {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;
		int qtde = 0 ;
		
		try {
			stm = conn.createStatement();
			
			String SQL = "SELECT COUNT(arquivo_id) AS qtde FROM palavra p" +
					" INNER JOIN arquivo_has_palavras ap ON ap.palavra_id = p.id" +
					" WHERE palavra = '"+ termo +"'" ; 
				
			ResultSet rs = stm.executeQuery(SQL);
			
			if ( rs.next() ){
				qtde = rs.getInt("qtde");
			}
			
			stm.close();
			//conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qtde;
	}

	@Override
	public ArrayList<Arquivo> getListaArquivo() {
		ArrayList<Arquivo> listaRetorno = new ArrayList<Arquivo>();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {
			stm = conn.createStatement();
			String SQL = " SELECT id, arquivo_nome, endereco_servidor, " +
					" quantidadePalavras, endereco_log FROM arquivo;";

			//System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()) {
				Arquivo arquivoRetorno = new Arquivo( rs.getString("arquivo_nome") );

				arquivoRetorno.setId( rs.getInt("id"));
				arquivoRetorno.setEnderecoServidor(rs.getString("endereco_servidor"));
				arquivoRetorno.setQtdPalavrasTotal(rs.getInt("quantidadePalavras"));
				arquivoRetorno.setEnderecoLog( rs.getString("endereco_log") );
				
				SQL = "SELECT AP.quantidade, P.palavra " +
						" FROM arquivo_has_palavras AS AP" +
						" INNER JOIN palavra AS P ON P.id = AP.palavra_id " +
						" WHERE AP.arquivo_id ="+ arquivoRetorno.getId()+";";
				Statement stm2 = conn.createStatement();
				ResultSet rs2 = stm2.executeQuery(SQL);
				
				while (rs2.next()){
					arquivoRetorno.adicionaTermo(rs2.getString("palavra"), rs2.getInt("quantidade"));
				}
				stm2.close();
				
				listaRetorno.add(arquivoRetorno);
			}

			return listaRetorno;
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
	}

}
