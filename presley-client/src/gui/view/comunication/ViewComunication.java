package gui.view.comunication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import beans.Desenvolvedor;
import beans.Item;
import beans.Tree;
import facade.PacketStruct;
import facade.PrincipalSUBJECT;

/**
 * Esta classe controla a comunicacao entre o cliente e o servidor.
 * @author Leandro Carlos, Samara Martins, Alysson Diniz e Joao Paulo 
 * @since 2008
 */
public class ViewComunication {	
	private ArrayList<String> atividades = new ArrayList<String>();	
	private HashMap<String,ArrayList<String>> conhecimentos = new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> problemas = new HashMap<String,ArrayList<String>>();
	private beans.Tree ontologia;//Armazena a ontologia
	
	//Constantes
	public final int ADICIONA_ATIVIDADE = 1; 
	public final int REMOVE_ATIVIDADE = 2;
	
	/**
	 * Construtor da classe ViewCommunication. Instancia a comunicacao como cliente, passando o ip do
	 * servidor e a porta para acesso remoto (1099 padrao RMI)
	 */
	public ViewComunication() {
		///*
		try {
			System.out.println("instanciando cliente");
		//	PrincipalSUBJECT.getInstance("client", "150.165.130.20", 1099);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//*/
		//teste();//TESTE
	}
	
	/**
	 * Retorna a ontolgia dos conhecimentos.
	 * @return Tree é a arvore de conhecimentos.
	 */
	public Tree getOntologia(){
		return ontologia;
	}
	
	/**
	 * Adiciona um novo conhecimento na ontologia.
	 * 
	 * @param item é o nó da ontologia onde será criado novo filho
	 * @param novoConhecimento é o nome do novo conhecimento
	 * @return true se sucesso ou false caso contrário
	 */
	public boolean adicionaConhecimento(Item item, String novoConhecimento){
		ArrayList<Item> localizados = ontologia.localizaFilho(item.getNome());
		if (localizados!=null) {
			for (Item conh : localizados) {
				if (conh.getPai()== null && item.getPai()== null) {
					conh.adicionaFilho(novoConhecimento);
					return true;
				}else{
					Item paiConh = conh.getPai();
					Item paiItem = item.getPai();
					if ((paiConh!=null&&paiItem!=null)&&paiConh.getNome().equals(paiItem.getNome())) {
						conh.adicionaFilho(novoConhecimento);
						return true;
					}
					return false;
				}
			}
			
		}
		
		return false;
	}
	
	/**
	 * Retorna as atividades cadastradas
	 * @return ArrayList<String> é a lista de atividades
	 */
	public ArrayList<String> getAtividades()
	{
		return this.atividades;
	}
	
	/**
	 * Retorna os conhecimentos envolvidos em uma dada atividade
	 * @param atividade é o nome da atividade
	 * @return ArrayList<String> é a lista de conhecimentos associados a atividade
	 */
	public ArrayList<String> getConhecimentosEnvolvidos(String atividade)
	{
		return this.conhecimentos.get(atividade);
	}
	
	/**
	 * Retorna os problemas associados a uma dada atividade
	 * @param atividade é o nome da atividade
	 * @return ArrayList<String> é a lista de problemas
	 */
	public ArrayList<String> getProblemas(String atividade)
	{
		return this.problemas.get(atividade);
	}
	
	/**
	 * Adiciona uma nova atividade a lista já existente e envia um pacote para o servidor
	 * para incluir essa ativadade no BD.
	 * @param atividade é o nome da atividade
	 * @param conhecimentos são os conhecimentos envolvidos em uma atividade
	 * @param problemas são os problemas associados a uma ativadade
	 */
	public void addAtividade(String atividade, ArrayList<String> conhecimentos, ArrayList<String> problemas)
	{
		this.atividades.add(atividade);
		this.conhecimentos.put(atividade, conhecimentos);
		this.problemas.put(atividade, problemas);
		String novaAtividade =  getAtividades().get(getAtividades().size()-1);	
    	Desenvolvedor des = new Desenvolvedor();
    	des.setEmail("coelhao@vai.pro.japao");
    	beans.TipoAtividade ati = new beans.TipoAtividade(novaAtividade, des, des, 0,  new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), false, null);
    	sendPack(ati, ADICIONA_ATIVIDADE);	
	}
	
	/**
	 * Remove uma ativadade da lista existente
	 * @param atividade é a atividade que será removida
	 */
	public void removeAtividade(String atividade)
	{
		this.atividades.remove(atividade);
		this.conhecimentos.remove(atividade);
		this.problemas.remove(atividade);
    	Desenvolvedor des = new Desenvolvedor();
    	des.setEmail("coelhao@vai.pro.japao");
		beans.TipoAtividade ati = new beans.TipoAtividade(atividade, des, des, 0,  new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), false, null);
		sendPack(ati, REMOVE_ATIVIDADE);	
	}
	
	/**
	 * Mótodo de teste que cria uma ontologia e uma atividade com seus conhecimentos e problemas associados.
	 * Esta ser para simular o ambiente operacional do sistema.
	 */
	public void teste()
	{
		try {
			
			Tree tree = new Tree("CONHECIMENTO");
			
			tree.adicionaFilho("Banco de Dados");
			
			tree.getFilho("Banco de Dados").adicionaFilho("MySQL");
			tree.getFilho("Banco de Dados").adicionaFilho("PostgresSQL");
			
			tree.adicionaFilho("LP");
			tree.getFilho("LP").adicionaFilho("JAVA");
			tree.getFilho("LP").adicionaFilho("C++");
			
			ontologia = tree;
			
			ArrayList<String> prob1 = new ArrayList<String>();
			prob1.add("RMI");
			prob1.add("Conector JDBC");
			
			ArrayList<String> conh1 = new ArrayList<String>();
			conh1.add(tree.getFilho("Banco de Dados").getFilho("MySQL").getNome());
			conh1.add("Banco de Dados");
			
			this.addAtividade("Implementar Presley", conh1, prob1);
	

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERRO ERRO ViewComunication: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo responsavel por enviar uma requisicao/pacote ao servidor 
	 * @param data o dado do pacote
	 * @param id a id com o tipo do pacote
	 * @return o pacote de resposta do servidor
	 */
	private PacketStruct sendPack(Object data, int id) {
		PacketStruct pack = new PacketStruct(data, id);
		PacketStruct packet = PrincipalSUBJECT.facade(pack);
		System.out.println(packet.getData());
		return packet;
	}
}


