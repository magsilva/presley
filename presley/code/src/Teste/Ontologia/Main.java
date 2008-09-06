package Testes;

import presley.ontologia.*;

enum CONHECIMENTO { 
	ON(0), J(1), C(2), JU(3), QS(4), P(5); 
	
	private int id;

		CONHECIMENTO(int id)
		{
			this.id = id;
		}
		
		public int getId()
		{
			return this.id;
		}
}

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * 						ON
		 * 					/		\
		 * 				J				C
		 * 			/		\		/		\
		 * 		JU				QS			P
		 */
				
		boolean[][] DAG = new boolean[6][6];
		
		DAG[CONHECIMENTO.ON.getId()][CONHECIMENTO.J.getId()] = true;
		DAG[CONHECIMENTO.ON.getId()][CONHECIMENTO.C.getId()] = true;
		DAG[CONHECIMENTO.J.getId()][CONHECIMENTO.JU.getId()] = true;
		DAG[CONHECIMENTO.J.getId()][CONHECIMENTO.QS.getId()] = true;
		DAG[CONHECIMENTO.C.getId()][CONHECIMENTO.QS.getId()] = true;
		DAG[CONHECIMENTO.C.getId()][CONHECIMENTO.P.getId()] = true;
		
		int counts[][] = new int [3][6];
		
		counts[0][CONHECIMENTO.P.getId()] = 2;
		counts[0][CONHECIMENTO.QS.getId()] = 3;
		counts[0][CONHECIMENTO.JU.getId()] = 1;
		counts[0][CONHECIMENTO.J.getId()] = 2;
		counts[0][CONHECIMENTO.C.getId()] = 4;
		counts[0][CONHECIMENTO.ON.getId()] = 6;
		
		counts[1][CONHECIMENTO.P.getId()] = 1;
		counts[1][CONHECIMENTO.QS.getId()] = 5;
		counts[1][CONHECIMENTO.JU.getId()] = 1;
		counts[1][CONHECIMENTO.J.getId()] = 6;
		counts[1][CONHECIMENTO.C.getId()] = 1;
		counts[1][CONHECIMENTO.ON.getId()] = 7;
		
		counts[2][CONHECIMENTO.P.getId()] = 1;
		counts[2][CONHECIMENTO.QS.getId()] = 1;
		counts[2][CONHECIMENTO.JU.getId()] = 1;
		counts[2][CONHECIMENTO.J.getId()] = 2;
		counts[2][CONHECIMENTO.C.getId()] = 1;
		counts[2][CONHECIMENTO.ON.getId()] = 3;
		
		Ontologia ont = new Ontologia(DAG, counts);
		
		int [] score = new int[3];
		
		for(int i=1; i < 6; i++)
		{
			System.out.println(ont.getScore(0, i));
			System.out.println(ont.getScore(1, i));
			//System.out.println(ont.getScore(2, i));
			
			System.out.println("**************************");
		}	
	}

}
