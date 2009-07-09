package dados.cvs;

import java.io.File;
import java.io.FileInputStream;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public class TesteVtdXml {

	public static void main(String args[]) {  
		try {
			File f = new File("C:/java/runtime-EclipseApplication/lucene/log/log-20083112.xml");
			// counting child elements of parlist
			int count = 0;
			// counting child elements of parlist named "par"
			int par_count = 0;
			
			FileInputStream fis =  new FileInputStream(f);
			byte[] b = new byte[(int) f.length()];
			fis.read(b);
			
			VTDGen vg = new VTDGen();
			vg.setDoc(b);
			vg.parse(true);
			VTDNav vn = vg.getNav();
			
			AutoPilot ap0 = new AutoPilot();
	        AutoPilot ap1 = new AutoPilot();
			
	        ap0.selectXPath("/log/logentry [paths/path='/lucene/java/trunk/src/test/org/apache/lucene/queryParser/TestQueryParser.java']");
			ap1.selectXPath("author");

			ap0.bind(vn);
			ap1.bind(vn);
			while(ap0.evalXPath()!=-1){
				count++;
	            System.out.println( ap1.evalXPathToString() );
			}

			ap0.resetXPath();
			System.out.println(" Qtde "+count);
		} catch (ParseException e) {
			System.out.println(" XML file parsing error \n"+e);
		} catch (NavException e) {
			System.out.println(" Exception during navigation "+e);
		} catch (XPathParseException e) {

		} catch (XPathEvalException e) {

		} catch (java.io.IOException e)	{
			System.out.println(" IO exception condition"+e);
		}   

	}  

}
