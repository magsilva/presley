package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnectionFactory {

	private static String user = "root";
	private static String pwd = "";
	private static Connection connection = null;
	
	private MySQLConnectionFactory(){
		
	}
	
	public static Connection open() {
		Connection novaConexao = null;
		try {
			
            Class.forName("com.mysql.jdbc.Driver");
            novaConexao = DriverManager.getConnection(
            		"jdbc:mysql://localhost/presley", user, pwd);
            
            System.out.println("\n-------------- Nova Conexao Criada!-----------\n");
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			e.printStackTrace();
		}
        
        return novaConexao;
	}
}
