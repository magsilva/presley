package validacao.implementacao;


import inferencia.Inferencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import persistencia.implementacao.ServicoArquivoImplDAO;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoProblemaImplDAO;
import persistencia.implementacao.ServicoSolucaoImplDAO;
import persistencia.interfaces.ServicoArquivo;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoProblema;
import persistencia.interfaces.ServicoSolucao;
import processaTexto.ProcessaSimilaridade;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.AtividadeInexistenteException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de problemas.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoProblemaImpl {
	
	ServicoAtividade servicoAtividade;
	ServicoSolucao servicoSolucao;
	ServicoProblema servicoProblema;
	ServicoArquivo servicoArquivo;
	ValidacaoArquivoImpl validacaoArquivo ;
	
	public ValidacaoProblemaImpl() {
		servicoProblema  = new ServicoProblemaImplDAO();
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoSolucao   = new ServicoSolucaoImplDAO();
		servicoArquivo   = new ServicoArquivoImplDAO();
		
		validacaoArquivo = new ValidacaoArquivoImpl();
	}
	
	/**
	 * Esse método atualiza o status do problema, ou seja, se ele foi resolvido
	 * ou não.
	 * @param id Identificador do problema.
	 * @param status Situacao do problema.
	 * @return true se a atualizacao foi concluida com sucesso.
	 */
	public boolean atualizarStatusDoProblema(int id, boolean status) throws ProblemaInexistenteException{
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();
		
		return servicoProblema.atualizarStatusDoProblema(id, status);
	}
	
	/**
	 * Esse método cadastra um novo problema na base de dados.
	 * @param idAtividade Identificador da atividade.
	 * @param descricao Descricao do problema relatado.
	 * @param dataDoRelato Data em que o problema foi encontrado.
	 * @param mensagem Mensagem a ser exibida a respeito do tipo do problema.
	 * @return true se o problema foi cadastrado com sucesso.
	 * @throws IOException 
	 */
	public boolean cadastrarProblema(Problema problema) 
			throws DescricaoInvalidaException, IOException {
		if (!ValidacaoUtil.validaDescricao( problema.getDescricao() )) throw new DescricaoInvalidaException();
		
		Projeto projeto = ValidacaoUtil.getProjetoAtivo();
		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = new HashMap<ArquivoJava, ArrayList<Desenvolvedor>>();
		
		// Cadastra as classes envolvidas no problema
		Map<ClasseJava, ArquivoJava> arquivos = problema.getClassesRelacionadas();
		for (Iterator<ClasseJava> it = arquivos.keySet().iterator(); it.hasNext();) {  
			ClasseJava classe = it.next();  
			ArquivoJava arquivoJava = arquivos.get(classe);  
			arquivoJava.localizaEndereco( projeto.getEndereco_Servidor_Leitura() );
			if (!servicoArquivo.arquivoExiste(arquivoJava)){
				servicoArquivo.criarArquivo(arquivoJava);
			}
			arquivoJava.setId( servicoArquivo.getArquivo(arquivoJava).getId() );

			arquivoDesenvolvedores.put(arquivoJava, validacaoArquivo.getDesenvolvedoresArquivo(arquivoJava));
			try {
				System.out.println( "Conteudo do Arquivo Java " + arquivoJava.getNome() + "  ||  " + arquivoJava.getTexto() );
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
		problema.setClassesRelacionadas(arquivos);
		
		// Identifica o conhecimeto do problema a se cadastrar
		ProcessaSimilaridade processaSimilaridade = new ProcessaSimilaridade();
		problema.setConhecimento( processaSimilaridade.verificaConhecimentoDoTexto( problema.getDescricao() + "  " + problema.getMensagem() ) ) ;

		// Retorna os desenvolvedores que receberam o problema
		Inferencia.getDesenvolvedores(arquivoDesenvolvedores, problema.getConhecimento());
		
		return servicoProblema.cadastrarProblema(problema);
//		return true;
	}
	
	/**
	 * Esse método retorna o objeto problema que possui tal id.
	 * @param id Identificador do problema.
	 * @return <Problema>
	 */	
	public Problema getProblema(int id) throws ProblemaInexistenteException {
		
		if (!servicoProblema.problemaExiste(id)) throw new ProblemaInexistenteException();

		return servicoProblema.getProblema(id);
	}
	
	/**
	 * Esse método recupera uma lista de problemas relatados durante o desenvolvimento
	 * de uma atividade.
	 * @param idAtividade Identificador da atividade
	 * @return ArrayList<Problema>
	 */
	public ArrayList<Problema> listarProblemasDaAtividade(int idAtividade) throws AtividadeInexistenteException {
		
		if (!servicoAtividade.atividadeExiste(idAtividade)) throw new AtividadeInexistenteException();
		
		return servicoProblema.listarProblemasDaAtividade(idAtividade);
	}
	
	/**
	 * Esse método verifica se um dado problema existe na base de dados.
	 * @param id Identificador do problema
	 * @return true se o problema existir na base de dados.
	 */
	public boolean problemaExiste(int id) {

		return servicoProblema.problemaExiste(id);
	}
	
	/**
	 * Esse método remove um problema relatado da base de dados.
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
