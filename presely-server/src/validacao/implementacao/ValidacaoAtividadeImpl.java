package validacao.implementacao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;

import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.implementacao.ServicoProblemaImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoProblema;
import excessao.AtividadeInexistenteException;
import excessao.ConhecimentoInexistenteException;
import excessao.DataInvalidaException;
import excessao.DescricaoInvalidaException;
import excessao.EmailInvalidoException;
import beans.Problema;
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

public class ValidacaoAtividadeImpl {
	
	ServicoAtividade servicoAtividade;
	ServicoConhecimento servicoConhecimento;
	ServicoProblema servicoProblema;
	ValidacaoProblemaImpl validacaoProblema;
	
	public ValidacaoAtividadeImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
		validacaoProblema = new ValidacaoProblemaImpl();
		servicoProblema = new ServicoProblemaImplDAO();
	}
	
	/**
	 * Esse método associa um conhecimento a uma atividade previamente cadastrada.
	 * @param idAtividade Identificador da atividade.
	 * @param nomeConhecimento Nome do conhecimento a ser associado.
	 * @return true se a associacao foi feita.
	 */
	public boolean adicionarConhecimentoAAtividade(int idAtividade,
			String nomeConhecimento) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idAtividade))throw new AtividadeInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoAtividade.adicionarConhecimentoAAtividade(idAtividade, nomeConhecimento);
	}
	
	/**
	 * Esse método associa uma sub-atividade a uma atividade maior. 
	 * @param idSubAtividade Identificador da sub-atividade 
	 * @param idAtividadePai Identificador da atividade pai.
	 * @return true se a associacao foi feita com sucesso.
	 */
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
	
	/**
	 * Esse método verifica se existe uma associacao entre conhecimento e atividade.
	 * @param idAtividade Identificador da atividade.
	 * @param nomeConhecimento Nome do conhecimento associado a atividade.
	 * @return true se existe associacao entre conhecimento e atividade.
	 */
	public boolean atividadeAssociadaAConhecimentoExiste(int idAtividade,
			String nomeConhecimento) throws AtividadeInexistenteException, ConhecimentoInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new AtividadeInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoAtividade.atividadeAssociadaAConhecimentoExiste(idAtividade, nomeConhecimento);
	}
	
	/**
	 * Esse método verifica se uma atividade existe na base de dados.
	 * @param id Identificador da atividade.
	 * @return true se a atividade existir na base de dados.
	 */
	public boolean atividadeExiste(int id) {
		
		return servicoAtividade.atividadeExiste(id);
	}
	
	/**
	 * Esse método atualiza o status se a atividade foi concluida ou
	 * não.
	 * @param id Identificador da atividade.
	 * @param terminada true se a atividade foi concluida.
	 * @return true se a operacao foi realizada com sucesso.
	 */
	public boolean atualizarStatusDaAtividade(int id, boolean terminada) {
		
		return servicoAtividade.atualizarStatusDaAtividade(id, terminada);
	}
	
	/**
	 * Este método cadastra uma nova atividade na base de dados.
	 * @param tipoAtividade Objeto atividade.
	 * @return true se a atividade foi concluida com sucesso.
	 * @throws EmailInvalidoException 
	 * @throws DescricaoInvalidaException 
	 * @throws DataInvalidaException 
	 */
	public boolean cadastrarAtividade(TipoAtividade tipoAtividade) throws EmailInvalidoException, 
			DescricaoInvalidaException, DataInvalidaException{
		
		String emailDesenvolvedor = tipoAtividade.getDesenvolvedor().getEmail();
		String emailGerente = tipoAtividade.getSupervisor().getEmail();
		String descricao = tipoAtividade.getDescricao();
		Date dataInicio = tipoAtividade.getDataInicio();
		Date dataFim = tipoAtividade.getDataFinal();
		
		if (!ValidacaoUtil.validaEmail(emailDesenvolvedor)) throw new EmailInvalidoException();
		if (!ValidacaoUtil.validaEmail(emailGerente)) throw new EmailInvalidoException();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		if (!ValidacaoUtil.verificaOrdemDatas(dataInicio, dataFim)) throw new DataInvalidaException();
		
		System.out.println("Atividade cadastrada com sucesso!");
		
		return servicoAtividade.cadastrarAtividade(emailDesenvolvedor, emailGerente, descricao, dataInicio, dataFim);
		
	}
	
	/**
	 * Este método cadastra uma nova atividade na base de dados.
	 * @param emailDesenvolvedor Email do desenvolvedor responsável pela execucao da atividade.
	 * @param emailGerente Email do gerente que determinou a atividade para um dado desenvolvedor.
	 * @param descricao Descricao da atividade a ser realizada
	 * @param dataInicio Data em que a atividade foi passada ao desenvolvor
	 * @param dataFim Data que a atividade foi terminada
	 * @return true se a atividade foi concluida com sucesso.
	 * @throws EmailInvalidoException 
	 * @throws DescricaoInvalidaException 
	 * @throws DataInvalidaException 
	 */
	public boolean cadastrarAtividade(String emailDesenvolvedor,
			String emailGerente, String descricao, Date dataInicio, Date dataFim) 
			throws EmailInvalidoException, DescricaoInvalidaException, DataInvalidaException {
		
		System.out.println("TipoAtividade adicionada com sucesso!");

		if (!ValidacaoUtil.validaEmail(emailDesenvolvedor)) throw new EmailInvalidoException();
		if (!ValidacaoUtil.validaEmail(emailGerente)) throw new EmailInvalidoException();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		if (!ValidacaoUtil.verificaOrdemDatas(dataInicio, dataFim)) throw new DataInvalidaException();
		
		return servicoAtividade.cadastrarAtividade(emailDesenvolvedor, emailGerente, descricao, dataInicio, dataFim);
	}
	
	/**
	 * Esse método retorna um objeto TipoAtividade com todos os dados de uma atividade
	 * previamente cadastrada.
	 * @param id Identificador da atividade
	 * @return <TipoAtividade>
	 */
	public TipoAtividade getAtividade(int id) throws Exception {
		
		TipoAtividade tipoAtividade = servicoAtividade.getAtividade(id);
		if (tipoAtividade == null) throw new Exception();
		
		return tipoAtividade;
	}
	
	/**
	 * Esse método retorna uma lista de conhecimentos que envolvem uma dada
	 * atividade.
	 * @param idAtividade Identificador da atividade
	 * @return ArrayList<Conhecimento>
	 */
	public ArrayList<Conhecimento> getConhecimentosEnvolvidosNaAtividade(
			int idAtividade) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new Exception();
		
		return servicoAtividade.getConhecimentosEnvolvidosNaAtividade(idAtividade);
	}
	
	/**
	 * Esse método retorna uma lista de sub-atividades associadas a uma atividade
	 * pai.
	 * @param idPai Identificador da atividade pai
	 * @return ArrayList<TipoAtividade>
	 */
	public ArrayList<TipoAtividade> getSubAtividades(int idPai) throws Exception {
		
		if (!servicoAtividade.atividadeExiste(idPai)) throw new Exception();
		
		return servicoAtividade.getSubAtividades(idPai);
	}
	
	/**
	 * Este metodo remove uma atividade previamente cadastrada na base de dados.
	 * @param id Identificador da atividade
	 * @return true se a atividade foi removida da base de dados.
	 */
	public boolean removerAtividade(int id) throws AtividadeInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(id)) throw new AtividadeInexistenteException();
		
		// Desassociando os conhecimentos da atividade.
		ArrayList<Conhecimento> conhecimentos = servicoAtividade.getConhecimentosEnvolvidosNaAtividade(id);
		Iterator<Conhecimento> it = conhecimentos.iterator();
		
		while (it.hasNext()) {
			Conhecimento conhecimento = it.next();
			servicoAtividade.removerConhecimentoDaAtividade(id, conhecimento.getDescricao());
		}
		
		// Desassociando as atividades com associacao
		ArrayList<TipoAtividade> filhos = servicoAtividade.getSubAtividades(id);
		Iterator<TipoAtividade> it2 = filhos.iterator();
		
		while (it2.hasNext()) {
			TipoAtividade subAtividade = it2.next();
			servicoAtividade.desassociarAtividades(subAtividade.getId(), id);
		}
		
		// Removendo problemas associados a atividade
		ArrayList<Problema> problemas = servicoProblema.listarProblemasDaAtividade(id);
		Iterator<Problema> it3 = problemas.iterator();
		
		while (it3.hasNext()) {
			Problema problema = it3.next();
			validacaoProblema.removerProblema(problema.getId());
		}
		
		return servicoAtividade.removerAtividade(id);
	}
	
	/**
	 * Esse metodo remove uma associacao entre um conhecimento e uma atividade.
	 * @param idAtividade Identificador da ativiadade.
	 * @param nomeConhecimento Nome do conhecimento
	 * @return true se a associacao foi desfeita.
	 */
	public boolean removerConhecimentoDaAtividade(int idAtividade,
			String nomeConhecimento) throws ConhecimentoInexistenteException, AtividadeInexistenteException {
		
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new AtividadeInexistenteException();
		
		return servicoAtividade.removerConhecimentoDaAtividade(idAtividade, nomeConhecimento);
	}
	
	/**
	 * Esse método desassocia duas atividades. 
	 * @param idSubAtividade Identificador da sub-atividade 
	 * @param idAtividadePai Identificador da atividade pai.
	 * @return true se a desassociacao foi feita com sucesso.
	 */
	public boolean desassociarAtividades(int idSubAtividade, int idAtividadePai)
			throws AtividadeInexistenteException {

		if (!servicoAtividade.atividadeExiste(idAtividadePai)) throw new AtividadeInexistenteException();
		if (!servicoAtividade.atividadeExiste(idSubAtividade)) throw new AtividadeInexistenteException();
		
		return servicoAtividade.desassociarAtividades(idSubAtividade, idAtividadePai);
	}
	
	/**
	 * Método que retorna todos os problemas associados a uma atividade.
	 * @return ArrayList<Problema> lista de todos os problemas da atividade.
	 */
	public ArrayList<Problema> getProblemas(int idAtividade)
			throws AtividadeInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new AtividadeInexistenteException();
		
		return servicoProblema.listarProblemasDaAtividade(idAtividade);
	}
	
	/**
	 * Metodo que retorna todas as atividades cadastradas no banco.
	 * @return ArrayList<TipoAtividade> lista com todas as atividades do banco.
	 */
	public ArrayList<TipoAtividade> listarAtividades() {
		
		return servicoAtividade.listarAtividades();
	}
}
