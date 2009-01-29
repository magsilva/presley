package com.hukarz.presley.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface StubInterface extends Remote {

	public PacketStruct sendReceivePacketStruct(PacketStruct pack) throws RemoteException;
}