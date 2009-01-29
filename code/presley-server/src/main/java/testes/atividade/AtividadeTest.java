package testes.atividade;

import java.sql.Date;
import java.util.ArrayList;

import junit.framework.TestCase;
import validacao.implementacao.ValidacaoAtividadeImpl;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TipoAtividade;
import com.hukarz.presley.excessao.AtividadeInexistenteException;
import com.hukarz.presley.excessao.DataInvalidaException;
import com.hukarz.presley.excessao.DescricaoInvalidaException;
import com.hukarz.presley.excessao.EmailInvalidoException;
import com.hukarz.presley.excessao.ProblemaInexistenteException;
import com.hukarz.presley.facade.PacketStruct;

import core.ExecuteClientQuery;

public class AtividadeTest extends TestCase{
	
	private Desenvolvedor desenv;
	private ExecuteClientQuery ecq;
	
	protected void setUp() throws Exception {
		new ValidacaoAtividadeImpl();
		ecq = new ExecuteClientQuery();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@SuppressWarnings("deprecation")
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
		Date dt1 = new Date(System.currentTimeMillis());
		Date dt2 = new Date(2008,9,19);
		desenv = new Desenvolvedor();
		ArrayList<Conhecimento> al = new ArrayList<Conhecimento>();
		int idpai = 1;
		Desenvolvedor sup = new Desenvolvedor();
		
		//tem que dar erro de atividade inexistente
		TipoAtividade at = new TipoAtividade("descricao",desenv,sup,idpai,dt1,dt2,false,al);
		at.setId(9999);
		PacketStruct ps = new PacketStruct(at,2);
		try {
			assertTrue(ecq.removerAtividade(ps));
			fail("nao deveria passar, pois nao existe tal atividade");
		} catch (AtividadeInexistenteException e) {
			;
		} catch (ProblemaInexistenteException e) {
			;
		} catch (Exception e) {
			fail("only god");
		}
		//----------------------------------------------
		
		//tem que dar erro de atividade inexistente
		at = new TipoAtividade("descricao",desenv,sup,idpai,dt1,dt2,false,al);
		at.setId(65);
		ps = new PacketStruct(at,2);
		try {
			assertTrue(ecq.removerAtividade(ps));
		} catch (AtividadeInexistenteException e) {
			fail("deveria ter removido");
		} catch (ProblemaInexistenteException e) {
			fail("problemas na remocao de problemas. nao deveria dar esse erro");
		} catch (Exception e) {
			fail("only god");
		}
		//----------------------------------------------
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
