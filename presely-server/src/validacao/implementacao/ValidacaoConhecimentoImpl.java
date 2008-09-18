package validacao.implementacao;

import java.util.ArrayList;
import java.util.Iterator;
import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.TipoAtividade;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;
import excessao.ConhecimentoInexistenteException;
import excessao.DescricaoInvalidaException;
import excessao.NomeInvalidoException;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de conhecimentos.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoConhecimentoImpl {
	
	ServicoConhecimento servicoConhecimento;
	ServicoAtividade servicoAtividade;
	ServicoDesenvolvedor servicoDesenvolvedor;
	
	public ValidacaoConhecimentoImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}
	
	/**
	 * Este metodo atualiza um conhecimento previamente cadastrado na base da dados 
	 * @param nome Nome do conhecimento a ser atualizado.
	 * @param novoNome Novo nome do conhecimento.
	 * @param descricao Nova descricao do conhecimento.
	 * @return true se o conhecimento foi atualizado.
	 * @throws NomeInvalidoException 
	 */
	public boolean atualizarConhecimento(String nome, String novoNome,
			String descricao) throws ConhecimentoInexistenteException, DescricaoInvalidaException, 
			NomeInvalidoException {
		
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		if (!ValidacaoUtil.validaNome(novoNome)) throw new NomeInvalidoException();
		
		if (!servicoConhecimento.conhecimentoExiste(nome)) throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.atualizarConhecimento(nome, novoNome, descricao);
	}
	
	/**
	 * Este mtodo verifica se um conhecimento existe na base de dados.
	 * @parame nome Nome do conhecimento para verificacao.
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
	public boolean criarConhecimento(String nome, String descricao) throws NomeInvalidoException,
		DescricaoInvalidaException,	ConhecimentoInexistenteException {
		
		if (!ValidacaoUtil.validaNome(nome)) throw new NomeInvalidoException();
		if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		
		if (servicoConhecimento.conhecimentoExiste(nome)) throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.criarConhecimento(nome, descricao);
	}
	
	/**
	 * Esse metodo retorna um objeto do tipo conhecimento que possui o nome
	 * passado por parametro.
	 * @param nome Nome do conhecimento a ser retornado.
	 * @return <Conhecimento>
	 */
	public Conhecimento getConhecimento(String nome) throws ConhecimentoInexistenteException {
		
		Conhecimento conhecimento = servicoConhecimento.getConhecimento(nome);
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		
		return conhecimento;
	}
	
	/**
	 * Este metodo remove um conhecimento previamente cadastrado. 
	 * @param nome Nome do conhecimento a ser removido.
	 * @return true se o conhecimento foi removido com sucesso
	 */
	public boolean removerConhecimento(String nome) throws ConhecimentoInexistenteException {
		
		if (!servicoConhecimento.conhecimentoExiste(nome)) throw new ConhecimentoInexistenteException();
		
		// Desassociar e excluir conhecimentos filhos:
		ArrayList<Conhecimento> conhecimentosFilhos = servicoConhecimento.getFilhos(nome);
		if (conhecimentosFilhos != null) {
			Iterator<Conhecimento> it1 = conhecimentosFilhos.iterator();
			
			while (it1.hasNext()) {
				Conhecimento conhecimentoFilho = it1.next();
				servicoConhecimento.desassociaConhecimentos(nome, conhecimentoFilho.getNome());
				servicoConhecimento.removerConhecimento(conhecimentoFilho.getNome());
			}
		}
		
		// Desassociar conhecimentos pais:
		ArrayList<Conhecimento> conhecimentosPais = servicoConhecimento.getFilhos(nome);
		if (conhecimentosPais != null) {
			Iterator<Conhecimento> it2 = conhecimentosPais.iterator();
			
			while (it2.hasNext()) {
				Conhecimento conhecimentoPai = it2.next();
				servicoConhecimento.desassociaConhecimentos(conhecimentoPai.getNome(), nome);
			}
		}
		
		// Desassociar atividades:
		ArrayList<TipoAtividade> atividades = servicoAtividade.listarAtividades();
		if (atividades != null) {
			Iterator<TipoAtividade> it3 = atividades.iterator();
			
			while (it3.hasNext()) {
				TipoAtividade atividade = it3.next();
				if (servicoAtividade.atividadeAssociadaAConhecimentoExiste(atividade.getId(), nome)) {
					servicoAtividade.removerConhecimentoDaAtividade(atividade.getId(), nome);
				}
			}
		}
		
		// Desassociar desenvolvedores:
		ArrayList<Desenvolvedor> desenvolvedores = servicoDesenvolvedor.getTodosDesenvolvedores();
		if (desenvolvedores != null) {
			Iterator<Desenvolvedor> it4 = desenvolvedores.iterator();
			
			while (it4.hasNext()) {
				Desenvolvedor desenvolvedor = it4.next();
				if (servicoDesenvolvedor.conhecimentoDoDesenvolvedorExiste(desenvolvedor.getEmail(), nome)) {
					servicoDesenvolvedor.removerConhecimentoDoDesenvolvedor(desenvolvedor.getEmail(), nome);
				}
			}
		}
		
		return servicoConhecimento.removerConhecimento(nome);
	}
	
	/**
	 * Esse metodo cria uma associacao de herança entre dois conhecimentos
	 * passados por parametro.
	 * @param nomeConhecimentoPai Nome do conhecimento Pai.
	 * @param nomeConhecimentoFilho Nome do conhecimento Filho.
	 * @return true de a associacao foi realizada com sucesso.
	 */
	public boolean associaConhecimentos(String nomeConhecimentoPai,
			String nomeConhecimentoFilho) throws ConhecimentoInexistenteException {
		
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoFilho)) 
			throw new ConhecimentoInexistenteException();
		
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoPai)) 
			throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.associaConhecimentos(nomeConhecimentoPai, nomeConhecimentoFilho);
	}
	
	/**
	 * Esse metodo desfaz uma associacao de herança entre dois conhecimentos
	 * passados por parametro.
	 * @param nomeConhecimentoPai Nome do conhecimento Pai.
	 * @param nomeConhecimentoFilho Nome do conhecimento Filho.
	 * @return true de a desassociacao foi realizada com sucesso.
	 */
	public boolean desassociaConhecimentos(String nomeConhecimentoPai,
			String nomeConhecimentoFilho) throws ConhecimentoInexistenteException {
		
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoFilho)) 
			throw new ConhecimentoInexistenteException();
		
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoPai)) 
			throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.desassociaConhecimentos(nomeConhecimentoPai, nomeConhecimentoFilho);
	}
	
	/**
	 * Metodo que retorna os conhecimentos filhos de um conhecimento.
	 * @param idConhecimentoPai
	 * @return ArrayList<Conhecimento> Lista dos conhecimentos filhos.
	 * @throws ConhecimentoInexistenteException
	 */
	public ArrayList<Conhecimento> getFilhos(String nomeConhecimentoPai)
			throws ConhecimentoInexistenteException {

		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoPai)) 
			throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.getFilhos(nomeConhecimentoPai);
	}
	
	/**
	 * Metodo que retorna os conhecimentos pais de um conhecimento.
	 * @param idConhecimentoPai
	 * @return ArrayList<Conhecimento> Lista dos conhecimentos filhos.
	 * @throws ConhecimentoInexistenteException
	 */
	public ArrayList<Conhecimento> getPais(String nomeConhecimentoFilho)
			throws ConhecimentoInexistenteException {

		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoFilho)) 
			throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.getPais(nomeConhecimentoFilho);
	}

	public ArrayList<Conhecimento> getListaConhecimento() {
		// TODO Auto-generated method stub
		return servicoConhecimento.getListaConhecimento();
	}

}
