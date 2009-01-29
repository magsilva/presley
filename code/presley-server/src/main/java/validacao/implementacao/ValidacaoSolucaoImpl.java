package validacao.implementacao;


import java.util.ArrayList;


import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;
import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.DesenvolvedorInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.excessao.SolucaoIniexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;


/**
 * 
 * @author RodrigoCMD
 * Essa classe contem metodos para a administracao e validao de solues.
 * 
 * ltima modificacao: 09/09/2008 por RodrigoCMD
 */

public class ValidacaoSolucaoImpl {
	
	ServicoSolucao servicoSolucao;
	ServicoProblema servicoProblema;
	ServicoDesenvolvedor servicoDesenvolvedor;
	
	public ValidacaoSolucaoImpl() {
		servicoSolucao = new ServicoSolucaoImplDAO();
		servicoProblema = new ServicoProblemaImplDAO();
		servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	}
	
	/**
	 * Esse m�todo atualiza a situacao de uma solu��o, ou seja, se ela foi �til ou
	 * n�o para a resolucao de um problema.
	 * @param id Identificador da solu��o.
	 * @param status Situacao da solu��o.
	 * @return true se a atualizacao foi realizada com sucesso.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean atualizarStatusDaSolucao(int id, boolean status) throws SolucaoIniexistenteException {
		
		if (!servicoSolucao.solucaoExiste(id)) throw new SolucaoIniexistenteException();
		
		return servicoSolucao.atualizarStatusDaSolucao(id, status);
	}
	
	/**
	 * Esse m�todo atualiza os dados uma solu��o
	 * @param solucao Solu��o a ser atualizada.
	 * @return true se a atualizacao foi realizada com sucesso.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean atualizarSolucao(Solucao solucao) throws SolucaoIniexistenteException {
		if (!servicoSolucao.solucaoExiste(solucao.getId())) throw new SolucaoIniexistenteException();
		
		return servicoSolucao.atualizarSolucao(solucao);
	}
	
	/**
	 * Esse m�todo cadastra uma nova solu��o proposta por um desenvolvedor para
	 * um problema.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @param idProblema Identificador do problema
	 * @param dataDaProposta Data em que a solucao foi proposta.
	 * @param mensagem Mensagem da solu��o sugerida.
	 * @return true se a solucao foi cadastrada na base de dados.
	 * @throws ProblemaInexistenteException 
	 */
	public Solucao cadastrarSolucao(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException {
		
		if (!servicoProblema.problemaExiste( solucao.getProblema().getId() )) 
			throw new ProblemaInexistenteException();

		if (!servicoDesenvolvedor.desenvolvedorExiste( solucao.getDesenvolvedor().getEmail() )) 
			throw new DesenvolvedorInexistenteException();
		
		return servicoSolucao.cadastrarSolucao( solucao );
	}
	
	/**
	 * Esse m�todo atualiza uma solu��o com os dados fornecidos no paramentro.
	 * @param solucao Solucao a ser atulizada 
	 * @return true se a solucao foi cadastrada na base de dados.
	 * @throws ProblemaInexistenteException, DesenvolvedorInexistenteException 
	 */
	public boolean atualizarStatusDoProblema(Solucao solucao) throws ProblemaInexistenteException, DesenvolvedorInexistenteException {
		
		if (!servicoProblema.problemaExiste( solucao.getProblema().getId() )) 
			throw new ProblemaInexistenteException();

		if (!servicoDesenvolvedor.desenvolvedorExiste( solucao.getDesenvolvedor().getEmail() )) 
			throw new DesenvolvedorInexistenteException();
		
		return servicoSolucao.atualizarSolucao( solucao );
	}
	
	/**
	 * Esse m�todo retorna um objeto do tipo Solucao que possui a id passada
	 * no parametro.
	 * @param id Identificador da solucao
	 * @return <Solucao>
	 * @throws SolucaoIniexistenteException 
	 */
	public Solucao getSolucao(int id) throws SolucaoIniexistenteException {
		
		Solucao solucao = servicoSolucao.getSolucao(id);
		
		if (solucao == null) {
			throw new SolucaoIniexistenteException();
		}
		
		return solucao;
	}
	
	/**
	 * Esse m�todo retorna uma lista de solu��es que foram aceitas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 */
	public ArrayList<Solucao> listarSolucoesAceitasDoDesenvolvedor(
			String emailDesenvolvedor) throws DescricaoInvalidaException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new DescricaoInvalidaException();

		return servicoSolucao.listarSolucoesAceitasDoDesenvolvedor(emailDesenvolvedor);
	}
	
	/**
	 * Esse m�todo retorna uma lista de solu��es propostas por um desenvolvedor para uma
	 * s�rie de problemas.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 */
	public ArrayList<Solucao> listarSolucoesDoDesenvolvedor(
			String emailDesenvolvedor) throws DescricaoInvalidaException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new DescricaoInvalidaException();

		return servicoSolucao.listarSolucoesDoDesenvolvedor(emailDesenvolvedor);
	}
	
	/**
	 * Esse m�todo retorna uma lista de solu��es que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 */
	public ArrayList<Solucao> listarSolucoesRejeitadasDoDesenvolvedor(
			String emailDesenvolvedor) throws DescricaoInvalidaException {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(emailDesenvolvedor)) throw new DescricaoInvalidaException();

		return servicoSolucao.listarSolucoesRejeitadasDoDesenvolvedor(emailDesenvolvedor);
	}
	
	/**
	 * Esse m�todo remove uma solu��o proposta por um desenvolvedor da base de dados.
	 * @param id Identificador da solu��o.
	 * @return true se a solu��o foi removida com sucesso.
	 * @throws SolucaoIniexistenteException 
	 */
	public boolean removerSolucao(int id) throws SolucaoIniexistenteException {
		
		if (!servicoSolucao.solucaoExiste(id)) throw new SolucaoIniexistenteException();
		
		return servicoSolucao.removerSolucao(id);
	}
	
	/**
	 * Esse m�todo verifica se existe uma solucao com tal identificador.
	 * @param id Identificador da solucao.
	 * @return true se a solucao existe.
	 */
	public boolean solucaoExiste(int id) {

		return servicoSolucao.solucaoExiste(id);
	}

	/**
	 * Esse m�todo retorna uma lista de solu��es que foram rejeitadas de um desenvolvedor
	 * para uma todos os problemas cadastrados no banco.
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DescricaoInvalidaException 
	 */
	public ArrayList<Solucao> listarSolucoesDoProblema(
			Problema problema) throws ProblemaInexistenteException {
		
		if (!servicoProblema.problemaExiste( problema.getId() )) throw new ProblemaInexistenteException();

		return servicoSolucao.getSolucoesDoProblema(problema);
	}
	

	/**
	 * Esse m�todo retorna uma lista de solu��es que foram retornadas de um desenvolvedor
	 * @param emailDesenvolvedor Email do desenvolvedor.
	 * @return ArrayList<Solucao>
	 * @throws DesenvolvedorInexistenteException 
	 */
	public ArrayList<Solucao> listarSolucoesRetornadasDoDesenvolvedor(
			Desenvolvedor desenvolvedor) throws DesenvolvedorInexistenteException  {
		
		if (!servicoDesenvolvedor.desenvolvedorExiste(desenvolvedor.getEmail())) throw new DesenvolvedorInexistenteException();

		return servicoSolucao.listarSolucoesRetornadasDoDesenvolvedor(desenvolvedor);
	}
	
}
