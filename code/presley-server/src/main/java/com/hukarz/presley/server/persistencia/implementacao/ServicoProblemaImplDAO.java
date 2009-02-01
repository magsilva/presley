package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;

public class ServicoProblemaImplDAO implements ServicoProblema{

	public boolean atualizarStatusDoProblema(int id, boolean status) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL;

			if(status){
				SQL = " UPDATE problema SET resolvido = 1"+
				" WHERE id = "+id+";";
			}else{
				SQL = " UPDATE problema SET resolvido = 0"+
				" WHERE id = "+id+";";
			}
			stm.execute(SQL);

		} catch (SQLException e) {
			//e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;

	}

	public boolean cadastrarProblema(Problema problema) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Map<ClasseJava, ArquivoJava> arquivosAssociados = new HashMap<ClasseJava, ArquivoJava>();
		arquivosAssociados = problema.getClassesRelacionadas();

		Statement stm = null;

		try {
			stm = conn.createStatement();

			String SQL = " INSERT INTO problema(descricao,dataRelato,mensagem,desenvolvedor_email, resolvido, conhecimento_nome) " +
			" VALUES ('"+problema.getDescricao()+"','"+problema.getData()+"','"+problema.getMensagem()+"', '"+
			problema.getDesenvolvedorOrigem().getEmail()+"', 0, '"+ problema.getConhecimento().getNome() +"');";	
			System.out.println(SQL);
			stm.execute(SQL);

			SQL = " SELECT id FROM problema"+
			" WHERE descricao = '"+problema.getDescricao()+"' AND dataRelato = '"+problema.getData()+"' AND "+
			" mensagem = '"+problema.getMensagem()+"' AND desenvolvedor_email = '"+problema.getDesenvolvedorOrigem().getEmail()+"';";	
			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);
			
			if (rs.next()){
				problema.setId( rs.getInt("id") );
				
				//percorre o map  
				for (Iterator<ClasseJava> it = arquivosAssociados.keySet().iterator(); it.hasNext();) {  
					ClasseJava classe = it.next();  
					ArquivoJava arquivoJava = arquivosAssociados.get(classe);  

					SQL = "INSERT INTO problema_has_classe(problema_id, arquivo_id, classe) " + 
					" VALUES ( " + problema.getId()+",'"+arquivoJava.getId()+"','"+ classe.getNomeClasse() + "');" ;
					System.out.println(SQL);
					stm.execute(SQL);
				} 
			}
	         

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}

		return true;

	}

	public boolean removerProblema(Problema problema) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		try {

			stm = conn.createStatement();

			if (this.problemaExiste(problema.getId())){
				
				String SQL = "DELETE FROM problema_has_classe WHERE problema_id = " + problema.getId() + ";" ;
				System.out.println(SQL);
				stm.execute(SQL);
				
				SQL = " DELETE FROM problema WHERE id = "+problema.getId()+";";
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
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public boolean problemaExiste(int id) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " SELECT * FROM problema WHERE "+
			" id = "+id+" ORDER BY id;";


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
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public Problema getProblema(int id) {

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoConhecimento servicoConhecimento = new ServicoConhecimentoImplDAO();

		try {

			stm = conn.createStatement();

			String SQL = " SELECT id, desenvolvedor_email, descricao, resolvido, dataRelato, mensagem, conhecimento_nome "+
			" FROM problema WHERE id = "+id+";";


			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				Problema p = new Problema();

				p.setId( rs.getInt("id") );
				p.setDescricao( rs.getString("descricao") );
				p.setResolvido( rs.getBoolean("resolvido") );
				p.setData( rs.getDate("dataRelato") );
				p.setMensagem( rs.getString("mensagem") );
				p.setDesenvolvedorOrigem( sd.getDesenvolvedor( rs.getString("desenvolvedor_email") ) ) ;

				p.setConhecimento( servicoConhecimento.getConhecimento(rs.getString("conhecimento_nome")) );
				return p;
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
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

	public ArrayList<Problema> getListaProblemas(Desenvolvedor desenvolvedor) {
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();

		Statement stm = null;

		ArrayList<Problema> list = new ArrayList<Problema>();
		ServicoDesenvolvedor sd = new ServicoDesenvolvedorImplDAO();
		ServicoConhecimento sc = new ServicoConhecimentoImplDAO();
		try {

			stm = conn.createStatement();

			String SQL = " SELECT id, desenvolvedor_email, descricao, resolvido, dataRelato, mensagem, conhecimento_nome" +
					" FROM problema WHERE resolvido = 0 and desenvolvedor_email = '"+ desenvolvedor.getEmail() +"' ORDER BY descricao;";

			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){

				Problema p = new Problema();

				p.setId(rs.getInt("id"));
				p.setDesenvolvedorOrigem( sd.getDesenvolvedor(rs.getString("desenvolvedor_email")));
				p.setDescricao(rs.getString("descricao"));
				p.setResolvido(rs.getBoolean("resolvido"));
				p.setData(rs.getDate("dataRelato"));
				p.setMensagem(rs.getString("mensagem"));
				p.setConhecimento( sc.getConhecimento(rs.getString("conhecimento_nome")) );
				
				list.add(p);

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
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}
	
	public ArrayList<String> getConhecimentosAssociados(String nomeProblema) {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;
		ArrayList<String> list = new ArrayList<String>();


		try {

			stm = conn.createStatement();

			//SELECT * FROM problema_has_conhecimento where problema_nome = 'Problema JUNIT';
			
			String SQL = " SELECT conhecimento_nome FROM problema_has_conhecimento where problema_nome = '"+nomeProblema+"';";

			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			while (rs.next()){
				list.add(rs.getString(1));
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
				System.out.println(" Houve erro no fechamento da conexão ");
				onConClose.printStackTrace();	             
			}
		}
	}

}
