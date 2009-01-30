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
	
	public RMIClient(String ipserver, int port) {
		this.ipserver = ipserver;
		try {
			//this.stub = (StubInterface) Naming.lookup("rmi://" + this.ipserver + "/" + NOME_STUB);
			Registry registry = LocateRegistry.getRegistry(this.ipserver);
            this.stub= (StubInterface) registry.lookup("stub");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getIpserver() {
		return ipserver;
	}

	public void setIpserver(String ipserver) {
		this.ipserver = ipserver;
	}

	private void requestFromServer(PacketStruct departurePack){
		try {
			this.backPack = stub.sendReceivePacketStruct(departurePack);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
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
