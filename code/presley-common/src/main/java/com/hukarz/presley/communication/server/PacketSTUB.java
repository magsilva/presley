package com.hukarz.presley.communication.server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.communication.facade.StubInterface;



public class PacketSTUB extends UnicastRemoteObject implements StubInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerBridge bridge;
	
	protected PacketSTUB(ServerBridge bridge) throws RemoteException {
		super();
		this.bridge = bridge;
		// TODO Auto-generated constructor stub
	}
	
	public PacketStruct sendReceivePacketStruct(PacketStruct pack) throws RemoteException {	
		return bridge.sendToServer(pack);
	}

}
