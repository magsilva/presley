package com.hukarz.presley.server;
import com.hukarz.presley.facade.PacketStruct;



public interface ServerBridge {

	public PacketStruct sendToServer( PacketStruct packet);
	
}
