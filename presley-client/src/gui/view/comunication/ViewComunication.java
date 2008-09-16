package gui.view.comunication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.Item;
import beans.Problema;
import beans.TipoAtividade;
import beans.Tree;
import facade.PacketStruct;
import facade.PrincipalSUBJECT;

/**
 * Esta classe controla a comunicacao entre o cliente e o servidor.
 * @author Leandro Carlos, Samara Martins, Alysson Diniz e Joao Paulo 
 * @since 2008
 */
public class ViewComunication implements CorePresleyOperations{	
	private ArrayList<String> atividades = new ArrayList<String>();	
	private ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();//Lista de todos os desenvolvedores
	private ArrayList<Conhecimento> listaConhecimentos = new ArrayList<Conhecimento>();//Lista de todos os conhecimentos
	private HashMap<String,ArrayList<Conhecimento>> conhecimentos = new HashMap<String,ArrayList<Conhecimento>>();
	private HashMap<String,ArrayList<Problema>> problemas = new HashMap<String,ArrayList<Problema>>();
	private beans.Tree ontologia;//Armazena a ontologia

	
	/**
	 * Construtor da classe ViewCommunication. Instancia a comunicacao como cliente, passando o ip do
	 * servidor e a porta para acesso remoto (1099 padrao RMI)
	 */
	public ViewComunication() {
		///*
		try {
			System.out.println("instanciando cliente");
			//PrincipalSUBJECT.getInstance("client", "150.165.130.20", 1099);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//*/
		teste();//TESTE
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
	public ArrayList<Conhecimento> getConhecimentosEnvolvidos(String atividade)
	{
		return this.conhecimentos.get(atividade);
	}
	
	/**
	 * Retorna os problemas associados a uma dada atividade
	 * @param atividade é o nome da atividade
	 * @return ArrayList<String> é a lista de problemas
	 */
	public ArrayList<Problema> getProblemas(String atividade)
	{
		return this.problemas.get(atividade);
	}
	
	
	/**
	 * Mótodo de teste que cria uma ontologia e uma atividade com seus conhecimentos e problemas associados.
	 * Esta ser para simular o ambiente operacional do sistema.
	 */
	public void teste()
	{
		try {
			
			Conhecimento conhecimentoAtividade1 = new Conhecimento();
			conhecimentoAtividade1.setNome("Banco de Dados");
			conhecimentoAtividade1.setDescricao("Area geral de banco de dados");
			Conhecimento conhecimentoAtividade2 = new Conhecimento();
			conhecimentoAtividade2.setNome("Banco de Dados Relacional");
			conhecimentoAtividade2.setDescricao("Tipo de Banco de Dados relacional");
			Conhecimento conhecimentoAtividade3 = new Conhecimento();
			conhecimentoAtividade3.setNome("LP");
			conhecimentoAtividade3.setDescricao("Linguagem de programacao");
			Conhecimento conhecimentoAtividade4 = new Conhecimento();
			conhecimentoAtividade4.setNome("JAVA");
			conhecimentoAtividade4.setDescricao("Linguagem JAVA");
			Conhecimento conhecimentoAtividade5 = new Conhecimento();
			conhecimentoAtividade5.setNome("C++");
			conhecimentoAtividade5.setDescricao("Linguagem C++");
			ArrayList<Conhecimento> listaConhecimentosAtividade = new ArrayList<Conhecimento>();
			listaConhecimentosAtividade.add(conhecimentoAtividade1);
			listaConhecimentosAtividade.add(conhecimentoAtividade2);

			
			
			Conhecimento conhecimentoDesenvolvedor = new Conhecimento();
			conhecimentoDesenvolvedor.setNome("MySQL");
			conhecimentoDesenvolvedor.setDescricao("Banco de dados relacional gratis de otima qualidade");
			ArrayList<Conhecimento> listaConhecimentosDesenvolvedor = new ArrayList<Conhecimento>();
			listaConhecimentosDesenvolvedor.add(conhecimentoDesenvolvedor);
			
			listaConhecimentos.add(conhecimentoAtividade1);
			listaConhecimentos.add(conhecimentoAtividade2);
			listaConhecimentos.add(conhecimentoAtividade3);
			listaConhecimentos.add(conhecimentoAtividade4);
			listaConhecimentos.add(conhecimentoAtividade5);
			listaConhecimentos.add(conhecimentoDesenvolvedor);
			
			Desenvolvedor desenvolvedor = new Desenvolvedor();
			desenvolvedor.setNome("FULANO");
			desenvolvedor.setEmail("fulano@algumDominio.com.br");
			desenvolvedor.setLocalidade("Rua Projetada");
			desenvolvedor.setListaConhecimento(listaConhecimentosDesenvolvedor);
			Desenvolvedor supervisor = new Desenvolvedor();
			supervisor.setNome("SICRANO");
			supervisor.setEmail("sicrano@algumDominio.com.br");
			supervisor.setLocalidade("Rua Projetada");
			supervisor.setListaConhecimento(listaConhecimentosDesenvolvedor);
			ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();
			listaDesenvolvedores.add(desenvolvedor);
			listaDesenvolvedores.add(supervisor);
			this.listaDesenvolvedores=listaDesenvolvedores;
			
			TipoAtividade atividade = new TipoAtividade("Implementar Presley",desenvolvedor,supervisor,0,new Date(System.currentTimeMillis()),
					new Date(System.currentTimeMillis()),false,listaConhecimentosAtividade);
			
			Tree tree = new Tree("CONHECIMENTO");
			
			tree.adicionaFilho("Banco de Dados");
			
			tree.getFilho("Banco de Dados").adicionaFilho("Banco de Dados Relacional");
			tree.getFilho("Banco de Dados").getFilho("Banco de Dados Relacional").adicionaFilho("MySQL");
			
			tree.adicionaFilho("LP");
			tree.getFilho("LP").adicionaFilho("JAVA");
			tree.getFilho("LP").adicionaFilho("C++");
			
			ontologia = tree;
			
			//this.adicionaAtividade(atividade);
	

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

	public boolean AdicionaConhecimento(Conhecimento conhecimento) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Adiciona uma nova atividade a lista já existente e envia um pacote para o servidor
	 * para incluir essa ativadade no BD.
	 * @param atividade contem a descriçao da atividade, os conhecimentos envolvidos, os desenvolvedores e as datas
	 */
	public boolean adicionaAtividade(TipoAtividade atividade) {
		// TODO Auto-generated method stub
    	PacketStruct respostaPacket = sendPack(atividade, ADICIONA_ATIVIDADE);
    	Boolean resposta = (Boolean)respostaPacket.getData();
    	if (resposta.booleanValue()==true) {
    		this.atividades.add(atividade.getDescricao());
    		this.conhecimentos.put(atividade.getDescricao(), atividade.getListaDeConhecimentosEnvolvidos());
		}
    	System.out.println("Resposta: "+resposta.booleanValue());
		return resposta.booleanValue();
    	//return true;//TESTE
	}
	
	/**
	 * Remove uma ativadade da lista existente
	 * @param atividade é a atividade que será removida
	 */
	public boolean removerAtividade(TipoAtividade atividade) {
		// TODO Auto-generated method stub
    	PacketStruct respostaPacket = sendPack(atividade, REMOVE_ATIVIDADE);
    	Boolean resposta = (Boolean)respostaPacket.getData();
    	if (resposta.booleanValue()==true) {
    		this.atividades.remove(atividade.getDescricao());
    		this.conhecimentos.remove(atividade.getDescricao());
		}
		return resposta.booleanValue();
	}

	public boolean associaConhecimentoAtividade(
			ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean associaProblemaAtividade(Problema problema,
			TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<Desenvolvedor> buscaDesenvolvedores(Problema problema,
			ArrayList<Conhecimento> listaConhecimento, int grauDeConfianca) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean desassociaConhecimentoAtividade(
			ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean desassociaProblemaAtividade(Problema problema,
			TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean encerrarAtividade(TipoAtividade atividade) {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(atividade,ENCERRA_ATIVIDADE);
    	//Boolean resposta = (Boolean)respostaPacket.getData();
		//return resposta.booleanValue();
		return false;
	}

	public boolean enviarMensagem(
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,ENVIA_MENSAGEM);//TESTE
    	//Boolean resposta = (Boolean)respostaPacket.getData();
		//return resposta.booleanValue();
		return false;
	}

	public ArrayList<Desenvolvedor> getListaDesenvolvedores() {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,LISTA_DESENVOLVEDORES);
    	//ArrayList<Desenvolvedor> resposta = (ArrayList<Desenvolvedor>)respostaPacket.getData();
    	//listaDesenvolvedores = resposta;
    	
		return listaDesenvolvedores;
	}

	public Desenvolvedor login(String user, String passwd) {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,LOGIN);//TESTE
    	//Desenvolvedor resposta = (Desenvolvedor)respostaPacket.getData();
		//return resposta;
		return null;
	}

	public boolean logout(Desenvolvedor desenvolvedor) {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,LOGOUT);//TESTE
    	//Boolean resposta = (Boolean)respostaPacket.getData();
		//return resposta.booleanValue();
		return false;
	}

	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor,
			Problema problema, boolean qualificacao) {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,QUALIFICA_DESENVOLVEDOR);//TESTE
    	//Boolean resposta = (Boolean)respostaPacket.getData();
    	//return resposta.booleanValue();
    			
		return false;
	}

	public ArrayList<Conhecimento> getListaConhecimentos() {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,LISTA_CONHECIMENTOS);
    	//ArrayList<Desenvolvedor> resposta = (ArrayList<Desenvolvedor>)respostaPacket.getData();
    	//listaDesenvolvedores = resposta;
    	
		return listaConhecimentos;
	}

	public boolean adicionaConhecimento(Conhecimento conhecimento) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem,
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema,
			String mensagem) {
		// TODO Auto-generated method stub
		return false;
	}
}


