package com.hukarz.presley.server.validacao.implementacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoArquivoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoLogControleVersaoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoArquivo;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoLogControleVersao;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;
import com.ximpleware.AutoPilot;
import com.ximpleware.EOFException;
import com.ximpleware.EncodingException;
import com.ximpleware.EntityException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public class ValidacaoLogControleVersaoImpl {
	private ServicoLogControleVersao servicoLogControleVersao = new ServicoLogControleVersaoImplDAO();
	private ServicoProjeto servicoProjeto = new ServicoProjetoImplDAO();
	private ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	private ServicoArquivo servicoArquivo = new ServicoArquivoImplDAO(); 
	private Logger logger = Logger.getLogger(this.getClass());
	private Projeto projeto;
	
	public ValidacaoLogControleVersaoImpl() {
		projeto = servicoProjeto.getProjetoAtivo();
	}


	public void registrarLogDoArquivo() {
		Date dataUltimoRegistro = servicoLogControleVersao.getDataHoraUltimoRegistro();

		try {
			// open a file and read the content into a byte array
			File f = new File(projeto.getEndereco_Log());

			FileInputStream fis =  new FileInputStream(f);
			byte[] b = new byte[(int) f.length()];
			fis.read(b);

			VTDGen vg = new VTDGen();
			vg.setDoc(b);
			vg.parse(true);  // set namespace awareness to true
			VTDNav vn = vg.getNav();

			AutoPilot ap0 = new AutoPilot();
			AutoPilot ap1 = new AutoPilot();
			AutoPilot ap2 = new AutoPilot();
			AutoPilot ap3 = new AutoPilot();

			ap0.selectXPath("/log/logentry");
			ap1.selectXPath("author");
			ap2.selectXPath("date");
			ap3.selectXPath("@revision");

			ap0.bind(vn);
			ap1.bind(vn);
			ap2.bind(vn);
			ap3.bind(vn);
			while(ap0.evalXPath()!=-1){
				String author = ap1.evalXPathToString();
				if (author.equals(""))
					continue;
				
				String dataLog   = ap2.evalXPathToString();
				String numeroRevisao = ap3.evalXPathToString();
				
				dataLog = dataLog.replace("T", " ").replace(".000000Z", "");
				Date data = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(dataLog);
				
				if (dataUltimoRegistro == null || data.after( dataUltimoRegistro )){
					ArrayList<ArquivoJava> arquivosAcessados = getArquivosLog( numeroRevisao );			
					Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedorCVS(author);

					servicoLogControleVersao.registrarLogDeRevisao(desenvolvedor, arquivosAcessados, data);
				}
			}

			ap0.resetXPath();

		} catch (ParseException e) {
			this.logger.trace(" XML file parsing error \n"+e);
		} catch (NavException e) {
			this.logger.trace(" Exception during navigation "+e);
		} catch (XPathParseException e) {

		} catch (XPathEvalException e) {

		} catch (java.io.IOException e)	{
			this.logger.trace(" IO exception condition"+e);
		} catch (DesenvolvedorInexistenteException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

	}

	
	private ArrayList<ArquivoJava> getArquivosLog(String numeroRevisao) throws IOException, EncodingException, EOFException, EntityException, ParseException, XPathParseException, XPathEvalException, NavException{
		ArrayList<ArquivoJava> retorno = new ArrayList<ArquivoJava>();
		
		// open a file and read the content into a byte array
		File f = new File(projeto.getEndereco_Log());

		FileInputStream fis =  new FileInputStream(f);
		byte[] b = new byte[(int) f.length()];
		fis.read(b);

		VTDGen vg = new VTDGen();
		vg.setDoc(b);
		vg.parse(true);  // set namespace awareness to true
		VTDNav vn = vg.getNav();
		
		AutoPilot ap0 = new AutoPilot();
		AutoPilot ap1 = new AutoPilot();
		
		ap0.selectXPath("/log/logentry[@revision= "+  numeroRevisao +" ]/paths/path");
		ap1.selectXPath("text()");
		
		ap0.bind(vn);
		ap1.bind(vn);
		
		while(ap0.evalXPath()!=-1){
			String enderecoLog = ap1.evalXPathToString();
			if (enderecoLog.endsWith(".java")){
				ArquivoJava arquivoJava = new ArquivoJava( enderecoLog.substring(enderecoLog.lastIndexOf("/")+1), projeto);
								
				arquivoJava.setEnderecoServidor(
						"/"+projeto.getNome() + "/"+
						enderecoLog.replaceFirst(projeto.getDiretorio_Subversion()+ "/", "")
				);
				if (!servicoArquivo.arquivoExiste(arquivoJava)){
					servicoArquivo.criarArquivo(arquivoJava);
				}
				arquivoJava.setId( servicoArquivo.getArquivo(arquivoJava).getId() );

				retorno.add( arquivoJava );
			}
			
		}				
		
		ap0.resetXPath();

		return retorno;
	}
}
