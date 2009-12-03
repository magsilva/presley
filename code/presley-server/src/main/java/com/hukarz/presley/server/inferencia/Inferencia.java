package com.hukarz.presley.server.inferencia;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;


/*
 * 
 * Created on 09/09/2008
 */

public class Inferencia implements Inference {
	
	private boolean useCodeHistory;
	private boolean useCommunicationHistory;

	public Inferencia(boolean useCommunicationHistory, boolean useCodeHistory) {
		this.useCommunicationHistory = useCommunicationHistory;
		this.useCodeHistory = useCodeHistory;
	}

	/* (non-Javadoc)
	 * @see com.hukarz.presley.server.inferencia.Inference#getDesenvolvedores(java.util.Map, com.hukarz.presley.beans.Problema)
	 */
	public ArrayList<Desenvolvedor> getDesenvolvedores(Map<ArquivoJava, 
			ArrayList<Desenvolvedor>> arquivoDesenvolvedores, Problema problema) 
			throws FileNotFoundException {

		Map<Desenvolvedor, Integer> participacaoDesenvolvedorArquivo = null;
		Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento = null;
		
		if (this.useCodeHistory) {
			participacaoDesenvolvedorArquivo = getParticipacaoDesenvolvedores(arquivoDesenvolvedores);
		}
		else {
			participacaoDesenvolvedorArquivo = new HashMap<Desenvolvedor, Integer>();
		}
		
		if (this.useCommunicationHistory) {
			participacaoDesenvolvedorConhecimento = 
				getParticipacaoDesenvolvedores(problema.getConhecimento(), problema.getDesenvolvedorOrigem());
		}
		else {
			participacaoDesenvolvedorConhecimento = new HashMap<Desenvolvedor, Integer>();
		}
		
		Map<Desenvolvedor, Integer> participacaoDesenvolvedor 
			= somarParticipacaoDosDesenvolvedores(problema, participacaoDesenvolvedorArquivo, 
					participacaoDesenvolvedorConhecimento);

		participacaoDesenvolvedor.remove(problema.getDesenvolvedorOrigem());
		ArrayList<Desenvolvedor> desenvolvedoresRecomendados = retornarMelhoresDesenvolvedores(problema, participacaoDesenvolvedor, 5);		

		RegistroExperimento registroExperimento = RegistroExperimento.getInstance();
		registroExperimento.setListaDesenvolvedores(desenvolvedoresRecomendados);
		registroExperimento.salvar();
		registroExperimento.limpar();
		
		return desenvolvedoresRecomendados;
	}
	
	/*	1º Passo 
	(Analisar a participação de cada Desenvolvedor nos Arquivos)
	 */
	protected Map<Desenvolvedor, Integer> getParticipacaoDesenvolvedores(Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores){
		Map<Desenvolvedor, Integer> desenvolvedorPorArq = new HashMap<Desenvolvedor, Integer>();
		
		for (Iterator<ArquivoJava> itArquivo = arquivoDesenvolvedores.keySet().iterator(); itArquivo.hasNext();) {
			ArquivoJava arquivo = itArquivo.next();
			ArrayList<Desenvolvedor> desenvolvedoresArq = arquivoDesenvolvedores.get(arquivo);

			for (Iterator<Desenvolvedor> it = desenvolvedoresArq.iterator(); it.hasNext();) {
				Desenvolvedor desenvolvedor =  it.next() ;
				if ( desenvolvedorPorArq.get(desenvolvedor) == null)
					desenvolvedorPorArq.put(desenvolvedor, 1);
				else
					desenvolvedorPorArq.put(desenvolvedor, desenvolvedorPorArq.get(desenvolvedor) + 1);
			}
		}
		
		return desenvolvedorPorArq;
	}

	/*	2º Passo 
	(Analisar a participação de cada Desenvolvedor nas resolucao de problemas
	referentes ao conhecimento passado)
	 */
	protected Map<Desenvolvedor, Integer> getParticipacaoDesenvolvedores(Conhecimento conhecimento, Desenvolvedor desenvolvedor){
		ServicoConhecimento servicoConhecimento = new ServicoConhecimentoImplDAO();
		Map<Desenvolvedor, Integer> desenvolvedorPorConhecimento = servicoConhecimento.getContribuintesConhecimento(conhecimento, desenvolvedor);
		
		return desenvolvedorPorConhecimento;
	}

	
	/*	3º Passo 
	(Soma os vetores de participação dos Desenvolvedor nos Arquivos e nas mensagens)
	 */
	protected Map<Desenvolvedor, Integer> somarParticipacaoDosDesenvolvedores(Problema problema, Map<Desenvolvedor, Integer> participacaoDesenvolvedorArq, 
			Map<Desenvolvedor, Integer> participacaoDesenvolvedorConhecimento ){
		Map<Desenvolvedor, Integer> pontuacaoParticipacao = new HashMap<Desenvolvedor, Integer>();
		
		RegistroExperimento registroExperimento = RegistroExperimento.getInstance();
		
		registroExperimento.setParticipacaoDesenvolvedorArquivo(participacaoDesenvolvedorArq);
		registroExperimento.setParticipacaoDesenvolvedorConhecimento(participacaoDesenvolvedorConhecimento);
		registroExperimento.setProblema(problema);
		
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
			participacaoDesenvolvedorArq		= classificacarDesenvolvedores(participacaoDesenvolvedorArq, pontuacaoMax);
			
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
	protected Map<Desenvolvedor, Integer> classificacarDesenvolvedores( Map<Desenvolvedor, Integer> participacaoDesenvolvedor,
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
	
	protected Map<Desenvolvedor, Integer> pontuarClassificacao(Desenvolvedor[] desenvolvedores, 
			Map<Desenvolvedor, Integer> participacaoDesenvolvedor, Integer pontuacaoMax) {

		Map<Desenvolvedor, Integer> pontuacaoParticipacao = new HashMap<Desenvolvedor, Integer>();
		for (int i = 0; i < desenvolvedores.length; i++) {
			Desenvolvedor desenvolvedor = desenvolvedores[i];
			int participacao			= participacaoDesenvolvedor.get(desenvolvedor) ;
			
			pontuacaoParticipacao.put(desenvolvedor, pontuacaoMax);
			
			if (i+1 < desenvolvedores.length){
				int participacaoProx = participacaoDesenvolvedor.get(desenvolvedores[i+1]) ;
				if (participacao > participacaoProx)
				pontuacaoMax = pontuacaoMax - 2;
			}
			
		}
		
		return pontuacaoParticipacao;
	}

	
	/*	4º Passo 
	(Seleciona os melhores desenvolvedores como retorno)
	 */
	protected ArrayList<Desenvolvedor> retornarMelhoresDesenvolvedores(Problema problema, Map<Desenvolvedor, Integer> participacaoDesenvolvedor, int qtde ){
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
		
		
		return listaDesenvolvedores;
	}

}
