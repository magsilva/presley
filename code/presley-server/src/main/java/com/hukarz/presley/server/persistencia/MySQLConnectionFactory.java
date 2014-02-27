package com.hukarz.presley.server.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hukarz.presley.server.core.PresleyProperties;

import org.apache.log4j.Logger;

public class MySQLConnectionFactory {
	
	private static String DEFAULT_USERNAME = "root";
	
	private static String DEFAULT_PASSWORD = "";
	
	private static String DEFAULT_URL = "jdbc:mysql://localhost/presley";
	
	private static String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";

	private static Connection connection = null;
	
	private static Logger logger = Logger.getLogger(MySQLConnectionFactory.class);
	
	private MySQLConnectionFactory() {
	}

	public synchronized static Connection open()
	{
		String url, username, password;
		
		PresleyProperties properties = PresleyProperties.getInstance();
		url = properties.getProperty("jdbc.url") == null ? properties.getProperty("jdbc.url") : DEFAULT_URL;
		username = properties.getProperty("jdbc.user") == null ? properties.getProperty("jdbc.user") : DEFAULT_USERNAME;
		password = properties.getProperty("jdbc.password") == null ? properties.getProperty("jdbc.password") : DEFAULT_PASSWORD;
		
		
		if (connection == null) {
			try {
				Class.forName(DEFAULT_DRIVER);
			} catch (Exception e) {}
			
			try {
				connection = DriverManager.getConnection(url, username, password);
				logger.info("Connection established with " + url);
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return connection;
	}

}
