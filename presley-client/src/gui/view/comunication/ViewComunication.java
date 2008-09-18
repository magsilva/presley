package gui.view.comunication;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import beans.BuscaDesenvolvedores;
import beans.Conhecimento;
import beans.ConhecimentoAtividade;
import beans.DadosAutenticacao;
import beans.Desenvolvedor;
import beans.Item;
import beans.Problema;
import beans.ProblemaAtividade;
import beans.TipoAtividade;
import beans.Tree;
import facade.PacketStruct;
import facade.PrincipalSUBJECT;
import excessao.*;

/**
 * Esta classe controla a comunicacao entre o cliente e o servidor.
 * @author Leandro Carlos, Samara Martins, Alysson Diniz e Joao Paulo 
 * @since 2008
 */
public class ViewComunication implements CorePresleyOperations{	
	private ArrayList<TipoAtividade> atividades = new ArrayList<TipoAtividade>();	
	private ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();//Lista de todos os desenvolvedores
	private ArrayList<Conhecimento> listaConhecimentos = new ArrayList<Conhecimento>();//Lista de todos os conhecimentos
	private ArrayList<Problema> listaProblemas = new ArrayList<Problema>();//Lista de todos os problemas
	private HashMap<String,ArrayList<Conhecimento>> conhecimentos = new HashMap<String,ArrayList<Conhecimento>>();//mapeamento nome de atividade e seus conhecimentos associados
	private HashMap<String,ArrayList<Problema>> problemas = new HashMap<String,ArrayList<Problema>>();//mapeamento nome de atividade e seus problemas associados
	private beans.Tree ontologia;//Armazena a ontologia

	
	/**
	 * Construtor da classe ViewCommunication. Instancia a comunicacao como cliente, passando o ip do
	 * servidor e a porta para acesso remoto (1099 padrao RMI)
	 */
	public ViewComunication(String ipServidor) {
		///*
		try {
			
			System.out.println("instanciando cliente");
			//PrincipalSUBJECT.getInstance("client", "150.165.130.196", 1099);
			PrincipalSUBJECT.getInstance("client", ipServidor , 1099);
			
			System.out.println("Passou do getInstance");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//*/
		
		this.listaDesenvolvedores = getListaDesenvolvedores();
		
		teste();//TESTE
	}
	
	/**
	 * Retorna a ontolgia dos conhecimentos.
	 * @return Tree � a arvore de conhecimentos.
	 */
	public Tree getOntologia(){
		//PacketStruct respostaPacket = sendPack(null, GET_ONTOLOGIA);
    	//Tree resposta = (Tree)respostaPacket.getData();
    	//if (resposta!=null) {
    	//	this.ontologia = resposta;
		//}
    	
		return ontologia;
	}
	
	/**
	 * Adiciona um novo conhecimento na ontologia.
	 * 
	 * @param item � o n� da ontologia onde ser� criado novo filho
	 * @param novoConhecimento � o nome do novo conhecimento
	 * @return true se sucesso ou false caso contr�rio
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
	 * Retorna os nomes das atividades cadastradas
	 * @return ArrayList<String> � a lista de atividades
	 */
	public ArrayList<String> getAtividades()
	{
		ArrayList<String> retorno = new ArrayList<String>();
		if (this.atividades!=null) {
			for (TipoAtividade atividade : this.atividades) {
				retorno.add(atividade.getDescricao());
			}
			
		}
		return retorno;
	}
	
	/**
	 * Retorna os conhecimentos envolvidos em uma dada atividade
	 * @param atividade � o nome da atividade
	 * @return ArrayList<String> � a lista de conhecimentos associados a atividade
	 */
	public ArrayList<Conhecimento> getConhecimentosEnvolvidos(String atividade)
	{
		return this.conhecimentos.get(atividade);
	}
	
	/**
	 * Retorna os problemas associados a uma dada atividade
	 * @param atividade � o nome da atividade
	 * @return ArrayList<String> � a lista de problemas
	 */
	public ArrayList<Problema> getProblemas(String atividade)
	{
		return this.problemas.get(atividade);
	}
	
	
	/**
	 * M�todo de teste que cria uma ontologia e uma atividade com seus conhecimentos e problemas associados.
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
			desenvolvedor.setEmail("a@a.a");
			desenvolvedor.setLocalidade("Rua Projetada");
			desenvolvedor.setSenha("123456");
			//desenvolvedor.setListaConhecimento(listaConhecimentosDesenvolvedor);
			Desenvolvedor supervisor = new Desenvolvedor();
			supervisor.setNome("SICRANO");
			supervisor.setEmail("sicrano1@algumDominio.com.br");
			supervisor.setLocalidade("Rua Projetada");
			//supervisor.setListaConhecimento(listaConhecimentosDesenvolvedor);
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
			//this.adicionaDesenvolvedor(desenvolvedor);
			System.out.println("Adicionando atividade no banco");
			//this.adicionaAtividade(atividade);
			System.out.println("Passou da adicao");
			//this.adicionaDesenvolvedor(desenvolvedor);

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
	 * Adiciona uma nova atividade a lista j� existente e envia um pacote para o servidor
	 * para incluir essa ativadade no BD.
	 * @param atividade contem a descri�ao da atividade, os conhecimentos envolvidos, os desenvolvedores e as datas
	 */
	public boolean adicionaAtividade(TipoAtividade atividade) throws Exception {

		boolean retorno = false;
		
		PacketStruct respostaPacket = sendPack(atividade, CorePresleyOperations.ADICIONA_ATIVIDADE);
    	if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
    		throw new Exception((String)respostaPacket.getData());
    	}
    	else {
    		retorno = (Boolean)respostaPacket.getData();
    		if (retorno == true) {
    			this.atividades.add(atividade);
    			this.conhecimentos.put(atividade.getDescricao(), atividade.getListaDeConhecimentosEnvolvidos());
    		}
    		System.out.println("Resposta: "+retorno);
    	}
		return retorno;
	}
	
	/**
	 * Remove uma ativadade da lista existente
	 * @param atividade � a atividade que ser� removida
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
		ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) throws Exception{

		ConhecimentoAtividade ca = new ConhecimentoAtividade();
		ca.setAtividade(atividade);
		ca.setConhecimento(listaConhecimento);
		
		PacketStruct respostaPacket = sendPack(ca, CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE);
		
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String)  respostaPacket.getData());
		}
		
		return true;
	}

	public boolean associaProblemaAtividade(Problema problema,
			TipoAtividade atividade) throws Exception{
		// TODO Auto-generated method stub
		ProblemaAtividade problemaAtividade = new ProblemaAtividade();
		problemaAtividade.setAtividade(atividade);
		problemaAtividade.setProblema(problema);
		PacketStruct respostaPacket = sendPack(problemaAtividade,ASSOCAR_PROBLEMA_ATIVIDADE);
    	
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}
		
		Boolean resposta = (Boolean)respostaPacket.getData();
    	//listaDesenvolvedores = resposta;
    	if (resposta!=null&&resposta.booleanValue()==true) {
    		this.listaProblemas.add(problema);
    		this.problemas.put(atividade.getDescricao(),listaProblemas);
    		ArrayList<Problema> problemasAssociados = this.problemas.get(atividade.getDescricao());
    		problemasAssociados.add(problema);	
    		return true;
		}
		
		return false;
	}

	public ArrayList<Desenvolvedor> buscaDesenvolvedores(ArrayList<String> listaConhecimentos, 
			int grauDeConfianca) {
		BuscaDesenvolvedores dadosDaBusca = new BuscaDesenvolvedores();
		dadosDaBusca.setGrauDeConfianca(grauDeConfianca);
		dadosDaBusca.setListaConhecimento(listaConhecimentos);
		PacketStruct respostaPacket = sendPack(dadosDaBusca, BUSCA_DESENVOLVEDORES);
		ArrayList<Desenvolvedor> desenvolvedores = (ArrayList<Desenvolvedor>)respostaPacket.getData();
		return desenvolvedores;
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
	//	PacketStruct respostaPacket = sendPack(null,CorePresleyOperations.GET_LISTA_DESENVOLVEDORES);
//    	ArrayList<Desenvolvedor> resposta = (ArrayList<Desenvolvedor>)respostaPacket.getData();
//    	listaDesenvolvedores = resposta;
//    	
//    	if(resposta == null) 
//    		System.out.println("RESPOSTA NULL");
//    	
//    	Iterator it = resposta.iterator();
//    	while (it.hasNext()) {
//    		Desenvolvedor des = (Desenvolvedor)it.next();
//    		System.out.println(des.getNome());
//    	}
    	
		return listaDesenvolvedores;
	}

	public Desenvolvedor login(String user, String passwd) throws Exception {
		DadosAutenticacao auth = new DadosAutenticacao();
		auth.setUser(user);
		auth.setPasswd(passwd);
		PacketStruct respostaPacket = sendPack(auth, CorePresleyOperations.LOG_IN);
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String)respostaPacket.getData());
		}
		Desenvolvedor resposta = (Desenvolvedor)respostaPacket.getData();
		return resposta;
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
	
	public ArrayList<Problema> getListaProblemas() {
		// TODO Auto-generated method stub
		//PacketStruct respostaPacket = sendPack(null,GET_LISTA_PROBLEMAS);
    	//ArrayList<Problema> resposta = (ArrayList<Problema>)respostaPacket.getData();
    	//listaProblemas = resposta;
    	
		return listaProblemas;
	}

	public boolean adicionaConhecimento(Conhecimento conhecimento) throws Exception{
		
		PacketStruct respostaPacket = sendPack(conhecimento, CorePresleyOperations.ADICIONA_CONHECIMENTO);
		
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}
			
		return true;
	}

	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem,
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema,
			String mensagem) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception{
		boolean retorno = true;
		
//		PacketStruct respostaPacket = sendPack(desenvolvedor,ADICIONA_DESENVOLVEDOR);//TESTE
//    	Boolean resposta = (Boolean)respostaPacket.getData();
//    	//return resposta.booleanValue();
//    	
//		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
//			throw new Exception((String) respostaPacket.getData());
//		}
		PacketStruct respostaPacket = sendPack(desenvolvedor, CorePresleyOperations.ADICIONA_DESENVOLVEDOR);//TESTE
    	Boolean resposta = (Boolean)respostaPacket.getData();
    	//return resposta.booleanValue();
    	
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}

		listaDesenvolvedores.add(desenvolvedor);
		
//		retorno = (Boolean)respostaPacket.getData();
    	
		return retorno;
	}

	/**
	 * Retorna uma lista de Atividades
	 * @return ArrayList<TipoAtividade> � a lista de atividades cadastradas
	 */
	public ArrayList<TipoAtividade> buscaAtividades() {
		// TODO Auto-generated method stub
		PacketStruct respostaPacket = sendPack(null, BUSCA_ATIVIDADE);
		ArrayList<TipoAtividade> resposta = (ArrayList<TipoAtividade>)respostaPacket.getData();
    	if (resposta!=null) {
    		atividades = resposta;
		}
    	
		return atividades;
	}

	public Desenvolvedor login(DadosAutenticacao authData) {
		PacketStruct respostaPacket = sendPack(authData, CorePresleyOperations.LOG_IN);
		if(respostaPacket.getData() != null) {
			Desenvolvedor user = (Desenvolvedor)respostaPacket.getData();
			return user;
		}
		return null;
	}

	public boolean desassociaProblemaAtividade(Problema problema) {
		// TODO Auto-generated method stub
		return false;
	}
}


