package persistencia.implementacao;

import beans.Projeto;
import persistencia.MySQLConnectionFactory;
import persistencia.interfaces.ServicoProjeto;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ServicoProjetoImplDAO implements ServicoProjeto {

	@Override
	public Projeto getProjetoAtivo() {
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;

		try {

			stm = conn.createStatement();

			String SQL = " SELECT nome, ativo, endereco_Servidor_Leitura, endereco_Servidor_Gravacao FROM projeto WHERE ativo = 1";


			System.out.println(SQL);
			ResultSet rs = stm.executeQuery(SQL);

			if (rs.next()){

				Projeto projeto = new Projeto();

				projeto.setAtivo(true);
				projeto.setNome(rs.getString("nome"));
				projeto.setEndereco_Servidor_Gravacao( rs.getString("endereco_Servidor_Gravacao") ) ;
				projeto.setEndereco_Servidor_Leitura( rs.getString("endereco_Servidor_Leitura") ) ;
				
				return projeto;

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

}
