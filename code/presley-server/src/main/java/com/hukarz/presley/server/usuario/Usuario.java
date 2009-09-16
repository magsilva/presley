package com.hukarz.presley.server.usuario;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorExisteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.ListagemDeConhecimentoInexistenteException;
import com.hukarz.presley.excessao.SenhaInvalidaException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;
import com.hukarz.presley.server.util.ValidacaoUtil;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de desenvolvedores.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class Usuario {
	
	ServicoConhecimento servicoConhecimento;
	ServicoSolucao servicoSolucao;
	ServicoDesenvolvedor servicoDesenvolvedor;
	Desenvolvedor desenvolvedor;
	
	public Usuario(Desenvolvedor desenvolvedor) {
		servicoConhecimento = new ServicoConhecimentoImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
		servicoSolucao = new ServicoSolucaoImplDAO();
		
		this.desenvolvedor = desenvolvedor;
	}
	
	/**
	 * Esse mtodo adiciona um conhecimento previamente cadastrado a um desenvolvedor.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser adicionado.
	 * @return true se o conhecimento foi adicionado ao desenvolvedor.
	 * @throws DescricaoInvalidaException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public boolean adicionarConhecimentoAoDesenvolvedor(
			String nomeConhecimento, int grau, int qntResposta) throws DescricaoInvalidaException, ConhecimentoInexistenteException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DescricaoInvalidaException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoDesenvolvedor.adicionarConhecimentoAoDesenvolvedor(
				desenvolvedor.getEmail(), nomeConhecimento, grau, qntResposta);
	}
	
	/**
	 * Esse mtodo atualiza os dados de um desenvolvedor previamente cadastrado na
	 * base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param novoEmail novo email do desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param cvsNome Local onde o desenvolvedor reside.
	 * @return true se a atualizacao foi concluida com sucesso.
	 * @throws DesenvolvedorInexistenteException 
	 * @throws SenhaInvalidaException 
	 */
	public boolean atualizarDesenvolvedor(String novoEmail,
			String nome, String cvsNome, String senha) throws DesenvolvedorInexistenteException, SenhaInvalidaException {
		
//		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorInexistenteException();
		if (!ValidacaoUtil.validaSenha(senha)) throw new SenhaInvalidaException();

		desenvolvedor.setEmail(novoEmail);
		desenvolvedor.setCVSNome(cvsNome);
		desenvolvedor.setNome(nome);
		desenvolvedor.setSenha(senha);
		
		return servicoDesenvolvedor.atualizarDesenvolvedor(desenvolvedor.getEmail(), novoEmail, nome, cvsNome, senha);
	}
	
	/**
	 * Esse mtodo verifica se existe relacao entre um conhecimento e um desenvolvedor/
	 * @param emailDesenvolvedor Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a associacao existe.
	 * @throws DesenvolvedorInexistenteException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public boolean desenvolvedorTemConhecimento(String nomeConhecimento) throws DesenvolvedorInexistenteException, ConhecimentoInexistenteException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoDesenvolvedor.conhecimentoDoDesenvolvedorExiste(desenvolvedor.getEmail(), nomeConhecimento);
	}
	
	/**
	 * Esse mtodo adiciona um novo desenvolvedor na base de dados.
	 * @param email Email do novo desenvolvedor.
	 * @param nome Nome do novo desenvolvedor.
	 * @param cvsNome Local onde o desenvolvedor reside.
	 * @return true se o desenvolvedor foi criado com sucesso.
	 * @throws DesenvolvedorInexistenteException 
	 * @throws SenhaInvalidaException 
	 * @throws ListagemDeConhecimentoInexistenteException 
	 */
	public boolean criarDesenvolvedor() throws DesenvolvedorExisteException, SenhaInvalidaException, ListagemDeConhecimentoInexistenteException {
		
		if (servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorExisteException();
		//if (!ValidacaoUtil.validaSenha(senha)) throw new SenhaInvalidaException();
		
		boolean teste = servicoDesenvolvedor.criarDesenvolvedor(
				desenvolvedor.getEmail(), 
				desenvolvedor.getNome(), 
				desenvolvedor.getCVSNome(),
				desenvolvedor.getSenha());
		/*
		Set<Conhecimento> setConhecimento = null;
		
		if(hashMap != null) {
			
			setConhecimento = hashMap.keySet();
			
			for(Conhecimento conhecimento: setConhecimento) {
				Double grau = hashMap.get(conhecimento);
				servicoDesenvolvedor.adicionarConhecimentoAoDesenvolvedor(
						desenvolvedor.getEmail(), conhecimento.getNome(), grau, 0);
			}
			
//		} else {
//			throw new ListagemDeConhecimentoInexistenteException();
		}
		*/	
		return teste;
		
	}
	
	/**
	 * Esse mtodo verifica se um dado desenvolvedor est cadastrado no sistema.
	 * @param email Email do desenvolvedor
	 * @return true se o desenvolvedro estiver cadastrado no sistema.
	 */
	public boolean desenvolvedorExiste() {
		return servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail());
	}
	
	
	/**
	 * Esse mtodo retorna uma lista de conhecimentos que o desenvolvedor possui
	 * @param email Email do desenvolvedor
	 * @return ArrayList<TopicoConhecimento>
	 * @throws DescricaoInvalidaException 
	 */
	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor() 
		throws DescricaoInvalidaException  {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DescricaoInvalidaException();
		
		return servicoDesenvolvedor.getConhecimentosDoDesenvolvedor(desenvolvedor.getEmail());
	}
	
	/**
	 * Este mtodo retorna o desenvolvedor que possui o email passado no parametro.
	 * @param email Email do desenvolvedor.
	 * @return <Desenvolvedor> 
	 * @throws DesenvolvedorInexistenteException 
	 */
	public Desenvolvedor getDesenvolvedor() throws DesenvolvedorInexistenteException {
		
		Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedor(this.desenvolvedor.getEmail());
		if (desenvolvedor == null) throw new DesenvolvedorInexistenteException();
		
		this.desenvolvedor.setCVSNome( desenvolvedor.getCVSNome() ) ;
		this.desenvolvedor.setEmail( desenvolvedor.getEmail() );
		this.desenvolvedor.setListaConhecimento( desenvolvedor.getListaConhecimento() );
		this.desenvolvedor.setListaEmail( desenvolvedor.getListaEmail() );
		this.desenvolvedor.setNome( desenvolvedor.getNome() );
		this.desenvolvedor.setSenha( desenvolvedor.getSenha() );
		
		return desenvolvedor;
	}
	
	/**
	 * Esse mtodo remove um conhecimento associado a um desenvolvedor especfico.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param nomeConhecimento Nome do conhecimento a ser removido do desenvolvedor.
	 * @return true se a associacao foi desfeita.
	 */
	public boolean removerConhecimentoDoDesenvolvedor( String nomeConhecimento) {
		return servicoDesenvolvedor.removerConhecimentoDoDesenvolvedor(desenvolvedor.getEmail(), nomeConhecimento);
	}
	
	/**
	 * Esse mtodo remove um desenvolvedor da base de dados. 
	 * @param email Email do desenvolvedor.
	 * @return true se o desenvolvedor foi removido com sucesso.
	 */
	public boolean removerDesenvolvedor() {
		String email = desenvolvedor.getEmail();
		
		// Remover Solucoes do Desenvolvedor
		ArrayList<Solucao> solucoes = servicoSolucao.listarSolucoesDoDesenvolvedor(email);
		Iterator<Solucao> it1 = solucoes.iterator();
		
		while (it1.hasNext()) {
			Solucao solucao = it1.next();
			servicoSolucao.removerSolucao(solucao.getId());
		}
		
		// Desassociar Conhecimentos
		ArrayList<Conhecimento> conhecimentos = servicoDesenvolvedor.getConhecimentosDoDesenvolvedor(email);
		Iterator<Conhecimento> it2 = conhecimentos.iterator();
		
		while (it2.hasNext()) {
			Conhecimento conhecimento = it2.next();
			servicoDesenvolvedor.removerConhecimentoDoDesenvolvedor(email, conhecimento.getNome());
		}
		
		return servicoDesenvolvedor.removerDesenvolvedor(email);
	}
	
	/**
	 * Lista todos os desenvolvedores do banco.
	 * @return
	 */
	public ArrayList<Desenvolvedor> getListaDesenvolvedores() {
		return servicoDesenvolvedor.getTodosDesenvolvedores();
	}
	
	/**
	 * Esse método retorna a quantidade de respostas que um desenvolvedor tem em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return quantidade de respostas.
	 */
	public int getQntResposta(String nomeConhecimento) 
		throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste( desenvolvedor.getEmail() )) throw new DesenvolvedorInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoDesenvolvedor.getQntResposta(desenvolvedor.getEmail(), nomeConhecimento);
	}
	
	/**
	 * Esse método retorna o grau de conhecimento que um desenvolvedor tem em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return grau de conhecimento.
	 */
	public int getGrau(String nomeConhecimento) 
		throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoDesenvolvedor.getGrau(desenvolvedor.getEmail(), nomeConhecimento);
	}
	
	/**
	 * Esse método altera a quantidade de respostas que um desenvolvedor possui em um conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a atualização foi feita com sucesso.
	 */
	public boolean updateQntResposta(String nomeConhecimento, int quantidade)
		throws DesenvolvedorInexistenteException, ConhecimentoInexistenteException{
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoDesenvolvedor.updateQntResposta(desenvolvedor.getEmail(), nomeConhecimento, quantidade);
		
	}
	
	/**
	 * Esse método altera o grau de conhecimento de um desenvolvedor em um determinado conhecimento/
	 * @param email Email do desenvolvedor que possui tal conhecimento.
	 * @param nomeConhecimento Nome do conhecimento associado ao desenvolvedor. 
	 * @return true se a atualização foi feita com sucesso.
	 */
	public boolean updateGrau(String email, String nomeConhecimento, int grau)
		throws DesenvolvedorInexistenteException, ConhecimentoInexistenteException{
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new DesenvolvedorInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new ConhecimentoInexistenteException();
		
		return servicoDesenvolvedor.updateGrau(email, nomeConhecimento, grau);
		
	}
	
	/**
	 * Metodo que autentica um usuario para acesso ao sistema.
	 * @param email Email do desenvolvedor
	 * @param senha Senha do desenvolvedor
	 * @return Desenvolvedor. Retorna o objeto desenvolvedor em caso positivo.
	 * @throws ErroDeAutenticacaoException EmailInvalidoException, 
	 * SenhaInvalidaException, DesenvolvedorInexistenteException
	 */
	public Desenvolvedor autenticaDesenvolvedor(DadosAutenticacao dados) throws DesenvolvedorInexistenteException,
			EmailInvalidoException, SenhaInvalidaException, ErroDeAutenticacaoException {
		
		String email = dados.getUser();
		String senha = dados.getPasswd();
		
//		if (!ValidacaoUtil.validaEmail(email)) throw new EmailInvalidoException();
//		if (!ValidacaoUtil.validaSenha(senha)) throw new SenhaInvalidaException();
//		
//		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new DesenvolvedorInexistenteException();
		
		Desenvolvedor desenvolvedor = servicoDesenvolvedor.autenticaDesenvolvedor(email, senha);
		
		if (desenvolvedor == null) throw new DesenvolvedorInexistenteException();
		
		return desenvolvedor;
	}
	
	public Desenvolvedor getDesenvolvedorPorNome() throws DesenvolvedorInexistenteException {
		desenvolvedor = servicoDesenvolvedor.getDesenvolvedorPorNome(desenvolvedor.getNome()) ;
		return desenvolvedor;
	}

}
