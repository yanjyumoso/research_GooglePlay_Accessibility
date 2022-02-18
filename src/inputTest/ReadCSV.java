package inputTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ReadCSV {
	
    HashMap<String, String> tools = new HashMap<String, String>();
	String fileName;
	
	public ReadCSV(String fileName) {
		this.fileName = fileName;
		read(fileName);
	}
	
	public HashMap<String, String> getTools() {
		return tools;
	}

	

	public void read(String fileName) {

	        String csvFile = fileName;
	        String line = "";
	        String cvsSplitBy = ",";

	        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] parts = line.split(cvsSplitBy);

	                tools.put(parts[0], parts[1]);

	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    }

	

}
