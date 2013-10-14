import java.awt.Desktop;
import java.net.URI;


public class bbutils {
	
	   public static void openURL(String url) {
		   // short url don't work with  some OS
		   if (!url.startsWith("http://")) url = "http://"+url; 
	    		  
		   try {  //attempt to use Desktop library from JDK 1.6+
	    	  	Desktop.getDesktop().browse(new URI(url));
	         }
	      catch (Exception ignore) {  //library not available or failed
	         String OS = (System.getProperty("os.name")).toUpperCase();
	         Runtime runtime = Runtime.getRuntime();
	         try {
	        	 if (OS.contains("MAC")) {
	        		 String[] args = { "osascript", "-e", "open location \"" + url + "\"" };
        			 runtime.exec(args);
	        	 }
	            else if (OS.contains("WIN")) {
	               runtime.exec(
	                  "rundll32 url.dll,FileProtocolHandler " + url);
	            }
	            else { //assume Unix or Linux
	            	// List of browsers 
	            	String[] browsers = { "google-chrome", "firefox", "opera",
	          		      "epiphany", "konqueror", "conkeror", "midori", "kazehakase", "mozilla" };
  	    	       String browser = null;
	               for (String b : browsers)
	            	   if (browser == null && Runtime.getRuntime().exec(new String[]
	                		 {"which", b}).getInputStream().read() != -1)
	            	   runtime.exec(new String[] {browser = b, url});
		               if (browser == null)
		                  return; 
	            }
		     }
		     catch (Exception e) {
		     	return;
		     }
		  }
      }
}
