package com.hukarz.presley.communication.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.hukarz.presley.communication.facade.StubInterface;



public class RMIServer {

	private int port;
	
	private ServerBridge bridge;
	
	public RMIServer(int port) {
		this.port = port;
	}
	
	public void setPort( int port ) {
		this.port = port;
	}
	
	public void setBridge( ServerBridge bridge ) {
		this.bridge = bridge;
	}
	
	public void start(){
		try {

            StubInterface packetSTUB = new PacketSTUB(bridge);

           // this.stub = (StubInterface) UnicastRemoteObject.exportObject(packetSTUB,0);

            Registry registry = LocateRegistry.createRegistry(this.port);

            registry.bind("stub", packetSTUB);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}			
	}

}
