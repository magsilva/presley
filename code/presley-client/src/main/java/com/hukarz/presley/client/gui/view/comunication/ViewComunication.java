package com.hukarz.presley.client.gui.view.comunication;


import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Item;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.communication.facade.PrincipalSUBJECT;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;

/**
 * Esta classe controla a comunicacao entre o cliente e o servidor.
 * @author Leandro Carlos, Samara Martins, Alysson Diniz e Joao Paulo 
 * @since 2008
 */
public class ViewComunication implements CorePresleyOperations{	
	private static final Logger logger = Logger.getLogger("com.hukarz.presley.client.gui.view.comunication.ViewComunication");
	
	private ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();//Lista de todos os desenvolvedores
	private ArrayList<Conhecimento> listaConhecimentos = new ArrayList<Conhecimento>();//Lista de todos os conhecimentos
	private ArrayList<Problema> listaProblemas = new ArrayList<Problema>();//Lista de todos os problemas
	private com.hukarz.presley.beans.Tree arvoreConhecimentos; //Armazena arvore de conhecimentos

	
	/**
	 * Construtor da classe ViewCommunication. Instancia a comunicacao como cliente, passando o ip do
	 * servidor e a porta para acesso remoto
	 */
	public ViewComunication() {
		try {
			PrincipalSUBJECT.getInstance(PrincipalSUBJECT.Mode.client, PrincipalSUBJECT.RMI_PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.listaDesenvolvedores = getListaDesenvolvedores();
	}
	
	/**
	 * Construtor da classe ViewCommunication. Instancia a comunicacao como cliente, passando o ip do
	 * servidor e a porta para acesso remoto e usa o ip passado como parametro.
	 */
	public ViewComunication(String ip) {
		try {

			//Criando objetos contendo listas de desenvolvedores, etc
			listaDesenvolvedores = new ArrayList<Desenvolvedor>();//Lista de todos os desenvolvedores
			listaConhecimentos = new ArrayList<Conhecimento>();//Lista de todos os conhecimentos
			listaProblemas = new ArrayList<Problema>();//Lista de todos os problemas
			arvoreConhecimentos = null;//Armazena a ontologia
			
			logger.info("instanciando cliente");
			PrincipalSUBJECT.getInstance(PrincipalSUBJECT.Mode.client, PrincipalSUBJECT.RMI_PORT);
			
			logger.info("Passou do getInstance");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.listaDesenvolvedores = getListaDesenvolvedores();
	}

	
	/**
	 * Retorna a ontolgia dos conhecimentos.
	 * @return Tree � a arvore de conhecimentos.
	 * @throws ConhecimentoInexistenteException 
	 */
	public org.eclipse.swt.widgets.Tree getArvoreGraficaDeConhecimentos(Composite parent, int style) throws ConhecimentoInexistenteException{
		Item raiz = getArvoreGraficaDeConhecimentos().getRaiz();
		
		org.eclipse.swt.widgets.Tree treeGrafico = new org.eclipse.swt.widgets.Tree(parent,style);
		org.eclipse.swt.widgets.TreeItem treeItemGrafico = new TreeItem(treeGrafico,style);
		treeItemGrafico.setText( raiz.getConhecimento().getNome() );
		ArrayList<Item> filhosModelo = raiz.getFilhos();
		if (filhosModelo!=null) 
			for (Item filho : filhosModelo) {
				constroiArvoreGraficaHelper(treeItemGrafico, filho);	
			}
				
		return treeGrafico;
	}

	/**
	 * M�todo auxiliar do m�todo anterior que constr�i a arvore. Este percore a arvore de forma recursiva em pre-ordem.
	 * @param arvoreGrafica � a �rvore gr�fica que ser� constru�a
	 * @param arvoreModelo � o modelo de �rvore a partir do qual ser�constru�da a �rvore gr�fica
	 */
	private void constroiArvoreGraficaHelper(org.eclipse.swt.widgets.TreeItem arvoreGrafica, Item arvoreModelo){
		if (arvoreModelo==null) {
			return;
		}
		
		//PROCESSAMENTO
		org.eclipse.swt.widgets.TreeItem novoItemGrafico = new TreeItem(arvoreGrafica, arvoreGrafica.getStyle());
		novoItemGrafico.setData( arvoreModelo.getConhecimento() );
		novoItemGrafico.setText(arvoreModelo.getConhecimento().getNome());
		
		ArrayList<Item> filhos = arvoreModelo.getFilhos(); 
		if (filhos==null) {
			constroiArvoreGraficaHelper(novoItemGrafico, null);//Percore da Esquerda para Direita
		}else{
			for (Item item : filhos) {
				constroiArvoreGraficaHelper(novoItemGrafico, item);//Percore da Esquerda para Direita	
			}	
		}
	}
	
	public Tree getArvoreGraficaDeConhecimentos()
			throws ConhecimentoInexistenteException {
		PacketStruct respostaPacket = sendPack(null, GET_ARVORECONHECIMENTOS);
    	Tree resposta = (Tree)respostaPacket.getData();
    	if (resposta!=null) {
    		this.arvoreConhecimentos = resposta;
		}
    	
		return arvoreConhecimentos;
	}
	
	/**
	 * Adiciona um novo conhecimento na ontologia.
	 * 
	 * @param item � o n� da ontologia onde ser� criado novo filho
	 * @param novoConhecimento � o nome do novo conhecimento
	 * @return true se sucesso ou false caso contr�rio
	 * @throws Exception 
	 */
	
	public boolean adicionaConhecimento(Conhecimento novoConhecimento, Conhecimento pai) throws Exception{
		
		ArrayList<Conhecimento> filhoPai = new ArrayList<Conhecimento>();
		filhoPai.add(novoConhecimento);
		filhoPai.add(pai);
		
		PacketStruct respostaPacket = sendPack(filhoPai, CorePresleyOperations.ADICIONA_CONHECIMENTO);
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}
			
		listaConhecimentos.add(novoConhecimento);
		return true;
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Mensagem> obterMensagens(Desenvolvedor des) {
		ArrayList<Mensagem> mensagens = null;
		PacketStruct respostaPacket = sendPack(des.getEmail(),CorePresleyOperations.OBTER_MENSAGENS);
		mensagens = (ArrayList<Mensagem>)respostaPacket.getData();
		return mensagens;
	}
	

	/**
	 * Retorna os problemas associados a um desenvolvedor
	 * @param atividade � o nome da atividade
	 * @return ArrayList<String> � a lista de problemas
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Problema> getProblemas(Desenvolvedor desenvolvedor)
	{
		PacketStruct respostaPacket = sendPack(desenvolvedor,CorePresleyOperations.GET_LISTA_PROBLEMAS);
    	ArrayList<Problema> resposta = (ArrayList<Problema>)respostaPacket.getData();
    	
    	
       	if(resposta == null){ 
    		logger.info("RESPOSTA NULL");
    		return new ArrayList<Problema>();
    	}
       	
       	listaProblemas = resposta;
		
		return this.listaProblemas;
	}

	/**
	 * Remove o problema informado
	 * @param problema � o problema a ser excluido
	 * @return boolean se a exclus�o ocorreu bem
	 */
	public boolean removerProblema(Problema problema) {
		PacketStruct respostaPacket = sendPack(problema, CorePresleyOperations.REMOVER_PROBLEMA);
		if(respostaPacket.getData() != null){
			return true;
		}
		return false;
	}
	
	/**
	 * M�todo de teste que cria uma ontologia e uma atividade com seus conhecimentos e problemas associados.
	 * Esta ser para simular o ambiente operacional do sistema.
	 */
//	public void teste()
//	{
//		try {
//			
//			Conhecimento conhecimentoAtividade1 = new Conhecimento();
//			conhecimentoAtividade1.setNome("Banco de Dados");
//			conhecimentoAtividade1.setDescricao("Area geral de banco de dados");
//			Conhecimento conhecimentoAtividade2 = new Conhecimento();
//			conhecimentoAtividade2.setNome("Banco de Dados Relacional");
//			conhecimentoAtividade2.setDescricao("Tipo de Banco de Dados relacional");
//			Conhecimento conhecimentoAtividade3 = new Conhecimento();
//			conhecimentoAtividade3.setNome("LP");
//			conhecimentoAtividade3.setDescricao("Linguagem de programacao");
//			Conhecimento conhecimentoAtividade4 = new Conhecimento();
//			conhecimentoAtividade4.setNome("JAVA");
//			conhecimentoAtividade4.setDescricao("Linguagem JAVA");
//			Conhecimento conhecimentoAtividade5 = new Conhecimento();
//			conhecimentoAtividade5.setNome("C++");
//			conhecimentoAtividade5.setDescricao("Linguagem C++");
//			ArrayList<Conhecimento> listaConhecimentosAtividade = new ArrayList<Conhecimento>();
//			listaConhecimentosAtividade.add(conhecimentoAtividade1);
//			listaConhecimentosAtividade.add(conhecimentoAtividade2);
//
//			
//			
//			Conhecimento conhecimentoDesenvolvedor = new Conhecimento();
//			conhecimentoDesenvolvedor.setNome("MySQL");
//			conhecimentoDesenvolvedor.setDescricao("Banco de dados relacional gratis de otima qualidade");
//			ArrayList<Conhecimento> listaConhecimentosDesenvolvedor = new ArrayList<Conhecimento>();
//			listaConhecimentosDesenvolvedor.add(conhecimentoDesenvolvedor);
//			
//			listaConhecimentos.add(conhecimentoAtividade1);
//			listaConhecimentos.add(conhecimentoAtividade2);
//			listaConhecimentos.add(conhecimentoAtividade3);
//			listaConhecimentos.add(conhecimentoAtividade4);
//			listaConhecimentos.add(conhecimentoAtividade5);
//			listaConhecimentos.add(conhecimentoDesenvolvedor);
//			
//			Desenvolvedor desenvolvedor = new Desenvolvedor();
//			desenvolvedor.setNome("FULANO");
//			desenvolvedor.setEmail("a@a.a");
//			desenvolvedor.setLocalidade("Rua Projetada");
//			desenvolvedor.setSenha("123456");
//			
//			//desenvolvedor.setListaConhecimento(listaConhecimentosDesenvolvedor);
//			Desenvolvedor supervisor = new Desenvolvedor();
//			supervisor.setNome("SICRANO");
//			supervisor.setEmail("sicrano1@algumDominio.com.br");
//			supervisor.setLocalidade("Rua Projetada");
//			//supervisor.setListaConhecimento(listaConhecimentosDesenvolvedor);
//			ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();
//			listaDesenvolvedores.add(desenvolvedor);
//			listaDesenvolvedores.add(supervisor);
//			this.listaDesenvolvedores=listaDesenvolvedores;
//			
//			TipoAtividade atividade = new TipoAtividade("Implementar Presley",desenvolvedor,supervisor,0,new Date(System.currentTimeMillis()),
//					new Date(System.currentTimeMillis()),false,listaConhecimentosAtividade);
//			
//			Tree tree = new Tree("CONHECIMENTO");
//			
//			tree.adicionaFilho("Banco de Dados");
//			
//			tree.getFilho("Banco de Dados").adicionaFilho("Banco de Dados Relacional");
//			tree.getFilho("Banco de Dados").getFilho("Banco de Dados Relacional").adicionaFilho("MySQL");
//			
//			tree.adicionaFilho("LP");
//			tree.getFilho("LP").adicionaFilho("JAVA");
//			tree.getFilho("LP").adicionaFilho("C++");
//			
//			ontologia = tree;
//			
//			//this.adicionaAtividade(atividade);
//			this.adicionaDesenvolvedor(desenvolvedor);
//			//this.adicionaAtividade(atividade);
//			//this.adicionaDesenvolvedor(desenvolvedor);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Metodo responsavel por enviar uma requisicao/pacote ao servidor 
	 * @param data o dado do pacote
	 * @param id a id com o tipo do pacote
	 * @return o pacote de resposta do servidor
	 */
	private PacketStruct sendPack(Object data, int id) {
		
		PacketStruct pack = new PacketStruct(data, id);
		PacketStruct packet = PrincipalSUBJECT.facade(pack);
		return packet;
	}

	public boolean adicionaProblema(Problema problema) throws Exception{
		PacketStruct respostaPacket  = sendPack(problema, CorePresleyOperations.ADICIONA_PROBLEMA);

		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}
		
		Boolean resposta = (Boolean)respostaPacket.getData();
    	if (resposta!=null&&resposta.booleanValue()==true) {
    		this.listaProblemas.add(problema);
    		return true;
		}
		
		return false;
	}
	
	public boolean enviarMensagem(
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) {
		//PacketStruct respostaPacket = sendPack(null,ENVIA_MENSAGEM);//TESTE
    	//Boolean resposta = (Boolean)respostaPacket.getData();
		//return resposta.booleanValue();
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public final ArrayList<Desenvolvedor> getListaDesenvolvedores() {
		PacketStruct respostaPacket = sendPack(null,CorePresleyOperations.GET_LISTA_DESENVOLVEDORES);
    	ArrayList<Desenvolvedor> resposta = (ArrayList<Desenvolvedor>)respostaPacket.getData();
    	listaDesenvolvedores = resposta;
    	
    	if(resposta == null){ 
    		logger.info("RESPOSTA NULL");
    		return new ArrayList<Desenvolvedor>();
    	}
    	
    	Iterator<Desenvolvedor> it = resposta.iterator();
    	while (it.hasNext()) {
    		Desenvolvedor des = it.next();
    		logger.info(des.getNome());
    	}
    	
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
		//return new Desenvolvedor();//TESTE
	}

	public boolean logout(Desenvolvedor desenvolvedor) {
		PacketStruct respostaPacket = sendPack(null,CorePresleyOperations.LOG_OUT);//TESTE
    	Boolean resposta = (Boolean)respostaPacket.getData();
		return resposta.booleanValue();
		//return false;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Conhecimento> getListaConhecimentos() {
		PacketStruct respostaPacket = sendPack(null,CorePresleyOperations.GET_LISTA_CONHECIMENTO);
		ArrayList<Conhecimento> resposta = (ArrayList<Conhecimento>)respostaPacket.getData();
		return resposta;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Problema> getListaProblemas() {
		PacketStruct respostaPacket = sendPack(null,GET_LISTA_PROBLEMAS);
    	ArrayList<Problema> resposta = (ArrayList<Problema>)respostaPacket.getData();
    	listaProblemas = resposta;
    	
		return listaProblemas;
	}

	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem,
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema,
			String mensagem) {
		Mensagem msg = new Mensagem();		
		msg.setDesenvolvedorOrigem(desenvolvedorOrigem);
		msg.setDesenvolvedoresDestino(desenvolvedoresDestino);
		msg.setProblema(problema);
		msg.setTexto(mensagem);
		logger.info("Mensagem dentro de view Comunication: "+msg.getTexto());
		PacketStruct respostaPacket = sendPack(msg, CorePresleyOperations.ENVIAR_MENSAGEM);
		Boolean retorno;
		retorno = (Boolean)respostaPacket.getData();
		logger.info("Retorno do enviar mensagem: " +retorno);
		return retorno;
	}


	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception{
		boolean retorno = true;
		
		PacketStruct respostaPacket = sendPack(desenvolvedor,CorePresleyOperations.ADICIONA_DESENVOLVEDOR);//TESTE
    	//return resposta.booleanValue();
    	
		if (respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}

		listaDesenvolvedores.add(desenvolvedor);
		
		retorno = (Boolean)respostaPacket.getData();
    	
		return retorno;
		//return true;
	}

	public Desenvolvedor login(DadosAutenticacao authData) {
		PacketStruct respostaPacket = sendPack(authData, CorePresleyOperations.LOG_IN);
		if(respostaPacket.getData() != null) {
			Desenvolvedor user = (Desenvolvedor)respostaPacket.getData();
			return user;
		}
		return null;
	}

	public boolean removerConhecimento(Conhecimento conhecimento) {
		PacketStruct respostaPacket = sendPack(conhecimento, CorePresleyOperations.REMOVER_CONHECIMENTO);
		if(respostaPacket.getData() != null){
			return true;
		}
		return false;
	}


	public boolean possuiFilhos(Conhecimento conhecimento)
			throws ConhecimentoInexistenteException {
		PacketStruct respostaPacket = sendPack(conhecimento, CorePresleyOperations.CONHECIMENTO_POSSUI_FILHOS);
		boolean retorno = false;
		if(respostaPacket.getData() != null){
			retorno = (Boolean)respostaPacket.getData();
		}		
		return retorno;
	}
	
	public boolean removerDesenvolvedor(Desenvolvedor desenvolvedor) {
		PacketStruct respostaPacket = sendPack(desenvolvedor, CorePresleyOperations.REMOVER_DESENVOLVEDOR);
		boolean retorno = false;
		if(respostaPacket.getData() != null){
			retorno = (Boolean)respostaPacket.getData();
		}		
		return retorno;

	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getConhecimentosAssociados(String problema) {
		PacketStruct respostaPacket = sendPack(problema,CorePresleyOperations.BUSCA_CONHECIMENTOS_PROBLEMA);
		
		return (ArrayList<String>) respostaPacket.getData();
	}
	

	public Solucao adicionaSolucao(Solucao solucao) {
		PacketStruct respostaPacket = sendPack(solucao, CorePresleyOperations.ADICIONA_SOLUCAO);
		Solucao retorno;
		retorno = (Solucao)respostaPacket.getData();
		logger.info("Retorno do enviar mensagem: " +retorno);
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Solucao> listarSolucoesDoProblema(Problema problema) {
		PacketStruct respostaPacket = sendPack(problema,CorePresleyOperations.GET_LISTA_SOLUCOES_PROBLEMA);
		
		return (ArrayList<Solucao>) respostaPacket.getData();
	}

	public boolean atualizarStatusSolucao(Solucao solucao) {
		PacketStruct respostaPacket = sendPack( solucao, CorePresleyOperations.ATUALIZAR_STATUS_SOLUCAO);
		boolean retorno = false;
		if(respostaPacket.getData() != null){
			retorno = (Boolean)respostaPacket.getData();
		}		
		return retorno;
	}
	
	public boolean atualizarStatusProblema(Problema problema) {
		PacketStruct respostaPacket = sendPack( problema, CorePresleyOperations.ATUALIZAR_STATUS_PROBLEMA);
		boolean retorno = false;
		if(respostaPacket.getData() != null){
			retorno = (Boolean)respostaPacket.getData();
		}		
		return retorno;
	}
	
	public boolean atualizarSolucao(Solucao solucao) {
		PacketStruct respostaPacket = sendPack( solucao, CorePresleyOperations.ATUALIZAR_SOLUCAO);
		boolean retorno = false;
		if(respostaPacket.getData() != null){
			retorno = (Boolean)respostaPacket.getData();
		}		
		return retorno;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(
			Desenvolvedor desenvolvedor){
		PacketStruct respostaPacket = sendPack(desenvolvedor,CorePresleyOperations.GET_LISTA_SOLUCOES_RETORNADAS);
		
		return (ArrayList<Solucao>) respostaPacket.getData();
	}
	
	// TODO: conhecimento.associar(arquivo) 
	public Conhecimento associaArquivo(Conhecimento conhecimento, Arquivo arquivo) throws Exception {

		// FIXME: gambiarra
		ArrayList<Arquivo> arquivosConhecimento = conhecimento.getArquivos();
		arquivosConhecimento.add(arquivo);
		ArrayList<Arquivo> arquivoCadastrar = new ArrayList<Arquivo>();
		arquivoCadastrar.add(arquivo);
		conhecimento.setArquivos(arquivoCadastrar);
		
		PacketStruct respostaPacket = sendPack(conhecimento, CorePresleyOperations.ASSOCIA_ARQUIVO_CONHECIMENTO);
		conhecimento.setArquivos(arquivosConhecimento);
		
		if (respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new Exception((String) respostaPacket.getData());
		}

		if(respostaPacket.getData() != null){
			return (Conhecimento)respostaPacket.getData();
		}		
		return conhecimento;
	}

	/**
	 * Retorna o Projeto utilizado no Presley.
	 * @return 
	 */
	public Projeto getProjetoAtivo(){
		PacketStruct respostaPacket = sendPack(null, GET_PROJETOS_ATIVO);
		Projeto projeto = (Projeto)respostaPacket.getData();
    	
		return projeto;
	}

	public boolean atualizarStatusProjeto(Projeto projeto)
			throws ProjetoInexistenteException {
		PacketStruct respostaPacket = sendPack(projeto,CorePresleyOperations.ATUALIZAR_STATUS_PROJETO);
    	
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new ProjetoInexistenteException();
		}

		return (Boolean)respostaPacket.getData();
	}

	public boolean criarProjeto(Projeto projeto) throws NomeInvalidoException {
		PacketStruct respostaPacket = sendPack(projeto,CorePresleyOperations.CRIAR_PROJETO);
    	
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new NomeInvalidoException();
		}

		return (Boolean)respostaPacket.getData();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Projeto> getListaProjetos(PacketStruct packet) {
		PacketStruct respostaPacket = sendPack(null,CorePresleyOperations.GET_PROJETOS);
		return (ArrayList<Projeto>) respostaPacket.getData();
	}

	public boolean removerProjeto(Projeto projeto)
			throws ProjetoInexistenteException {
		PacketStruct respostaPacket = sendPack(projeto,CorePresleyOperations.REMOVER_PROJETO);
    	
		if(respostaPacket.getId() == CorePresleyOperations.ERRO) {
			throw new ProjetoInexistenteException();
		}

		return (Boolean)respostaPacket.getData();
	}

	public Tree getArvoreConhecimentos()
			throws ConhecimentoInexistenteException {
		return null;
	}

	public Desenvolvedor getDesenvolvedorPorNome(String nome) throws DesenvolvedorInexistenteException
	{
		PacketStruct respostaPacket = sendPack(nome, GET_DESENVOLVEDOR_POR_NOME);
		Desenvolvedor desenvolvedor = (Desenvolvedor)respostaPacket.getData();
    	
		return desenvolvedor;
	}

	
}


