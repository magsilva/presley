package presley.view;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;

public class Mensagens extends ViewPart {

	public Mensagens() {
		// TODO Auto-generated constructor stub
	}

	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub

		Tree tree = new Tree(parent, SWT.BORDER | SWT.CHECK);
	    
		 
		TreeItem item = new TreeItem(tree, SWT.NONE );
		item.setText("Problemas");		      
		TreeItem item2 = new TreeItem(item, SWT.NONE | SWT.CHECK);
		item2.setText("Aqui");		
		TreeItem item3 = new TreeItem(item, SWT.NONE | SWT.CHECK);
		item3.setText("Vamos");	
		
		


	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
