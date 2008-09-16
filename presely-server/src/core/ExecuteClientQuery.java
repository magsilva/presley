package core;

/**
 * Esta classe implementa as opera��es q.... **** ........
 * @author EduardoPS
 * @since 2008
 */


import java.util.ArrayList;

import validacao.ValidacaoAtividade;
import validacao.excessao.AtividadeInexistenteException;
import validacao.excessao.ConhecimentoInexistenteException;
import validacao.excessao.DataInvalidaException;
import validacao.excessao.DescricaoInvalidaException;
import validacao.excessao.EmailInvalidoException;
import validacao.implementacao.ValidacaoAtividadeImpl;
import validacao.implementacao.ValidacaoConhecimentoImpl;
import validacao.implementacao.ValidacaoDesenvolvedorImpl;
import validacao.implementacao.ValidacaoProblemaImpl;
import validacao.implementacao.ValidacaoUtil;
import validacao.interfaces.ValidacaoDesenvolvedor;
import validacao.interfaces.ValidacaoProblema;

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
	 * OK
	 * @param packet
	 * @return
	 */
	public boolean adicionaConhecimento(PacketStruct packet) {
		Conhecimento conhecimento = (Conhecimento) packet.getData();

		return this.adicionaConhecimento(conhecimento); 
	}
	public boolean adicionaConhecimento(Conhecimento conhecimento) {

		ValidacaoConhecimentoImpl validacaoConhecimento = new ValidacaoConhecimentoImpl();

		try {
			validacaoConhecimento.criarConhecimento( conhecimento.getNome(), conhecimento.getDescricao() );
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	/**
	 * OK
	 * @param packet
	 * @return
	 */
	public boolean adicionaAtividade(PacketStruct packet) {
		TipoAtividade atividade = (TipoAtividade) packet.getData();

		return this.adicionaAtividade(atividade); 
	}
	public boolean adicionaAtividade(TipoAtividade novaAtividade) {

		ValidacaoAtividadeImpl validacaoAtividade = new ValidacaoAtividadeImpl();


		try {
			validacaoAtividade.cadastrarAtividade(novaAtividade.getDesenvolvedor().getEmail(), 
					novaAtividade.getSupervisor().getEmail(), 
					novaAtividade.getDescricao(), 
					novaAtividade.getDataInicio(), 
					novaAtividade.getDataFinal());
		} catch (EmailInvalidoException e) {
			return false;
		} catch (DescricaoInvalidaException e) {
			return false;
		} catch (DataInvalidaException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	/**
	 * OK
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
		
		ValidacaoAtividadeImpl valAtivConhecimento = new ValidacaoAtividadeImpl();
		for(Conhecimento c : listaConhecimento) {
			try {
				valAtivConhecimento.adicionarConhecimentoAAtividade(atividade.getId(), c.getNome());
			} catch (AtividadeInexistenteException e) {
				e.printStackTrace();
				return false;
			} catch (ConhecimentoInexistenteException e) {
				e.printStackTrace();
				return false;
			}
			catch (Exception e) {
				return false;
			}
		}
		return true;
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

		ValidacaoProblemaImpl valProblema = new ValidacaoProblemaImpl();
		valProblema.cadastrarProblema(atividade.getId(), problema.getDescricao(), problema.getData(), problema.getMensagem());
		return true;
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
		ValidacaoDesenvolvedorImpl validacaoDesenvolvedor = new ValidacaoDesenvolvedorImpl();
		
		ArrayList<Desenvolvedor> listaDesenvolvedores = validacaoDesenvolvedor.getListaDesenvolvedores();
		
		// TODO ontologia e inferencia.
		return listaDesenvolvedores;
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
		ValidacaoDesenvolvedorImpl validacaoDesenvolvedor =  new ValidacaoDesenvolvedorImpl();
		return validacaoDesenvolvedor.getListaDesenvolvedores();
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

		ValidacaoAtividadeImpl validacaoAtividade = new ValidacaoAtividadeImpl();

		try {
			validacaoAtividade.removerAtividade(atividade.getId());
		} catch (AtividadeInexistenteException e) {
			return false;
		}
		return true;
	}

	public ArrayList<Conhecimento> getListaConhecimentos() {
		// TODO Auto-generated method stub
		
		ValidacaoConhecimentoImpl valConhecimento = new ValidacaoConhecimentoImpl();
		try {
			valConhecimento.getConhecimento(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// TODO procurar Rodrigo
	public ArrayList<TipoAtividade> getListaAtividades() {
		return new ValidacaoAtividadeImpl().getSubAtividades();
	}




}
