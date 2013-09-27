/**
 * CSVRead : intially developped as part of Calendrier project
 * Create an arraylist of arrays representing the csv file content : [line] [column]
 * 
 * boolean readCSVfile  (String filename [,String csname])
 * 	String filename : external file 
 *  String csname : character encoding for instance Cp1252 for ISO latin 1
 *  	(see http://docs.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html)
 * boolean readCSVstream  (InputStream is, String csname) 
 *  InputStream is : input stream from file or resource
 *  String csname : character encoding
 *  
 * bb - sdtp - september 2013
 * 
 */

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

public class CSVRead{
	public ArrayList<String[]> Liste = new ArrayList<String[]>();
	//private File file;
    

    public boolean readCSVfile  (String filename)  {  
    	return readCSVfile  (filename, "Cp1252");
    }
	
	public boolean readCSVfile  (String filename, String csname)  {  
		boolean result= false;
		try {
			File file = new File(filename);
			if (file.exists()) {
				FileInputStream fis = new FileInputStream(filename);
				result = readCSVstream (fis, csname);
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public boolean readCSVstream  (InputStream is)   {  
		return readCSVstream  (is, "Cp1252");
	}
		
	public boolean readCSVstream  (InputStream is, String csname)   {  
			Charset cs = Charset.forName("Cp1252");
			try {
				cs = Charset.forName(csname);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
			}
			InputStreamReader r = new InputStreamReader(is, cs);
			try {
				if (is.available() >0 ) {
				
					BufferedReader CSVFile = new BufferedReader(r);


				//BufferedReader CSVFile = 
				 //   new BufferedReader(new FileReader(file));

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
				else return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return false;
			}
	    
	}
	
	public void writeCSVfile(String filename, boolean append) {
		File file = new File(filename);
		FileWriter fw = null; 
		// if file doesnt exists, then create it
		try {
			fw = new FileWriter(file.getAbsoluteFile(), true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		if (!Liste.isEmpty()) {
			String s = new String();
			try {
				Iterator itr = Liste.iterator();
			    s = "";
				while(itr.hasNext()) {
			    	String [] element = (String []) itr.next();
			    	for (int i=0; i < element.length; i+=1) { 
			    		if (i == element.length-1) s= element[i];
			    		else s= element[i]+",";
			    		bw.write(s);
			    	}
			    	bw.newLine();
			    };
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}	
		}
		
	}
} // CSVRead





