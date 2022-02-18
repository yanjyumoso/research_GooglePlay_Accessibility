package inputTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ReadFile {

	ArrayList<String> filesPaths = new ArrayList<String>();

	public ArrayList<String> getFilesPaths() {
		return filesPaths;
	}


	public ReadFile(String filePath) throws FileNotFoundException, IOException{

		load(filePath);

	}

	public void load(String filepath) throws FileNotFoundException, IOException {
		try {
			
			File file = new File(filepath);
			File[] fs = file.listFiles();
			for(File f : fs) {
				if(f.isDirectory())
					load(f.getAbsolutePath());
				else if(f.isFile())
					filesPaths.add(f.getAbsolutePath());
				else {
					System.out.println("Layout folder does not exists!");
					return;
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return;

	}

}
