/*
 * Ontologia.java
 *
 * Created on 18 de Agosto de 2008, 15:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ontologia;

import java.util.LinkedList;

/**
 *
 * @author leandro
 */
public class Ontologia {
    
    boolean [][] DAG; //DIRECT ACYCLIC GRAPH
    LinkedList<Integer> pilha;
    
    /** Creates a new instance of Ontologia */
    public Ontologia(boolean [][] DAG) 
    {
        this.DAG = DAG;
        pilha = new LinkedList<Integer>();
    }
    
    public void find_closed_paths(int start, int end, LinkedList<LinkedList<Integer>> list)
    {
	LinkedList<Integer> aux = null;	
	
	if(pilha.contains(new Integer(start)))
		return;
	
	pilha.add(new Integer(start));
	
	if(start == end)
	{
		aux = new LinkedList<Integer>();
		for(int i = 0; i < pilha.size(); i++)
			aux.add(pilha.get(i));
		
		list.add(aux);
		pilha.remove(pilha.indexOf(new Integer(start)));
		return;
	}	
	
	aux = this.getSons(start);
	
	for(int i=0; i < aux.size(); i++)
		find_closed_paths( aux.get(i).intValue(), end, list);

        this.pilha.remove(new Integer(start));
    }
    
    private LinkedList<Integer> getSons(int node_id)
    {
        LinkedList<Integer> ret = new LinkedList<Integer>();

        for(int i = 0; i < DAG.length; i++)
                if(DAG[node_id][i])
                        ret.add(new Integer(i));
        return ret;
    }
    
    /*
     *@param start O no raiz da ontologia 
     *@param end O no folha selecionado 
     *@param counts Os contadores de um determinado usuario
     *@return O valor do nivel do usuario sobre determinado conhecimento
     */
    public int getScore(int[] counts, LinkedList<LinkedList<Integer>> caminhos)
    {
        int ret = 0;
        int aux = 1;
        
        for(int i=0; i < caminhos.size(); i++)
        {
            for(int j=0; j < caminhos.get(i).size(); j++)
                aux *= counts[caminhos.get(i).get(j).intValue()];
            ret += aux;
        }
  
        return ret;    
    }
}
