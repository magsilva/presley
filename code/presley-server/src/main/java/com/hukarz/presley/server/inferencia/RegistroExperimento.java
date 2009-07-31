package com.hukarz.presley.server.inferencia;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;

public class RegistroExperimento {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void gerarArquivosExtra(
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorArq,
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento,
			Problema problema) throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();
		
		PrintWriter saidaPontuacao = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".extra"));
		
		// Ajustar Codigo apos o teste ******************
		ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();  
		listaDesenvolvedores.addAll( participacaoDesenvolvedorArq.keySet() );
		
		ArrayList<Desenvolvedor> listaDesenvolvedoresConhecimento = new ArrayList<Desenvolvedor>();  
		listaDesenvolvedoresConhecimento.addAll( participacaoDesenvolvedorConhecimento.keySet() );

		for (Desenvolvedor desenvolvedor : listaDesenvolvedoresConhecimento) {
			if (listaDesenvolvedores.indexOf(desenvolvedor)==-1)
				listaDesenvolvedores.add(desenvolvedor);
		}
		// <-
		
		saidaPontuacao.println( problema.getDescricao() );
				
		for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {

			saidaPontuacao.println( desenvolvedor.getEmail() + "," + 
					participacaoDesenvolvedorArq.get(desenvolvedor) +"," +
					participacaoDesenvolvedorConhecimento.get(desenvolvedor));
		}

		saidaPontuacao.close();
	}

	public void gerarArquivosRecomendations(ArrayList<Desenvolvedor> listaDesenvolvedores, 
			Problema problema) throws FileNotFoundException{
		Projeto projeto = problema.getProjeto();

		PrintWriter saidaRecomendacao = new PrintWriter(new 
				FileOutputStream(projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".recomendations"));

		for (Desenvolvedor desenvolvedor : listaDesenvolvedores) {
			saidaRecomendacao.println( desenvolvedor.getEmail() );

/*
			StringTokenizer st = new StringTokenizer( desenvolvedor.getListaEmail() );
			while (st.hasMoreTokens()){
				String email = st.nextToken();
				saidaRecomendacao.println( email );
			}
*/
		}

		saidaRecomendacao.close();
	}
	
	public void criarSolucoesValidas() throws ProblemaInexistenteException, DesenvolvedorInexistenteException, IOException{
		/*

		ValidacaoSolucaoImpl  validacaoSolucao = new ValidacaoSolucaoImpl();
		String conteudoEmail = "";

		Projeto projeto = problema.getProjeto();
		
		File file = new File( projeto.getEndereco_Servidor_Gravacao() + problema.getNumeroArquivoExperimento() + ".emails" );
		
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

			String linha = "";
			while( (linha = reader.readLine()) != null ){
				conteudoEmail += linha + " ";
			}

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
		} catch (FileNotFoundException e) {
			this.logger.info("Question " + problema.getNumeroArquivoExperimento() + " sem resposta.");
		}
		 */		
	}
	
}