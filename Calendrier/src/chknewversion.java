import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Pattern;

import bb.arraylist.*; 


public class chknewversion {
	
	
	public static String getLastVersion(String program) {
		String version = "";
		URL url;
		try {
			url = new URL("http://www.sdtp.com/versions/versions.csv");
			InputStream is = url.openStream();
			/* Now read the retrieved document from the stream. */
			bArrayList al = new bArrayList();
			al.setSeparator(';');
			al.readCSVstream(is);
			if (!al.isEmpty()) {
				Iterator<String[]> itr = al.iterator();
				while(itr.hasNext()) {
					String [] element =  itr.next();
					if (element[0].equals(program)) {
						version= element[1];
						break;
					}
				}
			}
			is.close();
		} catch (Exception e1) {
			// Connection error
			return "";
		}
		return version;
	}
	
	public static long VersionToInt (String version) {
		long result = 0;
		try {
			String ar [] = version.split(Pattern.quote("."));
			result = (65536*Integer.parseInt(ar[0]))+Integer.parseInt(ar[1]);
			result = (65536*result)+Integer.parseInt(ar[2]);
			result= (65536*result)+Integer.parseInt(ar[3]);
		} catch (Exception e) {
			return 0;
		}
		return result;
	}

}
