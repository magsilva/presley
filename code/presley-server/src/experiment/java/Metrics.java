import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Metrics {

	private File[] recommendationsFiles;
	private File[] repliesFiles;
	private double[] precisions;
	private double[] recalls;
	private double precisionTotal;
	private double recallTotal;
	private int total;


	public Metrics(String path) {
		File directory = new File(path);
		
		recommendationsFiles = directory.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".recomendations");  
			}  
		}); 	
		
		repliesFiles = directory.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".emails");  
			}  
		});
		
		precisions = new double[recommendationsFiles.length];
		recalls = new double[recommendationsFiles.length];
		precisionTotal = 0;
		recallTotal = 0;
		
	}

	public static void main(String[] args) throws FileNotFoundException {
		Metrics metrics = new Metrics(args[0]);
		metrics.calculate();
		metrics.report();
		metrics.detailedReport();
		
	}	
	
	public void detailedReport() throws FileNotFoundException {
		double precisionUntil;
		double recallUntil;
		int totalUntil;
		double precisionBy;
		double recallBy;
		int totalBy;
		
		System.out.println("PrecisionBy\tRecallBy\tPrecisionUntil\tRecallUntil");
		
		for (int n = 1; n <= 9; n++) {
			precisionUntil = 0;
			recallUntil = 0;
			totalUntil = 0;
			precisionBy = 0;
			recallBy = 0;
			totalBy = 0;
			for (int i = 0; i < this.repliesFiles.length; i++) {
				File repliesFile = this.repliesFiles[i];
				int numberOfReplies = getEmails(repliesFile).size();
				if (numberOfReplies <= n) {
					precisionUntil += precisions[i];
					recallUntil += recalls[i];
					totalUntil++;
				}
				if (numberOfReplies == n) {
					precisionBy += precisions[i];
					recallBy += recalls[i];
					totalBy++;
				}
				
			}
				
			precisionUntil = precisionUntil/totalUntil;
			recallUntil = recallUntil/totalUntil;
			/*System.out.println("N=" + n + "\t\tMessages=" + totalUntil);
			System.out.println("precision\t" + precisionUntil);
			System.out.println("recall\t\t" + recallUntil);*/
			
			precisionBy = precisionBy/totalBy;
			recallBy = recallBy/totalBy;
			/*System.out.println("N=" + n + "\t\tMessages=" + totalBy);
			System.out.println("precision\t" + precisionBy);
			System.out.println("recall\t\t" + recallBy);*/
			System.out.println(precisionBy + "\t" + recallBy + "\t" + precisionUntil + "\t" + recallUntil);
			
		}
	}
	

	public void calculate() throws FileNotFoundException {
		for (int i = 0; i < this.recommendationsFiles.length; i++) {
			File recommendationsFile = this.recommendationsFiles[i];
			File repliesFile = this.repliesFiles[i];
			
			System.out.println("file " + recommendationsFile.getName());
			List<String> recommendations = getEmails(recommendationsFile);
			List<String> replies = getEmails(repliesFile);
			
			precisions[i] = precision(recommendations, replies);
			recalls[i] = recall(recommendations, replies);
		}	
		
		total = this.recommendationsFiles.length;
				
		for (int i = 0; i < total; i++) {
			precisionTotal += this.precisions[i];
			recallTotal += this.recalls[i];
		}
		precisionTotal = precisionTotal/total;
		recallTotal = recallTotal/total;
		
	}

	private double recall(List<String> recommendations, List<String> replies) {
		int numberOfCorrectRecommendations = getNumberOfCorrectRecommendations(recommendations, replies);
		int numberOfReplies = replies.size();
		double recall = (double)numberOfCorrectRecommendations/numberOfReplies;
		System.out.println("recall " + recall);
		return recall;
	}

	private double precision(List<String> recommendations, List<String> replies) {
		int numberOfCorrectRecommendations = getNumberOfCorrectRecommendations(recommendations, replies);
		int numberOfRecommendations = recommendations.size();
		double precision = (double)numberOfCorrectRecommendations/numberOfRecommendations;
		System.out.println("precision " + precision);
		return precision;
	}

	private int getNumberOfCorrectRecommendations(List<String> recommendations, List<String> replies) {
		int hits = 0;
		for (String recommendation : recommendations) {
			if (replies.contains(recommendation)) {
				hits++;
			}
		}
		return hits;
	}

	public void report() {
		System.out.println("Total de emails " + total);
		System.out.println("Precision\t\t" + precisionTotal);
		System.out.println("Recall\t\t\t" + recallTotal);
	}
	
	private List<String> getEmails(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		List<String> emails = new ArrayList<String>();
		
		while (scanner.hasNext()) {
			emails.add(scanner.next());
		}
		return emails;
	}

}
