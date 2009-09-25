package com.hukarz.presley.communication.server;
import com.hukarz.presley.communication.facade.PacketStruct;



public interface ServerBridge {

	public PacketStruct sendToServer( PacketStruct packet);
	
}
