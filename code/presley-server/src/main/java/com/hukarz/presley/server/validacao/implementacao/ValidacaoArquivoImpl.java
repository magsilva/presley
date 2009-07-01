package com.hukarz.presley.server.validacao.implementacao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hukarz.presley.beans.Arquivo;
import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.ArquivoInexistenteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;


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
				
		return servicoArquivo.removerArquivo(arquivo);
	}

	/**
	 * Cria uma lista com os Desenvolvedores de cada arquivo java	
	 * @param problema
	 * @return
	 */	
	public Map<ArquivoJava, ArrayList<Desenvolvedor>> getDesenvolvedoresArquivos(Problema problema){
		// Cria uma lista com os Desenvolvedores de cada arquivo java		 
		Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores = new HashMap<ArquivoJava, ArrayList<Desenvolvedor>>();

		cadastrarArquivosProblema(problema);

		Collection<ArquivoJava> listaArquivo = problema.getClassesRelacionadas().values();

		try {
			// open a file and read the content into a byte array
			File f = new File(problema.getProjeto().getEndereco_Log());

			FileInputStream fis =  new FileInputStream(f);
			byte[] b = new byte[(int) f.length()];
			fis.read(b);

			VTDGen vg = new VTDGen();
			vg.setDoc(b);
			vg.parse(true);  // set namespace awareness to true
			VTDNav vn = vg.getNav();

			AutoPilot ap0 = new AutoPilot();
			AutoPilot ap1 = new AutoPilot();

			for (Arquivo arquivo : listaArquivo) {
				ArrayList<Desenvolvedor> desenvolvedores = new ArrayList<Desenvolvedor>();
				
				// Selecione os autores que tem registro de log com o arquivo solicitado
				ap0.selectXPath("/log/logentry [paths/path='"+ arquivo.getEnderecoLog() +"']");
				ap1.selectXPath("author");

				ap0.bind(vn);
				ap1.bind(vn);
				while(ap0.evalXPath()!=-1){
					String author = ap1.evalXPathToString();
					Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedorCVS(author);

					if (desenvolvedores.indexOf(desenvolvedor)==-1)
						desenvolvedores.add(desenvolvedor);
				}

				ap0.resetXPath();
				
				arquivoDesenvolvedores.put((ArquivoJava) arquivo, desenvolvedores);
			}
		} catch (ParseException e) {
			System.out.println(" XML file parsing error \n"+e);
		} catch (NavException e) {
			System.out.println(" Exception during navigation "+e);
		} catch (XPathParseException e) {

		} catch (XPathEvalException e) {

		} catch (java.io.IOException e)	{
			System.out.println(" IO exception condition"+e);
		} catch (DesenvolvedorInexistenteException e) {
			// e.printStackTrace();
		}

		return arquivoDesenvolvedores;
	}
	
	private void cadastrarArquivosProblema(Problema problema) {
		// Cadastra as classes envolvidas no problema
		Map<ClasseJava, ArquivoJava> arquivos = problema.getClassesRelacionadas();
		for (Iterator<ClasseJava> it = arquivos.keySet().iterator(); it.hasNext();) {  
			ClasseJava classe = it.next();  
			ArquivoJava arquivoJava = arquivos.get(classe);  
			
			if (!servicoArquivo.arquivoExiste(arquivoJava)){
				servicoArquivo.criarArquivo(arquivoJava);
			}
			arquivoJava.setId( servicoArquivo.getArquivo(arquivoJava).getId() );
		}
				
	}
}
