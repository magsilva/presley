package facade;
import server.RMIServer;
import server.ServerBridge;
import client.RMIClient;



public class PrincipalSUBJECT {

	private static Object instance;

	private PrincipalSUBJECT(){
	}
	/**
	 * Singleton da classe principal do pacote de comunicacao.
	 * @param mode String. Modo: server para servidor, client para cliente.
	 * @return instance. SIngleton
	 * @throws Exception Malformed Expression! caso nao seja server ou client
	 */
	public static Object getInstance(String mode, String ipserver, int port) throws Exception {
		if(instance == null) {
			if(mode.equalsIgnoreCase("server"))
				instance = new RMIServer(port);
			else if(mode.equalsIgnoreCase("client"))
				instance = new RMIClient(ipserver,port);
			else throw new Exception("Malformed Expression!");
		}
		return instance;
	}
	
	private static PacketStruct clientSide(PacketStruct departurePack){
		RMIClient client = (RMIClient)instance;
		PacketStruct backPack = null;
		try {
			backPack = client.getPacketFromServer(departurePack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return backPack;
	}
	
	private static void serverSide(int port, ServerBridge bridge){
		RMIServer server = (RMIServer)instance;
		if (port != 0)
			server.setPort(port);
		else
			server.setPort(1099);
		server.setBridge(bridge);
		server.start();
	}
	/**
	 * Metodo de acesso do cliente
	 * @param PacketStruct pack.Pacote com a requisica ao servidor
	 * @return PacketStruct pack. Pacote com a resposta do servidor
	 */
	public static PacketStruct facade(PacketStruct pack){
		if(instance instanceof RMIClient){
			return clientSide(pack);
		}else{
			;
		}
		return null;
	}
	/**
	 * Metodo de acesso do servidor
	 * @param int portserver. Numero da porta do servidor
	 * @param ServerBridge bridge. Interface com o servidor
	 */
	public static void facade(int portserver, ServerBridge bridge){
		if(instance instanceof RMIServer){
			serverSide(portserver, bridge);
		}else{
			;
		}
	}

}
