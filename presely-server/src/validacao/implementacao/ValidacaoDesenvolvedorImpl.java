package validacao.implementacao;


import java.util.ArrayList;

import beans.TipoAtividade;
import beans.Conhecimento;
import beans.Desenvolvedor;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de desenvolvedores.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoDesenvolvedorImpl{
	
	ServicoConhecimento servicoConhecimento;
	ServicoAtividade servicoAtividade;
	ServicoDesenvolvedor servicoDesenvolvedor;
	
	public ValidacaoDesenvolvedorImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}
	
	/**
	 * Esse mtodo adiciona um conhecimento previamente cadastrado a um desenvolvedor.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser adicionado.
	 * @return true se o conhecimento foi adicionado ao desenvolvedor.
	 */
	public boolean adicionarConhecimentoAoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new Exception();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new Exception();
		
		return servicoDesenvolvedor.adicionarConhecimentoAoDesenvolvedor(
				emailDesenvolvedor, nomeConhecimento);
	}

	/**
	 * Esse mtodo atualiza os dados de um desenvolvedor previamente cadastrado na
	 * base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param novoEmail novo email do desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param localidade Local onde o desenvolvedor reside.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarDesenvolvedor(String email, String novoEmail,
			String nome, String localidade) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.atualizarDesenvolvedor(email, novoEmail, nome, localidade);
	}

	/**
	 * Esse mtodo verifica se existe relacao entre um conhecimento e um desenvolvedor/
	 * @param emailDesenvolvedor Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a associacao existe.
	 */
	public boolean conhecimentoDoDesenvolvedorExiste(String emailDesenvolvedor,
			String nomeConhecimento) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new Exception();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new Exception();
		
		return servicoDesenvolvedor.conhecimentoDoDesenvolvedorExiste(emailDesenvolvedor, nomeConhecimento);
	}

	/**
	 * Esse mtodo adiciona um novo desenvolvedor na base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param localidade Local onde o desenvolvedor reside.
	 * @return true se o desenvolvedor foi criado com sucesso.
	 */
	public boolean criarDesenvolvedor(String email, String nome,
			String localidade) throws Exception {
		
		if (servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.criarDesenvolvedor(email, nome, localidade);
	}

	/**
	 * Esse mtodo verifica se um dado desenvolvedor est cadastrado no sistema.
	 * @param email Email do desenvolvedor
	 * @return true se o desenvolvedro estiver cadastrado no sistema.
	 */
	public boolean desenvolvedorExiste(String email) {
		
		return servicoDesenvolvedor.desenvolvedorExiste(email);
	}

	/**
	 * Esse mtodo retorna uma lista de atividades atribuidas a um desenvolvedor
	 * @param email Email do desenvolvedor
	 * @return ArrayList<TipoAtividade>
	 */
	public ArrayList<TipoAtividade> getAtividadesDoDesenvolvedor(String email) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.getAtividadesDoDesenvolvedor(email);
	}

	/**
	 * Esse mtodo retorna uma lista de conhecimentos que o desenvolvedor possui
	 * @param email Email do desenvolvedor
	 * @return ArrayList<Conhecimento>
	 */
	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email) 
			throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.getConhecimentosDoDesenvolvedor(email);
	}

	/**
	 * Este mtodo retorna o desenvolvedor que possui o email passado no parametro.
	 * @param email Email do desenvolvedor.
	 * @return <Desenvolvedor> 
	 */
	public Desenvolvedor getDesenvolvedor(String email) throws Exception {
		
		Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedor(email);
		if (desenvolvedor == null) throw new Exception();
		
		return desenvolvedor;
	}

	/**
	 * Esse mtodo remove um conhecimento associado a um desenvolvedor especfico.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser removido do desenvolvedor.
	 * @return true se a associacao foi desfeita.
	 */
	public boolean removerConhecimentoDoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) {
		
		return servicoDesenvolvedor.removerConhecimentoDoDesenvolvedor(emailDesenvolvedor, nomeConhecimento);
	}

	/**
	 * Esse mtodo remove um desenvolvedor da base de dados. 
	 * @param email Email do desenvolvedor.
	 * @return true se o desenvolvedor foi removido com sucesso.
	 */
	public boolean removerDesenvolvedor(String email) {
		
		return servicoDesenvolvedor.removerDesenvolvedor(email);
	}
	
	public ArrayList<Desenvolvedor> getListaDesenvolvedores() {
		return servicoDesenvolvedor.getTodosDesenvolvedores();
	}

}
