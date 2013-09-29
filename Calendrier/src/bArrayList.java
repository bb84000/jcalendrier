/**
 * ArrayList <String[] with CSV load and save functions 
 * 
 * boolean readCSVfile(String filename [String filename, String csname])
 *   String filename : filename
 *   String csname : charset string , "UTF8", "Cp1252"
 * 
 *  boolean readCSVstream(InputStream is, String  csname)
 *  	InputStream is : stream (from resource or other source)
 *  	String  csname : see above
 * 
 * bb - september 2013
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class bArrayList extends ArrayList<String[]>{
	
	
	private char separator = ',';
	private char delimiter = '"';
	
	private static final long serialVersionUID = 1L;

 	public void setSeparator(char sep){
 		separator = sep;
 	}
 	
 	public void setDelimiter(char del) {
 		delimiter= del;
 	}
 	
	public boolean readCSVfile(String filename){
    	try {
			File file = new File(filename);
			if (!file.exists()) {
				return false;
			}
			else {
				FileInputStream fis = new FileInputStream(filename);
				return readCSVstream (fis);
			}
		} catch (Exception e) {
			return false;
		}
    }

    // read file with charset support
	public boolean readCSVfile(String filename, String csname){
     	try {
			File file = new File(filename);
			if (!file.exists()) {
				return false;
			}
			else {
				FileInputStream fis = new FileInputStream(filename);
				return readCSVstream (fis, csname);
			}
		} catch (Exception e) {
			return false;
		}
    }
    
    // read stream w/o charset support
    public boolean readCSVstream(InputStream is) {
    	InputStreamReader r = new InputStreamReader(is);
		try {
			if (is.available()>0) return readstream (r);
			else return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}	
     }
    
   
    // read stream with charset support 
    public boolean readCSVstream(InputStream is, String  csname){
    	Charset cs = Charset.forName("Cp1252");
		try {
			cs = Charset.forName(csname);
		} catch (Exception e1) {
			cs = Charset.forName("Cp1252");
		}
		InputStreamReader r = new InputStreamReader(is, cs);
		try {
			if (is.available()>0) return readstream (r);
			else return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
    }
		
	// common routine to read streams	
	private boolean	readstream (InputStreamReader r) {
		try {
				BufferedReader CSVFile = new BufferedReader(r);
				String dataRow = CSVFile.readLine(); // Read first line.
				// The while checks to see if the data is null. If 
				// it is, we've hit the end of the file. If not, 
				while (dataRow != null){
					//on retire le délimiteur
					//dataRow= dataRow.replaceAll("\"", "");
					String[] dataArray = splitCSV(dataRow, ",");
					add(dataArray);
					dataRow = CSVFile.readLine(); // Read next line of data.
					
				}
				// Close the file once all data has been read.
				CSVFile.close();
				return true;
		} catch (IOException e) {
			return false;
		}
    }
	
	// take care of separator and delimiter to split line
	private String[] splitCSV (String s, String sp ){
		String otherThanQuote = " [^\"] ";
        String quotedString = String.format(" \" %s* \" ", otherThanQuote);
        String regex = String.format("(?x) "+ // enable comments, ignore white spaces
                separator+"                "+ // match a comma
                "(?=                       "+ // start positive look ahead
                "  (                       "+ //   start group 1
                "    %s*                   "+ //     match 'otherThanQuote' zero or more times
                "    %s                    "+ //     match 'quotedString'
                "  )*                      "+ //   end group 1 and repeat it zero or more times
                "  %s*                     "+ //   match 'otherThanQuote'
                "  $                       "+ // match the end of the string
                ")                         ", // stop positive look ahead
                otherThanQuote, quotedString, otherThanQuote);

        String[] tokens = s.split(regex);
        for(int i=0; i< tokens.length; i+=1) {
           tokens[i]= tokens[i].replaceAll(Character.toString(delimiter), "");

        }
		return tokens;
	}


}
