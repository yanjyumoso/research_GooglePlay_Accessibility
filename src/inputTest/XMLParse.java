package inputTest;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParse {


	WriteIntoCSV writeCSV = new WriteIntoCSV();


	ArrayList<String> targetNode = new ArrayList<String>();

	String targetValue = "android:contentDescription";

	String filePath = "";

	String fileName = "";

	int total;
	int nodeC;
	int descriptionC;
	public XMLParse() {

		targetNode.add("ImageButton");
		targetNode.add("ImageView");


	}

	public void parse(String filePath, String outputPath) {

		try {

			writeCSV.write("", outputPath);
			total = 0;
			nodeC = 0;
			descriptionC = 0;
			this.filePath = filePath;

			File file = new File(filePath);

			this.fileName = file.getName();

			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();

			//			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) {

				printNote(doc.getChildNodes(), outputPath);

			}


		} catch (Exception e) {
			System.out.println("xml parse");
			e.printStackTrace();
			System.out.println(e);
		}

	}

	private void printNote(NodeList nodeList, String outputPath) {
		total+= nodeList.getLength();
		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				if(targetNode.contains(tempNode.getNodeName())) {
					nodeC++;
					String id = "null";
					String description = "null";
					if (tempNode.hasAttributes()) {
						// get attributes names and values
						NamedNodeMap nodeMap = tempNode.getAttributes();
						if(nodeMap.getNamedItem("android:id")!=null) {
							id = nodeMap.getNamedItem("android:id").getNodeValue();
						}
						for (int i = 0; i < nodeMap.getLength(); i++) {
							Node node = nodeMap.item(i);
							if(node.getNodeName().equals(targetValue)) {
								descriptionC++;
								description = node.getNodeValue();
								break;
							}
						}
					}
					writeCSV.write(fileName + "," + id
							+ ","+ tempNode.getNodeName()+  ", " + description, outputPath);
				}

				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes(), outputPath);
				}
			}
		}

	}




}
