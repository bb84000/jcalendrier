/*
 * saints : part of Calendrier project
 * List of saints for the french calendar
 * This class can import an external list in CSV format if available in the current path to replace internal one
 * bb - sdtp - september 2013
 * 
 */

public class saints {
	
    
	private CSVRead csvsaints = new CSVRead();

	public String[][] saints = new String [31][12];
	
	// Constructor : Try to load an external CSV list ("saints.csv")
	// if not available, load list in resources
	
	saints () {
	    if (!csvsaints.readCSVfile("saints.csv")) {
	    	if (!csvsaints.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/saints.csv"))) {
	    		csvsaints.Liste = null;
	    	}
	    }
	    if 	(!(csvsaints.Liste == null))  {
	   			// only existing lines and columns are imported
	   			// Blank fields are ignored, so you can make a list with only the names to change		
				try {
					for (int i=0; i<31; i+=1) {
						for (int j=0; j<12; j+=1) {
						   String s = csvsaints.Liste.get(i)[j];
						   if (s.length() > 0) saints[i][j] = s; 
					    }
				    }
				}
				catch (Exception e){
				// do nothing, it is only to treat errors in the list
				}
	   	}
	} // end constructor saints

}
