package gui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import com.hukarz.presley.beans.Desenvolvedor;


public class LerMensagem extends ViewPart {
	private Composite parentComposite;
	
	private Label lblMensagemTitulo, lblMensagemDescricao;

	private static Text txtMensagemTitulo, txtMensagemDescricao;

	@Override
	public void createPartControl(Composite parent) {
		this.parentComposite = parent;
        GridLayout layout = new GridLayout();
        parent.setLayout(layout);
        layout.numColumns = 2;
        layout.verticalSpacing = 3;

		lblMensagemTitulo = new Label(parentComposite, SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		lblMensagemTitulo.setText("Título");
		lblMensagemTitulo.setSize(10, 200);	
		lblMensagemTitulo.setVisible(true);

		txtMensagemTitulo = new Text(parentComposite, SWT.MULTI | SWT.BORDER);
        GridData gdTitulo = new GridData( GridData.FILL_HORIZONTAL);
        txtMensagemTitulo.setLayoutData(gdTitulo);
		txtMensagemTitulo.setVisible(true);
		txtMensagemTitulo.setEnabled(false);
		txtMensagemTitulo.setBackground(new Color(txtMensagemTitulo.getDisplay(),255,255,255));

		lblMensagemDescricao = new Label(parentComposite, SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		lblMensagemDescricao.setText("Mensagem");
		lblMensagemDescricao.setVisible(true);

		txtMensagemDescricao = new Text(parentComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gdDescricao = new GridData( GridData.FILL_BOTH);
        txtMensagemDescricao.setLayoutData(gdDescricao);
		txtMensagemDescricao.setVisible(true);
		txtMensagemDescricao.setEnabled(false);
		txtMensagemDescricao.setBackground(new Color(txtMensagemTitulo.getDisplay(),255,255,255));
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public static void setCarregaMensagem(String titulo, String descricao) {
		if (txtMensagemTitulo != null)
			txtMensagemTitulo.setText(titulo);
		
		if (txtMensagemDescricao != null)
			txtMensagemDescricao.setText(descricao);
	}
	
}
