/*
 * CSVRead : intially developped as part of Calendrier project
 * Create an arraylist of arrays representing the csv file content : [line] [column]
 * bb - sdtp - september 2013
 * 
 */
import java.io.*;
import java.util.ArrayList;

public class CSVRead{
	public ArrayList<String[]> Liste;
	//private File file;
    

    
	public boolean readCSV  (String filename) throws Exception  {  
		boolean result= true;
		File file = new File(filename);
		if (file.exists()) {
			Liste = new ArrayList<String[]>();
			BufferedReader CSVFile = 
		        new BufferedReader(new FileReader(file));

		    String dataRow = CSVFile.readLine(); // Read first line.
		    // The while checks to see if the data is null. If 
		    // it is, we've hit the end of the file. If not, 
		    // process the data.
          
		 
		    while (dataRow != null){
		      //on retire le d�limiteur
			  dataRow= dataRow.replaceAll("\"", "");
		      String[] dataArray = dataRow.split(",");
		      Liste.add(dataArray);
     		  dataRow = CSVFile.readLine(); // Read next line of data.
		   }
		   // Close the file once all data has been read.
		   CSVFile.close();
	   }
	   else {
		   result= false;
	   }
 	 return result;
}
} // CSVRead





