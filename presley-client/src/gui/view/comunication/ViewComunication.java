package gui.view.comunication;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewComunication {	
	private static ArrayList<String> atividades = new ArrayList<String>();	
	private static HashMap<String,ArrayList<String>> conhecimentos = new HashMap<String,ArrayList<String>>();
	private static HashMap<String,ArrayList<String>> problemas = new HashMap<String,ArrayList<String>>();
	
	public static ArrayList<String> getAtividades()
	{
		return ViewComunication.atividades;
	}
	
	public static ArrayList<String> getConhecimentosEnvolvidos(String atividade)
	{
		return ViewComunication.conhecimentos.get(atividade);
	}
	
	public static ArrayList<String> getProblemas(String atividade)
	{
		return ViewComunication.problemas.get(atividade);
	}
	
	public static void addAtividade(String atividade, ArrayList<String> conhecimentos, ArrayList<String> problemas)
	{
		ViewComunication.atividades.add(atividade);
		ViewComunication.conhecimentos.put(atividade, conhecimentos);
		ViewComunication.problemas.put(atividade, problemas);
	}
	
	public static void teste()
	{
		ArrayList<String> conh = new ArrayList<String>();
		conh.add("JU");
		conh.add("QS");
		
		ArrayList<String> prob = new ArrayList<String>();
		prob.add("comunicao");
		prob.add("web");
		prob.add("RMI");
		
		ViewComunication.addAtividade("Java", conh, prob);
		
		ArrayList<String> conh1 = new ArrayList<String>();
		conh1.add("Ponteiro");
		conh1.add("QS");
		
		ArrayList<String> prob1 = new ArrayList<String>();
		prob1.add("Not a mumber");
		prob1.add("segmentation faul");
		prob1.add("stack error");
		
		ViewComunication.addAtividade("C++", conh1, prob1);
	}
}


