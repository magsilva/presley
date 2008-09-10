package gui.view.comunication;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewComunication {	
	private ArrayList<String> atividades = new ArrayList<String>();	
	private HashMap<String,ArrayList<String>> conhecimentos = new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> problemas = new HashMap<String,ArrayList<String>>();
	
	public ArrayList<String> getAtividades()
	{
		return this.atividades;
	}
	
	public ArrayList<String> getConhecimentosEnvolvidos(String atividade)
	{
		return this.conhecimentos.get(atividade);
	}
	
	public ArrayList<String> getProblemas(String atividade)
	{
		return this.problemas.get(atividade);
	}
	
	/**
	 * @param atividade
	 * @param conhecimentos
	 * @param problemas
	 */
	public void addAtividade(String atividade, ArrayList<String> conhecimentos, ArrayList<String> problemas)
	{
		this.atividades.add(atividade);
		this.conhecimentos.put(atividade, conhecimentos);
		this.problemas.put(atividade, problemas);
	}
	
	public void teste()
	{
		ArrayList<String> conh = new ArrayList<String>();
		conh.add("JU");
		conh.add("QS");
		
		ArrayList<String> prob = new ArrayList<String>();
		prob.add("comunicao");
		prob.add("web");
		prob.add("RMI");
		
		this.addAtividade("Java", conh, prob);
		
		ArrayList<String> conh1 = new ArrayList<String>();
		conh1.add("Ponteiro");
		conh1.add("QS");
		
		ArrayList<String> prob1 = new ArrayList<String>();
		prob1.add("Not a mumber");
		prob1.add("segmentation faul");
		prob1.add("stack error");
		
		this.addAtividade("C++", conh1, prob1);
	}
}


