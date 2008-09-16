package validacao.implementacao;

import java.util.ArrayList;

import beans.Desenvolvedor;
import beans.Mensagem;
import beans.Problema;

public class ValidacaoMensagemImpl {

	



		/**
		 * Este m�todo adiciona uma mensagem enviada por um desenvolvedor a outros desenvolvedores que possivelmente ir�o resolver solucionar o problema.
		 * @param desenvolvedorOrigem Desenvolvedor que possui um problema e envia a mensagem.
		 * @param desenvolvedoresDestino Para quem as mensagens ser�o armazenadas.
		 * @param problema Problema em quest�o.
		 * @param texto Texto que caracteriza a mensagem.
		 * @return true caso a mensagem seja armazenada com sucesso no banco de dados.
		 */
		public boolean adicionarMensagem(Desenvolvedor desenvolvedorOrigem, ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema, String texto) {
			return false;
		}
		
		/**
		 * Este m�todo retorna todas as mensagens armazenadas para um determinado desenvolvedor.
		 * @param desenvolvedorDestino Desenvolvedor que ir� receber as mensagens.
		 * @return Cole��o com todas as mensagens referentes ao desenvolvedorDestino.
		 */
		public Mensagem[] getMensagens(Desenvolvedor desenvolvedorDestino) {
			return null;
		}
		
}
