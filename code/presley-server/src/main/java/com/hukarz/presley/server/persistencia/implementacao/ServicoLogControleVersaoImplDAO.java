package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoLogControleVersao;

public class ServicoLogControleVersaoImplDAO implements
		ServicoLogControleVersao {
	
	public Date getDataHoraUltimoRegistro() {
		Connection conn = MySQLConnectionFactory.open();
		Statement stm = null;

		try {

			stm = conn.createStatement();
			String SQL = "SELECT MAX(data_hora) data_hora FROM log_controle_versao"; 	

			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){
				return rs.getTimestamp("data_hora") ;
			}else{
				return null;
			}

		} catch (SQLException e) {
			return null;
		} finally {
			try {
				stm.close();
				//conn.close();	            
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conex�o ");
				onConClose.printStackTrace();	             
			}
		}		

	}

	public void registrarLogDeRevisao(Desenvolvedor desenvolvedor,
			ArrayList<ArquivoJava> arquivosAcessados, Date data) {
		Connection conn = MySQLConnectionFactory.open();
		
		try {

			String SQL = " INSERT INTO log_controle_versao (desenvolvedor_email, arquivo_id, data_hora) " +
			" VALUES(?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(SQL);
			
			for (ArquivoJava arquivoJava : arquivosAcessados) {
				stmt.setString(1, desenvolvedor.getEmail());  
				stmt.setInt(2, arquivoJava.getId());  
				stmt.setTimestamp(3, new java.sql.Timestamp( data.getTime() ) );
				
				stmt.executeUpdate();
			}
			stmt.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}

	}

	public ArrayList<Desenvolvedor> getDesenvolvedoresArquivo(
			ArquivoJava arquivoJava, Date data) {
		ArrayList<Desenvolvedor> retorno = new ArrayList<Desenvolvedor>();
		ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
		
		Connection conn = MySQLConnectionFactory.open();

		try {

			String SQL = "SELECT desenvolvedor_email FROM log_controle_versao " +
					" WHERE arquivo_id = ? AND (data_hora BETWEEN ? AND ?)";
					//" GROUP BY desenvolvedor_email"; 	AND YEAR(data_hora) >= 2008
			PreparedStatement stmt = conn.prepareStatement(SQL);

			stmt.setInt(1, arquivoJava.getId());
			
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(data);
			cal.add(Calendar.MONTH, -1);
			
			stmt.setTimestamp(2, new java.sql.Timestamp( cal.getTime().getTime() ) );
						
			stmt.setTimestamp(3, new java.sql.Timestamp( data.getTime() ) );
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()){
				retorno.add( servicoDesenvolvedor.getDesenvolvedor( 
						rs.getString("desenvolvedor_email") ) ) ;
			}

			stmt.close();
			
			return retorno;
		} catch (SQLException e) {
			return null;
		}		
	}

}
