package validacao.implementacao;


import java.sql.Date;
import java.util.ArrayList;
import beans.Problema;
import persistencia.implementacao.ServicoProblemaImplDAO;
import persistencia.interfaces.ServicoProblema;

/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de problemas.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoProblemaImpl {
	
	ServicoProblema servicoProblema;
	
	public ValidacaoProblemaImpl() {
		servicoProblema = new ServicoProblemaImplDAO();
	}

	public boolean atualizarStatusDoProblema(int id, boolean status) {
		
		return servicoProblema.atualizarStatusDoProblema(id, status);
	}

	public boolean cadastrarProblema(int idAtividade, String descricao,
			Date dataDoRelato, String mensagem) {

		return servicoProblema.cadastrarProblema(idAtividade, descricao, dataDoRelato, mensagem);
	}

	public Problema getProblema(int id) {

		return servicoProblema.getProblema(id);
	}

	public ArrayList<Problema> listarProblemasDaAtividade(int idAtividade) {

		return servicoProblema.listarProblemasDaAtividade(idAtividade);
	}

	public boolean problemaExiste(int id) {

		return servicoProblema.problemaExiste(id);
	}

	public boolean removerProblema(int id) {

		return servicoProblema.removerProblema(id);
	}

}
