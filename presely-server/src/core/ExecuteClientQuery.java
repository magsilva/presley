package core;

/**
 * Esta classe implementa as operações q.... **** ........
 * @author EduardoPS
 * @since 2008
 */


import java.util.ArrayList;

import beans.BuscaDesenvolvedores;
import beans.Conhecimento;
import beans.ConhecimentoAtividade;
import beans.DadosAutenticacao;
import beans.Desenvolvedor;
import beans.Problema;
import beans.ProblemaAtividade;
import beans.TipoAtividade;
import beans.Tree;
import core.interfaces.CorePresleyOperations;
import facade.PacketStruct;

public class ExecuteClientQuery implements CorePresleyOperations{

	/**
	 * 
	 * @param packet
	 * @return
	 */
	public boolean adicionaConhecimento(PacketStruct packet) {
		Conhecimento conhecimento = (Conhecimento) packet.getData();

		return this.adicionaConhecimento(conhecimento); 
	}
	public boolean adicionaConhecimento(Conhecimento conhecimento) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param packet
	 * @return
	 */
	public boolean adicionaAtividade(PacketStruct packet) {
		TipoAtividade atividade = (TipoAtividade) packet.getData();

		return this.adicionaAtividade(atividade); 
	}
	public boolean adicionaAtividade(TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param packet
	 * @return
	 */
	public boolean associaConhecimentoAtividade(PacketStruct packet) {
		ConhecimentoAtividade conhecimentoAtividade = new ConhecimentoAtividade();
		ArrayList<Conhecimento> listaConhecimento = conhecimentoAtividade.getConhecimentos();
		TipoAtividade atividade = conhecimentoAtividade.getAtividade();
		
		return associaConhecimentoAtividade(listaConhecimento, atividade);
	}
	public boolean associaConhecimentoAtividade(
			ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param packet
	 * @return
	 */
	public boolean associaProblemaAtividade(PacketStruct packet) {
		ProblemaAtividade problemaAtividade = (ProblemaAtividade) packet.getData();
		
		Problema problema = problemaAtividade.getProblema();
		TipoAtividade atividade =  problemaAtividade.getAtividade();
		
		return associaProblemaAtividade(problema, atividade);
	}
	public boolean associaProblemaAtividade(Problema problema,
			TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 * @param packet
	 * @return
	 */
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(PacketStruct packet) {
		BuscaDesenvolvedores busca = (BuscaDesenvolvedores)packet.getData();
		
		Problema problema = busca.getProblema();
		ArrayList<Conhecimento> listaConhecimento = busca.getListaConhecimento();
		int grauDeConfianca = busca.getGrauDeConfianca();

		return buscaDesenvolvedores(problema, listaConhecimento, grauDeConfianca);
	}	
	public ArrayList<Desenvolvedor> buscaDesenvolvedores(Problema problema,
			ArrayList<Conhecimento> listaConhecimento, int grauDeConfianca) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean desassociaConhecimentoAtividade(PacketStruct packet) {
		ConhecimentoAtividade conhecimentoAtividade = (ConhecimentoAtividade)packet.getData();
		ArrayList<Conhecimento> listaConhecimento = conhecimentoAtividade.getConhecimentos();
		TipoAtividade atividade = conhecimentoAtividade.getAtividade();
		
		return desassociaConhecimentoAtividade(listaConhecimento, atividade);
	}
	public boolean desassociaConhecimentoAtividade(
			ArrayList<Conhecimento> listaConhecimento, TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean desassociaProblemaAtividade(PacketStruct packet) {
		ProblemaAtividade problemaAtividade = (ProblemaAtividade) packet.getData();
		Problema problema = problemaAtividade.getProblema();
		TipoAtividade atividade = problemaAtividade.getAtividade();
		return desassociaProblemaAtividade(packet);
	}
	public boolean desassociaProblemaAtividade(Problema problema, TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean encerrarAtividade(PacketStruct packet) {
		TipoAtividade atividade = (TipoAtividade) packet.getData();
		return false;
	}
	public boolean encerrarAtividade(TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean enviarMensagem(PacketStruct packet) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem,
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema,
			String mensagem) {
		// TODO Auto-generated method stub
		return false;
	}

	public ArrayList<Desenvolvedor> getListaDesenvolvedores() {
		// TODO Auto-generated method stub
		return null;
	}

	public Tree getOntologia() {
		// TODO Auto-generated method stub
		return null;
	}

	public Desenvolvedor login(PacketStruct packet) {
		DadosAutenticacao authData = (DadosAutenticacao) packet.getData();
		return login(authData);
	}
	public Desenvolvedor login(DadosAutenticacao authData) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean logout(PacketStruct packet) {
		Desenvolvedor authData = (Desenvolvedor) packet.getData();
		return logout(authData);
	}
	public boolean logout(Desenvolvedor desenvolvedor) {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean qualificaDesenvolvedor(PacketStruct packet) {
		
		return false;
	}
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor,
			Problema problema, boolean qualificacao) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removerAtividade(PacketStruct packet) {
		TipoAtividade atividade = (TipoAtividade) packet.getData();
		return removerAtividade(atividade);
	}
	public boolean removerAtividade(TipoAtividade atividade) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ArrayList<Conhecimento> getListaConhecimentos() {
		// TODO Auto-generated method stub
		return null;
	}




}
