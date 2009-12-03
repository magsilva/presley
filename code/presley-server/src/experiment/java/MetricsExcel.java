import java.io.FileNotFoundException;


public class MetricsExcel extends Metrics {

	public MetricsExcel(String path, int maxNumberOfRecommendations) {
		super(path, maxNumberOfRecommendations);
	}

	@Override
	public void report() {
		System.out.print("\t" + precisionTotal+"\t" + recallTotal);
	}

	@Override
	public void reportPlus() {
		System.out.print("\t" + precisionPlus + "\t" + recallPlus);
		System.out.print("\t" + (double)totalPlus/total );
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		String path = args[0];
		int maxNumberOfRecommendations = Integer.parseInt(args[1]);
		
		for (int i = 1; i <= maxNumberOfRecommendations; i++ ) {
			MetricsExcel metrics = new MetricsExcel(path, i);
			metrics.compute();
			metrics.computePlus();
			metrics.report();
		}
		
		System.out.println();
		System.out.println("-----------------------------------");
		
		for (int i = 1; i <= maxNumberOfRecommendations; i++ ) {
			MetricsExcel metrics = new MetricsExcel(path, i);
			metrics.compute();
			metrics.computePlus();
			metrics.reportPlus();
		}
	}		
}
