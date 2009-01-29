package testes.persistencia;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for testes.persistencia");
		//$JUnit-BEGIN$
		suite.addTestSuite(ServicoAtividadeImplDAOTest.class);
		suite.addTestSuite(ServicoDesenvolvedorImplDAOTest.class);
		suite.addTestSuite(ServicoConhecimentoImplDAOTest.class);
		suite.addTestSuite(ServicoSolucaoImplDAOTest.class);
		suite.addTestSuite(ServicoProblemaImplDAOTest.class);
		//$JUnit-END$
		return suite;
	}

}
