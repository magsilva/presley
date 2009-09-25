package persistencia.test;

import junit.framework.TestCase;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;

public class ServicoConhecimentoImplDAOTest extends TestCase {

	private ServicoConhecimento sc;
	
	protected void setUp() throws Exception {
		sc = new ServicoConhecimentoImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCriarConhecimento() {
		
		assertTrue(sc.criarConhecimento("Banco de dados", "Banco de dados relacional"));
			
		assertTrue(sc.criarConhecimento("SQL", "SQL rules!!"));
			
		assertTrue(sc.criarConhecimento("MySQL", "SGBD MySQL"));
			
		assertTrue(sc.criarConhecimento("PostGreSQL", "SGBD PostgreSQL"));
				
		if(sc.criarConhecimento("Banco de dados", "Banco de dados relacional"))
			fail("houve erro na criacao!");
		
	}
	
	public void testAtualizarConhecimento() {
		assertTrue(sc.atualizarConhecimento("Banco de dados","BD", "Banco de dados relacional"));
			
		assertTrue(sc.atualizarConhecimento("SQL","Statement Query Language", "Linguagem de consulta"));
			
		
		Conhecimento c = sc.getConhecimento("Statement Query Language");
		
		if(c.getDescricao().equals("Linguagem de consultar"))
			fail("houve erro na atualizacao!");
	}

	public void testConhecimentoExiste() {
		assertTrue(sc.conhecimentoExiste("BD"));
			
		assertTrue(sc.conhecimentoExiste("MySQL"));
			
		assertTrue(sc.conhecimentoExiste("PostgreSQL"));
			
		if(sc.conhecimentoExiste("SQL"))
			fail("houve erro na verificacao!");
		
	}

	public void testGetConhecimento() {
		Conhecimento c = sc.getConhecimento("BD");
		Conhecimento c2 = sc.getConhecimento("MySQL");
		
		assertTrue(c.getNome().equals("BD"));
		assertTrue(c2.getDescricao().equals("SGBD MySQL"));
		
		if (c.getDescricao().equals("Banco de dados relacionau"))
			fail("houve erro na selecao");
		if (c2.getNome().equals("MySQLa"))
			fail("houve erro na selecao");
		
	}

	public void testRemoverConhecimento() {
		assertTrue(sc.removerConhecimento("BD"));
			
		assertTrue(sc.removerConhecimento("MySQL"));
			
		assertTrue(sc.removerConhecimento("PostgreSQL"));
					
		assertTrue(sc.removerConhecimento("Statement Query Language"));
			
		if(sc.removerConhecimento("SQL"))
			fail("houve erro na remocao!");
		
		if(sc.removerConhecimento("Statement Query Language"))
			fail("houve erro na remocao!");
				
	}
	

}
