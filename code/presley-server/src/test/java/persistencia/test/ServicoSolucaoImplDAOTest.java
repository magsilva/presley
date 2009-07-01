package persistencia.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import com.hukarz.presley.beans.Solucao;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoSolucaoImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;
import com.hukarz.presley.server.persistencia.interfaces.ServicoSolucao;

public class ServicoSolucaoImplDAOTest extends TestCase {
	private ServicoDesenvolvedor sd;
	private ServicoProblema sp;
	private ServicoSolucao ss;
	
	protected void setUp() throws Exception {
		
		sd = new ServicoDesenvolvedorImplDAO();
		sp = new ServicoProblemaImplDAO();
		ss = new ServicoSolucaoImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private void criarDesenvolvedores() {
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "João Pessoa", "000000"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama", "123456"));
		
	}
	
	private void criarProblema() {
/*		
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 02, 15);
		Date dataDoRelato = new Date(cal.getTimeInMillis());
		
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		sp.cadastrarProblema(list.get(0).getId(), "Erro de transacao", dataDoRelato, "Stack trace do problema ...");
		sp.cadastrarProblema(list.get(0).getId(), "Erro de conexao", dataDoRelato, "Deu o seguinte erro de porta!! ...");
	
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
	
		sp.cadastrarProblema(list.get(0).getId(), "Erro de classpath", dataDoRelato, "Stack trace do problema, não acha o classpath ...");

*
*/
	}
	

	private void removerProblemas() {
/*		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		
		assertTrue(sp.removerProblema(plist.get(0)));
		assertTrue(sp.removerProblema(plist.get(1)));
		
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(sp.removerProblema(plist.get(0)));
*/		
	}
	
	private void removerDesenvolvedores(){
		assertTrue(sd.removerDesenvolvedor("amilcarsj@gmail.com"));
		
		assertTrue(sd.removerDesenvolvedor("asju@gmail.com"));
	}
	public void testCadastrarSolucao() {
				
/*		this.criarProblema();
		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
				
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 01, 01);
		Date dataDaProposta = new Date(cal.getTimeInMillis());
		
*/		
/*
  		assertTrue(ss.cadastrarSolucao("amilcarsj@gmail.com", plist.get(0).getId(), 
				dataDaProposta, "Isso provavelmente é erro no classpath, tente alterá-lo..."));
		
		assertTrue(ss.cadastrarSolucao("asju@gmail.com", plist.get(0).getId(), 
				dataDaProposta, "Deve ser bug da propria biblioteca, pesquisa no google ..."));
	
		if (ss.cadastrarSolucao("asju10@gmail.com", plist.get(0).getId(), 
				dataDaProposta, "Deve ser bug da propria biblioteca, pesquisa no google ..."))
				fail("não existe esse usuario!");
*/	
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
//		this.removerAtividade();
		
		ArrayList<Solucao> list = ss.listarSolucoesDoDesenvolvedor("amilcarsj@gmail.com");
		
		assertTrue(ss.removerSolucao(list.get(0).getId()));
		
		list = ss.listarSolucoesDoDesenvolvedor("asju@gmail.com");
		
		assertTrue(ss.removerSolucao(list.get(0).getId()));
		
		if (ss.removerSolucao(list.get(0).getId()))
			fail("essa solucao não devia existir mais no banco!");
		
		this.removerDesenvolvedores();
	}

	

}
