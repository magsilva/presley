package validacao.implementacao;

import java.util.ArrayList;

import persistencia.implementacao.ServicoMensagemImplDAO;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Mensagem;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;



public class ValidacaoMensagemImpl {

	ServicoMensagemImplDAO servicoMensagem;
	ValidacaoDesenvolvedorImpl validacaoDesenvolvedor;

	public ValidacaoMensagemImpl() {

		servicoMensagem = new ServicoMensagemImplDAO();
		validacaoDesenvolvedor = new ValidacaoDesenvolvedorImpl();

	}

	/**
	 * Este m�todo adiciona uma mensagem enviada por um desenvolvedor a outros desenvolvedores que possivelmente ir�o resolver solucionar o problema.
	 * @param desenvolvedorOrigem Desenvolvedor que possui um problema e envia a mensagem.
	 * @param desenvolvedoresDestino Para quem as mensagens ser�o armazenadas.
	 * @param problema Problema em quest�o.
	 * @param texto Texto que caracteriza a mensagem.
	 * @return true caso a mensagem seja armazenada com sucesso no banco de dados.
	 * @throws DesenvolvedorInexistenteException 
	 */
	public boolean adicionarMensagem(Desenvolvedor desenvolvedorOrigem, ArrayList<Desenvolvedor> desenvolvedoresDestino, Problema problema, String texto) throws DesenvolvedorInexistenteException {

		if(!validacaoDesenvolvedor.desenvolvedorExiste(desenvolvedorOrigem.getEmail())) throw new DesenvolvedorInexistenteException();
		for(Desenvolvedor d : desenvolvedoresDestino) {
			if(!validacaoDesenvolvedor.desenvolvedorExiste(d.getEmail())) throw new DesenvolvedorInexistenteException();
		}
		
		return servicoMensagem.adicionarMensagem(desenvolvedorOrigem, desenvolvedoresDestino, problema, texto);
	}

	/**
	 * Este m�todo retorna todas as mensagens armazenadas para um determinado desenvolvedor.
	 * @param desenvolvedorDestino Desenvolvedor que ir� receber as mensagens.
	 * @return Cole��o com todas as mensagens referentes ao desenvolvedorDestino.
	 */
	public ArrayList<Mensagem> getMensagens(String emailDesenvolvedorDestino) {
		
		
		return servicoMensagem.getMensagens(emailDesenvolvedorDestino);
	}

}
