package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnectionFactory {

	private static String user = "root";
	private static String pwd = "desterro";
	private static Connection connection = null;
	
	private MySQLConnectionFactory(){
		
	}

	/*public static Connection getConnection(){
		
		try {
			if (connection == null){
				try {
					
		            Class.forName("com.mysql.jdbc.Driver");
		            connection = DriverManager.getConnection(
		            		"jdbc:mysql://150.165.130.20/presley", 
		            		user, 
		            		pwd);
		            
		            System.out.println("\n-------------- Nova Conexao Criada!-----------\n");
		            
		        } catch (ClassNotFoundException e) {
		            e.printStackTrace();
		        } catch (SQLException e) {
					e.printStackTrace();
				}
		    
			}  else if (connection.isClosed()) {
				try {
					connection = null;
		            Class.forName("com.mysql.jdbc.Driver");
		            connection = DriverManager.getConnection(
		            		"jdbc:mysql://150.165.130.20/presley", 
		            		user, 
		            		pwd);
		            
		        } catch (ClassNotFoundException e) {
		            e.printStackTrace();
		        } catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}*/
	
	public static Connection open() {
		Connection novaConexao = null;
		try {
			
            Class.forName("com.mysql.jdbc.Driver");
            novaConexao = DriverManager.getConnection(
            		"jdbc:mysql://localhost/presley");
            
            System.out.println("\n-------------- Nova Conexao Criada!-----------\n");
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			e.printStackTrace();
		}
        
        return novaConexao;
	}
}
