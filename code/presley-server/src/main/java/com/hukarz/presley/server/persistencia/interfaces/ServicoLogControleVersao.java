package com.hukarz.presley.server.persistencia.interfaces;

import java.util.Date;
import java.util.ArrayList;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;

public interface ServicoLogControleVersao {

	public void registrarLogDeRevisao(Desenvolvedor desenvolvedor,
			ArrayList<ArquivoJava> arquivosAcessados, Date data);
	
	public Date getDataHoraUltimoRegistro();
	
	public ArrayList<Desenvolvedor> getDesenvolvedoresArquivo(ArquivoJava arquivoJava, Date data);
}
