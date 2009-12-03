package tools.threader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public class ListSvnLogins {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> nomesCVS = new ArrayList<String>();
		
		try {
			// open a file and read the content into a byte array
			File f = new File(args[0]);

			FileInputStream fis =  new FileInputStream(f);
			byte[] b = new byte[(int) f.length()];
			fis.read(b);

			VTDGen vg = new VTDGen();
			vg.setDoc(b);
			vg.parse(true);  // set namespace awareness to true
			VTDNav vn = vg.getNav();

			AutoPilot ap0 = new AutoPilot();
			AutoPilot ap1 = new AutoPilot();

			ap0.selectXPath("/log/logentry");
			ap1.selectXPath("author");

			ap0.bind(vn);
			ap1.bind(vn);
			while(ap0.evalXPath()!=-1) {
				String author = ap1.evalXPathToString();
				if (nomesCVS.indexOf(author) == -1)
					nomesCVS.add( author );
			}

			ap0.resetXPath();
			
			for (String nome : nomesCVS) {
				System.out.println(nome);
			}
			

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (NavException e) {
			e.printStackTrace();
		} catch (XPathParseException e) {
			e.printStackTrace();
		} catch (XPathEvalException e) {
			e.printStackTrace();
		} catch (java.io.IOException e)	{
			e.printStackTrace();
		}

	}

}
