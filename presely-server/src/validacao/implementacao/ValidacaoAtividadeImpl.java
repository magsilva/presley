package validacao.implementacao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import validacao.interfaces.ValidacaoAtividade;
import beans.Atividade;
import beans.Conhecimento;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de atividades que um
 * desenvolvedor deve realizar.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoAtividadeImpl implements ValidacaoAtividade{
	
	ServicoAtividade servicoAtividade;
	ServicoConhecimento servicoConhecimento;
	
	public ValidacaoAtividadeImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
	}

	public boolean adicionarConhecimentoAAtividade(int idAtividade,
			String nomeConhecimento) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idAtividade))throw new Exception();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new Exception();
		
		return servicoAtividade.adicionarConhecimentoAAtividade(idAtividade, nomeConhecimento);
	}

	public boolean associarAtividades(int idSubAtividade, int idAtividadePai) throws Exception {
		
		//Verificando se a atividade pai no  filha da atividade pai.
		ArrayList<Atividade> subAtividades = servicoAtividade.getSubAtividades(idSubAtividade);
		Iterator<Atividade> it = subAtividades.iterator();
		
		while (it.hasNext()) {
			Atividade atividade = it.next();
			if (atividade.getId() == idAtividadePai) {
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

	public boolean cadastrarAtividade(String emailDesenvolvedor,
			String emailGerente, String descricao, Date dataInicio, Date dataFim)
			throws Exception {

		if (!ValidacaoUtil.validaEmail(emailDesenvolvedor)) throw new Exception();
		if (!ValidacaoUtil.validaEmail(emailGerente)) throw new Exception();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new Exception();
		if (!ValidacaoUtil.verificaOrdemDatas(dataInicio, dataFim)) throw new Exception();
		
		return servicoAtividade.cadastrarAtividade(emailDesenvolvedor, emailGerente, descricao, dataInicio, dataFim);
	}

	public Atividade getAtividade(int id) throws Exception {
		
		Atividade atividade = servicoAtividade.getAtividade(id);
		if (atividade == null) throw new Exception();
		
		return atividade;
	}

	public ArrayList<Conhecimento> getConhecimentosEnvolvidosNaAtividade(
			int idAtividade) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new Exception();
		
		return servicoAtividade.getConhecimentosEnvolvidosNaAtividade(idAtividade);
	}

	public ArrayList<Atividade> getSubAtividades(int idPai) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idPai)) throw new Exception();
		
		return servicoAtividade.getSubAtividades(idPai);
	}

	public boolean removerAtividade(int id) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(id)) throw new Exception();
		
		ArrayList<Conhecimento> conhecimentos = servicoAtividade.getConhecimentosEnvolvidosNaAtividade(id);
		Iterator<Conhecimento> it = conhecimentos.iterator();
		
		while (it.hasNext()) {
			Conhecimento conhecimento = it.next();
			servicoAtividade.removerConhecimentoDaAtividade(id, conhecimento.getDescricao());
		}
		
		ArrayList<Atividade> filhos = servicoAtividade.getSubAtividades(id);
		Iterator<Atividade> it2 = filhos.iterator();
		
		while (it2.hasNext()) {
			Atividade subAtividade = it2.next();
			servicoAtividade.desassociarAtividades(subAtividade.getId(), id);
		}
		
		return servicoAtividade.removerAtividade(id);
	}

	public boolean removerConhecimentoDaAtividade(int idAtividade,
			String nomeConhecimento) {
		
		return servicoAtividade.removerConhecimentoDaAtividade(idAtividade, nomeConhecimento);
	}

}
