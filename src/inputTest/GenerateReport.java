package inputTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public class GenerateReport {
	
	class Pair{
		String apkName;
		double perc;
	}

	HashMap<String, Double> datas = new HashMap<String, Double>();
	ArrayList<Double> percents = new ArrayList<Double>();
	String fileName;
	double sum = 0;

	public GenerateReport(String fileName) {
		this.fileName = fileName;
		getData(fileName);

	}

	private void getData(String csvFile) {

		String line = "";
		String csvSplitBy = ",";

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			br.readLine();
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] parts = line.split(csvSplitBy);
				this.datas.put(parts[0], Double.parseDouble(parts[3]));  
				this.percents.add(Double.parseDouble(parts[3]));
				sum += Double.parseDouble(parts[3]);
			}
			
			
			//sort percents
			Collections.sort(this.percents);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
	    Set<T> keys = new HashSet<T>();
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}

	public void calculate() throws IOException {
		double min = min();
		double q1 = Q1();
		double median = median();
		double q3 = Q3();
		double max = max();
		FileWriter report = new FileWriter("report.txt");
		
		report.write("Total file count: " + percents.size() + "\n");
		report.write("Mean: " + sum/percents.size() + "\n");
		report.write("Min: " + min + " apk: " + getKeysByValue(datas, min) + "\n");
		report.write("Q1: " + q1 + " apk: " + getKeysByValue(datas, q1) + "\n");
		report.write("Median: " + median + " apk: " + getKeysByValue(datas, median) + "\n");
		report.write("Q3: " + q3 + " apk: " + getKeysByValue(datas, q3) + "\n");
		report.write("Max: " + max + " apk: " + getKeysByValue(datas, max) + "\n");

		report.close();
	}

	private double Q1() {
		int n = this.percents.size(); 		
		// Index of median  
		// of entire data 
		int mid_index = medianIndex(0, n); 
		// Median of first half 
		return this.percents.get(medianIndex( 0,  mid_index));
		
	}
	private double Q3() {
		int n = this.percents.size(); 		
		// Index of median  
		// of entire data 
		int mid_index = medianIndex(0, n); 
		// Median of second half 
		return this.percents.get(medianIndex(mid_index + 1, n)); 

				
	}
	

	private int medianIndex( int l, int r) 
	{ 
		int n = r - l + 1; 
		n = (n + 1) / 2 - 1; 
		return n + l; 
	}
	
	private double median() {
		int n = this.percents.size(); 		
		return this.percents.get(medianIndex( 0,  n));
	}
	
	private double max() {
		return Collections.max(percents);
	}
	
	private double min() {
		return Collections.min(percents);
	}

	
	
}
