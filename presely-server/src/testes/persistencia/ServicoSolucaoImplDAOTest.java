package testes.persistencia;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import beans.TipoAtividade;
import beans.Problema;
import beans.Solucao;
import persistencia.implementacao.ServicoAtividadeImplDAO;
import persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import persistencia.implementacao.ServicoProblemaImplDAO;
import persistencia.implementacao.ServicoSolucaoImplDAO;
import persistencia.interfaces.ServicoAtividade;
import persistencia.interfaces.ServicoDesenvolvedor;
import persistencia.interfaces.ServicoProblema;
import persistencia.interfaces.ServicoSolucao;
import junit.framework.TestCase;

public class ServicoSolucaoImplDAOTest extends TestCase {
	private ServicoDesenvolvedor sd;
	private ServicoProblema sp;
	private ServicoSolucao ss;
	private ServicoAtividade sa;
	
	protected void setUp() throws Exception {
		
		sd = new ServicoDesenvolvedorImplDAO();
		sp = new ServicoProblemaImplDAO();
		ss = new ServicoSolucaoImplDAO();
		sa = new ServicoAtividadeImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void criarDesenvolvedores() {
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "João Pessoa"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama"));
		
	}
	
	private void criarProblema() {
		this.cadastrarAtividades();
		
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 02, 15);
		Date dataDoRelato = new Date(cal.getTimeInMillis());
		
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		sp.cadastrarProblema(list.get(0).getId(), "Erro de transacao", dataDoRelato, "Stack trace do problema ...");
		sp.cadastrarProblema(list.get(0).getId(), "Erro de conexao", dataDoRelato, "Deu o seguinte erro de porta!! ...");
	
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
	
		sp.cadastrarProblema(list.get(0).getId(), "Erro de classpath", dataDoRelato, "Stack trace do problema, não acha o classpath ...");
		
	}
	
	private void cadastrarAtividades() {
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

	private void removerAtividade() {
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(sa.removerAtividade(list.get(0).getId()));
			
		list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(sa.removerAtividade(list.get(0).getId()));
		assertTrue(sa.removerAtividade(list.get(1).getId()));
		
	}

	private void removerProblemas() {
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		
		assertTrue(sp.removerProblema(plist.get(0).getId()));
		assertTrue(sp.removerProblema(plist.get(1).getId()));
		
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(sp.removerProblema(plist.get(0).getId()));
		
	}
	
	private void removerDesenvolvedores(){
		
		assertTrue(sd.removerDesenvolvedor("amilcarsj@gmail.com"));
		
		assertTrue(sd.removerDesenvolvedor("asju@gmail.com"));
	}
	public void testCadastrarSolucao() {
				
		this.criarProblema();
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
				
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 01, 01);
		Date dataDaProposta = new Date(cal.getTimeInMillis());
		
		
		assertTrue(ss.cadastrarSolucao("amilcarsj@gmail.com", plist.get(0).getId(), 
				dataDaProposta, "Isso provavelmente é erro no classpath, tente alterá-lo..."));
		
		assertTrue(ss.cadastrarSolucao("asju@gmail.com", plist.get(0).getId(), 
				dataDaProposta, "Deve ser bug da propria biblioteca, pesquisa no google ..."));
	
		if (ss.cadastrarSolucao("asju10@gmail.com", plist.get(0).getId(), 
				dataDaProposta, "Deve ser bug da propria biblioteca, pesquisa no google ..."))
				fail("não existe esse usuario!");
	
	}
	
	public void testListarSolucoesDoDesenvolvedor() {
		
		ArrayList<Solucao> list = ss.listarSolucoesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(list.get(0).getMensagem().equals("Isso provavelmente é erro no classpath, tente alterá-lo..."));
		
		if (list.get(0).getMensagem().equals("Isso provavelmente é erro no classpath, tente alterá-lo..."))
		
		list = ss.listarSolucoesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(list.get(0).getMensagem().equals("Deve ser bug da propria biblioteca, pesquisa no google ..."));
		
	}

	public void testAtualizarStatusDaSolução() {
		
		ArrayList<Solucao> list = ss.listarSolucoesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(ss.atualizarStatusDaSolucao(list.get(0).getId(), true));
		
		list = ss.listarSolucoesDoDesenvolvedor("amilcarsj@gmail.com");
		
		if (!list.get(0).isAjudou())
			fail("deveria estar verdadeiro");
		
		list = ss.listarSolucoesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(ss.atualizarStatusDaSolucao(list.get(0).getId(), false));
		
		list = ss.listarSolucoesDoDesenvolvedor("asju@gmail.com");
		
		if (list.get(0).isAjudou())
			fail("deveria estar falso");
		
	}
	
	public void testeGetSolucao(){
		
		ArrayList<Solucao> list = ss.listarSolucoesDoDesenvolvedor("amilcarsj@gmail.com");
		
		Solucao s = ss.getSolucao(list.get(0).getId());
		
		assertTrue(s.getMensagem().equals("Isso provavelmente é erro no classpath, tente alterá-lo..."));
		assertTrue(s.getDesenvolvedor().getEmail().equals("amilcarsj@gmail.com"));
		assertTrue(s.getProblema().getMensagem().equals("Stack trace do problema, não acha o classpath ..."));
		
		
	}

	public void testListarSolucoesAceitasDoDesenvolvedor() {
		
		ArrayList<Solucao> list = ss.listarSolucoesAceitasDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(list.get(0).getMensagem().equals("Isso provavelmente é erro no classpath, tente alterá-lo..."));
						
	}

	public void testListarSolucoesRejeitadasDoDesenvolvedor() {
		ArrayList<Solucao> list = ss.listarSolucoesRejeitadasDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(list.get(0).getMensagem().equals("Deve ser bug da propria biblioteca, pesquisa no google ..."));
		
	}

	public void testRemoverSolucao() {
		this.removerProblemas();
		this.removerAtividade();
		
		ArrayList<Solucao> list = ss.listarSolucoesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(ss.removerSolucao(list.get(0).getId()));
		
		list = ss.listarSolucoesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(ss.removerSolucao(list.get(0).getId()));
		
		if (ss.removerSolucao(list.get(0).getId()))
			fail("essa solucao não devia existir mais no banco!");
		
		this.removerDesenvolvedores();
	}

	

}
