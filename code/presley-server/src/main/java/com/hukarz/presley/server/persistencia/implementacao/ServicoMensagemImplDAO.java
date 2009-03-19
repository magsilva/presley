package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoMensagem;


public class ServicoMensagemImplDAO implements ServicoMensagem{

	ServicoProblemaImplDAO servicoProblema = new ServicoProblemaImplDAO();

	public boolean adicionarMensagem(ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;		
		try {
			stm = conn.createStatement();

			for (int i = 0; i < desenvolvedoresDestino.size(); i++) {
				String SQL = " INSERT INTO mensagem(desenvolvedor_destino_email, problema_id) VALUES ('"
					+desenvolvedoresDestino.get(i).getEmail()+"','"+problema.getId()+"');";
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
	
	public ArrayList<Mensagem> getMensagens(String emailDesenvolvedorDestino) {
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		ArrayList<Mensagem> mensagens = new ArrayList<Mensagem>();
		Statement stm = null;
	

		try {
			stm = conn.createStatement();

			String sql = "SELECT MEN.problema_id FROM mensagem MEN" +
			" INNER JOIN problema PRO ON PRO.id = MEN.problema_id AND PRO.resolvido = 0"+
			" WHERE MEN.desenvolvedor_destino_email = '"+emailDesenvolvedorDestino+"'";
			ResultSet rs = stm.executeQuery(sql);
			System.out.println(sql);
						
			while(rs.next()) {
				Mensagem msg = new Mensagem();
				Problema pro = new Problema();
				
				int id = rs.getInt(1);
				pro = servicoProblema.getProblema(id);
				
				Desenvolvedor des = pro.getDesenvolvedorOrigem();
				msg.setDesenvolvedorOrigem(des);
				msg.setProblema(pro);
				
				// msg.setTexto(rs.getString(3)); *************
				
				mensagens.add(msg);
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
				return null;
			}
		}
		return mensagens;
	}

}
