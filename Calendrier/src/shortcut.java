import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class shortcut {
	// create link in startup folder;
	public static void createWinShortcut(String appPath, String appName, String shcutname) {
		
		  try {
	           
			  String tmpdir = System.getProperty("java.io.tmpdir");
			  File file = new File(tmpdir+"/creshcut.vbs");
			  FileOutputStream	fop = new FileOutputStream(file);
	 
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				//  To have two quotes in vbs, double the quote.
	
				String content = "Set Shell = CreateObject(\"WScript.Shell\")\r\n";
				content += "startupDir = Shell.SpecialFolders(\"Startup\")\r\n";
				content += "Set link= Shell.CreateShortcut(startupDir & \"\\"+shcutname+"\")\r\n";
				content += "link.Arguments =  \"-jar \"\""+appPath+appName+"\"\"\"\r\n";
				content += "link.WindowStyle = 7  \r\n"; 
				content += "link.TargetPath = \"java\"\r\n";
				content += "link.WorkingDirectory = \""+appPath+"\"\r\n";
				content += "link.Save\r\n";
				// get the content in bytes
				byte[] contentInBytes = content.getBytes();
	 
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
				Process p = Runtime.getRuntime().exec("cscript \""+tmpdir+"creshcut.vbs\"");
				
				// Read script execution results to know when it is done
				InputStream is = p.getInputStream();
				int i = 0;
				StringBuffer sb = new StringBuffer();
				while ( (i = is.read()) != -1)
	               sb.append((char)i);
				// we can delete the script
				file.delete();
	       } catch (IOException e) {
	          // e.printStackTrace();
	       }
		  
	  
		
	}
	public static void deleteWinShortcut(String shcutname) {
		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
			  File file = new File(tmpdir+"/delshcut.vbs");
			  FileOutputStream	fop = new FileOutputStream(file);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				String content = "Set Shell = CreateObject(\"WScript.Shell\")\r\n";
				content += "startupDir = Shell.SpecialFolders(\"Startup\")\r\n";
				content += "Set fso =  CreateObject(\"Scripting.FileSystemObject\")\r\n";
				content += "fso.DeleteFile startupDir & \"\\"+shcutname+"\"\r\n";
				
				// get the content in bytes
				byte[] contentInBytes = content.getBytes();
 
				fop.write(contentInBytes);
				fop.flush();
				fop.close();
				Process p = Runtime.getRuntime().exec("cscript \""+tmpdir+"delshcut.vbs\"");
				InputStream is = p.getInputStream();
		        // Read script execution results to know when it is done
		        int i = 0;
		        StringBuffer sb = new StringBuffer();
		        while ( (i = is.read()) != -1)
		              sb.append((char)i);
		        // delete script
		        file.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}

}
