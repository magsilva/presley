package com.hukarz.presley.server.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class MySQLConnectionFactory {

	private static String user = "root";
	private static String pwd = "";
	private static Connection connection = null;
	private static Logger logger = Logger.getLogger(MySQLConnectionFactory.class);
	
	private MySQLConnectionFactory(){
		
	}
	
	public static Connection open() {
		
		if (connection == null){
			try {
				
	            Class.forName("com.mysql.jdbc.Driver");
	            connection = DriverManager.getConnection(
	            		"jdbc:mysql://localhost/presley", user, pwd);
	            
	            logger.info("\n-------------- Nova Conexao Criada!-----------\n");
	            
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
				e.printStackTrace();
			}
		}
        
        return connection;
	}
}
