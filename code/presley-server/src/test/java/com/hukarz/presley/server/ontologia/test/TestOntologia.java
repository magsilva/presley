package com.hukarz.presley.server.ontologia.test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import junit.framework.TestCase;

import com.hukarz.presley.server.ontologia.Ontologia;

public class TestOntologia extends TestCase {

	private Ontologia ontologia;
	private boolean[][] DAG;
	private int counts[][];
	
	enum CONHECIMENTO {
		ON(0), J(1), C(2), JU(3), QS(4), P(5);

		private int id;

		CONHECIMENTO(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}
	}

	public void setUp() throws Exception {
		super.setUp();
		
		DAG = new boolean[6][6];  //by default all false
		
		DAG[CONHECIMENTO.ON.getId()][CONHECIMENTO.J.getId()] = true;
		DAG[CONHECIMENTO.ON.getId()][CONHECIMENTO.C.getId()] = true;
		DAG[CONHECIMENTO.J.getId()][CONHECIMENTO.JU.getId()] = true;
		DAG[CONHECIMENTO.J.getId()][CONHECIMENTO.QS.getId()] = true;
		DAG[CONHECIMENTO.C.getId()][CONHECIMENTO.QS.getId()] = true;
		DAG[CONHECIMENTO.C.getId()][CONHECIMENTO.P.getId()]  = true;
		
		counts = new int [2][6];
		
		counts[0][CONHECIMENTO.P.getId()]  = 1;
		counts[0][CONHECIMENTO.QS.getId()] = 2;
		counts[0][CONHECIMENTO.JU.getId()] = 0;
		counts[0][CONHECIMENTO.J.getId()]  = 1;
		counts[0][CONHECIMENTO.C.getId()]  = 2;
		counts[0][CONHECIMENTO.ON.getId()] = 3;
		
		counts[1][CONHECIMENTO.P.getId()]  = 0;
		counts[1][CONHECIMENTO.QS.getId()] = 5;
		counts[1][CONHECIMENTO.JU.getId()] = 0;
		counts[1][CONHECIMENTO.J.getId()]  = 5;
		counts[1][CONHECIMENTO.C.getId()]  = 0;
		counts[1][CONHECIMENTO.ON.getId()] = 5;
		
		ontologia = new Ontologia(DAG, counts);
	}
	
	

	@SuppressWarnings("unchecked")
	public void testGetScore() {
		//para desenvolver 0
		int expectedScore = 0; 
		
		LinkedList<Vector<CONHECIMENTO>> caminhosConhecimento = new LinkedList<Vector<CONHECIMENTO>>();
		Vector<CONHECIMENTO> v = new Vector<CONHECIMENTO>();
		
		v.add(CONHECIMENTO.ON); v.add(CONHECIMENTO.J); v.add(CONHECIMENTO.QS);
		caminhosConhecimento.add(v);
		
		v = new Vector<CONHECIMENTO>();
		v.add(CONHECIMENTO.ON); v.add(CONHECIMENTO.C); v.add(CONHECIMENTO.QS);
		
		caminhosConhecimento.add(v);
		
		//score QS
		int temp = 1;
		for (Iterator iterator = caminhosConhecimento.iterator(); iterator.hasNext();) {
			Vector ve = (Vector) iterator.next();
			for (Iterator iterator2 = ve.iterator(); iterator2.hasNext();) {
				CONHECIMENTO co = (CONHECIMENTO) iterator2.next();
				temp *= counts[0][co.getId()];
			}
			expectedScore += temp;
			temp = 1;
		}
		
		assertEquals(expectedScore, ontologia.getScore(0, 4));
	}

}

