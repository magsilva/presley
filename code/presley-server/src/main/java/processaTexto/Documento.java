package processaTexto;
import java.util.Map;

public class Documento {
	private String texto;
	private int qtdPalavrasTotal;
	private String termosValidos;
	private Map<String, Integer> termosSelecionados;
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getQtdPalavrasTotal() {
		return qtdPalavrasTotal;
	}
	
	public void setQtdPalavrasTotal(int qtdPalavrasTotal) {
		this.qtdPalavrasTotal = qtdPalavrasTotal;
	}
	
	public String getTermosValidos() {
		return termosValidos;
	}
	
	public void setTermosValidos(String termosValidos) {
		this.termosValidos = termosValidos;
	}
	
	public Map<String, Integer> getTermosSelecionados() {
		return termosSelecionados;
	}
	
	public void setTermosSelecionados(Map<String, Integer> termosSelecionados) {
		this.termosSelecionados = termosSelecionados;
	}

}
