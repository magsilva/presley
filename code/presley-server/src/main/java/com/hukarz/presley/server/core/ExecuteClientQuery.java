package com.hukarz.presley.server.core;

/**
 * Esta classe implementa as opera��es que s�o chamadas pelo
 * @author EduardoPS, Bruno, Tatiane
 * @since 2008
 */



import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.hukarz.presley.beans.BuscaDesenvolvedores;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.ConhecimentoAtividade;
import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.ProblemaAtividade;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.QualificacaoDesenvolvedor;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.excessao.AtividadeInexistenteException;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DataInvalidaException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.excessao.SenhaInvalidaException;
import com.hukarz.presley.excessao.SolucaoIniexistenteException;
import com.hukarz.presley.server.core.interfaces.CorePresleyOperations;
import com.hukarz.presley.server.inferencia.Inferencia;
import com.hukarz.presley.server.ontologia.Ontologia;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoConhecimentoImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoDesenvolvedorImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoMensagemImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoProblemaImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoProjetoImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoSolucaoImpl;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoUtil;


/**
 * @author Adm Cleyton
 *
 */
public class ExecuteClientQuery implements CorePresleyOperations{


	/**
	 * Variaveis instanciadas uma �nica vez para ter acesso aos servicos de 
	 * persist�ncia oferecidos pelas classes de valida��o
	 */
	ValidacaoConhecimentoImpl  validacaoConhecimento;
	ValidacaoProblemaImpl 	   validacaoProblema; 
	ValidacaoDesenvolvedorImpl validacaoDesenvolvedor; 
	ValidacaoMensagemImpl 	   validacaoMensagem;
	ValidacaoSolucaoImpl       validacaoSolucao;
	ValidacaoProjetoImpl       validacaoProjeto;

	public ExecuteClientQuery() {
		validacaoConhecimento	= new ValidacaoConhecimentoImpl();
		validacaoProblema		= new ValidacaoProblemaImpl();
		validacaoDesenvolvedor	= new ValidacaoDesenvolvedorImpl();
		validacaoMensagem		= new ValidacaoMensagemImpl();
		validacaoSolucao		= new ValidacaoSolucaoImpl();
		validacaoProjeto		= new ValidacaoProjetoImpl();
	}

	/**
	 * OK
	 * @param packet
	 * @return
	 * @throws Exception 
	 * @throws ConhecimentoInexistenteException 
	 * @throws DescricaoInvalidaException 
	 */
	public boolean adicionaConhecimento(PacketStruct packet) throws DescricaoInvalidaException, ConhecimentoInexistenteException, Exception {
		ArrayList<Conhecimento> conhecimento = (ArrayList<Conhecimento>) packet.getData();
		if (conhecimento==null) {
			this.adicionaConhecimento(null, null);
		}
		Conhecimento pai = conhecimento.get(1);
		Conhecimento filho = conhecimento.get(0);

		this.adicionaConhecimento(pai,filho); 
		return true;
	}
	public boolean adicionaConhecimento(Conhecimento pai, Conhecimento filho) throws DescricaoInvalidaException, ConhecimentoInexistenteException, Exception {

		validacaoConhecimento.criarConhecimento( filho.getNome(), filho.getDescricao() );
		if (pai != null) {
			try {
				validacaoConhecimento.associaConhecimentos( pai.getNome(), filho.getNome());
			} catch (Exception e) {
				validacaoConhecimento.removerConhecimento(filho.getNome());
				throw e;
			}
		}
		return true;

	}

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws AtividadeInexistenteException 
	 * @throws DescricaoInvalidaException 
	 */
	public boolean associaProblemaAtividade(PacketStruct packet) throws DescricaoInvalidaException, AtividadeInexistenteException {
		ProblemaAtividade problemaAtividade = (ProblemaAtividade) packet.getData();

		Problema problema = problemaAtividade.getProblema();
		TipoAtividade atividade =  problemaAtividade.getAtividade();
		ArrayList<Conhecimento> listaConhecimentos = problemaAtividade.getListaConhecimentos();
		
		if (listaConhecimentos == null) {
			System.out.println("NULL ><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			return false;
		}
		
		if(listaConhecimentos.isEmpty()) {
			System.out.println("empty>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			return false;
		}
		
		for(Conhecimento c  : listaConhecimentos)
		System.out.println("chamada1 >>>>>>>>>>>>>>>>>>>>>> " + c.getNome());

		return associaProblemaAtividade(problema, atividade, listaConhecimentos);
	}
	public boolean associaProblemaAtividade(Problema problema,
			TipoAtividade atividade, 
			ArrayList<Conhecimento> listaConhecimentos) throws DescricaoInvalidaException, AtividadeInexistenteException {
/*
		validacaoProblema.cadastrarProblema(atividade.getId(), problema.getDescricao(), 
				problema.getData(), problema.getMensagem(), listaConhecimentos);

*/		return true;
	}

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(PacketStruct packet) throws DesenvolvedorInexistenteException {
		Problema problema = (Problema) packet.getData();

		return buscaDesenvolvedores(problema);
	}	

	public ArrayList<Desenvolvedor> buscaDesenvolvedores(Problema problema) throws DesenvolvedorInexistenteException {
		ArrayList<Desenvolvedor> listaDesenvolvedores = Inferencia.getDesenvolvedores(validacaoProblema.getDesenvolvedoresArquivo(problema), problema.getConhecimento());

		return listaDesenvolvedores;
	}


	public boolean desassociaProblemaAtividade(PacketStruct packet) throws ProblemaInexistenteException {
		if(packet.getData() == null) {
			return false;
		}
		Problema problema = (Problema)packet.getData();
		return desassociaProblemaAtividade(problema);
	}

	public boolean desassociaProblemaAtividade(Problema problema) throws ProblemaInexistenteException {
		return validacaoProblema.removerProblema(problema);
	}
	
	
	public boolean removerProblema(PacketStruct packet) throws ProblemaInexistenteException {
		if(packet.getData() == null) {
			return false;
		}
		Problema problema = (Problema)packet.getData();
		return removerProblema(problema);
	}

	public boolean removerProblema(Problema problema) throws ProblemaInexistenteException {
		return validacaoProblema.removerProblema(problema);
	}

	public boolean enviarMensagem(PacketStruct packet) throws DesenvolvedorInexistenteException {
		Mensagem msg = (Mensagem) packet.getData();
		return enviarMensagem(msg.getDesenvolvedoresDestino(), msg.getProblema());
	}

	public boolean enviarMensagem(
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema) throws DesenvolvedorInexistenteException {
		return validacaoMensagem.adicionarMensagem(desenvolvedoresDestino, problema);
	}

	public ArrayList<Mensagem> requisitaMensagens(Desenvolvedor desenvolvedorDestino) {
		return validacaoMensagem.getMensagens(desenvolvedorDestino.getEmail());
	}

	public ArrayList<Desenvolvedor> getListaDesenvolvedores() {

		return validacaoDesenvolvedor.getListaDesenvolvedores();
	}

	public Desenvolvedor login(PacketStruct packet) throws DesenvolvedorInexistenteException, EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException {
		DadosAutenticacao authData = (DadosAutenticacao) packet.getData();
		Desenvolvedor desenvolvedor = validacaoDesenvolvedor.autenticaDesenvolvedor(authData);

		return desenvolvedor;
	}
	public Desenvolvedor login(DadosAutenticacao authData) throws DesenvolvedorInexistenteException, EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException {
		return validacaoDesenvolvedor.autenticaDesenvolvedor(authData);
	}

	public boolean logout(PacketStruct packet) {
		Desenvolvedor authData = (Desenvolvedor) packet.getData();
		return logout(authData);
	}
	public boolean logout(Desenvolvedor desenvolvedor) {
		// J� que n�o existe sess�o, a opera��o de log-out � "ficticia"
		return true;
	}

	public boolean qualificaDesenvolvedor(PacketStruct packet) throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException {
		QualificacaoDesenvolvedor qualDes = (QualificacaoDesenvolvedor) packet.getData();
		Problema problema = qualDes.getProblema();
		Desenvolvedor desenvolvedor = qualDes.getDesenvolvedor();
		boolean foiUtil = qualDes.isFoiUtil();
		System.out.println("ExecuteClientQuery: "+ problema.getDescricao());
		return qualificaDesenvolvedor(desenvolvedor, problema, foiUtil);
	}
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor,
			Problema problema, boolean qualificacao) throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException {
		//ArrayList<String> conhecimentos = problema.getConhecimentos();
		ArrayList<String> conhecimentos = validacaoProblema.getConhecimentosAssociados(problema.getDescricao());
		// TODO: Should throw this exception?
		return Ontologia.incrementaRespostasDesenvolvedor(desenvolvedor, qualificacao, conhecimentos);
	}

	public ArrayList<Conhecimento> getListaConhecimentos() {
		ArrayList<Conhecimento> retorno = null;
		retorno = validacaoConhecimento.getListaConhecimento();
		return retorno;
	}

	public boolean adicionaDesenvolvedor(PacketStruct packet) throws Exception {
		Desenvolvedor desenvolvedor = (Desenvolvedor)packet.getData();
		return adicionaDesenvolvedor(desenvolvedor);
	}
	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception {
		validacaoDesenvolvedor.criarDesenvolvedor(desenvolvedor.getEmail(), 
				desenvolvedor.getNome(), 
				desenvolvedor.getCVSNome(), 
				desenvolvedor.getSenha(),
				desenvolvedor.getListaConhecimento());
		return true;
	}

	public Tree getOntologia() throws ConhecimentoInexistenteException {
		//Ontologia ontologia = new Ontologia(null, null);

		// TODO: Should throw this exception?
		return Ontologia.getArvoreDeConhecimentos();
	}

	public ArrayList<Problema> getListaProblemas(PacketStruct packet) {
		Desenvolvedor desenvolvedor = (Desenvolvedor) packet.getData();
		
		ArrayList<Problema> listaProblemas = validacaoProblema.getListaProblema(desenvolvedor);
		return listaProblemas;
	}


	public Object removerConhecimento(PacketStruct packet) throws ConhecimentoInexistenteException {
		Conhecimento conhecimento = (Conhecimento) packet.getData();
		return removerConhecimento(conhecimento);
	}
	public boolean removerConhecimento(Conhecimento conhecimento) throws ConhecimentoInexistenteException {
		return validacaoConhecimento.removerConhecimento(conhecimento.getNome());
	}

	public Object possuiFilhos(PacketStruct packet) throws ConhecimentoInexistenteException {
		Conhecimento conhecimento = (Conhecimento)packet.getData();
		return this.possuiFilhos(conhecimento);
	}
	public boolean possuiFilhos(Conhecimento conhecimento) throws ConhecimentoInexistenteException {
		return validacaoConhecimento.possuiFilhos(conhecimento);
	}

	public boolean removerDesenvolvedor(PacketStruct packet) {
		Desenvolvedor desenvolvedor = (Desenvolvedor)packet.getData();
		return this.removerDesenvolvedor(desenvolvedor);
	}
	public boolean removerDesenvolvedor(Desenvolvedor desenvolvedor) {
		return validacaoDesenvolvedor.removerDesenvolvedor(desenvolvedor.getEmail());
	}
	
	public ArrayList<Mensagem> obterMensagens(PacketStruct packet) {
		return this.obterMensagens((String)packet.getData());
	}

	public ArrayList<Mensagem> obterMensagens(String email) {
		return validacaoMensagem.getMensagens(email);
	}
	
	public ArrayList<String> buscaConhecimentosProblema(PacketStruct packet) {
		String nomeProblema = (String)packet.getData();
		return validacaoProblema.getConhecimentosAssociados(nomeProblema);
	}

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws DescricaoInvalidaException 
	 * @throws AtividadeInexistenteException 
	 * @throws DescricaoInvalidaException 
	 * @throws IOException 
	 */
	public boolean adicionaProblema(PacketStruct packet) throws DescricaoInvalidaException, IOException {
		Problema problema = (Problema) packet.getData();

		if (problema == null) {
			System.out.println("NULL ><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			return false;
		}
		
		if(problema.getDesenvolvedorOrigem() == null) {
			System.out.println("NULL DESENVOLVEDOR>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			return false;
		}
		
		return adicionaProblema(problema);
	}
	
	public boolean adicionaProblema(Problema problema) throws DescricaoInvalidaException, IOException {
		validacaoProblema.cadastrarProblema(problema);

		return true;
	}
	
	/**
	 * 
	 * @param packet
	 * @return
	 * @throws ProblemaInexistenteException 
	 * @throws DesenvolvedorInexistenteException
	 */
	public Solucao adicionaSolucao(PacketStruct packet) throws ProblemaInexistenteException, DesenvolvedorInexistenteException {
		Solucao solucao = (Solucao) packet.getData();

		return adicionaSolucao( solucao );
	}
	
	public Solucao adicionaSolucao(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException{
		return validacaoSolucao.cadastrarSolucao(solucao);
	}
	
	
	/**
	 * 
	 * @param packet
	 * @return
	 * @throws ProblemaInexistenteException 
	 * @throws ProblemaInexistenteException 
	 * @throws DesenvolvedorInexistenteException
	 */
	public ArrayList<Solucao> listarSolucoesDoProblema(PacketStruct packet){
		Problema problema = (Problema) packet.getData();
		ArrayList<Solucao> listaDeSolucoes = new ArrayList<Solucao>();
		
		try {
			listaDeSolucoes = listarSolucoesDoProblema( problema );
		} catch (ProblemaInexistenteException e) {
			e.printStackTrace();
		}
		
		return listaDeSolucoes;
	}
	
	public ArrayList<Solucao> listarSolucoesDoProblema(Problema problema) throws ProblemaInexistenteException{
		return validacaoSolucao.listarSolucoesDoProblema(problema);
	}
	
	public boolean atualizarStatusSolucao(PacketStruct packet) throws SolucaoIniexistenteException  {
		Solucao solucao = (Solucao) packet.getData();
		return atualizarStatusSolucao(solucao);
	}
	public boolean atualizarStatusSolucao(Solucao solucao) throws SolucaoIniexistenteException{
		return validacaoSolucao.atualizarStatusDaSolucao(solucao.getId(), solucao.isAjudou());
	}

	
	public boolean atualizarStatusProblema(PacketStruct packet) throws ProblemaInexistenteException  {
		Problema problema = (Problema) packet.getData();
		return atualizarStatusProblema(problema);
	}
	public boolean atualizarStatusProblema(Problema problema) throws ProblemaInexistenteException {
		return validacaoProblema.atualizarStatusDoProblema(problema.getId(), problema.isResolvido());
	}
	
	public boolean atualizarSolucao(PacketStruct packet) throws ProblemaInexistenteException, DesenvolvedorInexistenteException {
		Solucao solucao = (Solucao) packet.getData();
		return atualizarSolucao(solucao);
	}
	public boolean atualizarSolucao(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException  {
		return validacaoSolucao.atualizarStatusDoProblema(solucao);
	}
	

	/**
	 * @param packet
	 * @return
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(PacketStruct packet)
	throws DesenvolvedorInexistenteException{
		Desenvolvedor desenvolvedor = (Desenvolvedor) packet.getData();
		ArrayList<Solucao> listaDeSolucoes = new ArrayList<Solucao>();
		
		listaDeSolucoes = listarSolucoesRetornadasDoDesenvolvedor( desenvolvedor );
		
		return listaDeSolucoes;
	}
	
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(Desenvolvedor desenvolvedor)
	throws DesenvolvedorInexistenteException{
		return validacaoSolucao.listarSolucoesRetornadasDoDesenvolvedor(desenvolvedor);
	}
	

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws IOException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public Conhecimento associaArquivo(PacketStruct packet) throws ConhecimentoInexistenteException, IOException  {
		Conhecimento conhecimento = (Conhecimento) packet.getData();

		return associaArquivo(conhecimento);
	}
	
	public Conhecimento associaArquivo(Conhecimento conhecimento) throws ConhecimentoInexistenteException, IOException {
		return validacaoConhecimento.associaArquivo(conhecimento);
	}

	public Projeto getProjetoAtivo()  {
		return validacaoProjeto.getProjetoAtivo();
	}

	public boolean atualizarStatusProjeto(PacketStruct packet) throws ProjetoInexistenteException{
		Projeto projeto = (Projeto) packet.getData();
		return atualizarStatusProjeto( projeto );
	}
	public boolean atualizarStatusProjeto(Projeto projeto)
			throws ProjetoInexistenteException {
		return validacaoProjeto.atualizarStatusProjeto(projeto);
	}
	
	public boolean criarProjeto(PacketStruct packet) throws NomeInvalidoException {
		Projeto projeto = (Projeto) packet.getData();
		return criarProjeto( projeto );
	}
	public boolean criarProjeto(Projeto projeto) throws NomeInvalidoException {
		return validacaoProjeto.criarProjeto(projeto);
	}

	public boolean removerProjeto(PacketStruct packet) throws ProjetoInexistenteException {
		Projeto projeto = (Projeto) packet.getData();
		return removerProjeto( projeto );
	}
	public boolean removerProjeto(Projeto projeto)
			throws ProjetoInexistenteException {
		return validacaoProjeto.removerProjeto(projeto);
	}

	public ArrayList<Projeto> getListaProjetos(PacketStruct packet) {
		return validacaoProjeto.getProjetos();
	}
	
}
