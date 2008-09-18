package persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import persistencia.MySQLConnectionFactory;
import beans.Desenvolvedor;
import beans.Problema;

public class ServicoMensagemImplDAO {
	
	public boolean adicionarMensagem(Desenvolvedor desenvolvedorOrigem, ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema, String texto) {
		
		//MySQLConnectionFactory factory = new MySQLConnectionFactory();

		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;		

		try {
			stm = conn.createStatement();

			for (int i = 0; i < desenvolvedoresDestino.size(); i++) {
			String SQL = " INSERT INTO mensagem VALUES ('"
				+desenvolvedorOrigem.getEmail()+"','" +desenvolvedoresDestino.get(i)+"','"+problema.getId()+"','"+texto+"');";
			System.out.println(SQL);
			stm.execute(SQL);
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
				return false;
			}
		}
		return true;
	}
	
	public String[] getMensagens(Desenvolvedor desenvolvedorDestino) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		String[] m;

		try {
			stm = conn.createStatement();

			String sql = "select mensagem from mensagem where " +
			 "mensagem.desenvolvedor_destino_email = '"+desenvolvedorDestino.getEmail();
			ResultSet rs = stm.executeQuery(sql);
			ArrayList<String> mensagens = new ArrayList<String>();
						
			while(rs.next()) {
				mensagens.add(rs.getString(1));
			}
			
			m = new String[mensagens.size()];
			
			for (int i = 0; i < mensagens.size(); i++)
				m[i] = mensagens.get(i);

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
				return null;
			}
		}
		return m;
	}

}
