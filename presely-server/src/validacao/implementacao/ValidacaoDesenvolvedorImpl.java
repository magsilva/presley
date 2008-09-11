package validacao.implementacao;


import java.util.ArrayList;

import beans.Atividade;
import beans.Conhecimento;
import beans.Desenvolvedor;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;
import validacao.interfaces.ValidacaoDesenvolvedor;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de desenvolvedores.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoDesenvolvedorImpl implements ValidacaoDesenvolvedor{
	
	ServicoConhecimento servicoConhecimento;
	ServicoAtividade servicoAtividade;
	ServicoDesenvolvedor servicoDesenvolvedor;
	
	public ValidacaoDesenvolvedorImpl() {
		servicoAtividade = new ServicoAtividadeImplDAO();
		servicoConhecimento = new ServicoConhecimentoImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}
	
	public boolean adicionarConhecimentoAoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new Exception();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new Exception();
		
		return servicoDesenvolvedor.adicionarConhecimentoAoDesenvolvedor(
				emailDesenvolvedor, nomeConhecimento);
	}

	public boolean atualizarDesenvolvedor(String email, String novoEmail,
			String nome, String localidade) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.atualizarDesenvolvedor(email, novoEmail, nome, localidade);
	}

	public boolean conhecimentoDoDesenvolvedorExiste(String emailDesenvolvedor,
			String nomeConhecimento) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new Exception();
		if (!servicoConhecimento.conhecimentoExiste(nomeConhecimento)) throw new Exception();
		
		return servicoDesenvolvedor.conhecimentoDoDesenvolvedorExiste(emailDesenvolvedor, nomeConhecimento);
	}

	public boolean criarDesenvolvedor(String email, String nome,
			String localidade) throws Exception {
		
		if (servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.criarDesenvolvedor(email, nome, localidade);
	}

	public boolean desenvolvedorExiste(String email) {
		
		return servicoDesenvolvedor.desenvolvedorExiste(email);
	}

	public ArrayList<Atividade> getAtividadesDoDesenvolvedor(String email) throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.getAtividadesDoDesenvolvedor(email);
	}

	public ArrayList<Conhecimento> getConhecimentosDoDesenvolvedor(String email) 
			throws Exception {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(email)) throw new Exception();
		
		return servicoDesenvolvedor.getConhecimentosDoDesenvolvedor(email);
	}

	public Desenvolvedor getDesenvolvedor(String email) throws Exception {
		
		Desenvolvedor desenvolvedor = servicoDesenvolvedor.getDesenvolvedor(email);
		if (desenvolvedor == null) throw new Exception();
		
		return desenvolvedor;
	}

	public boolean removerConhecimentoDoDesenvolvedor(
			String emailDesenvolvedor, String nomeConhecimento) {
		
		return servicoDesenvolvedor.removerConhecimentoDoDesenvolvedor(emailDesenvolvedor, nomeConhecimento);
	}

	public boolean removerDesenvolvedor(String email) {
		
		return servicoDesenvolvedor.removerDesenvolvedor(email);
	}

}
