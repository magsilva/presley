package testes.atividade;

import java.util.ArrayList;
import java.sql.Date;

import core.ExecuteClientQuery;
import facade.PacketStruct;

import excessao.DataInvalidaException;
import excessao.DescricaoInvalidaException;
import excessao.EmailInvalidoException;
import validacao.implementacao.ValidacaoAtividadeImpl;

import junit.framework.TestCase;
import beans.Conhecimento;
import beans.Desenvolvedor;
import beans.TipoAtividade;

public class AtividadeTest extends TestCase{
	
	private Desenvolvedor desenv;
	private ValidacaoAtividadeImpl valida;
	private ExecuteClientQuery ecq;
	
	protected void setUp() throws Exception {
		valida = new ValidacaoAtividadeImpl();
		ecq = new ExecuteClientQuery();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAdicionarAtividade(){
		Date dt1 = new Date(System.currentTimeMillis());
		Date dt2 = new Date(2008,9,18);
		desenv = new Desenvolvedor();
		ArrayList<Conhecimento> al = new ArrayList<Conhecimento>();
		int idpai = 1;
		Desenvolvedor sup = new Desenvolvedor();
		
		//teste geral pode dar qualquer erro dos esperados
		TipoAtividade at = new TipoAtividade(null,desenv,sup,idpai,dt1,dt2,false,al);
		PacketStruct ps = new PacketStruct(at,1);

		try {
			assertTrue(ecq.adicionaAtividade(ps));
			fail("deveria dar alguma excecao prevista");
		} catch (EmailInvalidoException e) {
			;
		} catch (DescricaoInvalidaException e) {
			;
		} catch (DataInvalidaException e) {
			;
		} catch (Exception e) {
			fail("only god");
		}
		//----------------------------------------------
		
		//nao pode dar erro de email ou algum desconhecido
		desenv.setEmail("lucasvieira@gmail.com");
		sup.setEmail("boy@gmail.com");
		at = new TipoAtividade(null,desenv,sup,idpai,dt1,dt2,false,al);
		ps = new PacketStruct(at,1);

		try {
			assertTrue(ecq.adicionaAtividade(ps));
			fail("deveria dar alguma excecao prevista");
		} catch (EmailInvalidoException e) {
			fail("o email deveria estar certo");
		} catch (DescricaoInvalidaException e) {
			;
		} catch (DataInvalidaException e) {
			;
		} catch (Exception e) {
			fail("only god");
		}
		//----------------------------------------------
		
		//nao pode dar erro nenhum!
		at = new TipoAtividade("descricao",desenv,sup,idpai,dt1,dt2,false,al);
		ps = new PacketStruct(at,1);

		try {
			assertTrue(ecq.adicionaAtividade(ps));
		} catch (EmailInvalidoException e) {
			fail("o email deveria estar certo");
		} catch (DescricaoInvalidaException e) {
			fail("a descricao deveria estar correta!");
		} catch (DataInvalidaException e) {
			fail("as 2 datas estao corretas!");
		} catch (Exception e) {
			fail("only god");
		}
		//----------------------------------------------
		
		//tem que dar erro de datas somente
		dt1 = new Date(2009,9,10);
		dt2 = new Date(System.currentTimeMillis());
		at = new TipoAtividade("descricao",desenv,sup,idpai,dt1,dt2,false,al);
		ps = new PacketStruct(at,1);

		try {
			assertTrue(ecq.adicionaAtividade(ps));
			fail("deveria dar alguma excecao prevista: de datas");
		} catch (EmailInvalidoException e) {
			fail("o email deveria estar certo");
		} catch (DescricaoInvalidaException e) {
			fail("a descricao deveria estar correta!");
		} catch (DataInvalidaException e) {
			;
		} catch (Exception e) {
			fail("only god");
		}
		//----------------------------------------------
		
	}
	
	public void testRemoverAtividade(){
		
	}
	
	public void testBuscarAtividade(){
		
	}
	
	/*public void testEncerrarAtividade(){
		
	}
	
	public void testAssociarConhecimentoAtividade(){
		
	}
	
	public void testDesassociarConhecimentoAtividade(){
		
	}
	
	public void testDesassociarConhecimentoAtividade(){
		
	}*/

}
