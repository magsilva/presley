package com.hukarz.presley.communication.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.communication.facade.StubInterface;




public class RMIClient {

	private PacketStruct backPack;
	private String ipserver;
	private StubInterface stub;
	public static final String NOME_STUB = "stub";
	
	public RMIClient(int port) {
		try {
			Registry registry = LocateRegistry.getRegistry(this.ipserver);
            this.stub= (StubInterface) registry.lookup("stub");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	private void requestFromServer(PacketStruct departurePack){
		try {
			this.backPack = stub.sendReceivePacketStruct(departurePack);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public PacketStruct getPacketFromServer(PacketStruct departurePack) throws Exception{
		requestFromServer(departurePack);
		if (this.backPack != null)
			return this.backPack;
		else
			throw new Exception("Null value returned!");
	}

}
