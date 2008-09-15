package core.interfaces;

import beans.Conhecimento;
import beans.TipoAtividade;
import beans.Tree;

/**
 * 
 * @author Eduardo Serafim
 * Essa interface contem metodos básicos que o Cliente e Servidor Presley devem implementar.
 * 
 * Última modificacao: 15/09/2008 por EduardoPS
 */


public interface CorePresleyOperations {
	
	/**
	 * Este método cadastra uma nova atividade na base de dados.
	 * CÓDIGO DA OPERAÇÃO -> 01
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi adicionada com sucesso.
	 */
	public boolean adicionaAtividade(TipoAtividade atividade);
	
	/**
	 * Este método remove a atividade da base de dados.
	 * CÓDIGO DA OPERAÇÃO -> 02
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi removida com sucesso.
	 */
	public boolean removerAtividade(TipoAtividade atividade);

	/**
	 * Este método solicita a arvore de ontologia
	 * CÓDIGO DA OPERAÇÃO -> 03
	 * @return Tree Arvore de ontologia do sistema.
	 */
	public Tree getOntologia();	
	
	/**
	 * Este método solicita a arvore de ontologia
	 * CÓDIGO DA OPERAÇÃO -> 03
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 */
	public boolean AdicionaConhecimento(Conhecimento conhecimento);

	
}
