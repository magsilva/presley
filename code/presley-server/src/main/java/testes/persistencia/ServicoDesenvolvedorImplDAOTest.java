package testes.persistencia;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;

import com.hukarz.presley.beans.Conhecimento;
import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.beans.TipoAtividade;

public class ServicoDesenvolvedorImplDAOTest extends TestCase {

	private ServicoConhecimento sc;
	private ServicoDesenvolvedor sd;
	private ServicoAtividade sa;
	
	
	
	protected void setUp() throws Exception {
		sc = new ServicoConhecimentoImplDAO();
		sd = new ServicoDesenvolvedorImplDAO();
		sa = new ServicoAtividadeImplDAO();
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
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "Jo�o Pessoa", "123456"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama", "654321"));
		
		assertTrue(sd.criarDesenvolvedor("teste@gmail.com", "Email de teste", "USA", "1234567"));
		
		if (sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar", "PB", "000000"))
			fail("Usu�rio devia existir!");
	}
	
	public void testDesenvolvedorExiste() {
		
		assertTrue(sd.desenvolvedorExiste("amilcarsj@gmail.com"));
		
		assertTrue(sd.desenvolvedorExiste("asju@gmail.com"));
		
		if (sd.desenvolvedorExiste("amilcarsj@gmail.con"))
			fail("Usu�rio n�o devia existir!");
	}	
	
	public void testGetDesenvolvedor() {
	
		Desenvolvedor d = sd.getDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(d.getEmail().equals("amilcarsj@gmail.com"));
		assertTrue(d.getLocalidade().equals("Jo�o Pessoa"));
		assertTrue(d.getNome().equals("Amilcar Soares"));
		
		if(d.getNome().equals("Amilcar Soares Jr."))
			fail("Nome errado!");
		
		if(d.getLocalidade().equals("Jo�o Pessoa - PB"))
			fail("Localidade errada!");
	}
	
	public void testAtualizarDesenvolvedor() {
		
		assertTrue(sd.atualizarDesenvolvedor("amilcarsj@gmail.com", "amilcarpiox@hotmail.com",
				"Amilcar Soares Jr.", "Jo�o Pessoa", "123456"));
		
		assertTrue(sd.atualizarDesenvolvedor("asju@gmail.com", "asju@gmail.com",
				"ASJU", "Matsuyama-Ehime", "123456"));
				
		assertTrue(sd.getDesenvolvedor("amilcarpiox@hotmail.com").getLocalidade().equals("Jo�o Pessoa"));
	
		if (sd.getDesenvolvedor("asju@gmail.com").getLocalidade().equals("Matsuyama"))
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
		ArrayList<Conhecimento> list = sd.getConhecimentosDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(list.get(0).getNome().equals("MySQL"));
		assertTrue(list.get(1).getNome().equals("SQL"));
		
		if (list.get(0).getNome().equals("SQL"))
			fail("Lista n�o ordenada corretamente!");
		
	}
		
	public void testRemoverConhecimentoDoDesenvolvedor() {
		
		assertTrue(sd.removerConhecimentoDoDesenvolvedor("amilcarpiox@hotmail.com", "SQL"));
		
		assertTrue(sd.removerConhecimentoDoDesenvolvedor("asju@gmail.com", "MySQL"));

		assertTrue(sd.removerConhecimentoDoDesenvolvedor("asju@gmail.com", "SQL"));
		
		if (sd.removerConhecimentoDoDesenvolvedor("amilcarpiox@hotmail.com", "SQL"))
			fail("Conhecimento ja deveria ter sido removido!");
		
		if (sd.removerConhecimentoDoDesenvolvedor("asju@gmail.com", "PostgresSQL"))
			fail("N�o existe essa associacao!");
		
		this.removerConhecimentos();
	}
	
	public void testGetAtividadesDoDesenvolvedor() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2007, 01, 01);
		Date dataInicio = new Date(cal.getTimeInMillis());
		cal.set(2008,01,01);
		Date dataFim = new Date(cal.getTimeInMillis());
		
		sa.cadastrarAtividade("asju@gmail.com", "amilcarpiox@hotmail.com", "Criar Relatorio",
				dataInicio, dataFim);
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(list.get(0).getSupervisor().getEmail().equals("amilcarpiox@hotmail.com"));
		
		sa.removerAtividade(list.get(0).getId());
		
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
