package server;
import facade.PacketStruct;



public interface ServerBridge {

	public PacketStruct sendToServer( PacketStruct packet);
	
}
