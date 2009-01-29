package testes.persistencia;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import beans.TipoAtividade;

import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoConhecimentoImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoConhecimento;
import persistencia.interfaces.ServicoDesenvolvedor;
import junit.framework.TestCase;

public class ServicoAtividadeImplDAOTest extends TestCase {

	private ServicoAtividade sa;
	private ServicoDesenvolvedor sd;
	private ServicoConhecimento sc;
	
	protected void setUp() throws Exception {
		sa = new ServicoAtividadeImplDAO();
		sd = new ServicoDesenvolvedorImplDAO();
		sc = new ServicoConhecimentoImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void adicionarConhecimentos(){
		
		assertTrue(sc.criarConhecimento("Banco de dados", "Banco de dados relacional"));
		
		assertTrue(sc.criarConhecimento("SQL", "SQL rules!!"));
			
		assertTrue(sc.criarConhecimento("MySQL", "SGBD MySQL"));
			
		assertTrue(sc.criarConhecimento("PostGreSQL", "SGBD PostgreSQL"));
		
	}

	private void removerConhecimentos(){
		
		assertTrue(sc.removerConhecimento("Banco de dados"));
		
		assertTrue(sc.removerConhecimento("MySQL"));
			
		assertTrue(sc.removerConhecimento("PostgreSQL"));
					
		assertTrue(sc.removerConhecimento("SQL"));
		
	}
	
	private void criarDesenvolvedores() {
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "João Pessoa", "123456"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama", "654321"));
		
	}

	private void removerDesenvolvedores() {
		
		assertTrue(sd.removerDesenvolvedor("amilcarsj@gmail.com"));
		
		assertTrue(sd.removerDesenvolvedor("asju@gmail.com"));
		
	}
	
	
	public void testCadastrarAtividade() {

		this.criarDesenvolvedores();
		
		Calendar cal = Calendar.getInstance();
		cal.set(2007, 01, 01);
		Date dataInicio = new Date(cal.getTimeInMillis());
		cal.set(2008,01,01);
		Date dataFim = new Date(cal.getTimeInMillis());
		
		cal.set(2005, 01, 01);
		Date dataInicio2 = new Date(cal.getTimeInMillis());
		cal.set(2006,10,01);
		Date dataFim2 = new Date(cal.getTimeInMillis());
		
		assertTrue(sa.cadastrarAtividade("asju@gmail.com", "amilcarsj@gmail.com", "Criar Relatorio",
				dataInicio, dataFim) == 1);
		
		assertTrue(sa.cadastrarAtividade("amilcarsj@gmail.com", "asju@gmail.com", "Executar testes",
				dataInicio2, dataFim2) == 1);
		
		assertTrue(sa.cadastrarAtividade("amilcarsj@gmail.com", "asju@gmail.com", "Criar testes",
				dataInicio2, dataFim2) == 1);
						
	}
	
	public void testAtividadeExiste() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(sa.atividadeExiste(list.get(0).getId()));
		
		//como testar se ela eh falsa? Nao tem sentido chutar uma id, pq ela pode existir...
		
	}
		
	public void testAtualizarStatusDaAtividade() {
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(sa.atualizarStatusDaAtividade(list.get(0).getId(), true));
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(list.get(0).isConcluida());
		
		assertTrue(sa.atualizarStatusDaAtividade(list.get(0).getId(), false));
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
				
		if (list.get(0).isConcluida())
			fail("A tarefa devia estar com o status falso!");
		
	}

	public void testGetAtividade() {
		
//		Calendar cal = Calendar.getInstance();
//		cal.set(2005, 01, 01);
//		Date dataInicio2 = new Date(cal.getTimeInMillis());
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		TipoAtividade a = sa.getAtividade(list.get(0).getId());
		
		assertTrue(a.getDescricao().equals("Executar testes"));
//		assertTrue(a.getDataInicio().getTime() == dataInicio2.getTime());
		assertTrue(a.getDesenvolvedor().getEmail().equals("amilcarsj@gmail.com"));
		
		if (a.getDesenvolvedor().getEmail().equals("asju@gmail.com"))
			fail("email do desenvolvedor errado.");
		
	}
	
	public void testAdicionarConhecimentoAAtividade() {
		
		this.adicionarConhecimentos();
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
				
		int idAtividade = list.get(0).getId();
		
		assertTrue(sa.adicionarConhecimentoAAtividade(idAtividade, "SQL"));
		
		assertTrue(sa.adicionarConhecimentoAAtividade(idAtividade,"MySQL"));
		
		if (sa.adicionarConhecimentoAAtividade(-1,"MySQL"))
			fail("atividade não devia existir ");
		
		
	}

	public void testGetConhecimentosEnvolvidosNaAtividade() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		int idAtividade = list.get(0).getId();
		
		assertTrue(sa.getConhecimentosEnvolvidosNaAtividade(idAtividade).get(0).getNome().equals("MySQL"));
		assertTrue(sa.getConhecimentosEnvolvidosNaAtividade(idAtividade).get(1).getNome().equals("SQL"));

		if (sa.getConhecimentosEnvolvidosNaAtividade(idAtividade).get(0).getNome().equals("PostgreSQL"))
			fail("Não existe o conhecimento PostgreSQL associado a tal atividade");
		if (sa.getConhecimentosEnvolvidosNaAtividade(idAtividade).get(0).getNome().equals("Conhecimento"))
			fail("Não existe o conhecimento no banco");

	}
	
	public void testRemoverConhecimentoDaAtividade() {
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		int idAtividade = list.get(0).getId();
		
		assertTrue(sa.removerConhecimentoDaAtividade(idAtividade, "SQL"));
		assertTrue(sa.removerConhecimentoDaAtividade(idAtividade, "MySQL"));
		
		if (sa.removerConhecimentoDaAtividade(idAtividade, "SQL"))
			fail("Essa associacao deveria ter sido removida");
		
		if (sa.removerConhecimentoDaAtividade(-1, "SQL"))
			fail("Não existe tal atividade!");
		
		if (sa.removerConhecimentoDaAtividade(idAtividade, "Conhecimento"))
			fail("Tal conhecimento nao existe no banco");
		
		this.removerConhecimentos();
	}

	public void testAssociarAtividades(){
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
	
		ArrayList<TipoAtividade> list2 = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		
		int idAtividade = list.get(0).getId();
		int idAtividadePai = list2.get(0).getId();
		
		assertTrue(sa.associarAtividades(idAtividade, idAtividadePai));
		
		idAtividade = list.get(1).getId();
		
		assertTrue(sa.associarAtividades(idAtividade, idAtividadePai));
		
		
	}
	
	public void testGetSubAtividades() {
		
		ArrayList<TipoAtividade> list2 = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		int idAtividadePai = list2.get(0).getId();
		
		
		ArrayList<TipoAtividade> list = sa.getSubAtividades(idAtividadePai);
		
		assertTrue(list.get(0).getDesenvolvedor().getEmail().equals("amilcarsj@gmail.com"));
		assertTrue(list.get(0).getSupervisor().getEmail().equals("asju@gmail.com"));
		
		assertTrue(list.get(1).getDesenvolvedor().getEmail().equals("amilcarsj@gmail.com"));
		assertTrue(list.get(1).getSupervisor().getEmail().equals("asju@gmail.com"));
						
	}

	public void testRemoverAtividade() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(sa.removerAtividade(list.get(0).getId()));
			
		list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(sa.removerAtividade(list.get(0).getId()));
		assertTrue(sa.removerAtividade(list.get(1).getId()));
		
		if (sa.removerAtividade(list.get(0).getId()))
			fail("atividade não deveria existir mais...");
		
		this.removerDesenvolvedores();
		}

}
