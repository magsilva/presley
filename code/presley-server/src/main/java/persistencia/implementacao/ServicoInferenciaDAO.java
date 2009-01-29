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
		Desenvolvedor d = null;
		
		Statement stm = null;
		
		HashMap<Desenvolvedor, Double> mCand = new HashMap<Desenvolvedor, Double>();
		
		try{
			stm = conn.createStatement();
			
			String sql = "select desenvolvedor_email, grau, qtd_resposta " +
						 " from desenvolvedor_has_conhecimento as dc, desenvolvedor " +
						 " where dc.conhecimento_nome = '"+conhecimento+"' and dc.desenvolvedor_email = email " +
 				 		 " and grau > 0";
			
			ResultSet rs = stm.executeQuery(sql);
			
			System.out.println("SQL DA INFERENCIA: "+sql);
			//if(!rs.next()) throw new DesenvolvedorInexistenteException();
			
			while(rs.next()){
					System.out.println("DENTRO DO LACO DA INFERENCIA");
					/* Instanciando e iniciando valores do Desenvolvedor */
					d = new Desenvolvedor();
					d.setEmail(rs.getString(1));
					
					// Ajustando o 'valor' de seu conhecimento em um dado conhecimento 
					int grau = rs.getInt(2);
					int qtd = rs.getInt(3);
					double valor = ((double)((qtd*4)+(grau*6)))/10;
					 
					// Adicionando o candidato na tabela 
					mCand.put(d, valor);
			}
			if(d == null) {
				System.out.println("desenvolvedor null");
			}
			System.out.println("Email do desenvolvedor candidato: "+d.getEmail());
			System.out.println("Valor do desenvolvedor candidato: "+mCand.get(d));
			return mCand;
			
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
