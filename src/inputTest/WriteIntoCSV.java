package inputTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class WriteIntoCSV {



	public WriteIntoCSV() {


	}

	public void write(String content, String filePath) {
		try {
			File file = new File(filePath);
			if(!file.exists()){
				file.createNewFile();
			}

			if(!content.equals("")) {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(                        
						new FileOutputStream(file, true)));                              
				bw.write(content + "\n");
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}






}
