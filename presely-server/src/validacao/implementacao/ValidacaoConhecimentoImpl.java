package validacao.implementacao;

import beans.Conhecimento;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import validacao.excessao.ConhecimentoNaoEncontradoException;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de conhecimentos.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoConhecimentoImpl{
	
	ServicoConhecimento servicoConhecimento;
	ServicoAtividade servicoAtividade;
	
	public ValidacaoConhecimentoImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
	}

	/**
	 * Este metodo atualiza um conhecimento previamente cadastrado na base da dados 
	 * @param nome Nome do conhecimento a ser atualizado.
	 * @param novoNome Novo nome do conhecimento.
	 * @param descricao Nova descricao do conhecimento.
	 * @return true se o conhecimento foi atualizado.
	 */
	public boolean atualizarConhecimento(String nome, String novoNome,
			String descricao) throws Exception {
		
		if (!servicoConhecimento.conhecimentoExiste(nome)) throw new Exception();
		
		return servicoConhecimento.atualizarConhecimento(nome, novoNome, descricao);
	}

	/**
	 * Este metodo verifica se um conhecimento existe na base de dados.
	 * @param nome Nome do conhecimento para verificacao.
	 * @return true se o conhecimento existe.
	 */
	public boolean conhecimentoExiste(String nome) {
		
		return servicoConhecimento.conhecimentoExiste(nome);
	}

	/**
	 * Este metodo cria um novo conhecimento na base de dados
	 * @param nome Nome do novo conhecimento
	 * @param descricao Descricao do novo conhecimento
	 * @return true se o conhecimento foi inserido na base de dados.
	 */
	public boolean criarConhecimento(String nome, String descricao) throws Exception {
		
		if (!ValidacaoUtil.validaNome(nome)) throw new Exception();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new Exception();
		
		if (servicoConhecimento.conhecimentoExiste(nome)) throw new Exception();
		
		return servicoConhecimento.criarConhecimento(nome, descricao);
	}

	public Conhecimento getConhecimento(String nome) throws Exception {
		
		Conhecimento conhecimento = servicoConhecimento.getConhecimento(nome);
		if (conhecimento == null) throw new ConhecimentoNaoEncontradoException();
		
		return conhecimento;
	}

	/**
	 * Este metodo remove um conhecimento previamente cadastrado. 
	 * @param nome Nome do conhecimento a ser removido.
	 * @return true se o conhecimento foi removido com sucesso
	 */
	public boolean removerConhecimento(String nome) {
		
		return servicoConhecimento.removerConhecimento(nome);
	}

}
