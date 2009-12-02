/**
 * 
 */
package com.hukarz.presley.server.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Alan Kelon Oliveira de Moraes <alan@di.ufpb.br>
 *
 */
public class PresleyProperties {
	
	private static PresleyProperties instance = null;
	private Properties properties = null;
	
	private PresleyProperties() {
		this.properties = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream("presley.properties");
			this.properties.load(in);
			in.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static PresleyProperties getInstance() {
		if (null == instance) {
			instance = new PresleyProperties();
		}
		return instance;
	}
	
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}

}
