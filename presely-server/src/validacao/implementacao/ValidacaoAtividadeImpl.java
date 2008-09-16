package validacao.implementacao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import validacao.excessao.AtividadeInexistenteException;
import validacao.excessao.ConhecimentoInexistenteException;
import validacao.excessao.DataInvalidaException;
import validacao.excessao.DescricaoInvalidaException;
import validacao.excessao.EmailInvalidoException;
import beans.TipoAtividade;
import beans.Conhecimento;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de atividades que um
 * desenvolvedor deve realizar.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoAtividadeImpl{
	
	ServicoAtividade servicoAtividade;
	ServicoConhecimento servicoConhecimento;
	
	public ValidacaoAtividadeImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
	}

	public boolean adicionarConhecimentoAAtividade(int idAtividade,
			String nomeConhecimento) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idAtividade))throw new AtividadeInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoAtividade.adicionarConhecimentoAAtividade(idAtividade, nomeConhecimento);
	}

	public boolean associarAtividades(int idSubAtividade, int idAtividadePai) throws Exception {
		
		//Verificando se a atividade pai no  filha da atividade pai.
		ArrayList<TipoAtividade> subAtividades = servicoAtividade.getSubAtividades(idSubAtividade);
		Iterator<TipoAtividade> it = subAtividades.iterator();
		
		while (it.hasNext()) {
			TipoAtividade tipoAtividade = it.next();
			if (tipoAtividade.getId() == idAtividadePai) {
				throw new Exception();
			}
		}
		
		return servicoAtividade.associarAtividades(idSubAtividade, idAtividadePai);
	}

	public boolean atividadeAssociadaAConhecimentoExiste(int idAtividade,
			String nomeConhecimento) {
		
		return servicoAtividade.atividadeAssociadaAConhecimentoExiste(idAtividade, nomeConhecimento);
	}

	public boolean atividadeExiste(int id) {
		
		return servicoAtividade.atividadeExiste(id);
	}

	public boolean atualizarStatusDaAtividade(int id, boolean terminada) {
		
		return servicoAtividade.atualizarStatusDaAtividade(id, terminada);
	}
	
	public boolean cadastrarAtividade(TipoAtividade tipoAtividade) throws Exception {
		System.out.println("TipoAtividade adicionada com sucesso!");
		
		String emailDesenvolvedor = tipoAtividade.getDesenvolvedor().getEmail();
		String emailGerente = tipoAtividade.getSupervisor().getEmail();
		String descricao = tipoAtividade.getDescricao();
		Date dataInicio = tipoAtividade.getDataInicio();
		Date dataFim = tipoAtividade.getDataFinal();
		
		if (!ValidacaoUtil.validaEmail(emailDesenvolvedor)) throw new Exception();
		if (!ValidacaoUtil.validaEmail(emailGerente)) throw new Exception();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new Exception();
		if (!ValidacaoUtil.verificaOrdemDatas(dataInicio, dataFim)) throw new Exception();
		
		return servicoAtividade.cadastrarAtividade(emailDesenvolvedor, emailGerente, descricao, dataInicio, dataFim);
		
	}

	public boolean cadastrarAtividade(String emailDesenvolvedor,
			String emailGerente, String descricao, Date dataInicio, Date dataFim)
			throws Exception {
		
		System.out.println("TipoAtividade adicionada com sucesso!");

		if (!ValidacaoUtil.validaEmail(emailDesenvolvedor)) throw new EmailInvalidoException();
		if (!ValidacaoUtil.validaEmail(emailGerente)) throw new EmailInvalidoException();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		if (!ValidacaoUtil.verificaOrdemDatas(dataInicio, dataFim)) throw new DataInvalidaException();
		
		return servicoAtividade.cadastrarAtividade(emailDesenvolvedor, emailGerente, descricao, dataInicio, dataFim);
	}

	public TipoAtividade getAtividade(int id) throws Exception {
		
		TipoAtividade tipoAtividade = servicoAtividade.getAtividade(id);
		if (tipoAtividade == null) throw new Exception();
		
		return tipoAtividade;
	}

	public ArrayList<Conhecimento> getConhecimentosEnvolvidosNaAtividade(
			int idAtividade) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new Exception();
		
		return servicoAtividade.getConhecimentosEnvolvidosNaAtividade(idAtividade);
	}

	public ArrayList<TipoAtividade> getSubAtividades(int idPai) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idPai)) throw new Exception();
		
		return servicoAtividade.getSubAtividades(idPai);
	}

	public boolean removerAtividade(int id) throws AtividadeInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(id)) throw new AtividadeInexistenteException();
		
		ArrayList<Conhecimento> conhecimentos = servicoAtividade.getConhecimentosEnvolvidosNaAtividade(id);
		Iterator<Conhecimento> it = conhecimentos.iterator();
		
		while (it.hasNext()) {
			Conhecimento conhecimento = it.next();
			servicoAtividade.removerConhecimentoDaAtividade(id, conhecimento.getDescricao());
		}
		
		ArrayList<TipoAtividade> filhos = servicoAtividade.getSubAtividades(id);
		Iterator<TipoAtividade> it2 = filhos.iterator();
		
		while (it2.hasNext()) {
			TipoAtividade subAtividade = it2.next();
			servicoAtividade.desassociarAtividades(subAtividade.getId(), id);
		}
		
		return servicoAtividade.removerAtividade(id);
	}

	public boolean removerConhecimentoDaAtividade(int idAtividade,
			String nomeConhecimento) {
		
		return servicoAtividade.removerConhecimentoDaAtividade(idAtividade, nomeConhecimento);
	}

	public ArrayList<TipoAtividade> getTodasAtividades() {
		return servicoAtividade.getTodasAtividades();		
		 
	}

}
