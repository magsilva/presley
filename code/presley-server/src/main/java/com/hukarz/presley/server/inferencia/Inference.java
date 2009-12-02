package com.hukarz.presley.server.inferencia;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.Problema;

/**
 * @author Alan Kelon Oliveira de Moraes <alan@di.ufpb.br>
 *
 */
public interface Inference {

	public abstract ArrayList<Desenvolvedor> getDesenvolvedores(
			Map<ArquivoJava, ArrayList<Desenvolvedor>> arquivoDesenvolvedores,
			Problema problema) throws FileNotFoundException;

}