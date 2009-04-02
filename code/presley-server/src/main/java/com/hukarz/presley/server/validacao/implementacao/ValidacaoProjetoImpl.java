package com.hukarz.presley.server.validacao.implementacao;

import java.util.ArrayList;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;

public class ValidacaoProjetoImpl {

	ServicoProjeto servicoProjeto; 

	public ValidacaoProjetoImpl() {
		servicoProjeto = new ServicoProjetoImplDAO() ;
	}

	public ArrayList<Projeto> getProjetosAtivo() {
		return servicoProjeto.getProjetosAtivo() ;
	}
	
	public boolean criarProjeto(Projeto projeto) throws NomeInvalidoException {
		if (servicoProjeto.projetoExiste(projeto)) throw new NomeInvalidoException();
			
		return servicoProjeto.criarProjeto(projeto) ;
	}
	
	public boolean removerProjeto(Projeto projeto) throws ProjetoInexistenteException {
		if (!servicoProjeto.projetoExiste(projeto)) throw new ProjetoInexistenteException();
		
		return servicoProjeto.removerProjeto(projeto);
	}
	
	public boolean atualizarStatusProjeto(Projeto projeto) throws ProjetoInexistenteException {
		if (!servicoProjeto.projetoExiste(projeto)) throw new ProjetoInexistenteException();
		
		return servicoProjeto.atualizarStatusProjeto(projeto);
	}
	
	public ArrayList<Projeto> getProjetos() {
		return servicoProjeto.getProjetos();
	}
}
