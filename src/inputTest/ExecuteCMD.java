package inputTest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExecuteCMD {

	public ExecuteCMD() {

	}
	public String execute(String cmdCommand) {

		System.out.println(cmdCommand);
		StringBuilder stringBuilder = new StringBuilder();
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmdCommand);
			stringBuilder.append(cmdCommand+"\n");
			final InputStream is1 = process.getInputStream();
			new Thread(new Runnable() {
				public void run() {
					BufferedReader bufferedReader = null;
					String line = null;
					try {
						bufferedReader = new BufferedReader(new InputStreamReader(is1, "GBK"));
						while((line=bufferedReader.readLine()) != null){
							stringBuilder.append(line+"\n");
						}
						is1.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start(); 
			InputStream is2 = process.getErrorStream();
			BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
			StringBuilder buf = new StringBuilder(); 
			String line2 = null;
			while((line2 = br2.readLine()) != null) {
				buf.append(line2+"\n");
			}
			System.out.print(stringBuilder);
			System.out.print(buf);
			return stringBuilder + "&" + buf;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
				return e.toString();
			}
		}

}
