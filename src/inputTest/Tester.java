package inputTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class Tester {
	public static HashMap<String, Integer> allApis = new HashMap<String, Integer>();
	

	public static void main(String[] args) throws FileNotFoundException, IOException {

		try {
			ReadCSV rc = new ReadCSV("toolsPath.csv");

			//Get tools path
			String apktool = rc.getTools().get("apktool");
			String jad = rc.getTools().get("jad");
			String dex2jar = rc.getTools().get("dex2jar");
			String classesDex = rc.getTools().get("classes.dex");
			String apks = rc.getTools().get("apks");
			String rscript = rc.getTools().get("rscript");


			//Commands
			String apktoolCmd = "java -jar "+ apktool +" d ";
			String unzipCmd = ".\\unzip ";//Windows
			//String unzipCmd = "unzip ";//Linux
			String dex2jarCmd =  dex2jar + " -e ";
			String jadCmd =  jad + " -o -r -s java -d ";

			//needed objects
			ReadFile rf = new ReadFile(apks);
			ExecuteCMD exec = new ExecuteCMD();
			File csvStat = new File("csvStat.csv");


			//decode apks
			deleteFolders(exec);
			createFolders(exec);


			WriteIntoCSV cs = new WriteIntoCSV();
			cs.write("apkName,totalNode,targetNode,percentage", csvStat.getAbsolutePath());
			//start looping
			for(String ss : rf.filesPaths) {   //apks file path

				String commend = apktoolCmd + "\"" + ss + "\"";	//Windows
				//String commend = apktoolCmd + ss;				//Linux

				File apk = new File(ss);
				String apkName = apk.getName().substring(0, apk.getName().length()-4);

				File log = new File("Log" + File.separatorChar + apkName + ".txt");
				FileWriter fr = new FileWriter(log, true);



				//Parse XML to CSV and Calculate percentage
				xmlToCsv(commend, apkName, csvStat, exec, fr);

				//get dex2jar file
				dex2Jar(apkName, ss, unzipCmd, dex2jarCmd, classesDex, fr, exec);

				//unzip jar file
				unzipJar(apkName, unzipCmd, jadCmd, fr, exec);

				//parse java files to XML

				javaToXml(apkName, fr);

				//finish
				System.out.println("End with " + apkName);
				fr.close();
			}			

			WriteIntoCSV xmlReport = new WriteIntoCSV();
			xmlReport.write(allApis.size() + ",","xmlStat.csv" );
			allApis.forEach((k, v) -> {
				xmlReport.write(k + "," + v, "xmlStat.csv");

			});
			

			String report = "Stats.R";
			exec.execute(rscript + " " + report);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}

	}


	public static void deleteFolders(ExecuteCMD exec) {
		exec.execute("rm -r d2j_error");
		exec.execute("rm -r Java");
		exec.execute("rm -r CSV");
		exec.execute("rm -r XMLs");
		exec.execute("rm -r Log");
		exec.execute("rm csvStat.csv");
		exec.execute("rm report.txt");
		exec.execute("rm xmlReport.csv");
		exec.execute("rm xmlStat.csv");

	}

	/**
	 * Create needed folders to store data
	 * @param exec
	 */
	public static void createFolders(ExecuteCMD exec) {
		exec.execute("mkdir d2j_error");
		exec.execute("mkdir Java");
		exec.execute("mkdir CSV");
		exec.execute("mkdir XMLs");
		exec.execute("mkdir Log");
	}



	/**
	 * Parse xml file to csv, also calculate the percentage of target in total input
	 * @param commend
	 * @param apkName
	 * @param csvStat
	 * @param exec
	 * @param fr
	 * @throws IOException
	 */
	public static void xmlToCsv(String commend, String apkName, File csvStat, ExecuteCMD exec, FileWriter fr) throws IOException {
		XMLParse xmlParse = new XMLParse();

		fr.write("===========================================================\n");
		fr.write("======================Parse XML to CSV======================\n");
		System.out.println(commend);
		fr.write(exec.execute(commend));

		//read all xml files in layout forder
		File layout = new File(apkName+ File.separatorChar +"res" + File.separatorChar+ "layout" + File.separatorChar);
		boolean isExists = layout.exists();
		int total = 0;
		int node = 0;
		int description = 0;
		if(!isExists) {
			
			WriteIntoCSV noLayout = new WriteIntoCSV();
			noLayout.write(apkName, "LayoutException.csv");
		}else {

			ReadFile rxml = new ReadFile(apkName+ File.separatorChar +"res" + File.separatorChar+ "layout" + File.separatorChar);

			String csvName = "CSV" +File.separatorChar +apkName+".csv";
			for(String xml : rxml.filesPaths) {
				xmlParse.parse(xml, csvName);
				node += xmlParse.nodeC;
				total += xmlParse.total;
				description += xmlParse.descriptionC; 
			}
		}

		double result = 0;
		double desResult = 0;
		if(total != 0) {
			result = (double)node / total * 100;
			if(node != 0) {
				desResult = (double) description / node * 100;
			}
		}
		WriteIntoCSV cs = new WriteIntoCSV();
		cs.write(apkName + ","+ total + "," + node + "," + result + "," + desResult, csvStat.getAbsolutePath());
		fr.write(exec.execute("rm -r " + apkName));
	}


	/**
	 * unzip apk file, then using dex2jar to decode the classes.dex file to create a classes-dex2jar.jar
	 * @param apkName
	 * @param apkPath
	 * @param unzipCmd
	 * @param dex2jarCmd
	 * @param classesDex
	 * @param fr
	 * @param exec
	 * @throws IOException
	 */
	public static void dex2Jar(String apkName, String apkPath, String unzipCmd, String dex2jarCmd, String classesDex,FileWriter fr, ExecuteCMD exec) throws IOException {
		fr.write("===========================================================\n");
		fr.write("======================get dex2jar file=====================\n");
		//Windows
		fr.write(exec.execute(unzipCmd + "\"" + apkPath + "\" -d unzipApk"));
		//Linux
		//fr.write(exec.execute(unzipCmd +  ss + " -d unzipApk"));
		fr.write(exec.execute(dex2jarCmd + "d2j_error" + File.separatorChar
				+apkName+".zip "+ classesDex));
		fr.write(exec.execute("rm -r unzipApk"));
	}


	/**
	 * unzip classes-dex2jar.jar file to classes, then parse all class files to java files
	 * @param apkName
	 * @param unzipCmd
	 * @param jadCmd
	 * @param fr
	 * @param exec
	 * @throws IOException
	 */
	public static void unzipJar(String apkName, String unzipCmd, String jadCmd, FileWriter fr, ExecuteCMD exec) throws IOException {
		fr.write(exec.execute(unzipCmd + "-o classes-dex2jar.jar -d classes"));
		fr.write(exec.execute("rm classes-dex2jar.jar"));
		fr.write("===========================================================\n");
		fr.write("===================Parse classes to Javas==================\n");
		//Windows
		String test = jadCmd + "Java" + File.separatorChar + apkName + " \"classes\\**\\*.class\"" ;
		//Linux
		//String test = jadCmd + "Java" + File.separatorChar + apkName + " classes"+ File.pathSeparatorChar+"**"+ File.pathSeparatorChar+"*.class" ;
		fr.write(exec.execute(test));
		fr.write(exec.execute("rm -r classes"));
	}


	/**
	 * Perse java files to xml version with required information
	 * @param apkName
	 * @param fr
	 * @throws IOException
	 */
	public static void javaToXml(String apkName, FileWriter fr) throws IOException {
		fr.write("===========================================================\n");
		fr.write("=====================Parse Java to XML=====================\n");
		File java = new File("Java" + File.separatorChar +apkName);
		parse(java.getAbsolutePath(), java.getName(), apkName);

		fr.write("===========================================================\n");
	}

	/**
	 * Parse java file to XML file based on the given folder
	 * @param dirPath input folder
	 * @param xmlName output file name
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void parse(String dirPath, String xmlName, String apkName) throws FileNotFoundException, IOException {
		ReadFile rf = new ReadFile(dirPath);
		Set<String> apis = new HashSet<String>();
		for(String path : rf.filesPaths) {
			Visitor visitor = new Visitor();
			CompilationUnit comp = AstUnit.getCompilationUnit(path);
			comp.accept(visitor);
			WriteXML wx = new WriteXML(path, xmlName, visitor.getImport(), visitor.getMethodInvoke());

			//traverse the HashMap and update the apis 
			for(String k : wx.apis) {
				apis.add(k);
			}
		}
		WriteIntoCSV xmlReport = new WriteIntoCSV();
		String result = apkName + ",";
		for(String api : apis) {
			result += api + ",";
			if(!allApis.containsKey(api)) {
				allApis.put(api, 1);
			}else {
				allApis.replace(api, allApis.get(api) + 1);
			}
		}
		System.out.println(result);
		xmlReport.write(result.substring(0, result.length()-1), "xmlReport.csv");



	}

}
