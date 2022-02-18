package inputTest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class WriteXML {
	
	public Set<String> apis = new HashSet<String>(); 
	public WriteXML(String fileName, String xmlName, ArrayList<String> imports, HashMap<String, String> methods) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			//add elements to Document
			Element rootElement = doc.createElementNS(fileName, "file");
			//append root element to document
			doc.appendChild(rootElement);

			//append first child element to root element
			rootElement.appendChild(getImports(doc, imports));

			rootElement.appendChild(getMethods(doc, methods));

			//for output to file, console
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			//for pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);

			//write to console or file			
			StreamResult file = new StreamResult(new FileOutputStream("XMLs" + File.separatorChar  + xmlName + ".xml", true));
			
			//write data
			transformer.transform(source, file);
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}


	private Node getImports(Document doc, ArrayList<String> imports ) {
		Element e = doc.createElement("ImportDeclaration");

		//create name element
		for(String ss: imports) {
			e.appendChild(getImportsElement(doc, e, ss));
			
		}

		return e;
	}


	//utility method to create text node
	private Node getImportsElement(Document doc, Element element, String type) {
		Element node = doc.createElement("API");
		node.setAttribute("Type", type);
		apis.add(type);
		return node;
	}

	private Node getMethods(Document doc, HashMap<String, String> methods ) {
		Element e = doc.createElement("MethodInvocation");

		//create name element
		Iterator<Entry<String, String>> hmIterator = methods.entrySet().iterator(); 
		
		while(hmIterator.hasNext()) {
            Entry<String, String> mapElement = hmIterator.next(); 
            
			e.appendChild(getMethodsElement(doc, e,  mapElement.getKey().toString(),mapElement.getValue().toString()));
		}
		return e;
	}
	private Node getMethodsElement(Document doc, Element element, String type, String value) {
		Element node = doc.createElement("Method");
		node.setAttribute("Type", type);
		node.setAttribute("Value", value);
		return node;

	}

}