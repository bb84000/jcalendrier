import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DayCalRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private int year;
    private int quarter;
    String days= "DLMMJVSD";
    String iniday= "L";
    private int month;
    //private int dow;
    private PhaseMoon Moon = new PhaseMoon();
    private String[][] MoonDays = new String[56][2]; 
    public saints Saints = new saints();
    private boolean okMoon;
    public ArrayList<CalDay> YearDays= new ArrayList<CalDay>();;
    
    public class CalDay {
    	public DateTime date;
    	public String sdate;
    	public String saint;
    	public DateTime timelune;
    	public String typelune;
    	public CalDay(DateTime date, String sdate, String saint, DateTime timelune, String typelune) {
    		this.date= date;
    		this.sdate= sdate;
			this.saint= saint;
			this.timelune= timelune;
			this.typelune= typelune;
		}

    	
    }
    
    
    
    // initialization
    DayCalRenderer () throws Exception {
    	// todo 
		
    }
    
    // Set the displayed year
    // Todo ajouter les phases solaires
	//Todo ajouter les vacances scolaires
	public void setYear (int y){
    	year = y;
        int DaysCount = 365;
    	
        // Create an arraylist of all year days, begin 1st january
        DateTime CurDay = new DateTime(year,1,1,0,0,1);
        if (Moon.is_leapYear(year)) DaysCount = 366;
        YearDays.clear(); 
        
        // fill the arraylist with saints
        for (int i=0; i < DaysCount; i+=1){
        	CalDay tmpDay = new CalDay(CurDay, CurDay.toString("dd/MM/yyyy"), "",null, "");
        	String s = Saints.saints[CurDay.getDayOfMonth()-1][CurDay.getMonthOfYear()-1];
        	tmpDay.saint = s;
        	YearDays.add(tmpDay);
        	// increment day
        	CurDay = CurDay.plusDays(1);	
        }
      	// add moonphases to arraylist
        okMoon= Moon.Get_MoonDays(MoonDays, y); 
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy-HH:mm");
        DateTime tmpMoonD = new DateTime();;
        if (okMoon){ 
           	for (int i=0; i<56; i+=1) {
				try {
					tmpMoonD = formatter.parseDateTime(MoonDays[i][0]);
					CalDay tmpDay =YearDays.get(tmpMoonD.getDayOfYear()-1);
					tmpDay.typelune = MoonDays[i][1] ;
					tmpDay.timelune= tmpMoonD;
					YearDays.set(tmpMoonD.getDayOfYear()-1, tmpDay);
				} catch (Exception e) {
					// no more values in the moon table
					break;
				}
           	}
        }
       // for (int i=0; i<365; i+=1) {
       // 	System.out.println(YearDays.get(i).saint+"  "+YearDays.get(i).typelune ); 
       // }
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
				int dow = dt.getDayOfWeek();
				int dy = dt.getDayOfYear();
				iniday= days.substring(dow,dow+1 );
			    caption += row+1;
				caption +=" "+iniday;
		        String saint = "";
		       // if (month == column+1+quarter*3) 
		        //{
		        //	saint = Saints.saints [row][month-1];
		        saint=  YearDays.get(dy-1).saint;
		        caption += " "+saint;
		        	// Couleur dimanches
		        if (dow == 7) {  
		        	clr = new Color(0, 255, 255);
                    //component.setBackground(clr);
                    label.setBackground(clr);
                  }
                // Lune ?
		        
		        if (okMoon) {
		        	String lune = YearDays.get(dy-1).typelune;
		        	if (lune.length() > 0) {
		        		if (caption.length() >16) {
		        		    			caption= caption.substring(0, 15);
		        		    		}
		        		    		caption += " ("+lune+")";
		        	}
		        }
		       //Bold border around the current day ;
               DateTime now;
               now = new DateTime();
               if ((now.getYear()==dt.getYear()) && (now.getDayOfYear()==dt.getDayOfYear()))// && (now.getDayOfMonth()ayOfMonth()==dt.dayOfMonth())) 
                    {
                    Border line = BorderFactory.createLineBorder(clf, 2);
            	    label.setBorder(line);
            	    
              }
          label.setText(caption);
		   // }
			} catch (Exception e) {
				// N?e fait rien, le jopur n'existe pas
				//e.printStackTrace();
			}
	
    
    return component;
    }
}

