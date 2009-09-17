package com.hukarz.presley.server.core;

/**
 * Esta classe implementa as operações que são chamadas pelo
 * @author EduardoPS, Bruno, Tatiane
 * @since 2008
 */

import java.io.IOException;
import java.util.ArrayList;

import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.excessao.SenhaInvalidaException;
import com.hukarz.presley.excessao.SolucaoIniexistenteException;
import com.hukarz.presley.server.conhecimento.Conhecimento;
import com.hukarz.presley.server.core.interfaces.CorePresleyOperations;
import com.hukarz.presley.server.mensagem.ValidacaoMensagemImpl;
import com.hukarz.presley.server.mensagem.MensagemProblema;
import com.hukarz.presley.server.mensagem.MensagemSolucao;
import com.hukarz.presley.server.usuario.Usuario;
import com.hukarz.presley.server.util.CadastroProjeto;


/**
 * @author Adm Cleyton
 *
 */
public class ExecuteClientQuery implements CorePresleyOperations{


	/**
	 * Variaveis instanciadas uma única vez para ter acesso aos servicos de 
	 * persistência oferecidos pelas classes de validação
	 */
	Conhecimento		validacaoConhecimento;
	MensagemProblema	validacaoProblema; 
	ValidacaoMensagemImpl	validacaoMensagem;
	MensagemSolucao		validacaoSolucao;
	CadastroProjeto		validacaoProjeto ;
	Usuario				validacaoDesenvolvedor ;

	public ExecuteClientQuery() {
		validacaoProblema		= new MensagemProblema();
		validacaoMensagem		= new ValidacaoMensagemImpl();
		validacaoSolucao		= new MensagemSolucao();
		validacaoConhecimento	= new Conhecimento();
		validacaoProjeto		= new CadastroProjeto();
		validacaoDesenvolvedor	= new Usuario();
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
		ArrayList<TopicoConhecimento> conhecimento = (ArrayList<TopicoConhecimento>) packet.getData();
		if (conhecimento==null) {
			this.adicionaConhecimento(null, null);
		}
		TopicoConhecimento pai = conhecimento.get(1);
		TopicoConhecimento filho = conhecimento.get(0);

		this.adicionaConhecimento(pai,filho); 
		return true;
	}
	
	public boolean adicionaConhecimento(TopicoConhecimento pai, TopicoConhecimento filho) throws DescricaoInvalidaException, ConhecimentoInexistenteException, Exception {
		validacaoConhecimento.setConhecimento(pai);
		validacaoConhecimento.criarConhecimento( );
		if (pai != null) {
			try {
				validacaoConhecimento.associaConhecimentos( filho );
			} catch (Exception e) {
				validacaoConhecimento.removerConhecimento( );
				throw e;
			}
		}
		return true;
	}


	public ArrayList<Desenvolvedor> buscaDesenvolvedores(Problema problema) throws DesenvolvedorInexistenteException {
		return new ArrayList<Desenvolvedor>();
	}


	public boolean removerProblema(PacketStruct packet) throws ProblemaInexistenteException {
		if(packet.getData() == null) {
			return false;
		}
		Problema problema = (Problema)packet.getData();
		return removerProblema(problema);
	}

	public boolean removerProblema(Problema problema) throws ProblemaInexistenteException {
		validacaoProblema.setProblema(problema);
		return validacaoProblema.removerProblema();
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
		Usuario validacaoDesenvolvedor = new Usuario( );

		return validacaoDesenvolvedor.getListaDesenvolvedores();
	}

	public Desenvolvedor login(PacketStruct packet) throws DesenvolvedorInexistenteException, EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException {
		DadosAutenticacao authData = (DadosAutenticacao) packet.getData();
		
		Usuario validacaoDesenvolvedor = new Usuario( );
		Desenvolvedor desenvolvedor = validacaoDesenvolvedor.autenticaDesenvolvedor(authData);

		return desenvolvedor;
	}

	public Desenvolvedor login(DadosAutenticacao authData) throws DesenvolvedorInexistenteException, EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException {
		Usuario validacaoDesenvolvedor = new Usuario( );
		return validacaoDesenvolvedor.autenticaDesenvolvedor(authData);
	}

	public boolean logout(PacketStruct packet) {
		Desenvolvedor authData = (Desenvolvedor) packet.getData();
		return logout(authData);
	}
	public boolean logout(Desenvolvedor desenvolvedor) {
		// Já que não existe sessão, a operação de log-out é "ficticia"
		return true;
	}

	public ArrayList<TopicoConhecimento> getListaConhecimentos() {
		ArrayList<TopicoConhecimento> retorno = null;
		retorno = validacaoConhecimento.getListaConhecimento();
		return retorno;
	}

	public boolean adicionaDesenvolvedor(PacketStruct packet) throws Exception {
		Desenvolvedor desenvolvedor = (Desenvolvedor)packet.getData();
		return adicionaDesenvolvedor(desenvolvedor);
	}
	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception {
		validacaoDesenvolvedor.setDesenvolvedor(desenvolvedor);
		validacaoDesenvolvedor.criarDesenvolvedor();
		return true;
	}

	public Tree getArvoreConhecimentos() throws ConhecimentoInexistenteException {
		return validacaoConhecimento.getArvoreDeConhecimentos();
	}

	public ArrayList<Problema> getListaProblemas(PacketStruct packet) {
		Desenvolvedor desenvolvedor = (Desenvolvedor) packet.getData();
		validacaoProblema.setDesenvolvedor(desenvolvedor);
		ArrayList<Problema> listaProblemas = validacaoProblema.getListaProblema();
		return listaProblemas;
	}


	public Object removerConhecimento(PacketStruct packet) throws ConhecimentoInexistenteException {
		TopicoConhecimento conhecimento = (TopicoConhecimento) packet.getData();
		return removerConhecimento(conhecimento);
	}
	public boolean removerConhecimento(TopicoConhecimento conhecimento) throws ConhecimentoInexistenteException {
		validacaoConhecimento.setConhecimento(conhecimento);
		return validacaoConhecimento.removerConhecimento();
	}

	public Object possuiFilhos(PacketStruct packet) throws ConhecimentoInexistenteException {
		TopicoConhecimento conhecimento = (TopicoConhecimento)packet.getData();
		return this.possuiFilhos(conhecimento);
	}
	public boolean possuiFilhos(TopicoConhecimento conhecimento) throws ConhecimentoInexistenteException {
		validacaoConhecimento.setConhecimento(conhecimento);
		return validacaoConhecimento.possuiFilhos();
	}

	public boolean removerDesenvolvedor(PacketStruct packet) throws DesenvolvedorInexistenteException {
		Desenvolvedor desenvolvedor = (Desenvolvedor)packet.getData();
		return this.removerDesenvolvedor(desenvolvedor);
	}
	public boolean removerDesenvolvedor(Desenvolvedor desenvolvedor) throws DesenvolvedorInexistenteException {
		validacaoDesenvolvedor.setDesenvolvedor(desenvolvedor);
		return validacaoDesenvolvedor.removerDesenvolvedor();
	}
	
	public ArrayList<Mensagem> obterMensagens(PacketStruct packet) {
		return this.obterMensagens((String)packet.getData());
	}

	public ArrayList<Mensagem> obterMensagens(String email) {
		return validacaoMensagem.getMensagens(email);
	}
	
	public ArrayList<String> buscaConhecimentosProblema(PacketStruct packet) throws ProblemaInexistenteException {
		String nomeProblema = (String)packet.getData();
		Problema problema = new Problema();
		problema.setDescricao(nomeProblema);
		return validacaoProblema.getConhecimentosAssociados( );
	}

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws DescricaoInvalidaException 
	 * @throws AtividadeInexistenteException 
	 * @throws DescricaoInvalidaException 
	 * @throws IOException 
	 * @throws ProjetoInexistenteException 
	 * @throws ConhecimentoNaoEncontradoException 
	 * @throws ProblemaInexistenteException 
	 */
	public boolean adicionaProblema(PacketStruct packet) throws DescricaoInvalidaException, IOException, ProjetoInexistenteException, ConhecimentoNaoEncontradoException, ProblemaInexistenteException {
		Problema problema = (Problema) packet.getData();

		if (problema == null) {
			return false;
		}
		
		if(problema.getDesenvolvedorOrigem() == null) {
			return false;
		}
		
		return adicionaProblema(problema);
	}
	
	public boolean adicionaProblema(Problema problema) throws DescricaoInvalidaException, IOException, ProjetoInexistenteException, ConhecimentoNaoEncontradoException, ProblemaInexistenteException {
		validacaoProblema.setProblema(problema);
		validacaoProblema.cadastrarProblema();

		return true;
	}
	
	/**
	 * 
	 * @param packet
	 * @return
	 * @throws ProblemaInexistenteException 
	 * @throws DesenvolvedorInexistenteException
	 * @throws SolucaoIniexistenteException 
	 */
	public Solucao adicionaSolucao(PacketStruct packet) throws ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException {
		Solucao solucao = (Solucao) packet.getData();

		return adicionaSolucao( solucao );
	}
	
	public Solucao adicionaSolucao(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException{
		validacaoSolucao.setSolucao(solucao);
		return validacaoSolucao.cadastrarSolucao();
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
		validacaoSolucao.setProblema(problema);
		return validacaoSolucao.listarSolucoesDoProblema();
	}
	
	public boolean atualizarStatusSolucao(PacketStruct packet) throws SolucaoIniexistenteException, ProblemaInexistenteException, DesenvolvedorInexistenteException   {
		Solucao solucao = (Solucao) packet.getData();
		return atualizarStatusSolucao(solucao);
	}
	public boolean atualizarStatusSolucao(Solucao solucao) throws SolucaoIniexistenteException, ProblemaInexistenteException, DesenvolvedorInexistenteException{
		validacaoSolucao.setSolucao(solucao);
		return validacaoSolucao.atualizarStatusDaSolucao();
	}

	
	public boolean atualizarStatusProblema(PacketStruct packet) throws ProblemaInexistenteException  {
		Problema problema = (Problema) packet.getData();
		return atualizarStatusProblema(problema);
	}
	public boolean atualizarStatusProblema(Problema problema) throws ProblemaInexistenteException {
		validacaoProblema.setProblema(problema);
		return validacaoProblema.atualizarStatusDoProblema();
	}
	
	public boolean atualizarSolucao(PacketStruct packet) throws ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException {
		Solucao solucao = (Solucao) packet.getData();
		return atualizarSolucao(solucao);
	}
	public boolean atualizarSolucao(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException, SolucaoIniexistenteException  {
		validacaoSolucao.setSolucao(solucao);
		return validacaoSolucao.atualizarStatusDaSolucao();
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
		validacaoSolucao.setDesenvolvedor(desenvolvedor);
		return validacaoSolucao.listarSolucoesRetornadasDoDesenvolvedor();
	}
	

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws IOException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public TopicoConhecimento associaArquivo(PacketStruct packet) throws ConhecimentoInexistenteException, IOException  {
		TopicoConhecimento conhecimento = (TopicoConhecimento) packet.getData();

		return associaArquivo(conhecimento);
	}
	
	public TopicoConhecimento associaArquivo(TopicoConhecimento conhecimento) throws ConhecimentoInexistenteException, IOException {
		validacaoConhecimento.setConhecimento(conhecimento);
		return validacaoConhecimento.associaArquivo();
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
		validacaoProjeto.setProjeto(projeto);
		return validacaoProjeto.atualizarStatusProjeto();
	}
	
	public boolean criarProjeto(PacketStruct packet) throws NomeInvalidoException, ProjetoInexistenteException {
		Projeto projeto = (Projeto) packet.getData();
		return criarProjeto( projeto );
	}
	public boolean criarProjeto(Projeto projeto) throws NomeInvalidoException, ProjetoInexistenteException {
		validacaoProjeto.setProjeto(projeto);
		return validacaoProjeto.criarProjeto();
	}

	public boolean removerProjeto(PacketStruct packet) throws ProjetoInexistenteException {
		Projeto projeto = (Projeto) packet.getData();
		return removerProjeto( projeto );
	}
	public boolean removerProjeto(Projeto projeto)
			throws ProjetoInexistenteException {
		validacaoProjeto.setProjeto(projeto);
		return validacaoProjeto.removerProjeto();
	}

	public ArrayList<Projeto> getListaProjetos(PacketStruct packet) {
		return validacaoProjeto.getProjetos();
	}


	public Desenvolvedor getDesenvolvedorPorNome(PacketStruct packet) throws DesenvolvedorInexistenteException {
		String nome = (String) packet.getData();
		return getDesenvolvedorPorNome( nome );
	}
	@Override
	public Desenvolvedor getDesenvolvedorPorNome(String nome)
			throws DesenvolvedorInexistenteException {
		Desenvolvedor desenvolvedor = new Desenvolvedor();
		desenvolvedor.setNome(nome);
		validacaoDesenvolvedor.setDesenvolvedor(desenvolvedor);		
		return validacaoDesenvolvedor.getDesenvolvedorPorNome();
	}

}
