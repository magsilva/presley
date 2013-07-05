import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;


public class FiltroDeArquivos {
	protected File[] arquivosResposta;
	protected File[] arquivosRecomendacao;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String path = args[0];
		FiltroDeArquivos filtroDeArquivos = new FiltroDeArquivos(path);
		filtroDeArquivos.distribuir(path);
	}


	public FiltroDeArquivos(String endereco) throws IOException {
		File diretorio = new File(endereco);
		this.arquivosResposta = diretorio.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".emails");  
			}  
		});
		
		this.arquivosRecomendacao = diretorio.listFiles(new FilenameFilter() {  
			public boolean accept(File d, String name) {  
				return name.toLowerCase().endsWith(".recomendations");  
			}  
		}); 	
		
	}


	public void distribuir(String endereco) throws IOException {
		for (int i = 0; i < this.arquivosResposta.length; i++) {
			File arquivoRespostas = this.arquivosResposta[i];
			int qtdeRespostas = getQuantidadeEmails(arquivoRespostas);
			
			criarDiretorio(endereco+qtdeRespostas+"_I/");
			File arquivoRespostaDestino = new File( 
					arquivoRespostas.getAbsoluteFile().toString().replace(endereco, endereco+ qtdeRespostas +"_I/"));
			copiarArquivo(arquivoRespostas, arquivoRespostaDestino);
			
			File arquivoRecomendacaoDestino = new File(arquivoRespostaDestino.getAbsoluteFile().toString().replace(".emails", ".recomendations") );
			copiarArquivo(arquivosRecomendacao[i], arquivoRecomendacaoDestino);
			
			if (qtdeRespostas > 1){
				criarDiretorio(endereco+qtdeRespostas+"_MI/");
			}
			
			System.out.println();
		}
	}
	
	private int getQuantidadeEmails(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		int i = 0;
		
		while (scanner.hasNext()) {
			i++;
			scanner.next();
		}
		
		return i;
	}

	// Copia arquivo desejado, para o arquivo de destino    
	// Se o arquivo de destino não existir, ele será criado
	private void copiarArquivo(File src, File dst) throws IOException {        
		InputStream in = new FileInputStream(src);        
		OutputStream out = new FileOutputStream(dst);
		// Transferindo bytes de entrada para saída        
		byte[] buf = new byte[1024];        
		int len;        
		while ((len = in.read(buf)) > 0) {            
			out.write(buf, 0, len);        
		}        
		in.close();        
		out.close();   
	}

	private void criarDiretorio(String diretorio){
		File dir = new File(diretorio);
		
		if (!dir.exists()){
			if (dir.mkdir()){   
			    // System.out.println("Diretorio criado com sucesso!");   
			} else   
			    System.out.println("Erro ao criar diretorio!");   
		}
	}
}
