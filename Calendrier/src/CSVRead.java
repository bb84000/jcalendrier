/* Routine de lecture d'un fichier CSV délimité par des virgules
 * Remplit un ArrayList :colonne,ligne; 
 * 
 */
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;

public class CSVRead{
	public ArrayList<String[]> Liste;
	//private File file;
    

    
	public boolean readCSV  (String filename) throws Exception  {  
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
		      //on retire le délimiteur
			  dataRow= dataRow.replaceAll("\"", "");
		      String[] dataArray = dataRow.split(",");
		      Liste.add(dataArray);
     		  dataRow = CSVFile.readLine(); // Read next line of data.
		   }
		   // Close the file once all data has been read.
		   CSVFile.close();
		   return true;
	   }
	   else {
		   return false;	
	   }
 	
}
} // CSVRead





