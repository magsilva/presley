package com.hukarz.presley.server.validacao.implementacao;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNAnnotateHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.excessao.ArquivoInexistenteException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;


public class ValidacaoArquivoImpl {
	ServicoArquivo servicoArquivo;
	ServicoDesenvolvedor servicoDesenvolvedor;

	public ValidacaoArquivoImpl() {
		servicoArquivo = new ServicoArquivoImplDAO();
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
	public boolean atualizarArquivo(Arquivo arquivoAnterior, Arquivo arquivoNovo) throws ArquivoInexistenteException {
		if (!servicoArquivo.arquivoExiste(arquivoAnterior)) throw new ArquivoInexistenteException();
		
		return servicoArquivo.atualizarArquivo(arquivoAnterior, arquivoNovo);
	}
	
	/**
	 * Este mtodo verifica se um conhecimento existe na base de dados.
	 * @parame nome Nome do conhecimento para verificacao.
	 * @return true se o conhecimento existe.
	 */
	public boolean arquivoExiste(Arquivo arquivo) {
		
		return servicoArquivo.arquivoExiste(arquivo);
	}
	
	/**
	 * Este metodo cria um novo conhecimento na base de dados
	 * @param nome Nome do novo conhecimento
	 * @param descricao Descricao do novo conhecimento
	 * @return true se o conhecimento foi inserido na base de dados.
	 */
	public boolean criarArquivo(Arquivo arquivo) throws ArquivoInexistenteException {
		
		// if (!ValidacaoUtil.validaNome(nome)) throw new NomeInvalidoException();
		
		if (servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
		
		return servicoArquivo.criarArquivo(arquivo);
		
	}
	
	/**
	 * Esse metodo retorna um objeto do tipo conhecimento que possui o nome
	 * passado por parametro.
	 * @param nome Nome do conhecimento a ser retornado.
	 * @return <Conhecimento>
	 */
	public Arquivo getArquivo(Arquivo arquivo) throws ArquivoInexistenteException {
		
		Arquivo arquivoRetorno = servicoArquivo.getArquivo(arquivo);
		if (arquivoRetorno == null) throw new ArquivoInexistenteException();
		
		return arquivoRetorno;
	}
	
	/**
	 * Este metodo remove um conhecimento previamente cadastrado. 
	 * @param nome Nome do conhecimento a ser removido.
	 * @return true se o conhecimento foi removido com sucesso
	 */
	public boolean removerArquivo(Arquivo arquivo) throws ArquivoInexistenteException {
		
		if (!servicoArquivo.arquivoExiste(arquivo)) throw new ArquivoInexistenteException();
		
/*
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
*/		
		return servicoArquivo.removerArquivo(arquivo);
	}

	public ArrayList<Desenvolvedor> getDesenvolvedoresArquivo(Arquivo arquivo){
		final ArrayList<Desenvolvedor> desenvolvedores = new ArrayList<Desenvolvedor>();
		
        try {
            //1. first initialize the DAV protocol
            DAVRepositoryFactory.setup();
        	
            //we will annotate a publicly available file
            // SVNURL fileURL = SVNURL.parseURIEncoded("https://svn.svnkit.com/repos/svnkit/trunk/changelog.txt");
            // svn blame http://project-presley.googlecode.com/svn/trunk/presely-server/src/core/interfaces/CorePresleyOperations.java
            
            SVNURL fileURL = SVNURL.parseURIEncoded( arquivo.getEnderecoServidor() );
            
            //SVNLogClient is the class with which you can perform annotations
            SVNLogClient logClient = SVNClientManager.newInstance().getLogClient();
            boolean ignoreMimeType = false;
            boolean includeMergedRevisions = false;

            logClient.doAnnotate(fileURL, SVNRevision.UNDEFINED, SVNRevision.create(1), SVNRevision.HEAD,
                    ignoreMimeType /*not ignoring mime type*/, includeMergedRevisions /*not including merged revisions */,
                    new ISVNAnnotateHandler(){

						@Override
						public void handleEOF() {

						}

						@Override
						public void handleLine(Date date, long revision, String author, String line) throws SVNException {
				            handleLine(date, revision, author, line, null, -1, null, null, 0);
						}

						@Override
						public void handleLine(Date date, long revision, String author, String line, Date mergedDate, 
				                long mergedRevision, String mergedAuthor, String mergedPath, int lineNumber)
								throws SVNException {
							
							Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedorCVS(author);

						 	if (desenvolvedores.indexOf(desenvolvedor)==-1)
						 		desenvolvedores.add(desenvolvedor);
			            	System.out.println("|" + author + "|" );
						}

						@Override
						public boolean handleRevision(Date date, long revision, String author, File contents) throws SVNException {
							/* We do not want our file to be annotated for each revision of the range, but only for the last 
							 * revision of it, so we return false  
							 */
							return false;
						}}, null);
            
            System.out.println( "Passou logClient.doAnnotate " );
        } catch (SVNException svne) {
            System.out.println(svne.getMessage());
        }
		
		return desenvolvedores;
		
	}
}
