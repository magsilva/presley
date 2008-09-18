package persistencia.implementacao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import persistencia.MySQLConnectionFactory;
import excessao.DesenvolvedorInexistenteException;
import beans.Desenvolvedor;

public class ServicoInferenciaDAO {
	
	public HashMap<Desenvolvedor, Double> getDesenvolvedoresByConhecimento(String conhecimento) throws DesenvolvedorInexistenteException{
		
		//Connection conn = MySQLConnectionFactory.getConnection();
		Connection conn = MySQLConnectionFactory.open();
		
		Statement stm = null;
		
		HashMap<Desenvolvedor, Double> mCand = new HashMap<Desenvolvedor, Double>();
		
		try{
			stm = conn.createStatement();
			
			String sql = "select email, nome, localidade, grau, qtd_resposta from " +
						 "desenvolvedor_has_conhecimento as dc, desenvolvedor where " +
						 "dc.conhecimento_nome = '"+conhecimento+"' and dc.desenvolvedor_email = email and " +
						 		" grau > 0";
			
			ResultSet rs = stm.executeQuery(sql);
			
			if(!rs.next()) throw new DesenvolvedorInexistenteException();
			
			while(rs.next()){
				
					/* Instanciando e iniciando valores do Desenvolvedor */
					Desenvolvedor d = new Desenvolvedor();
					d.setEmail(rs.getString(1));
					d.setNome(rs.getString(2));
					d.setLocalidade(rs.getString(3));
					/* -------------------------------------------------- */
					
					/* Ajustando o 'valor' de seu conhecimento em um dado conhecimento */
					int grau = rs.getInt(4);
					int qtd = rs.getInt(5);
					double valor = ((double)((qtd*4)+(grau*6)))/10;
					/* -------- */

					/* Adicionando o candidato na tabela */
					mCand.put(d, valor);
					/* --------- */
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		} finally {
			try {
				stm.close();
				conn.close();
			} catch (SQLException onConClose) {
				System.out.println(" Houve erro no fechamento da conexo ");
				onConClose.printStackTrace();	             
			}
		}
		
		return null;
	}
	
}
