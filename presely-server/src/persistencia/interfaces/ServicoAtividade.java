package persistencia.interfaces;

import java.sql.Date;
import java.util.ArrayList;

import beans.TipoAtividade;
import beans.Conhecimento;

/**
 * 
 * @author Amilcar Jr
 * Essa interface contem metodos para a administracao de atividades que um
 * desenvolvedor deve realizar.
 * 
 * Última modificacao: 09/09/2008 por RodrigoCMD
 */

public interface ServicoAtividade {

	/**
	 * Este método cadastra uma nova atividade na base de dados.
	 * @param emailDesenvolvedor Email do desenvolvedor responsável pela execucao da atividade.
	 * @param emailGerente Email do gerente que determinou a atividade para um dado desenvolvedor.
	 * @param descricao Descricao da atividade a ser realizada
	 * @param dataInicio Data em que a atividade foi passada ao desenvolvor
	 * @param dataFim Data que a atividade foi terminada
	 * @return true se a atividade foi concluida com sucesso.
	 */
	
	public boolean cadastrarAtividade(String emailDesenvolvedor, String emailGerente,
			String descricao, Date dataInicio, Date dataFim);
	
	/**
	 * Este metodo remove uma atividade previamente cadastrada na base de dados.
	 * @param id Identificador da atividade
	 * @return true se a atividade foi removida da base de dados.
	 */
	public boolean removerAtividade(int id);
	
	/**
	 * Esse método retorna um objeto TipoAtividade com todos os dados de uma atividade
	 * previamente cadastrada.
	 * @param id Identificador da atividade
	 * @return <TipoAtividade>
	 */
	public TipoAtividade getAtividade(int id);
	
	/**
	 * Esse método retorna uma lista de sub-atividades associadas a uma atividade
	 * pai.
	 * @param idPai Identificador da atividade pai
	 * @return ArrayList<TipoAtividade>
	 */
	public ArrayList<TipoAtividade> getSubAtividades(int idPai);
	
	/**
	 * Esse método atualiza o status se a atividade foi concluida ou
	 * não.
	 * @param id Identificador da atividade.
	 * @param terminada true se a atividade foi concluida.
	 * @return true se a operacao foi realizada com sucesso.
	 */
	public boolean atualizarStatusDaAtividade(int id, boolean terminada);
	
	/**
	 * Esse método retorna uma lista de conhecimentos que envolvem uma dada
	 * atividade.
	 * @param idAtividade Identificador da atividade
	 * @return ArrayList<Conhecimento>
	 */
	public ArrayList<Conhecimento> getConhecimentosEnvolvidosNaAtividade(int idAtividade);
	
	/**
	 * Esse método associa um conhecimento a uma atividade previamente cadastrada.
	 * @param idAtividade Identificador da atividade.
	 * @param nomeConhecimento Nome do conhecimento a ser associado.
	 * @return true se a associacao foi feita.
	 */
	public boolean adicionarConhecimentoAAtividade(int idAtividade, String nomeConhecimento);
	
	/**
	 * Esse metodo remove uma associacao entre um conhecimento e uma atividade.
	 * @param idAtividade Identificador da ativiadade.
	 * @param nomeConhecimento Nome do conhecimento
	 * @return true se a associacao foi desfeita.
	 */
	public boolean removerConhecimentoDaAtividade(int idAtividade, String nomeConhecimento);

	/**
	 * Esse método verifica se uma atividade existe na base de dados.
	 * @param id Identificador da atividade.
	 * @return true se a atividade existir na base de dados.
	 */
	public boolean atividadeExiste(int id);

	/**
	 * Esse método verifica se existe uma associacao entre conhecimento e atividade.
	 * @param idAtividade Identificador da atividade.
	 * @param nomeConhecimento Nome do conhecimento associado a atividade.
	 * @return true se existe associacao entre conhecimento e atividade.
	 */
	public boolean atividadeAssociadaAConhecimentoExiste(int idAtividade, String nomeConhecimento);
	
	/**
	 * Esse método associa uma sub-atividade a uma atividade maior. 
	 * @param idSubAtividade Identificador da sub-atividade 
	 * @param idAtividadePai Identificador da atividade pai.
	 * @return true se a associacao foi feita com sucesso.
	 */
	public boolean associarAtividades(int idSubAtividade, int idAtividadePai);
	
	/**
	 * Esse método desassocia uma sub-atividade a uma atividade maior. 
	 * @param idSubAtividade Identificador da sub-atividade 
	 * @param idAtividadePai Identificador da atividade pai.
	 * @return true se a associacao foi feita com sucesso.
	 */
	public boolean desassociarAtividades(int idSubAtividade, int idAtividadePai);
	
	/**
	 * Metodo que retorna todas as atividades cadastradas no banco.
	 * @return ArrayList<TipoAtividade> lista com todas as atividades do banco.
	 */
	public ArrayList<TipoAtividade> listarAtividades();
}

