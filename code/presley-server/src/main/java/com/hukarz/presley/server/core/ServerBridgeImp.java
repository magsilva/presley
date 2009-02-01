package com.hukarz.presley.server.core;

import java.io.IOException;
import java.util.ArrayList;

import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.communication.server.ServerBridge;
import com.hukarz.presley.excessao.AtividadeInexistenteException;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.DataInvalidaException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorExisteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.SenhaInvalidaException;
import com.hukarz.presley.excessao.SolucaoIniexistenteException;
import com.hukarz.presley.server.core.interfaces.CorePresleyOperations;


/**
 * Esta classe implementa no servidor metodos para rotear
 * 
 * @author Alysson Diniz
 * @version 1.00
 *  
 */
public class ServerBridgeImp implements ServerBridge {



	public ServerBridgeImp() {
		System.out.println("instanciando ServerBridgeImp");
	}

	/**
	 * Recebe o pacote no servidor e o encaminha 
	 * de forma adequada. Implementado da interface ServerBridge
	 * 
	 * @param packet O pacote recebido
	 * @return Packet O pacote resposta para o cliente
	 */
	public PacketStruct sendToServer(PacketStruct packet) {
		PacketStruct pktRetorno = null;
		Object retorno = null;
		ExecuteClientQuery executeClientQuery = new ExecuteClientQuery(); 
		int typeRetorno = CorePresleyOperations.ERRO;

		switch ( packet.getId() ) {
		// Packet tipo 1: adicionaAtividade(TipoAtividade atividade) - ok
		case CorePresleyOperations.ADICIONA_ATIVIDADE:
			try {
				retorno = executeClientQuery.adicionaAtividade(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_ATIVIDADE;
			} catch (EmailInvalidoException e1) {
				retorno = "ERRO: Email inv�lido.";
			} catch (DescricaoInvalidaException e1) {
				retorno = "ERRO: Descri��o inv�lida.";
			} catch (DataInvalidaException e1) {
				retorno = "ERRO: Data inv�lida.";
			} catch (Exception e1) {
				retorno = "ERRO: Opera��o falhou.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 2: removerAtividade(TipoAtividade atividade) -ok
		case CorePresleyOperations.REMOVE_ATIVIDADE:
			try {
				retorno = executeClientQuery.removerAtividade(packet);
				typeRetorno = CorePresleyOperations.REMOVE_ATIVIDADE;
			} catch (AtividadeInexistenteException e2) {
				retorno = "ERRO: Atividade inexistente.";
			} catch (ProblemaInexistenteException e) {
				retorno = "ERRO: Problema inexistente.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 3: BUSCA_ATIVIDADE()			
		case CorePresleyOperations.BUSCA_ATIVIDADE:
			typeRetorno = CorePresleyOperations.BUSCA_ATIVIDADE;
			retorno = executeClientQuery.buscaAtividades();
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 4: ADICIONA_CONHECIMENTO
		case CorePresleyOperations.ADICIONA_CONHECIMENTO:
			try {
				retorno = executeClientQuery.adicionaConhecimento(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_CONHECIMENTO;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descri��o inv�lida.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Opera��o falhou.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 5: LOG_IN
		case CorePresleyOperations.LOG_IN:
			System.out.println((DadosAutenticacao)packet.getData());
			try {
				retorno = executeClientQuery.login(packet);
				typeRetorno = CorePresleyOperations.LOG_IN;
			} catch (DesenvolvedorInexistenteException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Desenvolvedor inexistente.";
			} catch (EmailInvalidoException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Email inv�lido.";
			} catch (SenhaInvalidaException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Senha inv�lida.";
			} catch (ErroDeAutenticacaoException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Erro de autentica��o.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 6: LOG_OUT
		case CorePresleyOperations.LOG_OUT:
			typeRetorno = CorePresleyOperations.LOG_OUT;
			retorno = executeClientQuery.logout(packet);
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 7: ENCERRAR_ATIVIDADE
		case CorePresleyOperations.ENCERRAR_ATIVIDADE:
			typeRetorno = CorePresleyOperations.ENCERRAR_ATIVIDADE;
			try {
				retorno = executeClientQuery.encerrarAtividade(packet);
			} catch (AtividadeInexistenteException e3) {
				e3.printStackTrace();
				retorno = "ERRO: Atividade Inexistente.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 8: ASSOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE:
			try {
				retorno = executeClientQuery.associaConhecimentoAtividade(packet);
				typeRetorno = CorePresleyOperations.ASSOCIAR_CONHECIMENTO_ATIVIDADE;
			} catch (AtividadeInexistenteException e) {
				retorno = "ERRO: Atividade inexistente.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Opera��o falhou.";
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 9: DESSASOCIAR_CONHECIMENTO_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE:
			try {
				retorno = executeClientQuery.desassociaConhecimentoAtividade(packet);
				typeRetorno = CorePresleyOperations.DESSASOCIAR_CONHECIMENTO_ATIVIDADE;
			} catch (ConhecimentoInexistenteException e1) {
				retorno = "ERRO: Conhecimento inexistente.";
				e1.printStackTrace();
			} catch (AtividadeInexistenteException e1) {
				retorno = "ERRO: Atividade inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 10: ASSOCAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.ASSOCIAR_PROBLEMA_ATIVIDADE:
			
			System.out.println(">>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			try {
				retorno = executeClientQuery.associaProblemaAtividade(packet);
				typeRetorno = CorePresleyOperations.ASSOCIAR_PROBLEMA_ATIVIDADE;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descri��o inv�lida.";
				e.printStackTrace();
			} catch (AtividadeInexistenteException e) {
				retorno = "ERRO: Atividade inexistente.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 11: DESSASOCIAR_PROBLEMA_ATIVIDADE
		case CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE:
			try {
				retorno = executeClientQuery.desassociaProblemaAtividade(packet);
				typeRetorno = CorePresleyOperations.DESSASOCIAR_PROBLEMA_ATIVIDADE;
			} catch (ProblemaInexistenteException e1) {
				retorno = "ERRO: Problema inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 12: BUSCA_DESENVOLVEDORES
		case CorePresleyOperations.BUSCA_DESENVOLVEDORES:
			System.out.println("busca desenvolvedores ");
			try {
				retorno = executeClientQuery.buscaDesenvolvedores(packet);
				ArrayList<com.hukarz.presley.beans.Desenvolvedor> des = (ArrayList<Desenvolvedor>)retorno;
				System.out.println("Email do desenvolvedor retornado: "+des.get(0).getEmail());
				typeRetorno = CorePresleyOperations.BUSCA_DESENVOLVEDORES;
			} catch (DesenvolvedorInexistenteException e2) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e2.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 13: QUALIFICA_DESENVOLVEDOR
		case CorePresleyOperations.QUALIFICA_DESENVOLVEDOR:
			try {
				retorno = executeClientQuery.qualificaDesenvolvedor(packet);
				typeRetorno = CorePresleyOperations.QUALIFICA_DESENVOLVEDOR;
			} catch (ConhecimentoInexistenteException e2) {
				retorno = "ERRO: Conhecimento inexistente.";
				e2.printStackTrace();
			} catch (DesenvolvedorInexistenteException e2) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e2.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 14: ENVIAR_MENSAGEM
		case CorePresleyOperations.ENVIAR_MENSAGEM:
			try {
				retorno = executeClientQuery.enviarMensagem(packet);
				typeRetorno = CorePresleyOperations.ENVIAR_MENSAGEM;
			} catch (DesenvolvedorInexistenteException e2) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e2.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 15: GET_LISTA_DESENVOLVEDORES
		case CorePresleyOperations.GET_LISTA_DESENVOLVEDORES:
			retorno = executeClientQuery.getListaDesenvolvedores();
			typeRetorno = CorePresleyOperations.GET_LISTA_DESENVOLVEDORES;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

			// Packet tipo 16: GET_LISTA_CONHECIMENTO
		case CorePresleyOperations.GET_LISTA_CONHECIMENTO:
			retorno = executeClientQuery.getListaConhecimentos();
			typeRetorno = CorePresleyOperations.GET_LISTA_CONHECIMENTO;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

			// Packet tipo 17: GET_ONTOLOGIA
		case CorePresleyOperations.GET_ONTOLOGIA:
			try {
				retorno = executeClientQuery.getOntologia();
				typeRetorno = CorePresleyOperations.GET_ONTOLOGIA;
			} catch (ConhecimentoInexistenteException e1) {
				retorno = "ERRO: Conhecimento inexistente.";
				e1.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 18: ADICIONA_DESENVOLVEDOR
		case CorePresleyOperations.ADICIONA_DESENVOLVEDOR:

			try {
				retorno = executeClientQuery.adicionaDesenvolvedor(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_DESENVOLVEDOR;
			} catch (DesenvolvedorExisteException e) {
				retorno = "ERRO: Desenvolvedor j� Cadastrado no Banco.";
				e.printStackTrace();
			} catch (SenhaInvalidaException e) {
				retorno = "ERRO: Senha Invalida.";
				e.printStackTrace();
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descricao Invalida.";
				e.printStackTrace();
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
				e.printStackTrace();
			} catch (DesenvolvedorInexistenteException e) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;	

			// Packet tipo 19: GET_LISTA_PROBLEMAS
		case CorePresleyOperations.GET_LISTA_PROBLEMAS:
			try {
				retorno = executeClientQuery.getListaProblemas(packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			typeRetorno = CorePresleyOperations.GET_LISTA_PROBLEMAS;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

			// Packet tipo 20: BUSCA_CONHECIMENTOS_RELACIONADOS
		case CorePresleyOperations.BUSCA_CONHECIMENTOS_RELACIONADOS:
			try {
				retorno = executeClientQuery.getListaConhecimentosEnvolvidos(packet);
			} catch (ConhecimentoInexistenteException e) {
				e.printStackTrace();
			}
			typeRetorno = CorePresleyOperations.BUSCA_CONHECIMENTOS_RELACIONADOS;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;
		case CorePresleyOperations.REMOVER_CONHECIMENTO:
			try {
				retorno = executeClientQuery.removerConhecimento(packet);
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
				e.printStackTrace();
			}
			typeRetorno = CorePresleyOperations.REMOVER_CONHECIMENTO;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

		case CorePresleyOperations.CONHECIMENTO_POSSUI_FILHOS:
			try {
				retorno = executeClientQuery.possuiFilhos(packet);
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
				e.printStackTrace();
			}
			typeRetorno = CorePresleyOperations.CONHECIMENTO_POSSUI_FILHOS;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

		case CorePresleyOperations.REMOVER_DESENVOLVEDOR:
			retorno = executeClientQuery.removerDesenvolvedor(packet);
			typeRetorno = CorePresleyOperations.REMOVER_DESENVOLVEDOR;


			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;
			
		case CorePresleyOperations.OBTER_MENSAGENS:
			retorno = executeClientQuery.obterMensagens(packet);
			typeRetorno = CorePresleyOperations.OBTER_MENSAGENS;
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;

		case CorePresleyOperations.BUSCA_CONHECIMENTOS_PROBLEMA:
			retorno = executeClientQuery.buscaConhecimentosProblema(packet);
			typeRetorno = CorePresleyOperations.BUSCA_CONHECIMENTOS_PROBLEMA;
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;
			// Packet tipo 26: ADICIONA_PROBLEMA
		case CorePresleyOperations.ADICIONA_PROBLEMA:
			try {
				retorno = executeClientQuery.adicionaProblema(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_PROBLEMA;
			} catch (DescricaoInvalidaException e1) {
				retorno = "ERRO: Problema Existente.";
				e1.printStackTrace();
			} catch (IOException e1) {
				retorno = "ERRO: Arquivo Inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 27: REMOVER_PROBLEMA
		case CorePresleyOperations.REMOVER_PROBLEMA:
			try {
				retorno     = executeClientQuery.removerProblema(packet);
				typeRetorno = CorePresleyOperations.REMOVER_PROBLEMA;
			} catch ( ProblemaInexistenteException e1) {
				retorno = "ERRO: Problema inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 28: ADICIONA_SOLUCAO
		case CorePresleyOperations.ADICIONA_SOLUCAO:
			try {
				retorno     = executeClientQuery.adicionaSolucao(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_SOLUCAO;
			} catch ( DesenvolvedorInexistenteException e1) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e1.printStackTrace();
			} catch ( ProblemaInexistenteException e1) {
				retorno = "ERRO: Problema inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 29: GET_LISTA_SOLUCOES_PROBLEMA
		case CorePresleyOperations.GET_LISTA_SOLUCOES_PROBLEMA:
			retorno     = executeClientQuery.listarSolucoesDoProblema(packet);
			typeRetorno = CorePresleyOperations.GET_LISTA_SOLUCOES_PROBLEMA;
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 30: ATUALIZAR_STATUS_SOLUCAO
		case CorePresleyOperations.ATUALIZAR_STATUS_SOLUCAO:
			try {
				retorno     = executeClientQuery.atualizarStatusSolucao(packet);
				typeRetorno = CorePresleyOperations.ATUALIZAR_STATUS_SOLUCAO;
			} catch ( SolucaoIniexistenteException e1) {
				retorno = "ERRO: Solucao inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 31: ATUALIZAR_STATUS_PROBLEMA
		case CorePresleyOperations.ATUALIZAR_STATUS_PROBLEMA:
			try {
				retorno     = executeClientQuery.atualizarStatusProblema(packet);
				typeRetorno = CorePresleyOperations.ATUALIZAR_STATUS_PROBLEMA;
			} catch ( ProblemaInexistenteException e1) {
				retorno = "ERRO: Problema inexistente.";
				e1.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 32: ATUALIZAR_SOLUCAO
		case CorePresleyOperations.ATUALIZAR_SOLUCAO:
			try {
				retorno     = executeClientQuery.atualizarSolucao(packet);
				typeRetorno = CorePresleyOperations.ATUALIZAR_SOLUCAO;
			} catch ( ProblemaInexistenteException e1) {
				retorno = "ERRO: Problema inexistente.";
				e1.printStackTrace();
			} catch (DesenvolvedorInexistenteException e) {
				retorno = "ERRO: Desenvolvedor inexistente.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 33: GET_LISTA_SOLUCOES_RETORNADAS
		case CorePresleyOperations.GET_LISTA_SOLUCOES_RETORNADAS:
			try {
				retorno = executeClientQuery.listarSolucoesRetornadasDoDesenvolvedor(packet);
			} catch (DesenvolvedorInexistenteException e) {
				e.printStackTrace();
			}
			typeRetorno = CorePresleyOperations.GET_LISTA_SOLUCOES_RETORNADAS;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;
			// Packet tipo 34: ASSOCIA_ARQUIVOS_CONHECIMENTO
		case CorePresleyOperations.ASSOCIA_ARQUIVO_CONHECIMENTO:
			try {
				retorno     = executeClientQuery.associaArquivo(packet);
				typeRetorno = CorePresleyOperations.ASSOCIA_ARQUIVO_CONHECIMENTO;
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
				e.printStackTrace();
			} catch (IOException e) {
				retorno = "ERRO: Arquivo n�o encontrado.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 35: GET_ONTOLOGIA
		case CorePresleyOperations.GET_PROJETO:
			retorno = executeClientQuery.getProjetoAtivo();
			typeRetorno = CorePresleyOperations.GET_PROJETO;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
		}
		
		return pktRetorno;

	}
}