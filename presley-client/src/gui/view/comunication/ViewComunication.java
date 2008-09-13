package gui.view.comunication;

import java.util.ArrayList;
import java.util.HashMap;
import facade.PacketStruct;
import facade.PrincipalSUBJECT;

/**
 * Esta classe controla a comunicacao entre o cliente e o servidor.
 * @author Leandro Carlos, Samara Martins, Alysson Diniz 
 * @since 2008
 */
public class ViewComunication {	
	private ArrayList<String> atividades = new ArrayList<String>();	
	private HashMap<String,ArrayList<String>> conhecimentos = new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> problemas = new HashMap<String,ArrayList<String>>();
	
	/**
	 * Construtor da classe ViewCommunication. Instancia a comunicacao como cliente, passando o ip do
	 * servidor e a porta para acesso remoto (1099 padrao RMI)
	 */
	public ViewComunication() {
		try {
			System.out.println("instanciando cliente");
			PrincipalSUBJECT.getInstance("client", "150.165.130.20", 1099);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getAtividades()
	{
		return this.atividades;
	}
	
	public ArrayList<String> getConhecimentosEnvolvidos(String atividade)
	{
		return this.conhecimentos.get(atividade);
	}
	
	public ArrayList<String> getProblemas(String atividade)
	{
		return this.problemas.get(atividade);
	}
	
	/**
	 * @param atividade
	 * @param conhecimentos
	 * @param problemas
	 */
	public void addAtividade(String atividade, ArrayList<String> conhecimentos, ArrayList<String> problemas)
	{
		this.atividades.add(atividade);
		this.conhecimentos.put(atividade, conhecimentos);
		this.problemas.put(atividade, problemas);
	}
	
	public void teste()
	{
		ArrayList<String> conh = new ArrayList<String>();
		conh.add("JU");
		conh.add("QS");
		
		ArrayList<String> prob = new ArrayList<String>();
		prob.add("comunicao");
		prob.add("web");
		prob.add("RMI");
		
		this.addAtividade("Java", conh, prob);
		
		ArrayList<String> conh1 = new ArrayList<String>();
		conh1.add("Ponteiro");
		conh1.add("QS");
		
		ArrayList<String> prob1 = new ArrayList<String>();
		prob1.add("Not a mumber");
		prob1.add("segmentation faul");
		prob1.add("stack error");
		
		this.addAtividade("C++", conh1, prob1);

	}
	
	/**
	 * Metodo responsavel por enviar uma requisicao/pacote ao servidor 
	 * @param data o dado do pacote
	 * @param id a id com o tipo do pacote
	 * @return o pacote de resposta do servidor
	 */
	public PacketStruct sendPack(Object data, int id) {
		PacketStruct pack = new PacketStruct(data, id);
		PacketStruct packet = PrincipalSUBJECT.facade(pack);
		System.out.println(packet.getData());
		return packet;
	}
}


