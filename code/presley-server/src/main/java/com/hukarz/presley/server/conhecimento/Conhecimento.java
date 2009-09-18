package com.hukarz.presley.server.conhecimento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Item;
import com.hukarz.presley.beans.Tree;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.server.inferencia.classificador.Classificador;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.util.Util;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de conhecimentos.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class Conhecimento {
	
	ServicoConhecimento servicoConhecimento;
	ServicoDesenvolvedor servicoDesenvolvedor;
	ServicoArquivo servicoArquivo; 
	TopicoConhecimento conhecimento;
	
	public Conhecimento() {
		servicoConhecimento		= new ServicoConhecimentoImplDAO();
		servicoDesenvolvedor	= new ServicoDesenvolvedorImplDAO();
		servicoArquivo			= new ServicoArquivoImplDAO();
	}

	public void setConhecimento(TopicoConhecimento conhecimento) {
		this.conhecimento = conhecimento;
	}

	/**
	 * Este metodo atualiza um conhecimento previamente cadastrado na base da dados 
	 * @param nome Nome do conhecimento a ser atualizado.
	 * @param novoNome Novo nome do conhecimento.
	 * @param novaDescricao Nova descricao do conhecimento.
	 * @return true se o conhecimento foi atualizado.
	 * @throws NomeInvalidoException 
	 */
	public boolean atualizarConhecimento(String novoNome, String novaDescricao) 
		throws ConhecimentoInexistenteException, DescricaoInvalidaException, NomeInvalidoException {
		
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		if (!Util.validaDescricao(novaDescricao)) throw new DescricaoInvalidaException();
		if (!Util.validaNome(novoNome)) throw new NomeInvalidoException();
		
		if (!servicoConhecimento.conhecimentoExiste(conhecimento.getNome())) throw new ConhecimentoInexistenteException();

		conhecimento.setNome(novoNome);
		conhecimento.setDescricao(novaDescricao);
		return servicoConhecimento.atualizarConhecimento(conhecimento.getNome(), novoNome, novaDescricao);
	}
	
	/**
	 * Este mtodo verifica se um conhecimento existe na base de dados.
	 * @parame nome Nome do conhecimento para verificacao.
	 * @return true se o conhecimento existe.
	 * @throws ConhecimentoInexistenteException 
	 */
	public boolean conhecimentoExiste() throws ConhecimentoInexistenteException {
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		return servicoConhecimento.conhecimentoExiste(conhecimento.getNome());
	}
	
	/**
	 * Este metodo cria um novo conhecimento na base de dados
	 * @param nome Nome do novo conhecimento
	 * @param descricao Descricao do novo conhecimento
	 * @return true se o conhecimento foi inserido na base de dados.
	 */
	public boolean criarConhecimento() throws NomeInvalidoException,
		DescricaoInvalidaException,	ConhecimentoInexistenteException {
		
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		if (!Util.validaNome(conhecimento.getNome())) throw new NomeInvalidoException();
		if (servicoConhecimento.conhecimentoExiste(conhecimento.getNome())) throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.criarConhecimento(conhecimento.getNome(), conhecimento.getDescricao());
		
	}
	
	/**
	 * Este metodo remove um conhecimento previamente cadastrado. 
	 * @param nome Nome do conhecimento a ser removido.
	 * @return true se o conhecimento foi removido com sucesso
	 */
	public boolean removerConhecimento() throws ConhecimentoInexistenteException {
		
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		String nome = conhecimento.getNome();
		
		if (!servicoConhecimento.conhecimentoExiste(nome)) throw new ConhecimentoInexistenteException();
		
		// Desassociar e excluir conhecimentos filhos:
		ArrayList<TopicoConhecimento> conhecimentosFilhos = servicoConhecimento.getFilhos(nome);
		if (conhecimentosFilhos != null) {
			Iterator<TopicoConhecimento> it1 = conhecimentosFilhos.iterator();
			
			while (it1.hasNext()) {
				TopicoConhecimento conhecimentoFilho = it1.next();
				servicoConhecimento.desassociaConhecimentos(nome, conhecimentoFilho.getNome());
				servicoConhecimento.removerConhecimento(conhecimentoFilho.getNome());
			}
		}
		
		// Desassociar conhecimentos pais:
		ArrayList<TopicoConhecimento> conhecimentosPais = servicoConhecimento.getFilhos(nome);
		if (conhecimentosPais != null) {
			Iterator<TopicoConhecimento> it2 = conhecimentosPais.iterator();
			
			while (it2.hasNext()) {
				TopicoConhecimento conhecimentoPai = it2.next();
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
	public boolean associaConhecimentos(TopicoConhecimento conhecimentoFilho) 
		throws ConhecimentoInexistenteException {
		
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
//		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoFilho)) 
//			throw new ConhecimentoInexistenteException();
//		
//		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimentoPai)) 
//			throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.associaConhecimentos(conhecimento.getNome(), conhecimentoFilho.getNome());
	}
	
	/**
	 * Esse metodo desfaz uma associacao de herança entre dois conhecimentos
	 * passados por parametro.
	 * @param nomeConhecimentoPai Nome do conhecimento Pai.
	 * @param nomeConhecimentoFilho Nome do conhecimento Filho.
	 * @return true de a desassociacao foi realizada com sucesso.
	 */
	public boolean desassociaConhecimentos(TopicoConhecimento conhecimentoFilho) throws ConhecimentoInexistenteException {
		
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(conhecimentoFilho.getNome())) 
			throw new ConhecimentoInexistenteException();
		
		if (!servicoConhecimento.conhecimentoExiste(conhecimento.getNome())) 
			throw new ConhecimentoInexistenteException();
		
		return servicoConhecimento.desassociaConhecimentos(conhecimento.getNome(), conhecimentoFilho.getNome());
	}
	
	public ArrayList<TopicoConhecimento> getListaConhecimento() {
		return servicoConhecimento.getListaConhecimento();
	}

	public boolean possuiFilhos() throws ConhecimentoInexistenteException {
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		ArrayList<TopicoConhecimento> listaConhecimento =  servicoConhecimento.getFilhos(conhecimento.getNome());

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
	public TopicoConhecimento associaArquivo() throws ConhecimentoInexistenteException, IOException {
		if (conhecimento == null) throw new ConhecimentoInexistenteException();
		if (!servicoConhecimento.conhecimentoExiste(conhecimento.getNome())) 
			throw new ConhecimentoInexistenteException();

		ArrayList<Arquivo> arquivos = conhecimento.getArquivos();
		
		for (Iterator<Arquivo> iterator = arquivos.iterator(); iterator.hasNext();) {
			Arquivo arquivo = iterator.next();
			Classificador processaDocumento = new Classificador() ;
			processaDocumento.setTexto( arquivo.getTexto() );
			arquivo = processaDocumento.transformaTextoEmArquivo() ;
			
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
    	TopicoConhecimento conhecimentoRaiz =  new TopicoConhecimento();
    	conhecimentoRaiz.setNome("Raiz") ;
    	Tree arvore = new Tree( conhecimentoRaiz );

    	ArrayList<TopicoConhecimento> filhosDoRaiz = new ArrayList<TopicoConhecimento>();

    	ArrayList<TopicoConhecimento> conhecimentos = getListaConhecimento();
    	Iterator<TopicoConhecimento> it = conhecimentos.iterator();

    	// Buscando os conhecimentos que nao possuem pais (ou seja, os filhos do raiz).
    	while (it.hasNext()) {
    		TopicoConhecimento conhecimento = it.next();
    		ArrayList<TopicoConhecimento> paisDoConhecimento = null;

    		paisDoConhecimento = servicoConhecimento.getPais(conhecimento.getNome());
    		if (paisDoConhecimento.size() == 0) {
    			filhosDoRaiz.add(conhecimento);
    		}
    	}

    	// Adicionando os filhos do raiz.
    	Iterator<TopicoConhecimento> it2 = filhosDoRaiz.iterator();
    	while (it2.hasNext()) {
    		TopicoConhecimento conhecimento = it2.next();
    		arvore.adicionaFilho(conhecimento);
    	}

    	// Obtendo as sub-árvores para montar a arvore completa.
    	Iterator<TopicoConhecimento> it3 = filhosDoRaiz.iterator();
    	while (it3.hasNext()) {
    		TopicoConhecimento filhoDoRaiz = it3.next();
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
    	TopicoConhecimento conhecimento      = servicoConhecimento.getConhecimento( nomeItemPai );
    	ArrayList<TopicoConhecimento> filhos = servicoConhecimento.getFilhos(conhecimento.getNome());

    	if (filhos.size() != 0) {
    		Iterator<TopicoConhecimento> it1 = filhos.iterator();

    		while (it1.hasNext()) {
    			TopicoConhecimento filho = it1.next();
    			itemPai.adicionaFilho(filho);
    			Item itemFilho = itemPai.getFilho(filho);
    			montaSubArvore(itemFilho);
    		}
    	}
    }
    
}
