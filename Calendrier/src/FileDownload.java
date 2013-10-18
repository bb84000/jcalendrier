import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import bb.arraylist.bArrayList;

public class FileDownload extends Thread implements Runnable {

	private FileDownloadEvent ie;
    private InputStream is = null;
    private DataInputStream dis = null;
    private int dataReadSize = 4096;
    private String downloadURL = null;
    private InputStream ist = null;
    public FileDownload (FileDownloadEvent event)
    {
        // Save the event object for later use.
        ie = event;
    }
 
    public void request (String url)
    {
        this.downloadURL = url;
	this.start();
    }
 
    //...
    public void run ()
    {
        boolean error = false;
        bArrayList al = null; 
         try {
            URL url = new URL(this.downloadURL);
            URLConnection fdCon = url.openConnection();
 
            int total = fdCon.getContentLength();
 
            is = url.openStream();  // throws an IOException
            dis = new DataInputStream(new BufferedInputStream(is));
 
            byte [] data = new byte[dataReadSize];
            int progress = 0, n;
            while ((n = dis.read(data)) != -1) {
            	progress += n;
                this.ie.dataReadProgress (progress, total, data);
            }
            is =  new ByteArrayInputStream(data);   ;       	
        } catch (Exception e)
        {
            error = true;
        }
        this.ie.done(error, is);
    }
    // ...

}
