package com.hukarz.presley.server.persistencia.implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.server.persistencia.MySQLConnectionFactory;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;

public class ServicoLogControleVersaoLine10ImplDAO extends
		ServicoLogControleVersaoImplDAO {

	@Override
	public ArrayList<Desenvolvedor> getDesenvolvedoresArquivo(
			ArquivoJava arquivoJava, Date data) {
		ArrayList<Desenvolvedor> retorno = new ArrayList<Desenvolvedor>();
		ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
		
		Connection conn = MySQLConnectionFactory.open();

		try {

			String SQL =
				"SELECT desenvolvedor_email, MAX(data_hora) as data_hora_10" +
				"	FROM log_controle_versao" +
				"	WHERE arquivo_id = ? AND data_hora <= ?" +
				"	GROUP BY desenvolvedor_email" +
				"	ORDER BY data_hora_10";
			PreparedStatement stmt = conn.prepareStatement(SQL);

			stmt.setInt(1, arquivoJava.getId());
			stmt.setTimestamp(2, new java.sql.Timestamp( data.getTime() ) );
			
			ResultSet rs = stmt.executeQuery();
		
			while (rs.next()){
				retorno.add(0, servicoDesenvolvedor.getDesenvolvedor( 
						rs.getString("desenvolvedor_email") ) ) ;
			}

			stmt.close();
			
			return retorno;
		} catch (SQLException e) {
			return null;
		}		

	}

	
}
