package core;

/**
 * Esta classe implementa as opera��es que s�o chamadas pelo
 * @author EduardoPS, Bruno, Tatiane
 * @since 2008
 */


import inferencia.Inferencia;

import java.util.ArrayList;
import java.util.Iterator;

import ontologia.Ontologia;

import excessao.AtividadeInexistenteException;
import excessao.ConhecimentoInexistenteException;
import excessao.DataInvalidaException;
import excessao.DescricaoInvalidaException;
import excessao.DesenvolvedorInexistenteException;
import excessao.EmailInvalidoException;
import excessao.ErroDeAutenticacaoException;
import excessao.ProblemaInexistenteException;
import excessao.SenhaInvalidaException;
import validacao.implementacao.ValidacaoAtividadeImpl;
import validacao.implementacao.ValidacaoConhecimentoImpl;
import validacao.implementacao.ValidacaoDesenvolvedorImpl;
import validacao.implementacao.ValidacaoMensagemImpl;
import validacao.implementacao.ValidacaoProblemaImpl;

import beans.BuscaDesenvolvedores;
import beans.Conhecimento;
import beans.ConhecimentoAtividade;
import beans.DadosAutenticacao;
import beans.Desenvolvedor;
import beans.Mensagem;
import beans.Problema;
import beans.ProblemaAtividade;
import beans.QualificacaoDesenvolvedor;
import beans.TipoAtividade;
import beans.Tree;
import core.interfaces.CorePresleyOperations;
import facade.PacketStruct;

public class ExecuteClientQuery implements CorePresleyOperations{


	/**
	 * Variaveis instanciadas uma �nica vez para ter acesso aos servicos de 
	 * persist�ncia oferecidos pelas classes de valida��o
	 */
	ValidacaoConhecimentoImpl  validacaoConhecimento;
	ValidacaoAtividadeImpl 	   validacaoAtividade;
	ValidacaoProblemaImpl 	   validacaoProblema; 
	ValidacaoDesenvolvedorImpl validacaoDesenvolvedor; 
	ValidacaoMensagemImpl 	   validacaoMensagem;
	ValidacaoConhecimentoImpl  valConhecimento; 

	public ExecuteClientQuery() {
		validacaoConhecimento  = new ValidacaoConhecimentoImpl();
		validacaoAtividade 	   = new ValidacaoAtividadeImpl();
		validacaoProblema 	   = new ValidacaoProblemaImpl();
		validacaoDesenvolvedor = new ValidacaoDesenvolvedorImpl();
		validacaoMensagem 	   = new ValidacaoMensagemImpl();
		valConhecimento 	   = new ValidacaoConhecimentoImpl();
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
		return true;
	}

	/**
	 * OK
	 * @param packet
	 * @return
	 * @throws Exception 
	 * @throws DataInvalidaException 
	 * @throws DescricaoInvalidaException 
	 * @throws EmailInvalidoException 
	 */
	public boolean adicionaAtividade(PacketStruct packet) throws EmailInvalidoException, DescricaoInvalidaException, DataInvalidaException, Exception {
		TipoAtividade atividade = (TipoAtividade) packet.getData();

		validacaoAtividade.cadastrarAtividade(atividade);
		return true;
	}
	public boolean adicionaAtividade(TipoAtividade novaAtividade) throws EmailInvalidoException, DescricaoInvalidaException, DataInvalidaException, Exception {

		validacaoAtividade.cadastrarAtividade(novaAtividade.getDesenvolvedor().getEmail(), 
				novaAtividade.getSupervisor().getEmail(), 
				novaAtividade.getDescricao(), 
				novaAtividade.getDataInicio(), 
				novaAtividade.getDataFinal(),
				novaAtividade.getListaDeConhecimentosEnvolvidos());
		return true;
	}

	/**
	 * OK
	 * @param packet
	 * @return
	 * @throws Exception 
	 */
	public boolean associaConhecimentoAtividade(PacketStruct packet) throws 
	AtividadeInexistenteException, 
	ConhecimentoInexistenteException, 
	Exception{

		ConhecimentoAtividade conhecimentoAtividade = new ConhecimentoAtividade();
		ArrayList<Conhecimento> listaConhecimento = conhecimentoAtividade.getConhecimentos();
		TipoAtividade atividade = conhecimentoAtividade.getAtividade();

		return associaConhecimentoAtividade(listaConhecimento, atividade);
	}
	public boolean associaConhecimentoAtividade(ArrayList<Conhecimento> listaConhecimento, 
			TipoAtividade atividade) throws AtividadeInexistenteException, ConhecimentoInexistenteException, Exception {

		for(Conhecimento c : listaConhecimento) {
			validacaoAtividade.adicionarConhecimentoAAtividade(atividade.getId(), c.getNome());
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

		return associaProblemaAtividade(problema, atividade);
	}
	public boolean associaProblemaAtividade(Problema problema,
			TipoAtividade atividade) throws DescricaoInvalidaException, AtividadeInexistenteException {

		validacaoProblema.cadastrarProblema(atividade.getId(), problema.getDescricao(), problema.getData(), problema.getMensagem());
		return true;
	}

	/**
	 * 
	 * @param packet
	 * @return
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(PacketStruct packet) throws DesenvolvedorInexistenteException {
		/*BuscaDesenvolvedores busca = (BuscaDesenvolvedores)packet.getData();

		Problema problema = busca.getProblema();
		ArrayList<String> listaConhecimento = busca.getListaConhecimento();
		int grauDeConfianca = busca.getGrauDeConfianca();

		return buscaDesenvolvedores(problema, listaConhecimento, grauDeConfianca);*/
		return new ArrayList<Desenvolvedor>();
	}	

	public ArrayList<Desenvolvedor> buscaDesenvolvedores(Problema problema,
			ArrayList<Conhecimento> listaConhecimento, int grauDeConfianca) throws DesenvolvedorInexistenteException {
		/*
		ArrayList<Desenvolvedor> listaDesenvolvedores = Inferencia.getDesenvolvedores(listaConhecimento, grauDeConfianca);

		return listaDesenvolvedores;
		*/
			return new ArrayList<Desenvolvedor>();

	}


	public boolean desassociaConhecimentoAtividade(PacketStruct packet) throws ConhecimentoInexistenteException, AtividadeInexistenteException {
		ConhecimentoAtividade conhecimentoAtividade = (ConhecimentoAtividade)packet.getData();
		ArrayList<Conhecimento> listaConhecimento = conhecimentoAtividade.getConhecimentos();
		TipoAtividade atividade = conhecimentoAtividade.getAtividade();

		return desassociaConhecimentoAtividade(listaConhecimento, atividade);
	}

	public boolean desassociaConhecimentoAtividade(
			ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) throws ConhecimentoInexistenteException, AtividadeInexistenteException {

		Iterator<Conhecimento> it = listaConhecimento.iterator();
		while(it.hasNext()) {
			Conhecimento conhecimento = (Conhecimento) it.next();
			validacaoAtividade.removerConhecimentoDaAtividade(atividade.getId(), conhecimento.getNome());
		}
		return true;
	}

	public boolean desassociaProblemaAtividade(PacketStruct packet) throws ProblemaInexistenteException {
		ProblemaAtividade problemaAtividade = (ProblemaAtividade) packet.getData();
		Problema problema = problemaAtividade.getProblema();
		return desassociaProblemaAtividade(problema);
	}

	public boolean desassociaProblemaAtividade(Problema problema) throws ProblemaInexistenteException {
		return validacaoProblema.removerProblema(problema.getId());
	}

	public boolean encerrarAtividade(PacketStruct packet) throws AtividadeInexistenteException {
		TipoAtividade atividade = (TipoAtividade) packet.getData();
		return encerrarAtividade(atividade);
	}
	public boolean encerrarAtividade(TipoAtividade atividade) throws AtividadeInexistenteException {
		return validacaoAtividade.atualizarStatusDaAtividade(atividade.getId(), true);
	}

	public boolean enviarMensagem(PacketStruct packet) throws DesenvolvedorInexistenteException {
		Mensagem msg = (Mensagem) packet.getData();
		return enviarMensagem(msg.getDesenvolvedorOrigem(), msg.getDesenvolvedoresDestino(), msg.getProblema(), msg.getTexto());

	}

	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem,
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema,
			String texto) throws DesenvolvedorInexistenteException {

		return validacaoMensagem.adicionarMensagem(desenvolvedorOrigem, desenvolvedoresDestino, problema, texto);
	}

	public Mensagem[] requisitaMensagens(Desenvolvedor desenvolvedorDestino) {
		validacaoMensagem.getMensagens(desenvolvedorDestino);
		return null;
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
		return qualificaDesenvolvedor(desenvolvedor, problema, foiUtil);
	}
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor,
			Problema problema, boolean qualificacao) throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException {
		ArrayList<String> conhecimentos = problema.getConhecimentos();
		return Ontologia.incrementaRespostasDesenvolvedor(desenvolvedor, qualificacao, conhecimentos);
	}

	public boolean removerAtividade(PacketStruct packet) throws AtividadeInexistenteException, ProblemaInexistenteException {
		TipoAtividade atividade = (TipoAtividade) packet.getData();
		return removerAtividade(atividade);
	}
	public boolean removerAtividade(TipoAtividade atividade) throws AtividadeInexistenteException, ProblemaInexistenteException {
		if(atividade == null) {
			System.out.println("atividade null");
		}
		System.out.println("ID ativiadade " + atividade.getId());
		validacaoAtividade.removerAtividade(atividade.getId());
		return true;
	}

	public ArrayList<Conhecimento> getListaConhecimentos() {
		ArrayList<Conhecimento> retorno = null;
		retorno = validacaoConhecimento.getListaConhecimento();
		return retorno;
	}

	public ArrayList<TipoAtividade> getListaAtividades() {
		return validacaoAtividade.listarAtividades();
	}

	public boolean adicionaDesenvolvedor(PacketStruct packet) throws Exception {
		Desenvolvedor desenvolvedor = (Desenvolvedor)packet.getData();
		return adicionaDesenvolvedor(desenvolvedor);
	}
	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) throws Exception {
		validacaoDesenvolvedor.criarDesenvolvedor(desenvolvedor.getEmail(), 
				desenvolvedor.getNome(), 
				desenvolvedor.getLocalidade(), 
				desenvolvedor.getSenha(),
				desenvolvedor.getListaConhecimento());
		return true;
	}

	public ArrayList<TipoAtividade> buscaAtividades() {
		return validacaoAtividade.listarAtividades();
	}


	public Tree getOntologia() throws ConhecimentoInexistenteException {
		//Ontologia ontologia = new Ontologia(null, null);

		return Ontologia.getArvoreDeConhecimentos();
	}

	public ArrayList<Problema> getListaProblemas() {
		ArrayList<Problema> listaProblemas = validacaoProblema.getListaProblema();
		return listaProblemas;
	}


	public ArrayList<Conhecimento> getListaConhecimentosEnvolvidos(
			PacketStruct packet) throws ConhecimentoInexistenteException {
		TipoAtividade atividade = (TipoAtividade) packet.getData();

		return getListaConhecimentosEnvolvidos(atividade);
	}	

	public ArrayList<Conhecimento> getListaConhecimentosEnvolvidos(
			TipoAtividade atividade) throws ConhecimentoInexistenteException {

		ArrayList<Conhecimento> listaConhecimento = new ArrayList<Conhecimento>();
		for(Conhecimento c: atividade.getListaDeConhecimentosEnvolvidos()) {
			Conhecimento conhecimentoAssociado = validacaoConhecimento.getConhecimento(c.getNome());
			listaConhecimento.add(c);
		}
		return listaConhecimento;
	}

}
