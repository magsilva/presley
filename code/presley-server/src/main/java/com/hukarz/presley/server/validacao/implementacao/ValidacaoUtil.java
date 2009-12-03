package com.hukarz.presley.server.validacao.implementacao;

import java.sql.Date;


public class ValidacaoUtil {
	
	public static boolean validaNome(String nome) {		
		return true;
	}
	
	public static boolean validaDescricao(String descricao) {
		if(descricao == null)
			return false;
		return true;
	}
	
	public static boolean validaEmail(String email) {

		if(email == null){
			return false;
		}
		if(email.length() == 0){
			return false;
		}
		if (email.indexOf("@") == -1) {
			return false;
		}
		if (email.indexOf(".") == -1) {
			return false;
		}
		return true;
	}
	
	public static boolean verificaOrdemDatas(Date dataInicio, Date dataFim) {
		
		if(dataInicio == null || dataFim == null)
			return false;
		
		long timeInicio = dataInicio.getTime();
		long timeFim = dataFim.getTime();
		
		if (timeInicio <= timeFim) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validaSenha(String senha) {
		if (senha.length() < 6) { 
			return false;
		}
		else { 
			return true;
		}
	}
	
}
