// Date of program submission:- 12-01-2014
/* Program for Ranked Retrieval by asv130130 (Name:- AMOL VAZE )*/

// Code for SAX Parser

import java.io.File;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ParseData extends DefaultHandler {
	private StringBuffer stringBuffer;
	private SAXParserFactory saxParserFactory;
	private SAXParser saxParser;
	private XMLReader xmlReader;
	
	public ParseData() throws ParserConfigurationException, SAXException {
		super();
		saxParserFactory = SAXParserFactory.newInstance();
		saxParser = saxParserFactory.newSAXParser();
		xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(this);
	}
	
	public void startDocument() throws SAXException {
		stringBuffer = new StringBuffer();
    }
	
	public void characters(char[] ch, int start, int length) {
			stringBuffer.append(ch, start, length);
	    }
	
	/*public String parse(File file) throws MalformedURLException, IOException, SAXException{
		xmlReader.parse(file.toURI().toURL().toString());
		return stringBuffer.toString();
	}*/
	
	
	public String parse(File file) {
		String f = null;
		try {
			f = file.toURI().toURL().toString();
			xmlReader.parse(file.toURI().toURL().toString());
		} catch (Exception e) {
			System.out.println(f + "\n" + e.getMessage());
		}
		return stringBuffer.toString();
	}
	
	
	
	
	
}