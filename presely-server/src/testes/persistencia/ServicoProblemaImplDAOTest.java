package testes.persistencia;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import beans.TipoAtividade;
import beans.Problema;

import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.implementacao.ServicoProblemaImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoDesenvolvedor;
import persistencia.interfaces.ServicoProblema;

import junit.framework.TestCase;

public class ServicoProblemaImplDAOTest extends TestCase {

	private ServicoAtividade sa;
	private ServicoDesenvolvedor sd;
	private ServicoProblema sp;
	
	protected void setUp() throws Exception {
		sa = new ServicoAtividadeImplDAO();
		sd = new ServicoDesenvolvedorImplDAO();
		sp = new ServicoProblemaImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void criarDesenvolvedores() {
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "João Pessoa"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama"));
		
	}

	private void removerDesenvolvedores(){
		
		assertTrue(sd.removerDesenvolvedor("amilcarsj@gmail.com"));
	
		assertTrue(sd.removerDesenvolvedor("asju@gmail.com"));
	}
	
	private void cadastrarAtividades(){
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
				dataInicio, dataFim));
		
		assertTrue(sa.cadastrarAtividade("amilcarsj@gmail.com", "asju@gmail.com", "Executar testes",
				dataInicio2, dataFim2));
		
		assertTrue(sa.cadastrarAtividade("amilcarsj@gmail.com", "asju@gmail.com", "Criar testes",
				dataInicio2, dataFim2));
	
	}
	
	private void removerAtividades(){
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(sa.removerAtividade(list.get(0).getId()));
			
		list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(sa.removerAtividade(list.get(0).getId()));
		assertTrue(sa.removerAtividade(list.get(1).getId()));
		
		if (sa.removerAtividade(list.get(0).getId()))
			fail("atividade não deveria existir mais...");
		
		this.removerDesenvolvedores();
		
	}
	
	public void testCadastrarProblema() {
		this.cadastrarAtividades();
		
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 02, 15);
		Date dataDoRelato = new Date(cal.getTimeInMillis());
		
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		sp.cadastrarProblema(list.get(0).getId(), "Erro de transacao", dataDoRelato, "Stack trace do problema ...");
		sp.cadastrarProblema(list.get(0).getId(), "Erro de conexao", dataDoRelato, "Deu o seguinte erro de porta!! ...");
	
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
	
		sp.cadastrarProblema(list.get(0).getId(), "Erro de classpath", dataDoRelato, "Stack trace do problema, não acha o classpath ...");
				
		if (sp.cadastrarProblema(-1, "Erro de transacao", dataDoRelato, "Stack trace do problema ..."))
			fail("Não existe usuario com essa id");
			
	}
	
	public void testListarProblemasDaAtividade() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(plist.get(0).getDescricao().equals("Erro de transacao"));
		assertTrue(plist.get(0).getMensagem().equals("Stack trace do problema ..."));
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		if(plist.get(0).getDescricao().equals("Erro de transacao"))
			fail("Descricao errada");
		
	}
	
	public void testAtualizarStatusDoProblema() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
				
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(sp.atualizarStatusDoProblema(plist.get(0).getId(), true));
		
		assertTrue(sp.atualizarStatusDoProblema(plist.get(1).getId(), false));
				
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		if(!plist.get(0).isResolvido())
			fail("problema deveria estar true!!");
		
		if(plist.get(1).isResolvido())
			fail("problema deveria estar false!!");
		
	}	
	
	public void testGetProblema(){
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		
		Problema p = sp.getProblema(plist.get(0).getId());
		
		assertTrue(p.getDescricao().equals("Erro de transacao"));
		assertTrue(p.getMensagem().equals("Stack trace do problema ..."));
		assertTrue(p.getAtividade().getDescricao().equals("Executar testes"));
		
		
	}
	
	public void testRemoverProblema() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
			
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		
		assertTrue(sp.removerProblema(plist.get(0).getId()));
		assertTrue(sp.removerProblema(plist.get(1).getId()));
		
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(sp.removerProblema(plist.get(0).getId()));
		
		this.removerAtividades();
		
	}

}
