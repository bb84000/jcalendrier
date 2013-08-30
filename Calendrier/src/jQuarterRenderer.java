import java.awt.Color;
import java.awt.Component;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

public class jQuarterRenderer extends DefaultTableCellRenderer {
    private int year;
    private int quarter;
    String days= "DLMMJVSD";
    String iniday= "L";
    private int month;
    private int dow;
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int pos = table.getName().indexOf("-");
        String syear =  table.getName().substring(0, pos); 
        year = Integer.parseInt(syear);   
    	//quarter = Integer.parseInt(table.getName().substring(5))-1;
        //year = Integer.parseInt(table.getName().substring(0, 4));   
    	quarter = Integer.parseInt(table.getName().substring(pos+1))-1;
        month = column+1+quarter*3;
    	DateTime dt;
		JLabel label = (JLabel) component;
		Color clr = new Color(255, 255, 255);
		label.setBackground(clr);
    	Color clf = new Color(64,64,64);
    	
  	    label.setForeground(clf);
    	//component.setBackground(clr);
    	String caption = "";
       
			try {
				dt = new DateTime(year, month, row+1, 12, 0 );
				dow = dt.getDayOfWeek();
				iniday= days.substring(dow,dow+1 );
			    caption += row+1;
				caption +=" "+iniday;
		        String saint = "";
		        if (month == column+1+quarter*3) {
		        	saint = saints.saints [row][month-1];
		        	caption += " "+saint;
		        	// Couleur dimanches
		        if (dow == 1) {  
		        	clr = new Color(0, 255, 255);
                    //component.setBackground(clr);
                    label.setBackground(clr);
                  }
                //DateTime now;
                //now = new DateTime();
               DateTime now;
               now = new DateTime();
               if ((now.getYear()==dt.getYear()) && (now.getDayOfYear()==dt.getDayOfYear()))// && (now.getDayOfMonth()ayOfMonth()==dt.dayOfMonth())) 
                    {
                    Border line = BorderFactory.createLineBorder(clf, 2);
            	    label.setBorder(line);
              }
          label.setText(caption);
		    }
			} catch (Exception e) {
				// N?e fait rien, le jopur n'existe pas
				//e.printStackTrace();
			}
	
    
    return component;
    }
}

