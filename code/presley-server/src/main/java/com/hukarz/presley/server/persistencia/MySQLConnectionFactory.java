package com.hukarz.presley.server.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hukarz.presley.server.core.PresleyProperties;


public class MySQLConnectionFactory {

	private static Connection connection = null;
	private MySQLConnectionFactory() {
	}

	public static Connection open() {
		if (connection == null) {
			try {
				PresleyProperties properties = PresleyProperties.getInstance();
				connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), 
						properties.getProperty("jdbc.user"), properties.getProperty("jdbc.password"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
	}
	
	public static void main(String arg[]) {
		Connection connection = MySQLConnectionFactory.open();
		System.out.println(connection.toString());
	}
	
}
