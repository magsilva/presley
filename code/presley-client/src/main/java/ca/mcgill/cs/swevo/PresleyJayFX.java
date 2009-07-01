package ca.mcgill.cs.swevo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.ui.PlatformUI;

import ca.mcgill.cs.swevo.jayfx.ConversionException;
import ca.mcgill.cs.swevo.jayfx.JayFX;
import ca.mcgill.cs.swevo.jayfx.JayFXException;
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
	private static PresleyJayFX instancia;
	private Projeto projeto;
	
	public static PresleyJayFX obterInstancia(Projeto projeto) {
		// -> Para fazer q só tenha uma classe fachada <-
		if (instancia == null) {
			instancia = new PresleyJayFX(projeto);
		} else {
			try {
				instancia.finalize();
				instancia = new PresleyJayFX(projeto);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
						
		IProgressMonitor lMonitor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()[0].getView( true ).getViewSite().getActionBars().getStatusLineManager().getProgressMonitor();
		try {
			instancia.initialize( ResourcesPlugin.getWorkspace().getRoot().getProject( projeto.getNome() ), lMonitor, true );
		} catch (JayFXException e1) {
			e1.printStackTrace();
		}

		return instancia;
	}

	public PresleyJayFX(Projeto projeto) {
		super();
		this.projeto = projeto;
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
    	}
    	
    	return retorno;
    }
    
    private Map<ClasseJava, ArquivoJava> getRelacionamentosMetodo( MethodElement methodElement ) {
    	Map<ClasseJava, ArquivoJava> retorno = new HashMap<ClasseJava, ArquivoJava>();

    	Set<IElement> lRange = getRange( methodElement, Relation.ACCESSES) ;
    	lRange.addAll( getRange( methodElement, Relation.CALLS ) ) ;

    	for (Iterator<IElement> iterator = lRange.iterator(); iterator.hasNext();) {
    		IElement element = iterator.next();
    		try{
    			if (isProjectElement(element)){
    				ClasseJava classe   = new ClasseJava( element.getDeclaringClass().getId() ) ;

    				ArquivoJava arquivo = new ArquivoJava( convertToJavaElement(element).getResource().getName(), getProjetoSelecionado());
    				arquivo.setEnderecoServidor( convertToJavaElement(element).getPath().toString() ) ;
    				retorno.put(classe, arquivo);
    			}
    		} catch (ConversionException e) {
    			
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
    public Map<String, String> getTodasClassesMetodos() 
    {
    	Map<String, String> listaElementos = new HashMap<String, String>();
		Set<IElement> elements = getAllElements() ;
		for (Iterator<IElement> iterator = elements.iterator(); iterator.hasNext();) {
			IElement element = iterator.next();
			String elemento = "";
			
			if ( !isProjectElement(element) )
				continue;
			
			// Assinatura do metodo
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
//					System.out.println(element.getId() + " ConversionException");
				} catch (JavaModelException e) {
//					System.out.println(element.getId() + " JavaModelException");
				}
			// Classe	
			} else if ( element.getCategory() == ICategories.CLASS )
				elemento = element.getShortName();
//				elemento = element.getPackageName() +"."+ element.getShortName();

			if (!elemento.equals(""))
				listaElementos.put(element.getId(), elemento);
		}
    	
    	return listaElementos;
    }
    
}
