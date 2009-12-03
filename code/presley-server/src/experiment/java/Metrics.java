import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Metrics {

	private static final int ALL_EMAILS = 0;
	protected int maxNumberOfRecommendations;
	protected File[] recommendationsFiles;
	protected File[] repliesFiles;
	protected double[] precisions;
	protected double[] recalls;
	protected double precisionTotal;
	protected double recallTotal;
	protected int total;
	protected double precisionPlus;
	protected double recallPlus;
	protected int totalPlus;


	public Metrics(String path, int maxNumberOfRecommendations) {
		File directory = new File(path);
		this.maxNumberOfRecommendations = maxNumberOfRecommendations;
		
		this.recommendationsFiles = directory.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".recomendations");  
			}  
		}); 	
		
		this.repliesFiles = directory.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".emails");  
			}  
		});
		
		this.precisions = new double[recommendationsFiles.length];
		this.recalls = new double[recommendationsFiles.length];
		this.total = precisions.length;
		this.precisionTotal = 0;
		this.recallTotal = 0;
		
		totalPlus = 0;
		precisionPlus = 0;
		recallPlus = 0;
		
	}

	public void compute() throws FileNotFoundException {
		computeAllPrecisionAndRecall();	
		
		for (int i = 0; i < total; i++) {
			precisionTotal += this.precisions[i];
			recallTotal += this.recalls[i];
		}
		precisionTotal = precisionTotal/total;
		recallTotal = recallTotal/total;
		
	}
	
	public double F1() {
		return (2*precisionTotal*recallTotal)/(precisionTotal + recallTotal);
	}

	private void computeAllPrecisionAndRecall() throws FileNotFoundException {
		for (int i = 0; i < total; i++) {
			File recommendationsFile = this.recommendationsFiles[i];
			File repliesFile = this.repliesFiles[i];
			
			List<String> recommendations = getEmails(recommendationsFile, this.maxNumberOfRecommendations);
			List<String> replies = getEmails(repliesFile, ALL_EMAILS);
			
			precisions[i] = precision(recommendations, replies);
			recalls[i] = recall(recommendations, replies);
				
		}
	}

	private double recall(List<String> recommendations, List<String> replies) {
		int numberOfCorrectRecommendations = getNumberOfCorrectRecommendations(recommendations, replies);
		int numberOfReplies = replies.size();
		double recall = 0;
		if (numberOfCorrectRecommendations > 0)
			recall = (double)numberOfCorrectRecommendations/numberOfReplies;
		return recall;
	}

	private double precision(List<String> recommendations, List<String> replies) {
		int numberOfCorrectRecommendations = getNumberOfCorrectRecommendations(recommendations, replies);
		int numberOfRecommendations = recommendations.size();
		double precision = 0;
		if (numberOfCorrectRecommendations > 0)
			precision = (double)numberOfCorrectRecommendations/numberOfRecommendations;
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
	
	public void computePlus() {
		for (int i = 0; i < total; i++) {
			precisionPlus += this.precisions[i];
			recallPlus += this.recalls[i];
			if (this.precisions[i] != 0) {
				totalPlus++;
			}
		}
		
		precisionPlus = precisionPlus/totalPlus;
		recallPlus = recallPlus/totalPlus;
		
		//System.out.println((2*precisionPlus*recallPlus)/(precisionPlus + recallPlus));
	}

	public void report() {
		System.out.println("********************************");
		System.out.println("Number of recommendations: " + this.maxNumberOfRecommendations);
		System.out.println("Emails: " + total);
		System.out.println("Precision\t\t" + precisionTotal);
		System.out.println("Recall\t\t\t" + recallTotal);
	}
	
	public void reportPlus() {
		System.out.println("********************************");
		System.out.println("Number of recommendations: " + this.maxNumberOfRecommendations);
		System.out.println("Emails w/ successfull recommendation: " + totalPlus 
				+ "(" + (double)totalPlus/total + ")");
		System.out.println("Precision\t\t" + precisionPlus);
		System.out.println("Recall\t\t\t" + recallPlus);
	}
	
	
	
	/**
	 * Returns all emails in the file as a list
	 * @param file Filename of the file with the emails
	 * @param maxEmails Number maximum of emails to fetch. Use ALL_EMAILS to fetch all.
	 * @return A List<String> with all emails
	 * @throws FileNotFoundException
	 */
	private List<String> getEmails(File file, int maxEmails) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		List<String> emails = new ArrayList<String>();
		int i = 0;
		
		if (ALL_EMAILS == maxEmails) {
			i = Integer.MIN_VALUE;
		}
		
		while (scanner.hasNext() && i < maxEmails) {
			emails.add(scanner.next());
			i++;
		}
		return emails;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		String path = args[0];
		int maxNumberOfRecommendations = Integer.parseInt(args[1]);
		
		
		for (int i = 1; i <= maxNumberOfRecommendations; i++ ) {
			Metrics metrics = new Metrics(path, i);
			metrics.compute();
			metrics.report();
			metrics.computePlus();
			metrics.reportPlus();
		}
		
	}	
	
	
	public void detailedReport() throws FileNotFoundException {
		reportBy();
		reportUntil();
	}
	
	private void reportUntil() throws FileNotFoundException {
		double precision;
		double recall;
		int totalPositive;
		int total;
		
		System.out.println("REPORT UNTIL");
		for (int n = 1; n <= this.maxNumberOfRecommendations; n++) {
			precision = 0;
			recall = 0;
			total = 0;
			totalPositive = 0;

			for (int i = 0; i < this.repliesFiles.length; i++) {
				File repliesFile = this.repliesFiles[i];
				int numberOfReplies = getEmails(repliesFile, ALL_EMAILS).size();
				
				if (numberOfReplies <= n) {
					precision += precisions[i];
					recall += recalls[i];
					total++;					
					if (precisions[i] != 0) {
						totalPositive++;
					}
				}
			}

			printReport(precision, recall, total, totalPositive, n);
		}
	}

	private void printReport(double precision, double recall,
			int total, int totalPositive, int n) {
		System.out.println("N = " + n + "\t\tTotal = " + total + "\t\tTotal+ = " + totalPositive);
		System.out.println("precision \t" + precision/total);
		System.out.println("precision+\t" + precision/totalPositive);
		System.out.println("recall \t\t" + recall/total);
		System.out.println("recall+\t\t" + recall/totalPositive);
	}

	private void reportBy() throws FileNotFoundException {
		double precision;
		double recall;
		int totalPositive;
		int total;
		
		System.out.println("REPORT BY");
		for (int n = 1; n <= this.maxNumberOfRecommendations; n++) {
			precision = 0;
			recall = 0;
			total = 0;
			totalPositive = 0;

			for (int i = 0; i < this.repliesFiles.length; i++) {
				File repliesFile = this.repliesFiles[i];
				int numberOfReplies = getEmails(repliesFile, ALL_EMAILS).size();
				
				if (numberOfReplies == n) {
					precision += precisions[i];
					recall += recalls[i];
					total++;					
					if (precisions[i] != 0) {
						totalPositive++;
					}
				}
			}
			printReport(precision, recall, totalPositive, total, n);
		}
	}

}
