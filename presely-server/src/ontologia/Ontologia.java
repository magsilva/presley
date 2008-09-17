package ontologia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import persistencia.MySQLConnectionFactory;
import validacao.excessao.ConhecimentoInexistenteException;
import validacao.excessao.DesenvolvedorInexistenteException;
import validacao.implementacao.ValidacaoConhecimentoImpl;
import validacao.implementacao.ValidacaoDesenvolvedorImpl;

import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.Problema;
import beans.Tree;

/**
 * Esta classe relaciona uma detrminada Ontologia,
 * com o conhecimento que um usuário possui em diversas
 * ahreas. Quantifica o conhecimento do usuahrio
 * para uma determanda area. Esta quantificacao eh
 * dada pela chamada do mehtodo getScore().
 * 
 * @author Leandro Carlos de Souza
 * @version 1.01
 *  
 */
public class Ontologia {
    
    boolean [][] DAG; /** DIRECT ACYCLIC GRAPH. */
    int [][] usersCounts; /** Contadores dos conhecimentos dos usuarios. */
    ArrayList<Integer> stack; /** Pilha de trabalho. */
    static ValidacaoDesenvolvedorImpl validacaoDesenvolvedor;
    static ValidacaoConhecimentoImpl validacaoConhecimento;
    
    /** 
     * Cria uma  nova instancia de ontologia
     * 
     * @param DAG o DIRECT ACYCLIC GRAPH associado (representando a ontologia).
     * @param usersCounts Contadores para os conhecimentos dos usuarios.
     * 
     */
    public Ontologia(boolean [][] DAG, int [][] usersCounts) 
    {
        this.DAG = DAG;
        this.usersCounts = usersCounts;
        stack = new ArrayList<Integer>();
        validacaoDesenvolvedor = new ValidacaoDesenvolvedorImpl();
        validacaoConhecimento = new ValidacaoConhecimentoImpl();
    }
    
    /**
     * Este metodo encontra todos os caminhos entre o no
     * start e o no end.
     * 
     * @param start O no de inicio (deve ser o pai)
     * @param end O no de trmino (deve ser um noh folha)
     * @param list Lista auxiliar SEMPRE deve ser igual a null.
     * @return ArrayList<ArrayList<Integer>> Uma lista de listas correspondente aos caminhos que ligam start e end na estrutura da ontologia.
     */
    public ArrayList<ArrayList<Integer>> find_closed_paths(int start, int end, ArrayList<ArrayList<Integer>> list)
    {
    	if(list == null)
    	{
    		ArrayList<ArrayList<Integer>> l = new ArrayList<ArrayList<Integer>>();
    		find_closed_paths(start, end, l);
    		return l;
    	}
    	
        ArrayList<Integer> aux = null; 
        
        if(stack.contains(new Integer(start)))
                return null;
        
        stack.add(new Integer(start));
        
        if(start == end)
        {
                aux = new ArrayList<Integer>();
                for(int i = 0; i < stack.size(); i++)
                        aux.add(stack.get(i));
                
                list.add(aux);
                stack.remove(stack.indexOf(new Integer(start)));
                return null;
        }       
        
        aux = this.getFirstSons(start);
        
        for(int i=0; i < aux.size(); i++)
                find_closed_paths( aux.get(i).intValue(), end, list);

        this.stack.remove(new Integer(start));
        
        return null;
    }
    
    /**
     * Este metodo retorna todos os filhos que estejam 
     * imediatamente ligados a um no.
     *  
     * @param node_id O identificador do no cujos filhos se deseja obter.
     * @return ArrayList<Integer> Uma lista com os identificadores dos filhos.
     */
    public ArrayList<Integer> getFirstSons(int node_id)
    {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for(int i = 0; i < DAG.length; i++)
                if(DAG[node_id][i])
                        ret.add(new Integer(i));
        return ret;
    }
    
    /**
     * Este metodo retorna todos os filhos ligados a um no.
     *  
     * @param node_id O identificador do no cujos filhos se deseja obter.
     * @param list Uma lista auxiliar. SEMPRE deve ter valor null, na chamada do metodo.
     * @return ArrayList<Integer> Uma lista com os identificadores de todos filhos.
     */
    public ArrayList<Integer> getAllSons(int node_id, ArrayList<Integer> list)
    {
    	if(list == null)
    	{
    		ArrayList<Integer> ret = new ArrayList<Integer>();
    		
    		ArrayList<Integer> aux = getFirstSons(node_id);
     
            for(int i=0; i < aux.size(); i++)
            	getAllSons(aux.get(i).intValue(), ret);
            
    		return ret;
    	}
    	
    	list.add(new Integer(node_id));
    	
    	ArrayList<Integer> aux = getFirstSons(node_id);
        
        for(int i=0; i < aux.size(); i++)
        	getAllSons(aux.get(i).intValue(), list);
        
        return null;
    }
    
    /**
     * Este metodo retorna todos os pais ligados a um no.
     *  
     * @param node_id O identificador do no cujos pais se deseja obter.
     * @param list Uma lista auxiliar. SEMPRE deve ter valor null, na chamada do metodo.
     * @return ArrayList<Integer> Uma lista com os identificadores de todos filhos.
     */
    public ArrayList<Integer> getAllParents(int node_id, ArrayList<Integer> list)
    {
    	if(list == null)
    	{
    		ArrayList<Integer> ret = new ArrayList<Integer>();
    		
    		ArrayList<Integer> aux = getFirstParents(node_id);
     
            for(int i=0; i < aux.size(); i++)
            	getAllSons(aux.get(i).intValue(), ret);
            
    		return ret;
    	}
    	
    	list.add(new Integer(node_id));
    	
    	ArrayList<Integer> aux = getFirstParents(node_id);
        
        for(int i=0; i < aux.size(); i++)
        	getAllSons(aux.get(i).intValue(), list);
        
        return null;
    }
    
    /**
     * Este metodo retorna o pior contador 
     * existente para um determinado cmhecimento.
     * 
     * @param conh_id O id do conhecimento abordado.
     * @return int O valor do contador mais baixo para o conhecimento em questao.
     */
    public int getWorst(int conh_id)
    {
    	int menor = 0;
    	
    	for(int i=0; i < usersCounts.length; i++)
    			if(usersCounts[i][conh_id] < menor)
    				menor = usersCounts[i][conh_id];
    	return menor;
    }
    
    /**
     * Este metodo retorna o melhor contador 
     * existente para um determinado cmhecimento.
     * 
     * @param conh_id O id do conhecimento abordado.
     * @return int O valor do contador mais alto para o conhecimento em questao.
     */
    public int getBest(int conh_id)
    {
    	int maior = 0;
    	
    	for(int i=0; i < usersCounts.length; i++)
    			if(usersCounts[i][conh_id] > maior)
    				maior = usersCounts[i][conh_id];
    	return maior;
    }
    
    /**
     * Este metodo retorna a media dos contadores
     * para um determinado cmhecimento.
     * 
     * @param conh_id O id do conhecimento abordado.
     * @return int O valor da media para o conhecimento em questao.
     */
    public int getMean(int conh_id)
    {
    	double sum = 0;
    	
    	for(int i=0; i < usersCounts.length; i++)
    		sum += usersCounts[i][conh_id];
    			
    	return (int) (sum/usersCounts.length);
    }
    
    /**
     * Este metodo retorna todos os pais que estejam 
     * imediatamente ligados a um no.
     *  
     * @param node_id O identificador do no cujos pais se deseja obter.
     * @return ArrayList<Integer> Uma lista com os identificadores dos pais.
     */
    public ArrayList<Integer> getFirstParents(int node_id)
    {
        ArrayList<Integer> ret = new ArrayList<Integer>();

        for(int i = 0; i < DAG.length; i++)
                if(DAG[i][node_id])
                        ret.add(new Integer(i));
        return ret;
    }

    /**
     * Este metodo adiciona um valor de conhecimento para um 
     * determinado usuario.
     * 
     * @param user_id O id do usuario.
     * @param conh_id O id do conhecimento
     * @param value O valor adicionado ao cohecimento
     */
    public void addValue(int user_id,int conh_id, int value)
    {
    	this.usersCounts[user_id][conh_id] = value;
    }
    
    /**
     * Calcula o score dada uma ontologia 
     * e contadores de um usuario.
     * 
     *@param start O no raiz da ontologia 
     *@param end O no folha selecionado 
     *@param counts Os contadores de um determinado usuario
     *@return O valor do nivel do usuario sobre determinado conhecimento
     */
    private int calculaScore(int[] counts, ArrayList<ArrayList<Integer>> caminhos)
    {
        double ret = 0;
        double aux = 1;
        
        for(int i=0; i < caminhos.size(); i++)
        {
        	aux = 1;
        	
            for(int j=0; j < caminhos.get(i).size(); j++)                
            	aux *= 1.0/counts[caminhos.get(i).get(j).intValue()];
            
            ret += aux;
            
        }
        
        return (int) Math.round(10000 * (1 - ret));    
    }
    
    /**
     * Retorna o score, dado o identificador do usuario,
     * e o identificador de um conhecimento.
     * 
     * @param user_id O identificador do usuario.]
     * @param conh_id O identificador do conhecimento que se quer calcularo score.
     * @return int O score do usuario parao conhecimento em questao.
     */
    public int getScore(int user_id, int conh_id)
    {
    	ArrayList<ArrayList<Integer>> caminhos = this.find_closed_paths(0, conh_id, null);
    	return this.calculaScore(usersCounts[user_id], caminhos);
    }
    
    public static boolean incrementaRespostasDesenvolvedor(Desenvolvedor desenvolvedor, boolean foiUtil, ArrayList<String> conhecimentos){
    	if(foiUtil){
    		String email = desenvolvedor.getEmail();
    		
    		for(String conhecimentoAtividade : conhecimentos){
      				
    			try{
    				int quantidade = validacaoDesenvolvedor.getQntResposta(email, conhecimentoAtividade) + 1;
    				boolean resposta = validacaoDesenvolvedor.updateQntResposta(email, conhecimentoAtividade, quantidade);
    				if(!resposta)
    					return false;
    			}
    			catch(ConhecimentoInexistenteException e){
    				return false;
    			}
    			catch(DesenvolvedorInexistenteException e){
    				return false;
    			}
    		}
    		
    		return true;
    		
    	}
    	return false;
    }
    
    public static boolean atualizaConhecimentosDesenvolvedor(Desenvolvedor desenvolvedor, ArrayList<Conhecimento> conhecimentos){
    	String email = desenvolvedor.getEmail();
		
		for(Conhecimento conhecimento : conhecimentos){			
			try{
				
				int grau = validacaoDesenvolvedor.getGrau(email, conhecimento.getNome()) + 1;
				validacaoDesenvolvedor.updateGrau(email, conhecimento.getNome(), grau);
					
			} catch(DesenvolvedorInexistenteException e){
				return false;
			}
			catch(ConhecimentoInexistenteException e){
				return false;
			}
			
		}
		
		return true;

    }
    
    /**
     * Este metodo retorna um objeto do tipo Tree que representa a arvore de 
     * conhecumentos utilizada pela ontologia.
     * 
     * @return Retorna a arvode de conhecimentos da ontologia.
     */
    public Tree getArvoreDeConhecimentos() {
    	Tree arvore = new Tree("Raiz");
    	
    	ArrayList<Conhecimento> conhecimentos = validacaoConhecimento.getListaConhecimento();
    	
    	while (conhecimentos.size() > 0) {
    		Iterator<Conhecimento> it = conhecimentos.iterator();
    		ArrayList<Conhecimento> conhecimentosPais = new ArrayList<Conhecimento>();
    		
    		while (it.hasNext()) {
    			
    		}
    		
    	}
    	
    	return arvore;
    }
}

