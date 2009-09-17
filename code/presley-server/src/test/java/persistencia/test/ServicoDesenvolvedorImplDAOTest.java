package persistencia.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.hukarz.presley.beans.TopicoConhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.server.persistencia.implementacao.ServicoConhecimentoImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoConhecimento;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;

public class ServicoDesenvolvedorImplDAOTest extends TestCase {
	private ServicoConhecimento sc;
	private ServicoDesenvolvedor sd;
	
	protected void setUp() throws Exception {
		sc = new ServicoConhecimentoImplDAO();
		sd = new ServicoDesenvolvedorImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void adicionarConhecimentos(){
		
		assertTrue(sc.criarConhecimento("Banco de dados", "Banco de dados relacional"));
		
		assertTrue(sc.criarConhecimento("SQL", "SQL rules!!"));
			
		assertTrue(sc.criarConhecimento("MySQL", "SGBD MySQL"));
			
		assertTrue(sc.criarConhecimento("PostGreSQL", "SGBD PostgreSQL"));
		
	}

	public void removerConhecimentos(){
		
		assertTrue(sc.removerConhecimento("Banco de dados"));
		
		assertTrue(sc.removerConhecimento("MySQL"));
			
		assertTrue(sc.removerConhecimento("PostgreSQL"));
					
		assertTrue(sc.removerConhecimento("SQL"));
		
	}
	
	public void testCriarDesenvolvedor() {
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "João Pessoa", "123456"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama", "654321"));
		
		assertTrue(sd.criarDesenvolvedor("teste@gmail.com", "Email de teste", "USA", "1234567"));
		
		if (sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar", "PB", "000000"))
			fail("Usuário devia existir!");
	}
	
	public void testDesenvolvedorExiste() {
		
		assertTrue(sd.desenvolvedorExiste("amilcarsj@gmail.com"));
		
		assertTrue(sd.desenvolvedorExiste("asju@gmail.com"));
		
		if (sd.desenvolvedorExiste("amilcarsj@gmail.con"))
			fail("Usuário não devia existir!");
	}	
	
	public void testGetDesenvolvedor() {
	
		Desenvolvedor d = sd.getDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(d.getEmail().equals("amilcarsj@gmail.com"));
		assertTrue(d.getCVSNome().equals("João Pessoa"));
		assertTrue(d.getNome().equals("Amilcar Soares"));
		
		if(d.getNome().equals("Amilcar Soares Jr."))
			fail("Nome errado!");
		
		if(d.getCVSNome().equals("João Pessoa - PB"))
			fail("Localidade errada!");
	}
	
	public void testAtualizarDesenvolvedor() {
		
		assertTrue(sd.atualizarDesenvolvedor("amilcarsj@gmail.com", "amilcarpiox@hotmail.com",
				"Amilcar Soares Jr.", "João Pessoa", "123456"));
		
		assertTrue(sd.atualizarDesenvolvedor("asju@gmail.com", "asju@gmail.com",
				"ASJU", "Matsuyama-Ehime", "123456"));
				
		assertTrue(sd.getDesenvolvedor("amilcarpiox@hotmail.com").getCVSNome().equals("João Pessoa"));
	
		if (sd.getDesenvolvedor("asju@gmail.com").getCVSNome().equals("Matsuyama"))
			fail("A localidade nao foi atualizada!");
		
	}

	
	public void testAdicionarConhecimentoAoDesenvolvedor() {
		
		this.adicionarConhecimentos();
		
//		assertTrue(sd.adicionarConhecimentoAoDesenvolvedor("amilcarpiox@hotmail.com", "SQL"));
//		
//		assertTrue(sd.adicionarConhecimentoAoDesenvolvedor("asju@gmail.com", "MySQL"));
//
//		assertTrue(sd.adicionarConhecimentoAoDesenvolvedor("asju@gmail.com", "SQL"));
//		
//		if (sd.adicionarConhecimentoAoDesenvolvedor("amilcarpiox@hotmail.com", "Conhecimento Qualquer"))
//			fail("Conhecimento nao deve existir!");
//		
//		if (sd.adicionarConhecimentoAoDesenvolvedor("amilcarsj@gmail.com", "Banco de dados"))
//			fail("Conhecimento nao deve existir!");
//		
		
	}
	
	public void testGetConhecimentosDoDesenvolvedor() {
		ArrayList<TopicoConhecimento> list = sd.getConhecimentosDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(list.get(0).getNome().equals("MySQL"));
		assertTrue(list.get(1).getNome().equals("SQL"));
		
		if (list.get(0).getNome().equals("SQL"))
			fail("Lista não ordenada corretamente!");
		
	}
		
	public void testRemoverConhecimentoDoDesenvolvedor() {
		
		assertTrue(sd.removerConhecimentoDoDesenvolvedor("amilcarpiox@hotmail.com", "SQL"));
		
		assertTrue(sd.removerConhecimentoDoDesenvolvedor("asju@gmail.com", "MySQL"));

		assertTrue(sd.removerConhecimentoDoDesenvolvedor("asju@gmail.com", "SQL"));
		
		if (sd.removerConhecimentoDoDesenvolvedor("amilcarpiox@hotmail.com", "SQL"))
			fail("Conhecimento ja deveria ter sido removido!");
		
		if (sd.removerConhecimentoDoDesenvolvedor("asju@gmail.com", "PostgresSQL"))
			fail("Não existe essa associacao!");
		
		this.removerConhecimentos();
	}
	
	public void testRemoverDesenvolvedor() {
		
		assertTrue(sd.removerDesenvolvedor("amilcarpiox@hotmail.com"));
		
		assertTrue(sd.removerDesenvolvedor("asju@gmail.com"));
		
		assertTrue(sd.removerDesenvolvedor("teste@gmail.com"));
		
		if(sd.removerDesenvolvedor("asju@gmail.com"))
			fail("desenvolvedor nao deveria existir mais...");
		
		if (sd.removerDesenvolvedor("amilcarsj@gmail.com"))
			fail("desenvolvedor previamente nao existe no banco");
	}

	

}
