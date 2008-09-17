package core;

/**
 * Esta classe implementa as operações q.... **** ........
 * @author EduardoPS
 * @since 2008
 */


import inferencia.Inferencia;

import java.util.ArrayList;
import java.util.Iterator;

import ontologia.Ontologia;

import sun.security.krb5.internal.crypto.Des;
import validacao.excessao.AtividadeInexistenteException;
import validacao.excessao.ConhecimentoInexistenteException;
import validacao.excessao.DataInvalidaException;
import validacao.excessao.DescricaoInvalidaException;
import validacao.excessao.EmailInvalidoException;
import validacao.implementacao.ValidacaoAtividadeImpl;
import validacao.implementacao.ValidacaoConhecimentoImpl;
import validacao.implementacao.ValidacaoDesenvolvedorImpl;
import validacao.implementacao.ValidacaoMensagemImpl;
import validacao.implementacao.ValidacaoProblemaImpl;
import validacao.implementacao.ValidacaoUtil;

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

	ValidacaoConhecimentoImpl  validacaoConhecimento;
	ValidacaoAtividadeImpl 	   validacaoAtividade;
	ValidacaoProblemaImpl 	   validacaoProblema; 
	ValidacaoDesenvolvedorImpl validacaoDesenvolvedor; 
	ValidacaoMensagemImpl 	   validacaoMensagem;
	ValidacaoConhecimentoImpl  valConhecimento; 
		
	public ExecuteClientQuery() {
		validacaoConhecimento  = new ValidacaoConhecimentoImpl();
		validacaoAtividade 	   = new ValidacaoAtividadeImpl();
		validacaoProblema 		   = new ValidacaoProblemaImpl();
		validacaoDesenvolvedor = new ValidacaoDesenvolvedorImpl();
		validacaoMensagem 	   = new ValidacaoMensagemImpl();
		valConhecimento 	   = new ValidacaoConhecimentoImpl();
	}
	
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
		
		for(Conhecimento c : listaConhecimento) {
			try {
				validacaoAtividade.adicionarConhecimentoAAtividade(atividade.getId(), c.getNome());
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

		try {
			return validacaoProblema.cadastrarProblema(atividade.getId(), problema.getDescricao(), problema.getData(), problema.getMensagem());
		} catch (DescricaoInvalidaException e) {
			e.printStackTrace();
			return false;
		} catch (AtividadeInexistenteException e) {
			e.printStackTrace();
			return false;
		}
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
		String[] conhecimentos = new String[problema.getConhecimentos().size()];
		for(int i = 0; i < listaConhecimento.size(); i++)
			conhecimentos[i] = listaConhecimento.get(i).getNome();
		
		ArrayList<Desenvolvedor> listaDesenvolvedores = Inferencia.getDesenvolvedores(conhecimentos, grauDeConfianca);
		
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
		
		boolean retorno = true;
		
		Iterator it = listaConhecimento.iterator();
		while(it.hasNext()) {
			Conhecimento conhecimento = (Conhecimento) it.next();
			try {
				return retorno = validacaoAtividade.removerConhecimentoDaAtividade(atividade.getId(), conhecimento.getNome());
			} catch (ConhecimentoInexistenteException e) {
				e.printStackTrace();
				return false;
			} catch (AtividadeInexistenteException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public boolean desassociaProblemaAtividade(PacketStruct packet) {
		ProblemaAtividade problemaAtividade = (ProblemaAtividade) packet.getData();
		Problema problema = problemaAtividade.getProblema();
		return desassociaProblemaAtividade(problema);
	}
	
	public boolean desassociaProblemaAtividade(Problema problema) {
		return validacaoProblema.removerProblema(problema.getId());
	}

	public boolean encerrarAtividade(PacketStruct packet) {
		TipoAtividade atividade = (TipoAtividade) packet.getData();
		return encerrarAtividade(atividade);
	}
	public boolean encerrarAtividade(TipoAtividade atividade) {
		return validacaoAtividade.atualizarStatusDaAtividade(atividade.getId(), true);
	}

	public boolean enviarMensagem(PacketStruct packet) {
		Mensagem msg = (Mensagem) packet.getData();
		return enviarMensagem(msg.getDesenvolvedorOrigem(), msg.getDesenvolvedoresDestino(), msg.getProblema(), msg.getTexto());
		
	}
	
	public boolean enviarMensagem(Desenvolvedor desenvolvedorOrigem,
			ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema,
			String texto) {
		
		return validacaoMensagem.adicionarMensagem(desenvolvedorOrigem, desenvolvedoresDestino, problema, texto);
	}
	
	public Mensagem[] requisitaMensagens(Desenvolvedor desenvolvedorDestino) {
		validacaoMensagem.getMensagens(desenvolvedorDestino);
		return null;
	}

	public ArrayList<Desenvolvedor> getListaDesenvolvedores() {
		
		return validacaoDesenvolvedor.getListaDesenvolvedores();
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
		QualificacaoDesenvolvedor qualDes = (QualificacaoDesenvolvedor) packet.getData();
		Problema problema = qualDes.getProblema();
		Desenvolvedor desenvolvedor = qualDes.getDesenvolvedor();
		boolean foiUtil = qualDes.isFoiUtil();
		return qualificaDesenvolvedor(desenvolvedor, problema, foiUtil);
	}
	public boolean qualificaDesenvolvedor(Desenvolvedor desenvolvedor,
			Problema problema, boolean qualificacao) {
		ArrayList<String> conhecimentos = problema.getConhecimentos();
		return Ontologia.incrementaRespostasDesenvolvedor(desenvolvedor, qualificacao, conhecimentos);
	}

	public boolean removerAtividade(PacketStruct packet) {
		TipoAtividade atividade = (TipoAtividade) packet.getData();
		return removerAtividade(atividade);
	}
	public boolean removerAtividade(TipoAtividade atividade) {

		boolean retorno = false;
		
		try {
			validacaoAtividade.removerAtividade(atividade.getId());
			retorno = true;
		} catch (AtividadeInexistenteException e) {
		}
		return retorno;
	}

	public ArrayList<Conhecimento> getListaConhecimentos() {
		ArrayList<Conhecimento> retorno = null;
		try {
			retorno = validacaoConhecimento.getListaConhecimento();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}
	
	public ArrayList<TipoAtividade> getListaAtividades() {
		return validacaoAtividade.listarAtividades();
	}

	public boolean adicionaDesenvolvedor(PacketStruct packet) {
		Desenvolvedor desenvolvedor = (Desenvolvedor)packet.getData();
		return adicionaDesenvolvedor(desenvolvedor);
	}
	public boolean adicionaDesenvolvedor(Desenvolvedor desenvolvedor) {
		
		boolean retorno = false;
		
		try {
			validacaoDesenvolvedor.criarDesenvolvedor(desenvolvedor.getEmail(), desenvolvedor.getNome(), desenvolvedor.getLocalidade());
			retorno = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

	public ArrayList<TipoAtividade> buscaAtividades() {
		return validacaoAtividade.listarAtividades();
	}
	

	public Tree getOntologia() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
