package testes.atividade;

import java.sql.Date;

import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.interfaces.ServicoAtividade;
import validacao.implementacao.ValidacaoAtividadeImpl;
import beans.TipoAtividade;
import core.ExecuteClientQuery;
import excessao.AtividadeInexistenteException;
import excessao.ProblemaInexistenteException;

public class TesteRemocao {
	
	public static void main(String args[]) {
		ExecuteClientQuery execute = new ExecuteClientQuery();
		
		ServicoAtividade servicoAtividade = new ServicoAtividadeImplDAO();
		servicoAtividade.cadastrarAtividade("a@a.a", 
				"a@a.a", 
				"PRESLEY1", 
				new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()+ 1000));

		TipoAtividade atividadeExistente = servicoAtividade.getAtividade(8);
		System.out.println("id atividade  = " + atividadeExistente.getId());
		
		try {
			ValidacaoAtividadeImpl validacao = new ValidacaoAtividadeImpl();
			validacao.removerAtividade(9);
		} catch (AtividadeInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProblemaInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
