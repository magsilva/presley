package dados.cvs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.hukarz.presley.beans.Desenvolvedor;
import com.hukarz.presley.server.persistencia.implementacao.ServicoDesenvolvedorImplDAO;
import com.hukarz.presley.server.persistencia.interfaces.ServicoDesenvolvedor;

public class AjusteArquivos {
	private String PATH = "C:/java/lucene/Experimento_Final/";
	private ServicoDesenvolvedor servicoDesenvolvedor = new ServicoDesenvolvedorImplDAO();
	
	private void ajustarArquivos() throws IOException, Exception {
		File diretorioCD = new File( PATH+"/question/" );

		File[] listagemDiretorio = diretorioCD.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith( ".question" );  
			}  
		}); 

		for (File arquivoQuestion : listagemDiretorio) {
			String linha = "";
			ArrayList<String> emails = new ArrayList<String>();

			FileReader fileReader = new FileReader(arquivoQuestion);
			BufferedReader reader = new BufferedReader(fileReader);

			System.out.println( arquivoQuestion.getAbsolutePath()  );
			// - Busca pelos e-mails dos desenvolvedores -
			linha = reader.readLine();
			String email = "";
			System.out.println( linha ) ;
			if (linha.contains("jira@apache.org")){
				String nome = extractNomeJira(linha);

				Desenvolvedor desenvolvedor ;
				desenvolvedor = servicoDesenvolvedor.getDesenvolvedorPorNome(nome);

				email = desenvolvedor.getEmail();
			} else {
				email = extractEmail(linha);
				servicoDesenvolvedor.autenticaDesenvolvedor(email, "1") ;
			}
			emails.add(email);
			
			
			// - Atualiza o arquivo -
			PrintWriter saidaQuestion = new PrintWriter(new FileOutputStream(arquivoQuestion));
			saidaQuestion.println( email );
			
			while( (linha = reader.readLine()) != null ){
				saidaQuestion.println( linha );
			}
						
			saidaQuestion.close();

			ajustarArquivosEmails( email, arquivoQuestion);
		}

	}

	private void ajustarArquivosEmails(String EmailQuestion, File arquivoQuestion) throws IOException, Exception {
		File arquivoEmail = new File( arquivoQuestion.toString().replace(".question", ".emails") );

		String linha = "";
		ArrayList<String> emails = new ArrayList<String>();

		FileReader fileReader = new FileReader(arquivoEmail);
		BufferedReader reader = new BufferedReader(fileReader);

		System.out.println( arquivoEmail.getAbsolutePath()  );
		if (arquivoEmail.getAbsolutePath().contains("1249165530156000.emails"))
			System.out.println();
			
		// - Busca pelos e-mails dos desenvolvedores -
		while( (linha = reader.readLine()) != null ){
			System.out.println( linha ) ;
			String email = "";
			if (linha.contains("jira@apache.org")){
				String nome = extractNomeJira(linha);

				Desenvolvedor desenvolvedor ;
				try {
					desenvolvedor = servicoDesenvolvedor.getDesenvolvedorPorNome(nome);
				} catch (Exception e) {
					desenvolvedor = servicoDesenvolvedor.autenticaDesenvolvedor(nome, "1");
				}

				email = desenvolvedor.getEmail();
			} else {
				email = servicoDesenvolvedor.getDesenvolvedorNaListaEmail(email).getEmail();
			}

			if (emails.indexOf(email)==-1 && !EmailQuestion.equals(email))
				emails.add(email);
		}


		// - Atualiza o arquivo -
		PrintWriter saidaPontuacao = new PrintWriter(new FileOutputStream(arquivoEmail));

		for (String email : emails) {
			saidaPontuacao.println( email );
		}

		saidaPontuacao.close();
		
	}
	
	private String extractEmail(String fromHeader) {
		
		if (fromHeader.contains("<")) {
			int beginIndex = fromHeader.indexOf('<') + 1;
			int endIndex = fromHeader.indexOf('>');
			return fromHeader.substring(beginIndex, endIndex);
		}
		else {
			return fromHeader;
		}
	}

	private String extractNomeJira(String fromHeader){
		int endIndex = fromHeader.indexOf("jira@apache.org");
		String nome = fromHeader.substring(0, endIndex);
		nome = nome.replace("<", "").replace(">", "");
		nome = nome.replace("\"", "");
		nome = nome.replace("(", "").replace(")", "");
		nome = nome.replace("JIRA", "").trim();
		
		return nome;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, Exception {
		AjusteArquivos ajusteArquivos = new AjusteArquivos();
		ajusteArquivos.ajustarArquivos();
	}
	
}
