package validacao.implementacao;

import java.util.ArrayList;

import beans.Desenvolvedor;
import beans.Mensagem;
import beans.Problema;

public class ValidacaoMensagemImpl {

	



		/**
		 * Este método adiciona uma mensagem enviada por um desenvolvedor a outros desenvolvedores que possivelmente irão resolver solucionar o problema.
		 * @param desenvolvedorOrigem Desenvolvedor que possui um problema e envia a mensagem.
		 * @param desenvolvedoresDestino Para quem as mensagens serão armazenadas.
		 * @param problema Problema em questão.
		 * @param texto Texto que caracteriza a mensagem.
		 * @return true caso a mensagem seja armazenada com sucesso no banco de dados.
		 */
		public boolean adicionarMensagem(Desenvolvedor desenvolvedorOrigem, ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema, String texto) {
			return false;
		}
		
		/**
		 * Este método retorna todas as mensagens armazenadas para um determinado desenvolvedor.
		 * @param desenvolvedorDestino Desenvolvedor que irá receber as mensagens.
		 * @return Coleção com todas as mensagens referentes ao desenvolvedorDestino.
		 */
		public Mensagem[] getMensagens(Desenvolvedor desenvolvedorDestino) {
			return null;
		}
		
}
