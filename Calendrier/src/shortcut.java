import java.io.File;
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
			// Write the file
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			// Run the vbs
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
	
	// delete link in startup folder
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
 
			// write the file
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			// run the script
			Process p = Runtime.getRuntime().exec("cscript \""+tmpdir+"delshcut.vbs\"");
			// Read script execution results to know when it is done
			InputStream is = p.getInputStream();
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
	
	// Create LInux shortcut
	
	public static void createLinuxShortcut(String appPath, String appName, String shcutname) {
		try {
			String home =System.getProperty("user.home")+"/";
			File file= null;
			// Gnome and consorts
			String gnomedir = home+".config/autostart/";
			// KDE and consorts
			String kdedir = home+".kde/Autostart/";
			String kde4dir = home+".kde4/Autostart/";
			if (new File(gnomedir).exists() && new File(gnomedir).isDirectory()) file = new File(gnomedir+shcutname);
			else if (new File(kdedir).exists() && new File(kdedir).isDirectory()) file = new File(kdedir+shcutname);
			else if (new File(kde4dir).exists() && new File(kde4dir).isDirectory()) file = new File(kde4dir+shcutname);
			FileOutputStream	fop = new FileOutputStream(file);
			
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// Create .desktop file
			String content = "[Desktop Entry]\r\n";
			content += "Type=Application\r\n";
			content += "Exec=java -jar /\""+appPath+appName+"\"\r\n";
			content += "Hidden=false\r\n";
			content += "NoDisplay=false\r\n";
			content += "X-GNOME-Autostart-enabled=true\r\n";
			content += "X-KDE-autostart-after=panel\r\n";
			content += "Name[fr]=Calendrier\r\n";
			content += "Name=Calendrier\r\n";
			content += "Comment[fr]=\r\n";
			content += "Comment=\r\n";
			
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
				
			// Write the file
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			file.setExecutable(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}

	public static void deleteLinuxShortcut(String shcutname) {
		String home =System.getProperty("user.home")+"/";
		// Gnome and consorts
		String gnomefile = home+".config/autostart/"+shcutname;
		// KDE and consorts
		String kdefile = home+".kde/Autostart/"+shcutname;
		String kde4file = home+".kde4/Autostart/"+shcutname;
		if (new File(gnomefile).exists()) (new File(gnomefile)).delete();
		else if (new File(kdefile).exists()) (new File(kdefile)).delete();
		else if (new File(kde4file).exists()) (new File(kde4file+shcutname)).delete();
	}
}
