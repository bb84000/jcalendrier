import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import bb.arraylist.bArrayList;
import bb.utils.bbutils;
import java.io.OutputStream;


public class chknewversion {
	private static String version;
		
	public static void getLastVersion(String program, String urs, String curver, String urlupdate) {
		final String prog = program;
		final String cver = curver;
		final String urlup = urlupdate;
		
		// Create the event notifier and pass ourself to it.
	    FileDownload req = new FileDownload (new FileDownloadEvent() {
	            // Define the actual handler for the event.
	            
				public void dataReadProgress (int done, int total, byte[] data)
	            {
	                //System.out.println("Progress: " + ((float)done/(float)total) * 100 + "%");
	            }
	            
	            public void done (boolean error, InputStream is) 
	            {
	            	bArrayList al = new bArrayList();
                	al.setSeparator(';');
                	al.readCSVstream(is); 
                	if (!al.isEmpty()) {
                		Iterator<String[]> itr = al.iterator();
                		while(itr.hasNext()) {
                			String [] element =  itr.next();
                			if (element[0].equals(prog)) {
                				version= element[1];
                				break;
                			}
                		} 
                	}
	            	try {
						if (version.length() > 0) {
							if (VersionToInt(version) > VersionToInt(cver)){
								if (JOptionPane.showConfirmDialog(null, "Une nouvelle version "+version+" est disponible.\n Voulez-vous la télécharger ?",  "Calendrier", JOptionPane.YES_NO_OPTION)== JOptionPane.OK_OPTION) {
									bbutils.openURL(urlup);
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
	            }
	        });
	        req.request(urs);

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
