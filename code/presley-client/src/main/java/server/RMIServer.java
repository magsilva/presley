package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import facade.StubInterface;


public class RMIServer {

	private StubInterface stub;

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
           // Naming.rebind("rmi://localhost:1099/stub", packetSTUB);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

}
