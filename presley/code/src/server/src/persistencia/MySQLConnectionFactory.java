package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;






public class MySQLConnectionFactory {

	private static String user = "root";
	private static String pwd = "123456";
	private Connection connection = null;
	
	public MySQLConnectionFactory(){
		
	}

	public Connection getConnection(){
		
		if (connection == null){
			try {
				
	            Class.forName("com.mysql.jdbc.Driver");
	            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/presley", user, pwd);
	            return connection;
	            
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
				e.printStackTrace();
			}
	    
		}  
			return connection;
		
	}
	
	
}
