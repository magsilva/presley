package dados.cvs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Precision {

	static String PATH = "C:/java/lucene/Experimento_Final/emails/";

	private File[] recommendationsFiles;
	private int[] frequencyOfCorrectRecommendations;
	private int threadsWithoutReplies;
	private int threadsWithReplies;

	public Precision(String path2) {
		File directory = new File(PATH);
		frequencyOfCorrectRecommendations = new int[7];
		threadsWithoutReplies = 0;
		threadsWithReplies = 0;

		recommendationsFiles = directory.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".recomendations");  
			}  
		}); 	
	}

	public static void main(String[] args) throws FileNotFoundException {
		Precision precision = new Precision(PATH);
		precision.calculate();
		precision.report();
	}

	private void calculate() throws FileNotFoundException {
		for (File recommendationFile : this.recommendationsFiles) {
			String[] recommendations = allRecommendations(recommendationFile);
			String replies = allReplies(recommendationFile);

			int accepts = 0;
			for (String recommendation : recommendations) {
				if (replies.contains(recommendation)) {
					accepts++;
				}
			}
			this.frequencyOfCorrectRecommendations[accepts]++;
		}		
	}

	private void report() {
		int correctRecommendations = 0;

		System.out.println("Total de emails " + (this.threadsWithReplies + this.threadsWithoutReplies));
		System.out.println("Emails sem resposta " + this.threadsWithoutReplies);
		System.out.println("Emails com resposta " + this.threadsWithReplies);

		System.out.println("0 acertos\t\t" + (this.frequencyOfCorrectRecommendations[0] - this.threadsWithoutReplies));
		
		for (int i = 1; i < this.frequencyOfCorrectRecommendations.length; i++) {
			System.out.println(i + " acertos\t\t" + this.frequencyOfCorrectRecommendations[i]);
			correctRecommendations += this.frequencyOfCorrectRecommendations[i];
		}
		System.out.println("Total de acertos\t" + correctRecommendations);
		System.out.println("Precisão\t\t" + ((float)correctRecommendations/this.threadsWithReplies));		
	}
	
	

	private String[] allRecommendations(File recomendationFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(recomendationFile);
		List<String> recommendations = new ArrayList<String>();
		while (scanner.hasNext()) {
			recommendations.add(scanner.next());
		}
		return recommendations.toArray(new String[0]);
	}

	
	
	private String allReplies(File recomendationFile) throws FileNotFoundException {
		File file = getReplyFileByRecommendationFile(recomendationFile);
		Scanner scanner = new Scanner(file);
		
		if (file.length() == 0) {
			++threadsWithoutReplies;
			return "";
		}

		StringBuilder emailsBuilder = new StringBuilder();
		while (scanner.hasNext()) {
			emailsBuilder.append(scanner.next());
		}
		++threadsWithReplies;
		return emailsBuilder.toString();
	}

	private File getReplyFileByRecommendationFile(File recommendationFile) throws FileNotFoundException {
		int dotIndex = recommendationFile.getName().indexOf('.');       		        		
		String index = recommendationFile.getName().substring(0, dotIndex);
		return new File(PATH + index + ".emails");
	}

}
