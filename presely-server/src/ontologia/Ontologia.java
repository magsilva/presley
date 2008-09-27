package ontologia;

import java.util.ArrayList;
import java.util.Iterator;

import validacao.implementacao.ValidacaoConhecimentoImpl;
import validacao.implementacao.ValidacaoDesenvolvedorImpl;
import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.Item;
import beans.Tree;
import excessao.ConhecimentoInexistenteException;
import excessao.DesenvolvedorInexistenteException;

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
	static ValidacaoDesenvolvedorImpl validacaoDesenvolvedor = new ValidacaoDesenvolvedorImpl();
	static ValidacaoConhecimentoImpl validacaoConhecimento = new ValidacaoConhecimentoImpl();

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

	/**
	 * Este método incrementa o número de respostas que um desenvolvedor possui em um conhecimento.
	 * @param desenvolvedor o desenvolvedor que terá a quantidade de respostas incrementadas.
	 * @param foiUtil qualificação da resposta dada pelo desenvolvedor.
	 * @param conhecimentos os conhecimentos que terão o número de respostas incrementadas.
	 * @return true se a quantidade de respostas foi incrementada.
	 * @throws DesenvolvedorInexistenteException 
	 * @throws ConhecimentoInexistenteException 
	 */
	public static boolean incrementaRespostasDesenvolvedor(Desenvolvedor desenvolvedor, boolean foiUtil, ArrayList<String> conhecimentos) throws ConhecimentoInexistenteException, DesenvolvedorInexistenteException{

		if(foiUtil){
			String email = desenvolvedor.getEmail();

			for(String conhecimentoAtividade : conhecimentos){

				int quantidade = validacaoDesenvolvedor.getQntResposta(email, conhecimentoAtividade) + 1;
				boolean resposta = validacaoDesenvolvedor.updateQntResposta(email, conhecimentoAtividade, quantidade);
				if(!resposta)
					return false;

			}

			return true;

		}
		return false;
	}


	/**
	 * Este método incrementa o grua de conhecimento que um desenvolvedor possui em um conhecimento.
	 * @param desenvolvedor o desenvolvedor em questão.
	 * @param conhecimentos os conhecimentos que terão o grau incrementado.
	 * @return true se o grau foi incrementado.
	 */
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
	 * @throws ConhecimentoInexistenteException 
	 */
	public static Tree getArvoreDeConhecimentos() throws ConhecimentoInexistenteException {
		Tree arvore = new Tree("Raiz");
		
		ArrayList<Conhecimento> filhosDoRaiz = new ArrayList<Conhecimento>();
		System.out.println("Numero de Filhos do raiz = " + filhosDoRaiz.size()); //teste
		validacaoConhecimento = new ValidacaoConhecimentoImpl();
		
		ArrayList<Conhecimento> conhecimentos = validacaoConhecimento.getListaConhecimento();
		System.out.println("Numero de conhrcimentos no banco = " + conhecimentos.size());
		Iterator<Conhecimento> it = conhecimentos.iterator();

		// Buscando os conhecimentos que nao possuem pais (ou seja, os filhos do raiz).
		while (it.hasNext()) {
			Conhecimento conhecimento = it.next();
			ArrayList<Conhecimento> paisDoConhecimento = null;

			paisDoConhecimento = validacaoConhecimento.getPais(conhecimento.getNome());

			if (paisDoConhecimento.size() == 0) {
				filhosDoRaiz.add(conhecimento);
			}
		}

		// Adicionando os filhos do raiz.
		Iterator<Conhecimento> it2 = filhosDoRaiz.iterator();
		while (it2.hasNext()) {
			Conhecimento conhecimento = it2.next();
			arvore.adicionaFilho(conhecimento.getNome());
		}

		// Obtendo as sub-árvores para montar a arvore completa.
		Iterator<Conhecimento> it3 = filhosDoRaiz.iterator();
		while (it3.hasNext()) {
			Conhecimento filhoDoRaiz = it3.next();
			Item item = arvore.getFilho(filhoDoRaiz.getNome());
			montaSubArvore(item);
		}

		return arvore;
	}

	/**
	 * Metodo recursivo que monta a subarvore de um determinado item da arvode
	 * de conhecimentos.
	 * 
	 * @param item Item para o qual sera montada a sub-arvore
	 * @throws ConhecimentoInexistenteException
	 */
	private static void montaSubArvore(Item itemPai) throws ConhecimentoInexistenteException {
		String nomeItemPai = itemPai.getNome();
		Conhecimento conhecimento = validacaoConhecimento.getConhecimento(nomeItemPai);

		ArrayList<Conhecimento> filhos = validacaoConhecimento.getFilhos(conhecimento.getNome());
		if (filhos != null) {
			Iterator<Conhecimento> it1 = filhos.iterator();

			while (it1.hasNext()) {
				Conhecimento filho = it1.next();
				itemPai.adicionaFilho(filho.getNome());
				Item itemFilho = itemPai.getFilho(filho.getNome());
				montaSubArvore(itemFilho);
			}
		}
	}
}

