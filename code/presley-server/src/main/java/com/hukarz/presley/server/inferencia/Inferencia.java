package com.hukarz.presley.server.inferencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoSolucaoImpl;


/*
 * Created on 09/09/2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Presley
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Inferencia {

	//	public static void main(String args[]){
	//		ArrayList<Desenvolvedor> lDesenv = getDesenvolvedores(new String[]{"java", "conector"}, 50);
	//		
	//		for(Desenvolvedor d : lDesenv){
	//		}
	//		
	//	}

	public static ArrayList<Desenvolvedor> getDesenvolvedores(Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores,
			Problema problema) {

		Map<Desenvolvedor, Integer> participacaoDesenvolvedorArq = getParticipacaoDesenvolvedores(arquivoDesenvolvedores);
		Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento = getParticipacaoDesenvolvedores(problema.getConhecimento(), problema.getDesenvolvedorOrigem());
		
		Map<Desenvolvedor, Integer> participacaoDesenvolvedor = somarParticipacaoDosDesenvolvedores(participacaoDesenvolvedorArq, participacaoDesenvolvedorConhecimento);

		return retornarMelhoresDesenvolvedores(problema, participacaoDesenvolvedor, 5);
	}
	
	/*	1º Passo 
	(Analisar a participação de cada Desenvolvedor nos Arquivos)
	 */
	private static Map<Desenvolvedor, Integer> getParticipacaoDesenvolvedores(Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores){
		Map<Desenvolvedor, Integer> desenvolvedorPorArq = new HashMap<Desenvolvedor, Integer>();
		
		for (Iterator<ArquivoJava> itArquivo = arquivoDesenvolvedores.keySet().iterator(); itArquivo.hasNext();) {
			ArquivoJava arquivo = itArquivo.next();
			ArrayList<Desenvolvedor> desenvolvedoresArq = arquivoDesenvolvedores.get(arquivo);

			for (Iterator<Desenvolvedor> it = desenvolvedoresArq.iterator(); it.hasNext();) {
				Desenvolvedor desenvolvedor =  it.next() ;
				if ( desenvolvedorPorArq.get(desenvolvedor) == null)
					desenvolvedorPorArq.put(desenvolvedor, 1);
				else
					desenvolvedorPorArq.put(desenvolvedor, desenvolvedorPorArq.get(desenvolvedor) +1);
			}
		}
		
		return desenvolvedorPorArq;
	}

	/*	2º Passo 
	(Analisar a participação de cada Desenvolvedor nas resolucao de problemas
	referentes ao conhecimento passado)
	 */
	private static Map<Desenvolvedor, Integer> getParticipacaoDesenvolvedores(Conhecimento conhecimento, Desenvolvedor desenvolvedor){
		ServicoConhecimento servicoConhecimento = new ServicoConhecimentoImplDAO();
		ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
		
		Map<Desenvolvedor, Integer> desenvolvedorPorConhecimento = servicoConhecimento.getContribuintesConhecimento(conhecimento, desenvolvedor);
		
		// Calcula o conhecimento informado pelo cadastro de desenvolvedor
		ArrayList<Desenvolvedor> desenvolvedoresConhecimento = servicoDesenvolvedor.getDesenvolvedoresPorConhecimento(conhecimento);
		for (Iterator<Desenvolvedor> iterator = desenvolvedoresConhecimento.iterator(); iterator.hasNext();) {
			Desenvolvedor desenvolvedorConhecimento =  iterator.next();
			Integer qtde = desenvolvedorPorConhecimento.get(desenvolvedorConhecimento) ;

			if (qtde ==0)
				qtde = desenvolvedorConhecimento.getListaConhecimento().get(conhecimento).intValue() ;
			else
				qtde *= desenvolvedorConhecimento.getListaConhecimento().get(conhecimento); 

			desenvolvedorPorConhecimento.put(desenvolvedorConhecimento, qtde);			
		}
		
		
		return desenvolvedorPorConhecimento;
	}

	
	/*	3º Passo 
	(Soma os vetores de participação dos Desenvolvedor nos Arquivos e nas mensagens)
	 */
	private static Map<Desenvolvedor, Integer> somarParticipacaoDosDesenvolvedores(Map<Desenvolvedor, Integer> participacaoDesenvolvedorArq, 
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento ){
		Map<Desenvolvedor, Integer> pontuacaoParticipacao = new HashMap<Desenvolvedor, Integer>();
		
		if (participacaoDesenvolvedorArq.size() == 0){
			pontuacaoParticipacao = participacaoDesenvolvedorConhecimento;
		} else if (participacaoDesenvolvedorConhecimento.size() == 0){
			pontuacaoParticipacao = participacaoDesenvolvedorArq;
		} else {
			int pontuacaoMax = 0;
			if (participacaoDesenvolvedorArq.size() > participacaoDesenvolvedorConhecimento.size())
				pontuacaoMax = participacaoDesenvolvedorArq.size();
			else 
				pontuacaoMax = participacaoDesenvolvedorConhecimento.size();
			
			participacaoDesenvolvedorConhecimento	= classificacarDesenvolvedores(participacaoDesenvolvedorConhecimento, pontuacaoMax);
			participacaoDesenvolvedorArq			= classificacarDesenvolvedores(participacaoDesenvolvedorArq, pontuacaoMax);
			
			pontuacaoParticipacao = participacaoDesenvolvedorArq ;
			
			for (Iterator<Desenvolvedor> it = participacaoDesenvolvedorConhecimento.keySet().iterator(); it.hasNext();) {
				Desenvolvedor desenvolvedor = it.next();
				int pontuacao = participacaoDesenvolvedorConhecimento.get(desenvolvedor) ;
				
				if ( pontuacaoParticipacao.keySet().contains(desenvolvedor) ){
					pontuacao += pontuacaoParticipacao.get(desenvolvedor);
					pontuacaoParticipacao.remove(desenvolvedor);
				}
				
				pontuacaoParticipacao.put(desenvolvedor, pontuacao);			
			}
			
		}
		
		return pontuacaoParticipacao;
	}
	
	// Metodo para classificar os desenvolvedores por participação
	private static Map<Desenvolvedor, Integer> classificacarDesenvolvedores( Map<Desenvolvedor, Integer> participacaoDesenvolvedor,
			Integer pontuacaoMax) {
		Desenvolvedor[] classificacaoDesenvolvedores = new Desenvolvedor[ participacaoDesenvolvedor.size() ];
		
		Integer maiorParticipacao = 0, menorParticipacao = -1 ; 
		for (Iterator<Desenvolvedor> it = participacaoDesenvolvedor.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();
			Integer participacao = participacaoDesenvolvedor.get(desenvolvedor) ;
			
			if (menorParticipacao == -1)
				menorParticipacao = participacao;
			
			// Se o desenvolvedor tiver a maior participação encontrada  
			if (maiorParticipacao < participacao){
				for (int i = classificacaoDesenvolvedores.length-2; i >= 0 ; i--) {
					Desenvolvedor desenvolvedorArray = classificacaoDesenvolvedores[i];
					classificacaoDesenvolvedores[i+1] = desenvolvedorArray;
				}
				classificacaoDesenvolvedores[0] = desenvolvedor;
				maiorParticipacao = participacao;
			// Se o desenvolvedor tiver a menor participação encontrada
			} else if (menorParticipacao > participacao) {
				for (int i = 0; i < classificacaoDesenvolvedores.length; i++) {
					if (classificacaoDesenvolvedores[i] == null){
						classificacaoDesenvolvedores[i] = desenvolvedor;
						break;
					}         
				}
				menorParticipacao = participacao;
			// Se o desenvolvedor tiver uma participação entre a maior e a menor
			} else {
				for (int i = 0; i < classificacaoDesenvolvedores.length; i++) {
					Integer participacaoArray = participacaoDesenvolvedor.get( classificacaoDesenvolvedores[i] ) ;
					if (participacaoArray <= participacao ){
						
						for (int i2 = classificacaoDesenvolvedores.length-2; i2 >= i ; i2--) {
							Desenvolvedor desenvolvedorArray = classificacaoDesenvolvedores[i2];
							classificacaoDesenvolvedores[i2+1] = desenvolvedorArray;
						}
						
						classificacaoDesenvolvedores[i] = desenvolvedor;
						break;
					}
				}				
			}
		}
		
		return pontuarClassificacao(classificacaoDesenvolvedores, participacaoDesenvolvedor, pontuacaoMax);
	}
	
	private static Map<Desenvolvedor, Integer> pontuarClassificacao(Desenvolvedor[] desenvolvedores, 
			Map<Desenvolvedor, Integer> participacaoDesenvolvedor, Integer pontuacaoMax) {

		Map<Desenvolvedor, Integer> pontuacaoParticipacao = new HashMap<Desenvolvedor, Integer>();
		for (int i = 0; i < desenvolvedores.length; i++) {
			Desenvolvedor desenvolvedor = desenvolvedores[i];
			int participacao			= participacaoDesenvolvedor.get(desenvolvedor) ;
			
			pontuacaoParticipacao.put(desenvolvedor, pontuacaoMax);
			
			if (i+1 < desenvolvedores.length){
				int participacaoProx = participacaoDesenvolvedor.get(desenvolvedores[i+1]) ;
				if (participacao > participacaoProx)
				pontuacaoMax--;
			}
			
		}
		
		return pontuacaoParticipacao;
	}

	/*	4º Passo 
	(Seleciona os melhores desenvolvedores como retorno)
	 */
	private static ArrayList<Desenvolvedor> retornarMelhoresDesenvolvedores(Problema problema, Map<Desenvolvedor, Integer> participacaoDesenvolvedor, int qtde ){
		ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();
		Desenvolvedor desenvolvedorMenor = new Desenvolvedor();

		for (Iterator<Desenvolvedor> it = participacaoDesenvolvedor.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();
			double porcentagem = participacaoDesenvolvedor.get(desenvolvedor) ;
			
			if ( listaDesenvolvedores.size() < qtde )
				listaDesenvolvedores.add( desenvolvedor );
			else {
				double porcentagemRetorno = participacaoDesenvolvedor.get(desenvolvedorMenor) ;
				if (porcentagem > porcentagemRetorno){
					listaDesenvolvedores.remove(desenvolvedorMenor);
					listaDesenvolvedores.add(desenvolvedor);
				}
			}
				
			
			// Seleciona o Desenvolvedor com menor porcentagem
			if ( listaDesenvolvedores.size() == qtde ){
				for (int i = 0; i < qtde; i++) {
					if (i==0)
						desenvolvedorMenor = listaDesenvolvedores.get(i);
					else{
						double porcentagemDesenvolvedor = participacaoDesenvolvedor.get(listaDesenvolvedores.get(i)) ;
						double porcentagemDesenvolvedorMenor = participacaoDesenvolvedor.get(desenvolvedorMenor) ;
						
						if (porcentagemDesenvolvedor < porcentagemDesenvolvedorMenor)
							desenvolvedorMenor = listaDesenvolvedores.get(i);
					}
				}
			}
		}
		
		gerarLog(listaDesenvolvedores, participacaoDesenvolvedor);
		criarSolucoesValidas(listaDesenvolvedores, problema);
		
		return listaDesenvolvedores;
	}
	
	private static void gerarLog(ArrayList<Desenvolvedor> listaDesenvolvedores, 
			Map<Desenvolvedor, Integer> participacaoDesenvolvedor){
		ServicoProjeto servicoProjeto = new ServicoProjetoImplDAO();
		Projeto projeto = servicoProjeto.getProjetosAtivo().get(0);
		
		File diretorioCD = new File( projeto.getEndereco_Servidor_Gravacao() );
		
		File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".recomendacoes");  
			}  
		}); 

		try {  			
			String nomeArquivo = "000" + Integer.toString(listagemDiretorio.length + 1);
			nomeArquivo = nomeArquivo.substring(nomeArquivo.length()-4);
			PrintWriter saidaRecomendacao = new PrintWriter(new 
					FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + nomeArquivo + ".recomendacoes"));
			
			PrintWriter saidaPontuacao = new PrintWriter(new 
					FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + nomeArquivo + ".extra"));
			for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {
				
		    	StringTokenizer st = new StringTokenizer( desenvolvedor.getListaEmail() );
				while (st.hasMoreTokens()){
					String email = st.nextToken();
					saidaRecomendacao.println( email );
				}

				saidaPontuacao.println( desenvolvedor.getEmail() + " - " + participacaoDesenvolvedor.get(desenvolvedor));
			}
			
			saidaRecomendacao.close();
			saidaPontuacao.close();
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		}  
	}
	
	private static void criarSolucoesValidas(ArrayList<Desenvolvedor> listaDesenvolvedores, Problema problema){
		try {
			ValidacaoSolucaoImpl  validacaoSolucao = new ValidacaoSolucaoImpl();

			ServicoProjeto servicoProjeto = new ServicoProjetoImplDAO();
			Projeto projeto = servicoProjeto.getProjetosAtivo().get(0);

			File diretorioCD = new File( projeto.getEndereco_Servidor_Gravacao() );

			File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
				public boolean accept(File d, String name) {  
					return name.toLowerCase().endsWith(".recomendacoes");  
				}  
			}); 

			String nomeArquivoEmail = "000" + Integer.toString(listagemDiretorio.length);
			nomeArquivoEmail = nomeArquivoEmail.substring(nomeArquivoEmail.length()-4);

			File file = new File( projeto.getEndereco_Servidor_Gravacao() + nomeArquivoEmail+ ".emails" );
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			
			String conteudoEmail = reader.toString();
			
			for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {

				StringTokenizer st = new StringTokenizer( desenvolvedor.getListaEmail() );
				while (st.hasMoreTokens()){
					String email = st.nextToken();
					if (conteudoEmail.contains(email)){

						Solucao solucao = new Solucao();
						solucao.setAjudou(true);
						solucao.setProblema(problema);
						solucao.setData( new Date(System.currentTimeMillis()) ) ;
						solucao.setMensagem("");
						solucao.setDesenvolvedor(desenvolvedor);

						validacaoSolucao.cadastrarSolucao(solucao);
						break;
					}
				} 
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ProblemaInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DesenvolvedorInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
