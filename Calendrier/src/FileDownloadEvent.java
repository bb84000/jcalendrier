import java.io.InputStream;
import java.io.OutputStream;

/*
 * Part of non blocking file download system 
 * 
 */

public interface FileDownloadEvent {
	 // This is just a regular method so it can return something or
    // take arguments if you like.
    public void dataReadProgress (int done, int total, byte data[]);
    public void done(boolean error, InputStream is);
}
