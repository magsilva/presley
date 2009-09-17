package com.hukarz.presley.server.util;

import java.util.ArrayList;

import com.hukarz.presley.beans.Projeto;
import com.hukarz.presley.excessao.NomeInvalidoException;
import com.hukarz.presley.excessao.ProjetoInexistenteException;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProjetoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProjeto;

public class CadastroProjeto {

	ServicoProjeto servicoProjeto; 
	Projeto projeto;
	
	public CadastroProjeto() {
		servicoProjeto = new ServicoProjetoImplDAO() ;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Projeto getProjetoAtivo() {
		projeto = servicoProjeto.getProjetoAtivo() ; 
		return projeto;
	}
	
	public boolean criarProjeto() throws NomeInvalidoException, ProjetoInexistenteException {
		if (projeto == null) throw new ProjetoInexistenteException();
		if (servicoProjeto.projetoExiste(projeto)) throw new NomeInvalidoException();
			
		return servicoProjeto.criarProjeto(projeto) ;
	}
	
	public boolean removerProjeto() throws ProjetoInexistenteException {
		if (projeto == null) throw new ProjetoInexistenteException();
		if (!servicoProjeto.projetoExiste(projeto)) throw new ProjetoInexistenteException();
		
		return servicoProjeto.removerProjeto(projeto);
	}
	
	public boolean atualizarStatusProjeto() throws ProjetoInexistenteException {
		if (projeto == null) throw new ProjetoInexistenteException();
		if (!servicoProjeto.projetoExiste(projeto)) throw new ProjetoInexistenteException();
		
		return servicoProjeto.atualizarStatusProjeto(projeto);
	}
	
	public ArrayList<Projeto> getProjetos() {
		return servicoProjeto.getProjetos();
	}
}
