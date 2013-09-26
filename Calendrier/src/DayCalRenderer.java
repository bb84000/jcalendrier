/*
 * Table cell renderer and functions to populate the calendar
 * Use an extended label with a custom paint method to have the mmoon icons
 * on the right of the cell.  
 */


import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;



class DayCalRenderer
	extends		JLabel
	implements	TableCellRenderer
{

	private static final long serialVersionUID = 1L;
	// icon name
	private ImageIcon Image = null;
	// background color 
	private Color bkcolor = new Color(255,255,255);
	// lines draw parameters
	private ArrayList<LineDraw> Lines;
	
	// dLIne structure
	private class LineDraw {
		public int xb;
		public int yb;
		public int xe;
		public int ye;
		public Color col;
		public int thick; 
		public LineDraw (int lxb, int lyb, int lxe, int lye, Color lcol, int lthick) {
			xb= lxb;
			yb= lyb;
			xe= lxe;
			ye= lye;
			col= lcol;
			thick= lthick;
		}
	}
	
	public Color colA = new Color(255,204,0);
	public Color colB = new Color(255,0,0);
	public Color colC = new Color(0,153,0);
	public int selDay = -1;
	
	
	// cell dimensions
	private int width = 0;
	private int height = 0;
	
	private saints Saints;
	private int year;
    private int quarter;
    String days= "DLMMJVSD";
    String iniday= "L";
    private int month;
    //private int dow;
     public boolean ShowMoon;
    private String[][] MoonDays; 
    private CSVRead VacScol;
    private CSVRead Feries;
    public boolean ShowVacA;
    public boolean ShowVacB; 
    public boolean ShowVacC; 
    private DateTime seas;
    
    
    private boolean okMoon;
    // Arraylist containing all year days with their properties
    public ArrayList<CalDay> YearDays= new ArrayList<CalDay>();
    
 // Day structure and properties
    public class CalDay {
    	public DateTime date;
    	public String sdate;
    	public String saint;
    	public DateTime timelune;
    	public String typelune;
    	public String typevacscol;
    	public String zonevacscol; 
    	public String ferie;
    	public String season;
    	public DateTime seasondate;
    	public CalDay(DateTime ddate, String dsdate, String dsaint, DateTime dtimelune, String dtypelune, String dtypevacscol, String dzonevacscol, String dferie, String dseason, DateTime dseasondate ) {
    	    date= ddate;
    		sdate= dsdate;
			saint= dsaint;
			timelune= dtimelune;
			typelune= dtypelune;
			typevacscol= dtypevacscol;
			zonevacscol = dzonevacscol;
			ferie = dferie;
			season = dseason;
			seasondate = dseasondate;
		}
     }
    
	// Initialize permanent classes
    
    public DayCalRenderer  ()
	{
		Saints = new saints();
		//Moon = new PhaseMoon();
		MoonDays = new String[56][2];
		// Scolar holidays
		VacScol = new CSVRead();
		try {
			if (!VacScol.readCSVfile("vacscol.csv")) {
				if (!VacScol.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/vacscol.csv"))) {
					VacScol.Liste= null; 
				}
			}	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			VacScol.Liste= null; 
		}
		
		// Ferie days
		Feries = new CSVRead();
		try {
			if (!Feries.readCSVfile("ferie.csv")) {
				//boolean fe = Feries.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/ferie.csv"));
				if (!Feries.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/ferie.csv"))) {
					Feries.Liste= null; 
				}
			}
		} catch (Exception e) {
			Feries.Liste= null; 
		}
		// Sunset and sunrise
		// Other holidays
	}
	
    // Set the displayed year

	public void setYear (int y){
    	year = y;
        int DaysCount = 365;
        astro Astr = new astro();   	
        // Create an arraylist of all year days, begin 1st january
        DateTime CurDay = new DateTime(year,1,1,0,0,1);
        if (Astr.isLeapYear(year)) DaysCount = 366;
        YearDays.clear(); 


        // fill the days list with saints
        for (int i=0; i < DaysCount; i+=1){
        	CalDay tmpDay = new CalDay(CurDay, CurDay.toString("dd/MM/yyyy"), "",null, "","","","","",null);
        	String s = Saints.saints[CurDay.getDayOfMonth()-1][CurDay.getMonthOfYear()-1];
        	tmpDay.saint = s;
        	YearDays.add(tmpDay);
        	// increment day
        	CurDay = CurDay.plusDays(1);	
        }
      	
        // add moonphases to days list
        okMoon= Astr.GetMoonDays(MoonDays, y); 
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy-HH:mm");
        DateTime tmpMoonD = new DateTime();;
        if (okMoon ){ 
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
        
        // Add feries to days list
        if (!(Feries.Liste == null)){
            // Mobile fetes
            try {

    		} catch (Exception e1) {
    			// Do nothing, ther eis an error elsewere 
    		}
        	DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
    	   for (int i=0; i<Feries.Liste.size();i+=1) {
    		   String typday = Feries.Liste.get(i)[0];
    		   String s1 = Feries.Liste.get(i)[1];
    		   String s2 = Feries.Liste.get(i)[2];
    		  
    		   try {
				DateTime datemin = format.parseDateTime(s2);
    			if (datemin.getYear() <= year) {
				if (typday.equals("FIXE")){
					if (datemin.getYear() <= year) {
					   CurDay= new DateTime(year, datemin.getMonthOfYear(), datemin.getDayOfMonth(),0,0); 
					   YearDays.get(CurDay.getDayOfYear()-1).ferie= s1;
					 }
				}
				else {
					DateTime paq = Astr.getPaques(year);
					DateTime dep = Astr.GetDeportes(year);
					DateTime mer = Astr.GetFetmeres(year);
					if (typday.equals("DIPAQ"))	YearDays.get(paq.getDayOfYear()-1).ferie= s1;
					else if (typday.equals("LUPAQ")) YearDays.get(paq.getDayOfYear()).ferie= s1;
					else if (typday.equals("JEASC")) YearDays.get(paq.getDayOfYear()+38).ferie= s1;
					else if (typday.equals("DIPEN")) YearDays.get(paq.getDayOfYear()+48).ferie= s1;
					else if (typday.equals("LUPEN")) YearDays.get(paq.getDayOfYear()+48).ferie= s1;
					else if (typday.equals("SOUDEP")) {
						if (!(dep.getDayOfYear() == paq.getDayOfYear())) YearDays.get(dep.getDayOfYear()-1).ferie= s1;
					}
					else if (typday.equals("FETMER")) {
						//if (!(mer.getDayOfYear() == paq.getDayOfYear()+49)) YearDays.get(mer.getDayOfYear()-1).ferie= s1;
						//else 
						YearDays.get(mer.getDayOfYear()-1).ferie= s1;
						}
					}
    			}
			} catch (Exception e) {
				// invalid date
				//e.printStackTrace();
			}
    	   }
        }
        
        // Add scolar holidays to days list
        if (!(VacScol.Liste==null)) {
        	DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
        	
        	for (int i=0; i<VacScol.Liste.size()-1;i+=1) {
        		String s = VacScol.Liste.get(i)[0];
        		try {
					y = Integer.parseInt(s);
					if (y==year){
						DateTime datebeg = format.parseDateTime(VacScol.Liste.get(i)[1]);
						DateTime dateend = format.parseDateTime(VacScol.Liste.get(i)[2]);
						int j = datebeg.getDayOfYear();
						while (j <= dateend.getDayOfYear()){
							YearDays.get(j-1).typevacscol = VacScol.Liste.get(i)[3];	
							String ss = YearDays.get(j-1).zonevacscol;
							YearDays.get(j-1).zonevacscol = ss+ VacScol.Liste.get(i)[4];
							//System.out.println(YearDays.get(j-1).sdate+"-"+YearDays.get(j-1).zonevacscol);
							j +=1;
						}
					}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
        		
        	}
        
        }
        // Add seasons
        String [] season = {"Printemps","Eté","Automne","Hiver"};
        for (int i = 0; i < 4; i+= 1) {
        seas = Astr.GetSaisonDate(year, i);
        YearDays.get(seas.getDayOfYear()-1).season = season [i];
        YearDays.get(seas.getDayOfYear()-1).seasondate = seas;
        }
        
	}
	
	
	public Component getTableCellRendererComponent( JTable table,
				Object value, boolean isSelected, boolean hasFocus,
				int row, int column )
	{
		//Colors
		Color sunday_col = new Color(192, 255, 255);
	    // Retrieve table font 
		setFont(table.getFont());
		quarter = Integer.parseInt(table.getName())-1;
        month = column+1+quarter*3;
    	DateTime dt;
		setBackground(table.getBackground());
    	String caption="";
  	    setForeground(table.getForeground());
  	    //System.out.println(width);
   	    try {
			dt = new DateTime(year, month, row+1, 12, 0 );
			int dow = dt.getDayOfWeek();
			int dy = dt.getDayOfYear();
			if (hasFocus) {
				selDay = dy-1;
			    //System.out.println(selDay);
			}
			iniday= days.substring(dow,dow+1 );
		    caption += row+1;
			caption +=" "+iniday;
	        String saint = "";
	        saint=  YearDays.get(dy-1).saint;
	        // Saints et/ou fériés
	        if (YearDays.get(dy-1).ferie.length() == 0) {
	        	caption += " "+saint;
	        }
	        else {
	        	String snt= YearDays.get(dy-1).ferie;
	        	//System.out.println (saint.charAt(1));
	        	if (!(snt.charAt(0) == '.')) caption += " "+snt;
	        	else caption += " "+saint;
	        	setBackground(sunday_col);
	        }
	        
	        // Couleur dimanches
	        if (dow == 7) {  
	        	
              //component.setBackground(clr);
              setBackground(sunday_col);
            }

	        // Moon phases
	        if (okMoon && ShowMoon) {
	        	String lune = YearDays.get(dy-1).typelune;
	        	if (lune.length() > 0) {
	        		ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/"+lune+".png"));
	        		setHorizontalTextPosition(SwingConstants.LEFT);
	        		setIcon(icon);
	        	} 
	        }
	        
	        // scolar holidays 
	        //System.out.println(YearDays.get(dy-1).sdate+"-"+YearDays.get(dy-1).zonevacscol);
	        String s = YearDays.get(dy-1).zonevacscol;
	        //Lines.clear();
	        Lines =new ArrayList<LineDraw>();
	        LineDraw Line;
	        if (s.contains("A") && ShowVacA)
	        {
	        	Line= new LineDraw(width-9, 0, width-9, height-2, colA, 2);
	        	Lines.add(Line);
	        }
	        if (s.contains("B") && ShowVacB )
	        {
	        	Line= new LineDraw(width-6, 0, width-6, height-2, colB, 2);
	        	Lines.add(Line);
	        }
	         if (s.contains("C") && ShowVacC)
	        {
	        	Line= new LineDraw(width-3, 0, width-3, height-2, colC, 2);
	        	Lines.add(Line);
	        }
	        
	        
	         //Bold border around the current day ;
	        DateTime now;
	        now = new DateTime();
	        
	        if ((now.getYear()==dt.getYear()) && (now.getDayOfYear()==dt.getDayOfYear()))// && (now.getDayOfMonth()ayOfMonth()==dt.dayOfMonth())) 
              	{
              		Border line = BorderFactory.createLineBorder(Color.RED, 1);
              		setBorder(line);
              	}
	        else if (hasFocus) {
	        	Border line = BorderFactory.createLineBorder(table.getForeground(), 1);
			
	        	
              	   setBorder(line);	

	        	}
	        else  setBorder(null);
	       
        setText( caption );
 	    } catch (Exception e) {
			// N?e fait rien, le jour n'existe pas
			// Efface caption et lignes
 	    	setText ("");
 	    	Border line = BorderFactory.createLineBorder(table.getForeground(), 0); setBorder(line);
	        Lines.clear();
  	  }
  	    return this;
	}

	
	public void paint( Graphics g )
	{
		// This is a hack to paint the background.  Normally a JLabel can
		// paint its own background, but due to an apparent bug or
		// limitation in the TreeCellRenderer, the paint method is
		// required to handle this.
		g.setColor(this.bkcolor);	
		// cell dimensions are only fixed after a first paint
		width= getWidth();
		height = getHeight();
		// Draw a rectangle in the background of the cell
		g.fillRect( 0, 0, width - 1, height - 1 );
		
		//super.paint( g );

		// Draw holidays lines
		
		if (Lines.size() > 0)
		{
			Color curCol = g.getColor();
			for (int i=0; i < Lines.size(); i+= 1) {
			g.setColor(Lines.get(i).col);
				for (int j= 0; j<Lines.get(i).thick ;j+=1) {
					g.drawLine(Lines.get(i).xb+j, Lines.get(i).yb, Lines.get(i).xe+j, Lines.get(i).ye);
				}		
			}
				g.setColor(curCol);
		}
		super.paint( g );
		// Paint icon on right side of label
		try {
			if (Image != null){   
				int x = getWidth()- Image.getIconWidth() -1;
				int y = (getHeight()-Image.getIconHeight()) / 2;			
			Image.paintIcon(this, g, x, y);}
			Image = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
	}

	// replace original setIcon
	public void setIcon(ImageIcon img){
		Image= img;
		
	}
	
	public void setBackground(Color clr) {
		bkcolor= clr;
	}
	 
	//public void drawLines () {

		
	//}
}