package server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import facade.PacketStruct;
import facade.StubInterface;


public class PacketSTUB extends UnicastRemoteObject implements StubInterface {

	private ServerBridge bridge;
	
	protected PacketSTUB(ServerBridge bridge) throws RemoteException {
		super();
		this.bridge = bridge;
		// TODO Auto-generated constructor stub
	}
	
	public PacketStruct sendReceivePacketStruct(PacketStruct pack) throws RemoteException {	
		System.out.println(pack.getData());
		return bridge.sendToServer(pack);
	}

}
