package xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public abstract class XMLParser {

	protected Document docu;
	
	public abstract void parse ( File f ) throws Exception;
	
	protected void init ( File f ) throws Exception
	{
		
		DocumentBuilderFactory dbf =
				DocumentBuilderFactory.newInstance();
		
		dbf.setIgnoringElementContentWhitespace(true);
		
		DocumentBuilder db =
				dbf.newDocumentBuilder();
		
		docu = db.parse( f );
		
	}
	
}
