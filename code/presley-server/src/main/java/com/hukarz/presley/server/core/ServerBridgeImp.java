package com.hukarz.presley.server.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.hukarz.presley.beans.DadosAutenticacao;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.communication.facade.PacketStruct;
import com.hukarz.presley.communication.server.ServerBridge;
import com.hukarz.presley.excessao.ConhecimentoInexistenteException;
import com.hukarz.presley.excessao.ConhecimentoNaoEncontradoException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorExisteException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ErroDeAutenticacaoException;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
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

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public ServerBridgeImp() {
		this.logger.info("instanciando ServerBridgeImp");
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
		// Packet tipo 4: ADICIONA_CONHECIMENTO
		case CorePresleyOperations.ADICIONA_CONHECIMENTO:
			try {
				retorno = executeClientQuery.adicionaConhecimento(packet);
				typeRetorno = CorePresleyOperations.ADICIONA_CONHECIMENTO;
			} catch (DescricaoInvalidaException e) {
				retorno = "ERRO: Descrição inválida.";
			} catch (ConhecimentoInexistenteException e) {
				retorno = "ERRO: Conhecimento inexistente.";
			} catch (Exception e) {
				retorno = "ERRO: Operação falhou.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 5: LOG_IN
		case CorePresleyOperations.LOG_IN:
			try {
				retorno = executeClientQuery.login(packet);
				typeRetorno = CorePresleyOperations.LOG_IN;
			} catch (DesenvolvedorInexistenteException e2) {
				//e2.printStackTrace();
				retorno = "ERRO: Desenvolvedor inexistente.";
			} catch (EmailInvalidoException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Email inválido.";
			} catch (SenhaInvalidaException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Senha inválida.";
			} catch (ErroDeAutenticacaoException e2) {
				e2.printStackTrace();
				retorno = "ERRO: Erro de autenticação.";
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				

			// Packet tipo 6: LOG_OUT
		case CorePresleyOperations.LOG_OUT:
			typeRetorno = CorePresleyOperations.LOG_OUT;
			retorno = executeClientQuery.logout(packet);
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
		case CorePresleyOperations.GET_ARVORECONHECIMENTOS:
			try {
				retorno = executeClientQuery.getArvoreConhecimentos();
				typeRetorno = CorePresleyOperations.GET_ARVORECONHECIMENTOS;
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
				retorno = "ERRO: Desenvolvedor já Cadastrado no Banco.";
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
			} catch (ConhecimentoNaoEncontradoException e){
				retorno = "ERRO: Conhecimento não encontrado." +
						" Por favor detalhar melhor o problema.";
				e.printStackTrace();
			} catch (DescricaoInvalidaException e1) {
				retorno = "ERRO: Problema Existente.";
				e1.printStackTrace();
			} catch (ProjetoInexistenteException e) {
				retorno = "ERRO: Projeto Inexistente.";
				e.printStackTrace();
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
				retorno = "ERRO: Arquivo não encontrado.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 35: GET_PROJETOATIVO
		case CorePresleyOperations.GET_PROJETOS_ATIVO:
			retorno = executeClientQuery.getProjetoAtivo();
			typeRetorno = CorePresleyOperations.GET_PROJETOS_ATIVO;

			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 36: CRIAR_PROJETO
		case CorePresleyOperations.CRIAR_PROJETO:
			try {
				retorno     = executeClientQuery.criarProjeto(packet);
				typeRetorno = CorePresleyOperations.CRIAR_PROJETO;
			} catch (NomeInvalidoException e) {
				retorno = "ERRO: Projeto já Cadastrado.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 37: REMOVER_PROJETO
		case CorePresleyOperations.REMOVER_PROJETO:
			try {
				retorno     = executeClientQuery.removerProjeto(packet);
				typeRetorno = CorePresleyOperations.REMOVER_PROJETO;
			} catch (ProjetoInexistenteException e) {
				retorno = "ERRO: Projeto Inexistente.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 38: ATUALIZAR_STATUS_PROJETO
		case CorePresleyOperations.ATUALIZAR_STATUS_PROJETO:
			try {
				retorno     = executeClientQuery.atualizarStatusProjeto(packet);
				typeRetorno = CorePresleyOperations.ATUALIZAR_STATUS_PROJETO;
			} catch (ProjetoInexistenteException e) {
				retorno = "ERRO: Projeto Inexistente.";
				e.printStackTrace();
			}
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
			// Packet tipo 39: GET_PROJETOS
		case CorePresleyOperations.GET_PROJETOS:
			retorno     = executeClientQuery.getListaProjetos(packet);
			typeRetorno = CorePresleyOperations.GET_PROJETOS;
			pktRetorno = new PacketStruct(retorno, typeRetorno);
			break;				
		}
		
		return pktRetorno;

	}
}