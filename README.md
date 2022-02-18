# CSE491
This is the tool for CSE491 undergraduate research project. 

The goal of the research project is to understand the usage of accessbility features 
in Android Applications on Google Play. The main outcome of this proposal is the development of a 
tool that given an some apk will decompile it and extract the accessibility APIs and whether 
the developers provided assistive content descriptions. Subsequently, a high-level analysis.

### Usage
The tool needs to run under Java 13.<br /> 
In command line, 
    
    java -jar cse491.jar <filePath>



### This tool uses the following tools as an aid: 
1. apktool_2.4.1jar
2. dex2jar-2.0
3. jad.exe
4. for Windows also need unzip.exe

### The tool will decompile each apk file and when complete all following steps, it will go to the next apk file:
1. Using apktool to decompile apk which will disassembling resources to nearly original form 
(including resources.arsc, classes.dex, 9.png. and XMLs).
2. Scan all the XML files which under the /res/layout folder. The tool will create csv file for each apk and 
store the contents that we would like to record. 
3. Unzip the apk file to generate the classes.dex which will be used in the next step.
4. Using dex2jar to convert .dex file to .class files (zipped as classes-dex2jar.jar).
5. Unzip the classes-dex2jar.jar to get all .class files.
5. Using jad.exe to decompile all .classes file to .java files which will disassembling resources to nearly original form.
6. Using ASTParse to parse all .java files to XML files
7. End with current apk file and go to the next apk file.

### Report include:
* CSV Folder
  * CSV files: records the usage of accessibility features within each apk, 
  including XML file names, components ID, conmoents names, and content descriptions.
* JAVA Folder
  * Java files: each apk has a subfolder named with original apk name and each subfolder has all its java files. 
* XMLs Folder
  * XML files: each apk has a xml file named with original apk name and each xml file, includeing the java file name,
  ImportDeclaration and MethodInvocation
* Log Folder
  * TXT files: each apk has a txt file which record the detail for all steps.
* d2j_error
  * dex2jar error zips: some apk could cause error.zip generated when using dex2jar
* csvStat.csv
  * A analysis including apk name, total node count in res/layout xml files, target node(e.g., Imagebutton, ImageView), percentage of target nodes. 
* report.txt
   * A high-level analysis (e.g., descriptive statistics like mean, min, max, q1, q2, median) of the prevalence of the accessibility features 

  
### Source Code:
* AstUnit.java, Vistor.java
  * ASTParser to parse a java file to a abstract syntax tree and a Vistor which can tranverse the AST to get the needed information. 
* ExecuteCMD.java
  * Let user running system commond in Java.
* GenerateReport.java
  * Generate a final report based on the data in CsvStat.csv, include min, max, mean, q1, q3, median.
* ReadFile.java
  * Traverse the given forder, return all files' path under this folder.
* Tester.java
  * Main method.
* WriteIntoCSV.java
  * Create a CSV file with given information.
* WriteXML.java
  * Create a XML file with given information.
* XMLParse.java
  * Parse the XML file and output the result into a CSV file.
* ReadCSV.java
  * Read file line by line and store the information into a HashMap
  
  
  
  
  
  
  
