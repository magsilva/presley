package inferencia;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import persistencia.MySQLConnectionFactory;
import beans.Desenvolvedor;

/*
 * Created on 09/09/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Presley
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Inferencia {
	
	public static void main(String args[]){
		ArrayList<Desenvolvedor> lDesenv = getDesenvolvedores(new String[]{"java", "conector"}, 50);
		
		for(Desenvolvedor d : lDesenv){
			System.out.println(d);
		}
		
	}
	
	public static ArrayList<Desenvolvedor> getDesenvolvedores(String[] conhecimentos, double conf){
    
		//MySQLConnectionFactory factory = new MySQLConnectionFactory();
		int max = conhecimentos.length;
		int cont = 1;
		
		Connection conn = MySQLConnectionFactory.getConnection();
		
		Map<String, Double> mCand = new HashMap<String, Double>();
		Map<String, Desenvolvedor> mDes = new HashMap<String, Desenvolvedor>();
		
		for(String c : conhecimentos){
		
			try{
				
				Statement stm = conn.createStatement();
				
				String sql = "select email, nome, localidade, grau, qtd_resposta from " +
							 "desenvolvedor_has_conhecimento as dc, desenvolvedor where " +
							 "dc.conhecimento_nome = '"+c+"' and dc.desenvolvedor_email = email and " +
							 		" grau > 0";
				
				ResultSet rs = stm.executeQuery(sql);
				
				while(rs.next()){
					
					/* Instanciando e iniciando valores do Desenvolvedor */
					Desenvolvedor d = new Desenvolvedor();
					d.setEmail(rs.getString(1));
					d.setNome(rs.getString(2));
					d.setLocalidade(rs.getString(3));
					/* -------------------------------------------------- */
					
					mDes.put(d.getEmail(), d);
					
					/* Ajustando o 'valor' de seu conhecimento em um dado conhecimento */
					int grau = rs.getInt(4);
					int qtd = rs.getInt(5);
					double valor = ((double)((qtd*4)+(grau*6)))/10;
					valor = valor * ((double)cont/max);
					/* -------- */

					/* Verifica se o candidato ja existe na tabela */
					if(mCand.containsKey(d.getEmail())){
						mCand.put(d.getEmail(), mCand.get(d.getEmail())+valor);
					} else{
						mCand.put(d.getEmail(), valor);
					}
					/* --------- */
				}
				
			} catch(SQLException e){
				
			}
 
			cont++; // Proximo conhecimento
		}
		
		/* Ordenando os candidatos pelo conhecimento */
		ArrayList<Desenvolvedor> l = new ArrayList<Desenvolvedor>();
		double confianca = 0.0;
		
		while(true){
			String chave = getMaiorCandidato(mCand, conf);
			
			if(chave == null) break;			
			l.add(mDes.get(chave));
		}
		/* --------------------- */
		
		Set<Entry<String, Double>> s = mCand.entrySet();
		
		/* Adicionando apenas os candidatos aptos na lista */
		for(Entry<String, Double> e : s){
			System.out.println(e.getKey() + " -- " + e.getValue());
		}
		/* ----------- */
		
		System.out.println(l.size());
		
		return l;
	}
	
	public static String getMaiorCandidato(Map cand, double conf){
		double temp = 0.0;
		String email = null;
		
		Set<Entry<String, Double>> s = cand.entrySet();
		
		/* Buscando o maior candidato da tabela */
		for(Entry<String, Double> row : s){
			if(row.getValue() > temp){
				email = row.getKey();
			}
		}
		/* ---------------- */
		
		cand.remove(email);
		
		return (temp>=conf)?email:null;
		
	}
    
}
