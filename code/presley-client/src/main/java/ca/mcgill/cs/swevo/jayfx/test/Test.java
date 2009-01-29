/* JayFX - A Fact Extractor Plug-in for Eclipse
 * Copyright (C) 2006  McGill University (http://www.cs.mcgill.ca/~swevo/jayfx)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * $Revision: 1.6 $
 */

package ca.mcgill.cs.swevo.jayfx.test;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import ca.mcgill.cs.swevo.jayfx.JayFX;
import ca.mcgill.cs.swevo.jayfx.JayFXException;
import ca.mcgill.cs.swevo.jayfx.model.IElement;
import ca.mcgill.cs.swevo.jayfx.model.Relation;

/**
 * @author martin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Test implements IWorkbenchWindowActionDelegate {

    private IStructuredSelection aSelection;
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
     */
    public void dispose() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
     */
    public void init(IWorkbenchWindow lWindow) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {
    	try
		{
    		JayFX lDB = new JayFX();
    		IProgressMonitor lMonitor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences()[0].getView( true ).getViewSite().getActionBars().getStatusLineManager().getProgressMonitor();
    		lDB.initialize( getSelectedProject(), lMonitor, true );
    		//lDB.dumpConverter();
    		//System.out.println("--- Calls ---");
    		Set lAllElements = lDB.getAllElements();
    		IElement[] lElements = (IElement[]) lAllElements.toArray( new IElement[lAllElements.size()]);
    		
    		for( int i = 0; i < lElements.length; i++ )
    		{
    			Set lTargets = lDB.getRange( lElements[i], Relation.T_CALLS );
    			if( lTargets.size() > 0 )
    			{
    				System.out.println( lElements[i] );
    				for( Iterator j = lTargets.iterator(); j.hasNext(); )
    				{
    					System.out.println("    " + j.next() );
    				}
    			}
    		}
    		
		}
    	catch( JayFXException lException )
		{
    		lException.printStackTrace();
		}
    	catch( AssertionError lError )
		{
    		lError.printStackTrace();
		}
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        if( selection instanceof IStructuredSelection )
            aSelection = (IStructuredSelection)selection;

    }
    
    private IProject getSelectedProject()
	{
		IProject lReturn = null;
		Iterator i = aSelection.iterator();
		if( i.hasNext() )
		{
			Object lNext = i.next();
			if( lNext instanceof IResource )
			{
				lReturn = ((IResource)lNext).getProject();
			}
			else if( lNext instanceof IJavaElement )
			{
				IJavaProject lProject = ((IJavaElement)lNext).getJavaProject();
				lReturn = lProject.getProject();
			}
		}
		return lReturn;
	}

}
