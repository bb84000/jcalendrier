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

import java.util.ArrayList;
import java.util.List;


public class jQuarterRenderer extends DefaultTableCellRenderer {
    private int year;
    private int quarter;
    String days= "DLMMJVSD";
    String iniday= "L";
    private int month;
    private int dow;
    private uPhaseMoon Moon = new uPhaseMoon();
    public String[][] MoonDays = new String[56][2]; 
    public saints Saints = new saints();
   
    // initialization
    jQuarterRenderer () throws Exception {
    	// chargement des listes diverses si nécessaire 
		
    }
    
    // Choisit l'année; Crée en même temps la table des lunaisons
	// Todo ajouter les phases solaires
	//Todo ajouter les vacances scolaires
	public void setYear (int y){
    	year = y;
        Moon.Get_MoonDays(MoonDays, y);
	}
	
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
    {Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    	quarter = Integer.parseInt(table.getName())-1;
        month = column+1+quarter*3;
    	DateTime dt;
		JLabel label = (JLabel) component ;
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
		        	saint = Saints.saints [row][month-1];
		        	caption += " "+saint;
		        	// Couleur dimanches
		        if (dow == 7) {  
		        	clr = new Color(0, 255, 255);
                    //component.setBackground(clr);
                    label.setBackground(clr);
                  }
                // Lune ?
		        String curday = String.format("%02d/%02d/%04d", row+1, month, year);
		        
		        for (int i=0; i<=55; i+=1){
		        	String s = MoonDays[i][0] ;	
		            if (s.contains(curday)) {
		            	if (caption.length() >16) {
		            		caption= caption.substring(1, 16);
		            	}
		            	caption += " ("+ MoonDays[i][1]+")";
		            	break;
		            }
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

