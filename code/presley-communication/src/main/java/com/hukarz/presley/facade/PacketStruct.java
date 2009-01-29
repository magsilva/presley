package com.hukarz.presley.facade;

import java.io.Serializable;

public class PacketStruct implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object data;	
	private int id;
	
	public PacketStruct(Object data, int id){
		this.data = data;
		this.id = id;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
