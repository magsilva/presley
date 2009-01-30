package atividade.test;

import java.sql.Date;


import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.excessao.AtividadeInexistenteException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.server.core.ExecuteClientQuery;
import com.hukarz.presley.server.persistencia.implementacao.ServicoAtividadeImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoAtividade;
import com.hukarz.presley.server.validacao.implementacao.ValidacaoAtividadeImpl;


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
