package com.hukarz.presley.server.validacao.implementacao;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.server.inferencia.Inferencia;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoMensagemImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoMensagem;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;
import com.hukarz.presley.server.processaTexto.ProcessaSimilaridade;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de problemas.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoProblemaImpl {
	
	ServicoSolucao  servicoSolucao;
	ServicoProblema servicoProblema;
	ServicoArquivo  servicoArquivo;
	ServicoMensagem servicoMensagem;
	ServicoProjeto  servicoProjeto;
	
	ValidacaoArquivoImpl validacaoArquivo ;
	
	public ValidacaoProblemaImpl() {
		servicoProblema = new ServicoProblemaImplDAO();
		servicoSolucao  = new ServicoSolucaoImplDAO();
		servicoArquivo  = new ServicoArquivoImplDAO();
		servicoMensagem = new ServicoMensagemImplDAO();
		servicoProjeto	= new ServicoProjetoImplDAO();
		
		validacaoArquivo = new ValidacaoArquivoImpl();
	}
	
	/**
	 * Esse m�todo atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou n�o.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema(int id, boolean status) throws ProblemaInexistenteException{
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();
		
		return servicoProblema.atualizarStatusDoProblema(id, status);
	}
	
	/**
	 * Esse m�todo cadastra um novo problema na base de dados.
	 * @param idAtividade Identificador da atividade.
	 * @param descricao Descricao do problema relatado.
	 * @param dataDoRelato Data em que o problema foi encontrado.
	 * @param mensagem Mensagem a ser exibida a respeito do tipo do problema.
	 * @return true se o problema foi cadastrado com sucesso.
	 * @throws IOException 
	 * @throws ProjetoInexistenteException 
	 * @throws ConhecimentoNaoEncontradoException 
	 */
	public Problema cadastrarProblema(Problema problema) 
	throws DescricaoInvalidaException, IOException, ProjetoInexistenteException, ConhecimentoNaoEncontradoException {

		if (!ValidacaoUtil.validaDescricao( problema.getDescricao() )) throw new DescricaoInvalidaException();

		if (!servicoProjeto.projetoExiste(problema.getProjeto())) throw new ProjetoInexistenteException();

		System.out.println("Ok --- Valida��o");
		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = getDesenvolvedoresArquivo(problema);
		
		System.out.println("Ok --- lista com os Desenvolvedores de cada arquivo"); 

		// Identifica o conhecimeto do problema a se cadastrar
		ProcessaSimilaridade processaSimilaridade = new ProcessaSimilaridade();
		
		String comentariosCodigo = "";
		for (Iterator<ArquivoJava> it = arquivoDesenvolvedores.keySet().iterator(); it.hasNext();) {  
			ArquivoJava arquivoJava = it.next();  
			try {
				comentariosCodigo += " " +arquivoJava.getTexto();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			
		}
		problema.setConhecimento( processaSimilaridade.verificaConhecimentoDoTexto( problema.getDescricao() + "  " + 
				problema.getMensagem() + " " + comentariosCodigo ) ) ;

		System.out.println("Ok --- conhecimeto do problema");
		// Retorna os desenvolvedores que receber�o o problema
		ArrayList<Desenvolvedor> desenvolvedores = Inferencia.getDesenvolvedores(arquivoDesenvolvedores, 
				problema.getConhecimento(), problema.getDesenvolvedorOrigem());
	
		problema = servicoProblema.cadastrarProblema(problema) ;

		System.out.println("Ok --- Cadastro do Problema");
		servicoMensagem.adicionarMensagem(desenvolvedores, problema);
		
		System.out.println("Ok --- Mensagem Adcionada");
	
		return problema;

	}

	/**
	 * Cria uma lista com os Desenvolvedores de cada arquivo java	
	 * @param problema
	 * @return
	 */
	public Map<ArquivoJava, ArrayList<Desenvolvedor>> getDesenvolvedoresArquivo(Problema problema){
		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = new HashMap<ArquivoJava, ArrayList<Desenvolvedor>>();
		
		Projeto projeto = problema.getProjeto();
		
		// Cadastra as classes envolvidas no problema
		Map<ClasseJava, ArquivoJava> arquivos = problema.getClassesRelacionadas();
		for (Iterator<ClasseJava> it = arquivos.keySet().iterator(); it.hasNext();) {  
			ClasseJava classe = it.next();  
			ArquivoJava arquivoJava = arquivos.get(classe);  
			
			// Busca o endere�o do arquivo no servidor entre os endere�os dos projetos ativos
			arquivoJava.localizaEndereco( projeto.getEndereco_Servidor_Gravacao() ) ;
			
			if (!servicoArquivo.arquivoExiste(arquivoJava)){
				servicoArquivo.criarArquivo(arquivoJava);
			}
			arquivoJava.setId( servicoArquivo.getArquivo(arquivoJava).getId() );

			arquivoDesenvolvedores.put(arquivoJava, validacaoArquivo.getDesenvolvedoresArquivo(arquivoJava));
			/*
			try {
				System.out.println( "Conteudo do Arquivo Java " + arquivoJava.getNome() + "  ||  " + arquivoJava.getTexto() );
			} catch (IOException e) {
				e.printStackTrace();
			}
			*/
		}
		
		//problema.setClassesRelacionadas(arquivos);
		
		return arquivoDesenvolvedores;
	}
	
	/**
	 * Esse m�todo retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id) throws ProblemaInexistenteException {
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();

		return servicoProblema.getProblema(id);
	}
	
	/**
	 * Esse m�todo verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 */
	public boolean problemaExiste(int id) {

		return servicoProblema.problemaExiste(id);
	}
	
	/**
	 * Esse m�todo remove um problema relatado da base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema foi removido da base de dados.
	 * @throws ProblemaInexistenteException 
	 */
	public boolean removerProblema(Problema problema) throws ProblemaInexistenteException {
		if (!servicoProblema.problemaExiste(problema.getId())) throw new ProblemaInexistenteException();
		
		// Remover Solucoes do Problema
		ArrayList<Solucao> solucoes = servicoSolucao.getSolucoesDoProblema(problema);
		Iterator<Solucao> it1 = solucoes.iterator();
		
		while (it1.hasNext()) {
			Solucao solucao = it1.next();
			servicoSolucao.removerSolucao(solucao.getId());
		}

		return servicoProblema.removerProblema(problema);
	}

	public ArrayList<Problema> getListaProblema(Desenvolvedor desenvolvedor) {
		ArrayList<Problema> problemas = servicoProblema.getListaProblemas(desenvolvedor);
		return problemas;
	}
	
	public ArrayList<String> getConhecimentosAssociados(String nomeDoProblema) {
		
		ArrayList<String> problemas = servicoProblema.getConhecimentosAssociados(nomeDoProblema);
		return problemas;
	}

}
