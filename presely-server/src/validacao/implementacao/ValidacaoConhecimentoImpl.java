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

	public boolean atualizarConhecimento(String nome, String novoNome,
			String descricao) throws Exception {
		
		if (!servicoConhecimento.conhecimentoExiste(nome)) throw new Exception();
		
		return servicoConhecimento.atualizarConhecimento(nome, novoNome, descricao);
	}

	public boolean conhecimentoExiste(String nome) {
		
		return servicoConhecimento.conhecimentoExiste(nome);
	}

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

	public boolean removerConhecimento(String nome) {
		
		return servicoConhecimento.removerConhecimento(nome);
	}

}
