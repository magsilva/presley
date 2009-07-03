package persistencia.test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import junit.framework.TestCase;


import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.implementacao.ServicoProblemaImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;
import com.hukarz.presley.server.persistencia.interfaces.ServicoProblema;

public class ServicoProblemaImplDAOTest extends TestCase {

	private ServicoDesenvolvedor sd;
	private ServicoProblema sp;
	
	protected void setUp() throws Exception {
		sd = new ServicoDesenvolvedorImplDAO();
		sp = new ServicoProblemaImplDAO();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private void criarDesenvolvedores() {
		
		assertTrue(sd.criarDesenvolvedor("amilcarsj@gmail.com", "Amilcar Soares", "João Pessoa", "123456"));
		
		assertTrue(sd.criarDesenvolvedor("asju@gmail.com", "ASJU", "Matsuyama", "654321"));
		
	}

	private void removerDesenvolvedores(){
		
		assertTrue(sd.removerDesenvolvedor("amilcarsj@gmail.com"));
	
		assertTrue(sd.removerDesenvolvedor("asju@gmail.com"));
	}
	
	
	public void testCadastrarProblema() {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2008, 02, 15);
		Date dataDoRelato = new Date(cal.getTimeInMillis());
		
		
//		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
/*
 * 		
		sp.cadastrarProblema(list.get(0).getId(), "Erro de transacao", dataDoRelato, "Stack trace do problema ...");
		sp.cadastrarProblema(list.get(0).getId(), "Erro de conexao", dataDoRelato, "Deu o seguinte erro de porta!! ...");
	
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
	
		sp.cadastrarProblema(list.get(0).getId(), "Erro de classpath", dataDoRelato, "Stack trace do problema, não acha o classpath ...");
				
		if (sp.cadastrarProblema(-1, "Erro de transacao", dataDoRelato, "Stack trace do problema ..."))
			fail("Não existe usuario com essa id");
			
*/
	}
	
	public void testAtualizarStatusDoProblema() {
/*		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
				
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(sp.atualizarStatusDoProblema(plist.get(0).getId(), true));
		
		assertTrue(sp.atualizarStatusDoProblema(plist.get(1).getId(), false));
				
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		if(!plist.get(0).isResolvido())
			fail("problema deveria estar true!!");
		
		if(plist.get(1).isResolvido())
			fail("problema deveria estar false!!");
*/		
	}	
	
	public void testGetProblema(){
/*		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
		
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		Problema p = sp.getProblema(plist.get(0).getId());
		
		assertTrue(p.getDescricao().equals("Erro de transacao"));
		assertTrue(p.getMensagem().equals("Stack trace do problema ..."));
		assertTrue(p.getAtividade().getDescricao().equals("Executar testes"));
		
*/	
	}
	
	public void testRemoverProblema() {
/*		
		ArrayList<TipoAtividade> list = sd.getAtividadesDoDesenvolvedor("amilcarsj@gmail.com");
			
		ArrayList<Problema> plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		
		assertTrue(sp.removerProblema(plist.get(0)));
		assertTrue(sp.removerProblema(plist.get(1)));
		
		
		list = sd.getAtividadesDoDesenvolvedor("asju@gmail.com");
		
		plist = sp.listarProblemasDaAtividade(list.get(0).getId());
		
		assertTrue(sp.removerProblema(plist.get(0)));
		
		this.removerAtividades();
*/		
	}

}