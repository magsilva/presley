package com.hukarz.presley.communication.facade;
import com.hukarz.presley.communication.client.RMIClient;
import com.hukarz.presley.communication.server.RMIServer;
import com.hukarz.presley.communication.server.ServerBridge;




public class PrincipalSUBJECT {
	
	public enum Mode {
		client, server
	}
	
	private static Object instance;

	public static final int RMI_PORT = 1099;

	private PrincipalSUBJECT() {
	}
	
	private static PacketStruct clientSide(PacketStruct departurePack) {
		RMIClient client = (RMIClient)instance;
		PacketStruct backPack = null;
		try {
			backPack = client.getPacketFromServer(departurePack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return backPack;
	}
	
	/**
	 * Metodo de acesso do servidor
	 * @param int portserver. Numero da porta do servidor
	 * @param ServerBridge bridge. Interface com o servidor
	 */
	public static void facade(int portserver, ServerBridge bridge){
		if(instance instanceof RMIServer){
			serverSide(portserver, bridge);
		}
	}
	
	/**
	 * Metodo de acesso do cliente
	 * @param PacketStruct pack.Pacote com a requisica ao servidor
	 * @return PacketStruct pack. Pacote com a resposta do servidor
	 */
	public static PacketStruct facade(PacketStruct pack){
		if(instance instanceof RMIClient){
			return clientSide(pack);
		} 
		else {
			return null;
		}
	}

	public static Object getInstance(Mode mode, int port) throws Exception {
		if (instance == null) {
			if (Mode.server == mode) {
				instance = new RMIServer(port);
			}
			else if (Mode.client == mode) {
				instance = new RMIClient(port);
			}
			else {
				throw new Exception("Malformed Expression!");
			}
		}
		return instance;
	}
	private static void serverSide(int port, ServerBridge bridge){
		RMIServer server = (RMIServer)instance;
		if (port != 0) {
			server.setPort(port);
		}
		else {
			server.setPort(PrincipalSUBJECT.RMI_PORT);
		}
		server.setBridge(bridge);
		server.start();
	}

}
