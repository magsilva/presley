package com.hukarz.presley.server.inferencia.recomendador;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.server.inferencia.RegistroExperimento;

public class RecomendadorLine10 extends Recomendador {

	@Override
	public ArrayList<Desenvolvedor> getDesenvolvedores(
			Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores,
			Problema problema) throws FileNotFoundException {
		Map<Desenvolvedor, Integer> participacaoDesenvolvedorArquivo = 
			getParticipacaoDesenvolvedores(arquivoDesenvolvedores);
		
		participacaoDesenvolvedorArquivo.remove(problema.getDesenvolvedorOrigem());
		ArrayList<Desenvolvedor> desenvolvedoresRecomendados = retornarMelhoresDesenvolvedores(problema, participacaoDesenvolvedorArquivo, 5);		

		RegistroExperimento registroExperimento = RegistroExperimento.getInstance();
		registroExperimento.setParticipacaoDesenvolvedorArquivo(participacaoDesenvolvedorArquivo);
		registroExperimento.setParticipacaoDesenvolvedorConhecimento( new HashMap<Desenvolvedor, Integer>()  );
		registroExperimento.setProblema(problema);
		registroExperimento.setListaDesenvolvedores(desenvolvedoresRecomendados);
		registroExperimento.salvar();
		registroExperimento.limpar();
		
		return desenvolvedoresRecomendados;
	}

	
	
	@Override
	protected Map<Desenvolvedor, Integer> getParticipacaoDesenvolvedores(
			Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores) {
		Map<Desenvolvedor, Integer> desenvolvedorPorArquivo = new HashMap<Desenvolvedor, Integer>();
		
		ArrayList<Desenvolvedor> desenvolvedoresArquivo = getDesenvolvedorEmTodosOsArquivos(arquivoDesenvolvedores);

		for (Desenvolvedor desenvolvedor : desenvolvedoresArquivo) {
			int posicao = 0;
			
			for (Iterator<ArquivoJava> itArquivo = arquivoDesenvolvedores.keySet().iterator(); itArquivo.hasNext();) {
				ArquivoJava arquivo = itArquivo.next();
				ArrayList<Desenvolvedor> desenvolvedores = arquivoDesenvolvedores.get(arquivo);
				posicao += desenvolvedores.indexOf(desenvolvedor) ;
			}
			
			desenvolvedorPorArquivo.put(desenvolvedor, posicao);
		}
			
		return desenvolvedorPorArquivo;
	}

	
	private ArrayList<Desenvolvedor> getDesenvolvedorEmTodosOsArquivos(Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores){
		ArrayList<Desenvolvedor> desenvolvedoresArquivo = new ArrayList<Desenvolvedor>();
		
		// Os desenvolvedores retornados devem está em todos os arquivos
		if (arquivoDesenvolvedores.size() > 0){
			ArquivoJava primeiroArquivo = (ArquivoJava) arquivoDesenvolvedores.keySet().toArray()[0]; 
			for (Desenvolvedor desenvolvedorArquivo : arquivoDesenvolvedores.get(primeiroArquivo)) {
				desenvolvedoresArquivo.add(desenvolvedorArquivo);
			}
		}

		for (Desenvolvedor desenvolvedor : desenvolvedoresArquivo) {
			boolean achou = false ;
			
			for (Iterator<ArquivoJava> itArquivo = arquivoDesenvolvedores.keySet().iterator(); itArquivo.hasNext();) {
				ArquivoJava arquivo = itArquivo.next();
				ArrayList<Desenvolvedor> desenvolvedores = arquivoDesenvolvedores.get(arquivo);
				if (desenvolvedores.indexOf(desenvolvedor) >-1){
					achou = true;
					break;
				}
			}
			
			if (!achou){
				desenvolvedoresArquivo.remove(desenvolvedor);
			}
		}
		
		return desenvolvedoresArquivo;		
	}

	@Override
	protected ArrayList<Desenvolvedor> retornarMelhoresDesenvolvedores(
			Problema problema, Map<Desenvolvedor, Integer> participacaoDesenvolvedor, int qtde) {
		
		ArrayList<Desenvolvedor> listaDesenvolvedores = new ArrayList<Desenvolvedor>();
		for (Iterator<Desenvolvedor> it = participacaoDesenvolvedor.keySet().iterator(); it.hasNext();) {
			Desenvolvedor desenvolvedor = it.next();
			double classificacao = participacaoDesenvolvedor.get(desenvolvedor) ;
	
			int posicao = 0;
			for (Desenvolvedor desenvolvedorLista : listaDesenvolvedores) {
				if (classificacao < participacaoDesenvolvedor.get(desenvolvedorLista) ){
					break;
				}
				posicao++;
			}
			listaDesenvolvedores.add(posicao, desenvolvedor);
		}
		
		RegistroExperimento registroExperimento = RegistroExperimento.getInstance();
		registroExperimento.setListaDesenvolvedores(listaDesenvolvedores);
	
		return listaDesenvolvedores;
	}

}
