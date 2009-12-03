package com.hukarz.presley.server.validacao.implementacao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Item;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.processaTexto.ProcessaDocumento;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de conhecimentos.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoConhecimentoImpl {
	
	ServicoConhecimento servicoConhecimento;
	ServicoDesenvolvedor servicoDesenvolvedor;
	ServicoArquivo servicoArquivo; 
	
	public ValidacaoConhecimentoImpl() {
		servicoConhecimento		= new ServicoConhecimentoImplDAO();
		servicoDesenvolvedor	= new ServicoDesenvolvedorImplDAO();
		servicoArquivo			= new ServicoArquivoImplDAO();
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
		//if (!ValidacaoUtil.validaDescricao(descricao)) throw new DescricaoInvalidaException();
		
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
		
//		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoFilho)) 
//			throw new ConhecimentoInexistenteException();
//		
//		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoPai)) 
//			throw new ConhecimentoInexistenteException();
		
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

//		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoPai)) 
//			throw new ConhecimentoInexistenteException();
		
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

		return servicoConhecimento.getPais(nomeConhecimentoFilho);
	}

	public ArrayList<Conhecimento> getListaConhecimento() {
		
		return servicoConhecimento.getListaConhecimento();
	}

	public boolean possuiFilhos(Conhecimento conhecimento) throws ConhecimentoInexistenteException {
		
		ArrayList<Conhecimento> listaConhecimento =  servicoConhecimento.getFilhos(conhecimento.getNome());

		if((listaConhecimento != null) && (listaConhecimento.size() == 0)) {
			return false;
		}
		return true;
	}

	/**
	 * Esse metodo cria associa os arquivos no conhecimento passado como paramentro
	 * e cria a lista de palavras-chave do arquivo
	 * @param conhecimento
	 * @return
	 * @throws ConhecimentoInexistenteException
	 * @throws IOException
	 */
	// TODO @alan analisar
	// XXX: associação de arquivos a um conhecimento 
	public Conhecimento associaArquivo(Conhecimento conhecimento) throws ConhecimentoInexistenteException, IOException {
		
		if (!servicoConhecimento.conhecimentoExiste(conhecimento.getNome())) {
			throw new ConhecimentoInexistenteException();
		}

		for (Arquivo arquivo : conhecimento.getArquivos()) {
			ProcessaDocumento processaDocumento = new ProcessaDocumento() ;
			arquivo = processaDocumento.getDocumentoProcessado(arquivo) ;
			
			if (!servicoArquivo.arquivoExiste(arquivo)){
				servicoArquivo.criarArquivo(arquivo);
			}
			
			arquivo.setId( servicoArquivo.getArquivo(arquivo).getId() );

			servicoArquivo.associaPalavrasArquivo(arquivo, arquivo.getTermosSelecionados());
			servicoConhecimento.associaArquivo(conhecimento, arquivo);
		}
		
		
		return conhecimento; 
	}

    /**
     * Este metodo retorna um objeto do tipo Tree que representa a arvore de
     * conhecumentos.
     *
     * @return Retorna a arvode de conhecimentos da ontologia.
     * @throws ConhecimentoInexistenteException
     */
    public Tree getArvoreDeConhecimentos() throws ConhecimentoInexistenteException {
    	Conhecimento conhecimentoRaiz =  new Conhecimento();
    	conhecimentoRaiz.setNome("Raiz") ;
    	Tree arvore = new Tree( conhecimentoRaiz );

    	ArrayList<Conhecimento> filhosDoRaiz = new ArrayList<Conhecimento>();

    	ArrayList<Conhecimento> conhecimentos = getListaConhecimento();
    	Iterator<Conhecimento> it = conhecimentos.iterator();

    	// Buscando os conhecimentos que nao possuem pais (ou seja, os filhos do raiz).
    	while (it.hasNext()) {
    		Conhecimento conhecimento = it.next();
    		ArrayList<Conhecimento> paisDoConhecimento = null;

    		paisDoConhecimento = getPais(conhecimento.getNome());

    		if (paisDoConhecimento.size() == 0) {
    			filhosDoRaiz.add(conhecimento);
    		}
    	}

    	// Adicionando os filhos do raiz.
    	Iterator<Conhecimento> it2 = filhosDoRaiz.iterator();
    	while (it2.hasNext()) {
    		Conhecimento conhecimento = it2.next();
    		arvore.adicionaFilho(conhecimento);
    	}

    	// Obtendo as sub-árvores para montar a arvore completa.
    	Iterator<Conhecimento> it3 = filhosDoRaiz.iterator();
    	while (it3.hasNext()) {
    		Conhecimento filhoDoRaiz = it3.next();
    		Item item = arvore.getFilho(filhoDoRaiz);
    		montaSubArvore(item);
    	}

    	return arvore;
    }

    /**
     * Metodo recursivo que monta a subarvore de um determinado item da arvode
     * de conhecimentos.
     *
     * @param item Item para o qual sera montada a sub-arvore
     * @throws ConhecimentoInexistenteException
     */
    private void montaSubArvore(Item itemPai) throws ConhecimentoInexistenteException {
    	String nomeItemPai = itemPai.getConhecimento().getNome();
    	Conhecimento conhecimento = getConhecimento(nomeItemPai);

    	ArrayList<Conhecimento> filhos = getFilhos(conhecimento.getNome());
    	if (filhos.size() != 0) {
    		Iterator<Conhecimento> it1 = filhos.iterator();

    		while (it1.hasNext()) {
    			Conhecimento filho = it1.next();
    			itemPai.adicionaFilho(filho);
    			Item itemFilho = itemPai.getFilho(filho);
    			montaSubArvore(itemFilho);
    		}
    	}
    }
}
