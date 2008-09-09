package comunicacao;

import core.ServerBridge;
import core.ServerBridgeImp;

public class Comunicacao {

	ServerBridge server;
	
	public Comunicacao() {
	
		server = new ServerBridgeImp();
		
	}
	
	public Packet sendToServer(Packet packet) {
		server.sendPacket(packet);
		return packet;
	}
}
