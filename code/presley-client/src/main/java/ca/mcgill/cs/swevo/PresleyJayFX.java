package ca.mcgill.cs.swevo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.PlatformUI;

import ca.mcgill.cs.swevo.jayfx.ConversionException;
import ca.mcgill.cs.swevo.jayfx.JayFX;
import ca.mcgill.cs.swevo.jayfx.JayFXException;
import ca.mcgill.cs.swevo.jayfx.model.FlyweightElementFactory;
import ca.mcgill.cs.swevo.jayfx.model.ICategories;
import ca.mcgill.cs.swevo.jayfx.model.IElement;
import ca.mcgill.cs.swevo.jayfx.model.MethodElement;
import ca.mcgill.cs.swevo.jayfx.model.Relation;

import com.hukarz.presley.beans.ArquivoJava;
import com.hukarz.presley.beans.ClasseJava;
import com.hukarz.presley.beans.Projeto;

public class PresleyJayFX extends JayFX {

	//    private ProgramDatabase aDB = new ProgramDatabase();

	// Sigleton
	private Projeto projeto;
	private Map<String, String> listaElementosProjeto = new HashMap<String, String>();

	public PresleyJayFX (Projeto projeto) throws JayFXException {
		super();
		this.projeto = projeto;

		IProgressMonitor progressMonitor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()[0].getView( true ).getViewSite().getActionBars().getStatusLineManager().getProgressMonitor();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IProject project = workspaceRoot.getProject(projeto.getNome());
		initialize(project, progressMonitor, true);

		// Busca Todos os Elementos no projeto
		listaElementosProjeto = getTodasClassesMetodos();
	}

	public Projeto getProjetoSelecionado(){
		return projeto;
	}

	/**
	 * Metodo que retorna todas as classes e nomes de arquivos relacionados ao elemento informado 
	 * esrita pelo desenvolvedor
	 * @return <classe ou metodo,arquivo>
	 * @throws ConversionException 
	 * @throws ConversionException 
	 */
	public Map<ClasseJava, ArquivoJava> getElementoRelacionamento( IElement element ) throws ConversionException {
		Map<ClasseJava, ArquivoJava> retorno = new HashMap<ClasseJava, ArquivoJava>();
		if ( element.getCategory() == ICategories.METHOD ){
			retorno.putAll( getRelacionamentosMetodo( (MethodElement) element ) );
		} else if ( element.getCategory() == ICategories.CLASS ){
			Set<IElement> lRange = getRange( element, Relation.DECLARES );

			for (Iterator<IElement> elementoClasse = lRange.iterator(); elementoClasse.hasNext();) {
				IElement elemento = elementoClasse.next();
				if (elemento.getCategory() == ICategories.METHOD)
					retorno.putAll( getRelacionamentosMetodo( (MethodElement) elemento ) );
			}
			
			lRange = getRange( element, Relation.T_EXTENDS_CLASS ); 
			retorno.putAll( getListaClasseArquivo( lRange )  );

			lRange = getRange( element, Relation.T_EXTENDS_INTERFACES );
			retorno.putAll( getListaClasseArquivo( lRange )  );
		}

		return retorno;
	}

	private Map<ClasseJava, ArquivoJava> getRelacionamentosMetodo( MethodElement methodElement ) throws ConversionException {
		
		Set<IElement> lRange = getRange( methodElement, Relation.T_ACCESSES) ;
		lRange.addAll( getRange( methodElement, Relation.T_CALLS ) ) ;

		return getListaClasseArquivo(lRange);    	
	}

	private Map<ClasseJava, ArquivoJava> getListaClasseArquivo(Set<IElement> elements) {
		Map<ClasseJava, ArquivoJava> retorno = new HashMap<ClasseJava, ArquivoJava>();

		for (Iterator<IElement> iterator = elements.iterator(); iterator.hasNext();) {
			IElement element = iterator.next();
			if (isProjectElement(element)){
				ClasseJava classe;
				
				if (element.getDeclaringClass() != null )
					classe   = new ClasseJava( element.getDeclaringClass().getId() ) ;
				else
					classe   = new ClasseJava( element.getId()) ; 
				
				try{
					ArquivoJava arquivo = new ArquivoJava( convertToJavaElement(element).getResource().getName(), getProjetoSelecionado());
					arquivo.setEnderecoServidor( convertToJavaElement(element).getPath().toString() ) ;
					retorno.put(classe, arquivo);
				} catch (Exception e) {
					// e.printStackTrace();
					// Como o Lucene est� com erros no Eclipse isso � necessario 
				}
				
			}
		}


		return retorno;    	
	}
	
	/**
	 * Retorna todos as Classes e metodos no projeto na forma escrita pelo programador
	 * @return A Set of IElement objects representing all the elements in the 
	 * program database.
	 * @throws ConversionException 
	 * @throws JavaModelException 
	 */
	private Map<String, String> getTodasClassesMetodos() 
	{
		Map<String, String> listaElementos = new HashMap<String, String>();
		Set<IElement> elements = getAllElements() ;
		for (Iterator<IElement> iterator = elements.iterator(); iterator.hasNext();) {
			IElement element = iterator.next();
			String elemento = "";

			if ( !isProjectElement(element) ) {
				continue;
			}

			// Assinatura do metodo
			/*
			if ( element.getCategory() == ICategories.METHOD ){
				try {
					IMethod metodo = (IMethod) convertToJavaElement(element);
					//elemento = element.getPackageName() +"."+ element.getShortName();
					elemento = element.getShortName();
					elemento = elemento.substring(0, elemento.indexOf("(") ) +"(" ;

					String[] parameterNames = metodo.getParameterNames();
					String[] parameterTypes = metodo.getParameterTypes();

					for (int i = 0; i < parameterNames.length; i++) {
						elemento += parameterNames[i] + " " + Signature.getSignatureSimpleName( parameterTypes[i] ) + ",";
					}

					if (elemento.substring(elemento.length()-1, elemento.length()).equals(","))
						elemento = elemento.substring(0, elemento.length()-1)+ ") ";
					else
						elemento += ")"; 
				} catch (ConversionException e) {

				} catch (JavaModelException e) {

				}
			// Classe	
			} else*/ 
			if ( element.getCategory() == ICategories.CLASS )
				elemento = element.getShortName();
			//				elemento = element.getPackageName() +"."+ element.getShortName();

			if (!elemento.equals("")){
				listaElementos.put(elemento.toLowerCase(), element.getId());
				//System.out.println(elemento +"\t" + element.getId());
			}
		}

		return listaElementos;
	}


	/**
	 * Metodo que retorna todas as classes e nomes de arquivos relacionados a mensagem 
	 * esrita pelo desenvolvedor
	 * @return <classe ou metodo,arquivo>
	 * @throws ConversionException 
	 * @throws ConversionException 
	 */
	public Map<ClasseJava, ArquivoJava> getClassesRelacionadas( String texto, String separadorPalavras ) throws ConversionException {
		Map<ClasseJava, ArquivoJava> retorno = new HashMap<ClasseJava, ArquivoJava>();

		StringTokenizer st = new StringTokenizer(texto, separadorPalavras);

		while (st.hasMoreTokens()){   
			String palavra = st.nextToken().toLowerCase().toLowerCase();

			if (!Character.isLetter(palavra.charAt(0))) {
				continue;
			}

			if (palavra.contains(".")) {
				StringTokenizer novaPalavra = new StringTokenizer(palavra, ".");
				palavra = novaPalavra.nextToken();
			}
			
			if (this.listaElementosProjeto.get(palavra) != null) {
                String nomeClasse = listaElementosProjeto.get(palavra);		
				IElement elemento ;
				elemento = FlyweightElementFactory.getElement( ICategories.CLASS, nomeClasse );
				retorno.putAll( getElementoRelacionamento(elemento) );

				ClasseJava classe;   
				classe = new ClasseJava( elemento.getId() ); 

				ArquivoJava arquivo = new ArquivoJava(convertToJavaElement(elemento).getResource().getName(), getProjetoSelecionado());
				arquivo.setEnderecoServidor( convertToJavaElement(elemento).getPath().toString() ) ;
				arquivo.setPrincipal(true);
				
				retorno.remove(classe);
				retorno.put(classe, arquivo);
			}						
		}

		return retorno;    	
	}

	public Map<String, String> getListaElementosProjeto() {
		return listaElementosProjeto;
	}

}
