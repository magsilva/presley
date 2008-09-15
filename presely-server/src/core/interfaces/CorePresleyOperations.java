package core.interfaces;

import beans.Conhecimento;
import beans.TipoAtividade;
import beans.Tree;

/**
 * 
 * @author Eduardo Serafim
 * Essa interface contem metodos b�sicos que o Cliente e Servidor Presley devem implementar.
 * 
 * �ltima modificacao: 15/09/2008 por EduardoPS
 */


public interface CorePresleyOperations {
	
	/**
	 * Este m�todo cadastra uma nova atividade na base de dados.
	 * C�DIGO DA OPERA��O -> 01
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi adicionada com sucesso.
	 */
	public boolean adicionaAtividade(TipoAtividade atividade);
	
	/**
	 * Este m�todo remove a atividade da base de dados.
	 * C�DIGO DA OPERA��O -> 02
	 * @param TipoAtividade atividade 
	 * @return true se a atividade foi removida com sucesso.
	 */
	public boolean removerAtividade(TipoAtividade atividade);

	/**
	 * Este m�todo solicita a arvore de ontologia
	 * C�DIGO DA OPERA��O -> 03
	 * @return Tree Arvore de ontologia do sistema.
	 */
	public Tree getOntologia();	
	
	/**
	 * Este m�todo solicita a arvore de ontologia
	 * C�DIGO DA OPERA��O -> 03
	 * @param Conhecimento conhecimento
	 * @return true se o conhecimento foi adicionado com sucesso.
	 */
	public boolean AdicionaConhecimento(Conhecimento conhecimento);

	
}
