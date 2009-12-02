import java.io.FileNotFoundException;


public class UnsolvedProblemsLocator extends Metrics {
	
	public UnsolvedProblemsLocator(String path, int maxNumberOfRecommendations) {
		super(path, maxNumberOfRecommendations);
	}

	public void listUnsolvedProblems() {
		for (int i = 0; i < this.recommendationsFiles.length; i++) {
			if (0 == this.precisions[i]) {
				String filename = this.recommendationsFiles[i].getName().replace(".recomendations", ".question");
				System.out.print(filename + " ");
			}
		}
	}


	public static void main(String[] args) throws FileNotFoundException {
		String path = args[0];
		int maxNumberOfRecommendations = Integer.parseInt(args[1]);
		UnsolvedProblemsLocator upl = new UnsolvedProblemsLocator(path, maxNumberOfRecommendations);

		upl.compute();
		upl.listUnsolvedProblems();
		

	}
}
