/**
 * ArrayList <String[] with CSV load and save functions 
 * 
 * boolean readCSVfile(String filename [, String csname])
 *   	String filename : filename
 *  	String csname : charset string , "UTF8", "Cp1252" (optional)
 * 
 *  boolean readCSVstream(InputStream is[, String  csname])
 *  	InputStream is : stream (from resource or other source)
 *  	String  csname : see above
 *  
 *  long writeCSVfile(String filename[, String  csname])
 *  	String filename : filename
 * 	 	String  csname : see above
 * 		return byte written
 * 
 *  void setSeparator(char sep)
 *  CSV separator, default is coma
 *  
 *  void setDelimiter(char del)
 *  CSV delimiter, default is double quote
 * 
 * bb - september 2013
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

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
		
	// common routine to read CSV streams	
	private boolean	readstream (InputStreamReader r) {
		try {
				BufferedReader CSVFile = new BufferedReader(r);
				String dataRow = CSVFile.readLine(); // Read first line.
				// The while checks to see if the data is null. If 
				// it is, we've hit the end of the file. If not, 
				while (dataRow != null){
					String[] dataArray = readCSVline(dataRow);
					// add data only if not an empty line
					if (dataRow.length()>0) add(dataArray);
					dataRow = CSVFile.readLine(); // Read next line of data.
				}
				// Close the file once all data has been read.
				CSVFile.close();
				return true;
		} catch (IOException e) {
			return false;
		}
    }
	
	
	// Read CSV line, "home made" 
	public String [] readCSVline (String s) {
		// create an arraylist ot get the fields 
		ArrayList<String> list = new ArrayList<>();
		while (s.length()>0) {
			// delimiter at beginning skip it
			if (s.charAt(0)== delimiter) {
				s = s.substring(1); 
				// search field ending delimiter
				int p = s.indexOf(delimiter);
				if (p>=0) {
					list.add(s.substring(0,p));
					//remove end delimiter
					s = s.substring(p+1);
					// separator is now at beginning
					try {
						if (s.charAt(0)== separator) s = s.substring (1);
						else s = "";
					} catch (Exception e) {
						s = "";
					}
				}
				// no end delimiter, search separator
				else {
					p = s.indexOf(separator);
					if (p>=0){
						list.add(s.substring(0,p));
						s = s.substring(p+1);
					}
					// no separator likely end of line
					else {
						list.add(s);
						s= "";
					}
				}
			}
			else {
				int p = s.indexOf(separator);
				if (p>=0) {
					list.add(s.substring(0,p));
					s = s.substring(p+1);
				}
				// no separator likely end of line
				else {
					list.add(s);
					s= "";
				}
			}
		}
		String []strArray = new String[list.size()];
		return (String[]) list.toArray(strArray);
	}
	
	
	// write file to disk
	
	public long writeCSVfile(String filename){
		try {
			File file = new File(filename);
			FileOutputStream fs = new FileOutputStream(file.getAbsoluteFile());
			writeCSVstream(fs);
			return file.length();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
	
	// write file to disk with charset support
	public long writeCSVfile(String filename, String csname){
		try {
			File file = new File(filename);
			FileOutputStream fs = new FileOutputStream(file.getAbsoluteFile());
			writeCSVstream(fs, csname);
			return file.length();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return 0;
		}
	}
	
	private void writeCSVstream(FileOutputStream fs ) {
		OutputStreamWriter osw = new OutputStreamWriter(fs);
		writestream (osw);
	}
	
	private void writeCSVstream(FileOutputStream fs, String csname ) {
		OutputStreamWriter osw;
		try {
			osw = new OutputStreamWriter(fs, csname);
			writestream (osw);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void writestream(OutputStreamWriter osw) {
			try {
				
				BufferedWriter bw = new BufferedWriter(osw);
				Iterator<String[]> itr = iterator();
				while(itr.hasNext()) {
					String [] element =  itr.next();
					String s="";
					for (int i=0; i< element.length; i+=1) {
						s += delimiter+element[i]+delimiter;
						if (i < element.length-1) s+= separator;
					}
					bw.write(s);
					bw.newLine();
				}
				bw.close();
				//result = file.length();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
	}
	
	public void sort (int fld) {
		final int i = fld;
		try {
			Collections.sort(this,new Comparator<String[]>() {
				public int compare(String[] strings, String[] otherStrings) {
					return strings[i].compareTo(otherStrings[i]);
				}
			});
		} catch (Exception e) {
			// Do nothing, index out of bounds
		}
	} // end sort routine


}
