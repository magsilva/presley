/* JayFX - A Fact Extractor Plug-in for Eclipse
 * Copyright (C) 2006  McGill University (http://www.cs.mcgill.ca/~swevo/jayfx)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * $Revision: 1.7 $
 */

package ca.mcgill.cs.swevo.jayfx; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.ui.PlatformUI;

import beans.ArquivoJava;
import beans.ClasseJava;
import beans.Projeto;

import ca.mcgill.cs.swevo.jayfx.model.ClassElement;
import ca.mcgill.cs.swevo.jayfx.model.FlyweightElementFactory;
import ca.mcgill.cs.swevo.jayfx.model.ICategories;
import ca.mcgill.cs.swevo.jayfx.model.IElement;
import ca.mcgill.cs.swevo.jayfx.model.MethodElement;
import ca.mcgill.cs.swevo.jayfx.model.Relation;

/**
 * Facade for the JavaDB component.  This component takes in a Java projects
 * and produces a database of the program relations between all the source 
 * element in the input project and dependent projects.
 */
public class JayFX
{
	// Sigleton
	private static JayFX instancia;
	
	// The database object should be used for building the database
    private ProgramDatabase aDB = new ProgramDatabase();
    
    // The analyzer is a wrapper providing additional query functionalities
    // to the database.
    private Analyzer aAnalyzer = new Analyzer( aDB );
    
    // A converter object that needs to be initialized with the database.
    private FastConverter aConverter = new FastConverter();
    
    // A Set of all the packages in the "project"
    private Set<String> aPackages = new HashSet<String>();
    
    // A flag that remembers whether the class-hierarchy analysis is enabled.
    private boolean aCHAEnabled = false;
    
    /** for testing purposes */
    public void dumpConverter()
    {
    	aConverter.dump();
    }
    
    public static JayFX obterInstancia(Projeto projeto) {
        // -> Para fazer q só tenha uma classe fachada <-
        if (instancia == null) {
          instancia = new JayFX();
        }
		IProgressMonitor lMonitor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()[0].getView( true ).getViewSite().getActionBars().getStatusLineManager().getProgressMonitor();
		try {
			instancia.initialize( ResourcesPlugin.getWorkspace().getRoot().getProject( projeto.getNome() ), lMonitor, true );
		} catch (JayFXException e1) {
			e1.printStackTrace();
		}

        return instancia;
      }
    
    /** 
	 * Returns a Java element associated with pElement.  This method cannot 
	 * track local or anonymous types or any of their members, primitive types, or array types.
	 * It also cannot find initializers, default constructors that are not explicitly declared, or 
	 * non-top-level types outside the project analyzed.  If such types are 
	 * passed as argument, a ConversionException is thrown.
	 * @param pElement The element to convert. Never null.
	 * @return Never null.
	 * @throws ConversionException If the element cannot be 
	 */
    public IJavaElement convertToJavaElement( IElement pElement ) throws ConversionException
    {
    	return aConverter.getJavaElement( pElement );
    }
    
    /**
	 * Returns an IElement describing the argument Java element.  Not designed
	 * to be able to find initializer blocks or arrays.
	 * @param pElement Never null.
	 * @return Never null
	 * @throws ConversionException if the element cannot be converted.
	 */
    public IElement convertToElement( IJavaElement pElement ) throws ConversionException
    {
    	return aConverter.getElement( pElement );
    }
    
    /**
     * Returns the range of the relation pRelation for domain pElement.
     * @param pElement The domain element
     * @param pRelation The relation to query
     * @return A Set of IElement objects representing all the elements in the range.
     */
    public Set<IElement> getRange( IElement pElement, Relation pRelation )
    {
    	if( (pRelation == Relation.DECLARES) && !(isProjectElement(pElement)))
    		return getDeclaresForNonProjectElement( pElement );
    	if( (pRelation == Relation.EXTENDS_CLASS) && !(isProjectElement(pElement)))
    		return getExtendsClassForNonProjectElement( pElement );
    	if( (pRelation == Relation.IMPLEMENTS_INTERFACE ) && !(isProjectElement(pElement)))
    		return getInterfacesForNonProjectElement( pElement, true );
    	if( (pRelation == Relation.EXTENDS_INTERFACES ) && !(isProjectElement(pElement)))
    		return getInterfacesForNonProjectElement( pElement, false );
    	if( (pRelation == Relation.OVERRIDES) || (pRelation == Relation.T_OVERRIDES ))
    	{
    	    if( !isCHAEnabled() )
    	    {
    	        throw new RelationNotSupportedException("CHA must be enabled to support this relation");
    	    }
    	}
    	return aAnalyzer.getRange( pElement, pRelation );
    }
    
    /**
     * Convenience method that returns the elements declared by an element
     * that is not in the project.
     * @param pElement The element to analyze.  Cannot be null.
     * @return A set of elements declared.  Cannot be null.  Emptyset if
     * there is any problem converting the element.
     */
    private Set<IElement> getDeclaresForNonProjectElement( IElement pElement )
    {
    	assert( pElement != null );
    	Set<IElement> lReturn = new HashSet<IElement>();
    	if( pElement.getCategory() == ICategories.CLASS )
    	{
    		try
			{
    			IJavaElement lElement = convertToJavaElement( pElement );
    			if( lElement instanceof IType )
    			{
    				IField[] lFields = ((IType)lElement).getFields();
    				for( int i = 0; i < lFields.length; i++ )
    				{
    					lReturn.add( convertToElement( lFields[i] ));
    				}
    				IMethod[] lMethods = ((IType)lElement).getMethods();
    				for( int i = 0; i < lMethods.length; i++ )
    				{
    					lReturn.add( convertToElement( lMethods[i] ));
    				}
    				IType[] lTypes = ((IType)lElement).getTypes();
    				for( int i = 0; i < lTypes.length; i++ )
    				{
    					lReturn.add( convertToElement( lTypes[i] ));
    				}
    			}
			}
    		catch( ConversionException pException )
			{
    			// Nothing, we return the empty set.
			}
    		catch( JavaModelException pException )
			{
    			// Nothing, we return the empty set.
			}
    	}
    	return lReturn;
    }
    
    /**
     * Convenience method that returns the superclass of a class that is not in
     * the project.
     * @param pElement The element to analyze.  Cannot be null.
     * @return A set of elements declared.  Cannot be null.  Emptyset if
     * there is any problem converting the element.
     */
    private Set<IElement> getExtendsClassForNonProjectElement( IElement pElement )
    {
    	assert( pElement != null );
    	Set<IElement> lReturn = new HashSet<IElement>();
    	if( pElement.getCategory() == ICategories.CLASS )
    	{
    		try
			{
    			IJavaElement lElement = convertToJavaElement( pElement );
    			if( lElement instanceof IType )
    			{
    				String lSignature = ((IType)lElement).getSuperclassTypeSignature();
    				if( lSignature != null )
    				{	
    					IElement lSuperclass = FlyweightElementFactory.getElement( ICategories.CLASS, 
    							aConverter.resolveType( lSignature, (IType)lElement).substring(1, lSignature.length()-1));    					
    					assert(lSuperclass instanceof ClassElement);
    					lReturn.add( lSuperclass );
    				}
    			}
			}
    		catch( ConversionException lException )
			{
    			// Nothing, we return the empty set.
			}
    		catch( JavaModelException lException )
			{
    			// Nothing, we return the empty set.
			}
    	}
    	return lReturn;
    }
    
    /**
     * Convenience method that returns the interfaces class that is not in
     * the project implements.
     * @param pElement The element to analyze.  Cannot be null.
     * @param pImplements if we want the implements interface relation.  False for the extends interface relation
     * @return A set of elements declared.  Cannot be null.  Emptyset if
     * there is any problem converting the element.
     */
    private Set<IElement> getInterfacesForNonProjectElement( IElement pElement, boolean pImplements )
    {
    	assert( pElement != null );
    	Set<IElement> lReturn = new HashSet<IElement>();
    	if( pElement.getCategory() == ICategories.CLASS )
    	{
    		try
			{
    			IJavaElement lElement = convertToJavaElement( pElement );
    			if( lElement instanceof IType )
    			{
    				if( ((IType)lElement).isInterface() == !pImplements )
    				{
    					String[] lInterfaces = ((IType)lElement).getSuperInterfaceTypeSignatures();
    					if( lInterfaces != null )
    					{	
    						for( int i = 0 ; i < lInterfaces.length; i++ )
    						{
    							IElement lInterface = FlyweightElementFactory.getElement( ICategories.CLASS, aConverter.resolveType( lInterfaces[i], (IType)lElement).substring(1, lInterfaces[i].length()-1));
    							lReturn.add( lInterface );
    						}
    					}
    				}
    			}
			}
    		catch( ConversionException lException )
			{
    			// Nothing, we return the empty set.
			}
    		catch( JavaModelException lException )
			{
    			// Nothing, we return the empty set.
			}
    		
    	}
    	return lReturn;
    }

    
    /**
     * Convenience method.  Returns the subset of elements in the range of the relation pRelation 
     * for domain pElement that are in the analyzed project.
     * @param pElement The domain element
     * @param pRelation The relation to query
     * @return A Set of IElement objects representing all the elements in the range.
     */
    public Set<IElement> getRangeInProject( IElement pElement, Relation pRelation )
    {
    	Set<IElement> lRange = aAnalyzer.getRange( pElement, pRelation );
    	Set<IElement> lReturn = new HashSet<IElement>();
    	
    	for ( IElement lElement : lRange )
    	{
    		if( isProjectElement( lElement ))
    		{
    			lReturn.add( lElement );
    		}
    	}
/*    	for( Iterator i = lRange.iterator(); i.hasNext(); )
    	{
    		IElement lNext = (IElement)i.next();
    		if( isProjectElement( lNext ))
    		{
    			lReturn.add( lNext );
    		}
    	}
*/
    	return lReturn;
    }
    
    /**
     * Returns all the elements in the database in their lighweight form.
     * @return A Set of IElement objects representing all the elements in the 
     * program database.
     */
    public Set<IElement> getAllElements()
    {
    	return aDB.getAllElements();
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
					elemento = element.getPackageName() +"."+ element.getShortName();
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
				elemento = element.getPackageName() +"."+ element.getShortName();

			if (!elemento.equals(""))
				listaElementos.put(element.getId(), elemento);
		}
    	
    	return listaElementos;
    }
    
    
    /**
     * Check if class-hierarchy analysis is enabled.
     * @return <code>true</code> if Class-hierarchy analysis is enabled; <code>false</code> otherwise.
     */
    public boolean isCHAEnabled()
    {
        return aCHAEnabled;
    }
    
    /**
     * Initializes the program database with information about relations
     * between all the source elements in pProject and all of its dependent
     * projects.
     * @param pProject The project to analyze.  Should never be null.
     * @param pProgress A progress monitor.  Can be null.
     * @param pCHA Whether to calculate overriding relationships between methods and to use these
     * in the calculation of CALLS and CALLS_BY relations.
     * @throws JayFXException If the method cannot complete correctly
     */
    public void initialize( IProject pProject, IProgressMonitor pProgress, boolean pCHA ) throws JayFXException
    {
    	
    	assert( pProject != null );
    	aCHAEnabled = pCHA;
    	
    	// Collect all target classes
    	List<ICompilationUnit> lTargets = new ArrayList<ICompilationUnit>();
    	for (IJavaProject lNext : getJavaProjects(pProject))
    	{
    		lTargets.addAll( getCompilationUnits( lNext ));
    	}
    	
    	// Process all the target classes
    	ASTCrawler lAnalyzer = new ASTCrawler( aDB, aConverter );
    	if( pProgress != null ) pProgress.beginTask( "Building program database", lTargets.size());

    	for( ICompilationUnit lCU : lTargets )
    	{
          	try
  			{
          		IPackageDeclaration[] lPDs = lCU.getPackageDeclarations();
          		if( lPDs.length > 0 )
          		{
          			aPackages.add( lPDs[0].getElementName() );
          		}
  			}
          	catch( JavaModelException lException )
  			{
          		throw new JayFXException( lException );
  			}
          	lAnalyzer.analyze( lCU );
          	if( pProgress != null ) pProgress.worked(1);
    	}
    	
    	/*
    	int lSize = lTargets.size();
    	int k = 0;
        for( Iterator i = lTargets.iterator(); i.hasNext(); )
        {
            k++;
        	ICompilationUnit lCU = (ICompilationUnit)i.next();
        	try
			{
        		IPackageDeclaration[] lPDs = lCU.getPackageDeclarations();
        		if( lPDs.length > 0 )
        		{
        			aPackages.add( lPDs[0].getElementName() );
        		}
			}
        	catch( JavaModelException pException )
			{
        		throw new JavaDBException( pException );
			}
        	lAnalyzer.analyze( lCU );
        	if( pProgress != null ) pProgress.worked(1);
        	
        	System.out.println( k + "/" + lSize );
        	if( k == 1414 )
        	{
        	    System.out.println( k + "/" + lSize );
        	}
        }
        */
                
        if( !pCHA )
        {
            if( pProgress != null ) pProgress.done();
            return;
        }
        
        // Process the class hierarchy analysis
        if( pProgress != null ) pProgress.beginTask( "Performing class hierarchy analysis", aDB.getAllElements().size());
        Set<IElement>  lToProcess = new HashSet<IElement>();
        lToProcess.addAll( aDB.getAllElements() );
//        int lSize = aDB.getAllElements().size();
//        k = 0;
        while( lToProcess.size() > 0 )
        {
//            k++;
            IElement lNext = (IElement)lToProcess.iterator().next();
            lToProcess.remove( lNext );
            if( lNext.getCategory() == ICategories.METHOD )
            {
                if( !isAbstractMethod( lNext ))
                {
                    Set<IElement> lOverrides = getOverridenMethods( lNext );
                    for( IElement lMethod : lOverrides)
                    {
                        if( !isProjectElement( lMethod ))
                        {
                            int lModifiers = 0;
                            try
                            {
                                IJavaElement lElement = convertToJavaElement( lMethod );
                                if( lElement instanceof IMember )
                                {
                                    lModifiers = ((IMember)lElement).getFlags();
                                    if( Modifier.isAbstract( lModifiers ))
                                    {
                                        lModifiers += 16384;
                                    }
                                }
                            }
                            catch( ConversionException lException )
                            {
                                // Ignore, the modifiers used is 0
                            }
                            catch( JavaModelException lException )
                            {
                                // Ignore, the modifierds used is 0
                            }
                            aDB.addElement( lMethod, lModifiers ); 
                        }
                        aDB.addRelationAndTranspose( lNext, Relation.OVERRIDES, lMethod );
                    }
/*
                    for( Iterator j = lOverrides.iterator(); j.hasNext(); )
                    {
                        IElement lMethod = (IElement)j.next();
                        if( !isProjectElement( lMethod ))
                        {
                            int lModifiers = 0;
                            try
                            {
                                IJavaElement lElement = convertToJavaElement( lMethod );
                                if( lElement instanceof IMember )
                                {
                                    lModifiers = ((IMember)lElement).getFlags();
                                    if( Modifier.isAbstract( lModifiers ))
                                    {
                                        lModifiers += 16384;
                                    }
                                }
                            }
                            catch( ConversionException pException )
                            {
                                // Ignore, the modifiers used is 0
                            }
                            catch( JavaModelException pException )
                            {
                                // Ignore, the modifiers used is 0
                            }
                            aDB.addElement( lMethod, lModifiers ); 
                        }
                        aDB.addRelationAndTranspose( lNext, Relation.OVERRIDES, lMethod );
                    }
*/
                }
            }
            pProgress.worked(1);
            //System.out.println( k + "/" + lSize );
        }
        pProgress.done();
    }
    
    /**
     * Returns whether pElement is an element in the packages analyzed.
     * @param pElement Not null
     * @return true if pElement is a project element.
     */
    public boolean isProjectElement( IElement pElement )
    {
    	assert( pElement != null );
    	return aPackages.contains( pElement.getPackageName() );
    }
    
    /**
     * Returns all projects to analyze in IJavaProject form, including the
     * dependent projects.
     * @param pProject The project to analyze (with its dependencies.  Should not be null.
     * @return A list of all the dependent projects (including pProject).  Never null.
     * @throws JayFXException If the method cannot complete correctly.
     */
    private static List<IJavaProject> getJavaProjects( IProject pProject ) throws JayFXException
    {
    	assert( pProject != null );
    	
        List<IJavaProject> lReturn = new ArrayList<IJavaProject>();
        try
        {
            lReturn.add( JavaCore.create( pProject ) );
            IProject[] lReferencedProjects = pProject.getReferencedProjects();
            for( int i = 0 ; i < lReferencedProjects.length; i++ )
            {
                lReturn.add( JavaCore.create( lReferencedProjects[i] ));
            }
        }
        catch( CoreException pException )
        {
            throw new JayFXException( "Could not extract project information", pException );
        }
        return lReturn;
    }
    	
    /**
     * Returns all the compilation units in this projects
     * @param pProject The project to analyze.  Should never be null.
     * @return The compilation units to generate.  Never null.
     * @throws JayFXException If the method cannot complete correctly
     */
    private static List<ICompilationUnit> getCompilationUnits( IJavaProject pProject ) throws JayFXException
    {
    	assert( pProject != null );
    	
        List<ICompilationUnit> lReturn = new ArrayList<ICompilationUnit>();
        
        try
        {
            IPackageFragment[] lFragments = pProject.getPackageFragments();
            for( int i = 0; i < lFragments.length; i++ )
            {
                ICompilationUnit[] lCUs = lFragments[i].getCompilationUnits();
                for( int j = 0; j < lCUs.length; j++ )
                {
                    lReturn.add( lCUs[j] );
                }
            }
        }
        catch( JavaModelException pException )
        {
            throw new JayFXException( "Could not extract compilation units from project", pException);
        }
        return lReturn;
    }
    
    /**
     * Returns the modifier flag for the element
     * @return An integer representing the modifier. 0 if the element cannot be found.
     */
    public int getModifiers( IElement pElement )
    {
    	return aDB.getModifiers( pElement );
    }
    
//    /**
//     * Returns whether pElement is an interface type that exists in the 
//     * DB.
//     */
//    public boolean isInterface( IElement pElement )
//    {
//    	return aAnalyzer.isInterface( pElement );
//    }
    
    /** 
     * Returns all the non-static methods that pMethod potentially 
     * implements.
     * @param pMethod The method to check.  Cannot be null.
     * @return a Set of methods found, or the empty set (e.g., if pMethod is
     * abstract.
     */
    public Set<IElement> getOverridenMethods( IElement pMethod )
    {
        assert( pMethod != null && pMethod instanceof MethodElement );
        Set<IElement> lReturn = new HashSet<IElement>();
        
        if( !isAbstractMethod( pMethod ))
        {
            Set<IElement> lToProcess = new HashSet<IElement>();
            lToProcess.addAll( getRange( pMethod.getDeclaringClass(), Relation.EXTENDS_CLASS ));
            lToProcess.addAll( getRange( pMethod.getDeclaringClass(), Relation.IMPLEMENTS_INTERFACE ));
            while( lToProcess.size() > 0 )
            {
                IElement lType = (IElement)lToProcess.iterator().next();
                lReturn.addAll( matchMethod( (MethodElement)pMethod, lType ));
                lToProcess.addAll( getRange( lType, Relation.EXTENDS_CLASS ));
                lToProcess.addAll( getRange( lType, Relation.IMPLEMENTS_INTERFACE ));
                lToProcess.addAll( getRange( lType, Relation.EXTENDS_INTERFACES ));
                lToProcess.remove( lType );
            }
        }
        return lReturn;
    }
    
    /**
     * Returns a non-static, non-constructor method that matches pMethod 
	 * but that is declared in pClass.
	 * null if none are found.
	 * Small concession to correctness here for sake of efficiency: methods are 
	 * matched only if they parameter types match exactly.
	 * 
     * @param pMethod
     * @param pClass
     * @return
     */
	private Set <IElement>matchMethod( MethodElement pMethod, IElement pClass )
	{
		Set<IElement> lReturn = new HashSet<IElement>();
		String lThisName = pMethod.getName();
		
		Set<IElement> lElements = getRange( pClass, Relation.DECLARES );
		for (IElement lMethodElement : lElements)
		{
			if( lMethodElement.getCategory() == ICategories.METHOD )
			{
				if(  !((MethodElement) lMethodElement).getName().startsWith("<init>") &&
				     !((MethodElement) lMethodElement).getName().startsWith("<clinit>"))
				{
					if( !Modifier.isStatic( aDB.getModifiers( lMethodElement )))
					{
						if( lThisName.equals( ((MethodElement)lMethodElement).getName() ))
						{
							pMethod.getParameters().equals( ((MethodElement)lMethodElement).getParameters() );
							lReturn.add( lMethodElement );
							break;
						}
					}
				}
			}
		}
		
/*		for( Iterator i = lElements.iterator(); i.hasNext(); )
		{
			IElement lNext = (IElement)i.next();
			if( lNext.getCategory() == ICategories.METHOD )
			{
				if(  !((MethodElement)lNext).getName().startsWith("<init>") &&
				     !((MethodElement)lNext).getName().startsWith("<clinit>"))
				{
					if( !Modifier.isStatic( aDB.getModifiers( lNext )))
					{
						if( lThisName.equals( ((MethodElement)lNext).getName() ))
						{
							pMethod.getParameters().equals( ((MethodElement)lNext).getParameters() );
							lReturn.add( lNext );
							break;
						}
					}
				}
			}
		}
*/		
		return lReturn;
	}
	
    
    /**
     * Returns whether pElement is an non-implemented method, either 
     * in an interface or as an abstract method in an abstract class.
     * Description of JayFX
     * TODO: Get rid of the magic number
     */
    public boolean isAbstractMethod( IElement pElement )
    {
    	boolean lReturn = false;
    	if( pElement.getCategory() == ICategories.METHOD )
    	{
    		if( aDB.getModifiers( pElement ) >= 16384 )
    		{
    			lReturn = true;
    		}
    	}
    	return lReturn;
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
			Set<IElement> lRange = aDB.getRange( element, Relation.DECLARES );
			
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
    	lRange.addAll( getRange( methodElement, Relation.T_CALLS ) );

    	for (Iterator<IElement> iterator = lRange.iterator(); iterator.hasNext();) {
    		IElement element = iterator.next();
    		try{
    			if (isProjectElement(element)){
    				ClasseJava classe   = new ClasseJava( element.getDeclaringClass().getId() ) ;

    				ArquivoJava arquivo = new ArquivoJava( convertToJavaElement(element).getResource().getName() );
    				
    				retorno.put(classe, arquivo);
    			}
    		} catch (ConversionException e) {
    			
    		}
   		}

   		return retorno;    	
    }
    
}
