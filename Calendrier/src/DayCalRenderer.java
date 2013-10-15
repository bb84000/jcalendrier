/*
 * Table cell renderer and functions to populate the calendar
 * Use an extended label with a custom paint method to have the mmoon icons
 * on the right of the cell.  
 */


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import bb.arraylist.*;



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
	public Color colback = new Color(255,255,255);
	public Color colsun = new Color (192,255,255);
	public int selDay = -1;
	public boolean ShowMoon;
	public boolean ShowVacA;
	public boolean ShowVacB; 
	public boolean ShowVacC; 
	// Arraylist containing all year days with their properties
	public ArrayList<CalDay> YearDays= new ArrayList<CalDay>();
	public String[] HalfImages = { "","" }; 
	public String workingDirectory = "";

	// cell dimensions
	private int width = 0;
	private int height = 0;

	private int year;
	private int quarter;
	String days= "DLMMJVSD";
	String iniday= "L";
	private int month;
	//private int dow;

	private String[][] MoonDays; 
	private bArrayList feries;
	public bArrayList imagesHalf;
	public String imgfile;
	private bArrayList saints;
	private bArrayList vacscol;
	private DateTime seas;



	private boolean okMoon;


	// Day structure and properties
	public class CalDay {
		public DateTime date;
		public String sdate;
		public String saint;
		public String lsaint; 
		public DateTime timelune;
		public String typelune;
		public String typevacscol;
		public String zonevacscol; 
		public String ferie;
		public String season;
		public DateTime seasondate;
		public CalDay(DateTime ddate, String dsdate, String dsaint, String dlsaint, DateTime dtimelune, String dtypelune, String dtypevacscol, String dzonevacscol, String dferie, String dseason, DateTime dseasondate ) {
			date= ddate;
			sdate= dsdate;
			saint= dsaint;
			lsaint = dlsaint;
			timelune= dtimelune;
			typelune= dtypelune;
			typevacscol= dtypevacscol;
			zonevacscol = dzonevacscol;
			ferie = dferie;
			season = dseason;
			seasondate = dseasondate;
		}
	}



	// Classes and variables initialization

	public void init() {
		// panel half images
		imagesHalf = new bArrayList();
		imgfile= "imgshalf.csv";
		if (workingDirectory.length()> 0) imgfile = workingDirectory+"/"+imgfile;
		imagesHalf.readCSVfile(imgfile);


		// saints arrays
		saints = new bArrayList();
		String sntfile = "saints.csv";

		if (workingDirectory.length()> 0) sntfile = workingDirectory+"/saints.csv";
		if (!saints.readCSVfile(sntfile, "cp1252")) {
			saints.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/saints.csv" ),"cp1252");
		}


		//Moon = new PhaseMoon();
		MoonDays = new String[56][2];

		// Ferie days
		feries = new bArrayList();
		String ferfile = "ferie.csv";
		if (workingDirectory.length()> 0) ferfile = workingDirectory+"/ferie.csv";
		if (!feries.readCSVfile(ferfile)) {
			feries.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/ferie.csv"));
		}

		// Scolar holidays
		vacscol = new bArrayList();
		if (!vacscol.readCSVfile("vacscol.csv")) {
			vacscol.readCSVstream(ClassLoader.class.getResourceAsStream("/resources/vacscol.csv"));
		}	
	}
	// Set the displayed year
	public void setYear (int y){
		year = y;
		int DaysCount = 366;
		astro Astr = new astro(); 

		// Get half image if exists
		HalfImages [0] = "";
		HalfImages [1] = "";       
		if (!imagesHalf.isEmpty())
		{
			Iterator<String[]> itr = imagesHalf.iterator();
			while(itr.hasNext()) {
				int yr = 0;
				String [] element =  itr.next();
				yr = Integer.parseInt(element [0]);
				if (yr==y) {
					int hlf = Integer.parseInt(element[1]);
					HalfImages [hlf] = element[2];
				}
			}
		} // end half images

		// Create an arraylist of all year days, begin 1st january
		DateTime CurDay = new DateTime(year,1,1,0,0,1);

		// Clone saints list to process leap ane non leap years 
		bArrayList CurSaints = (bArrayList) saints.clone(); 

		if (!Astr.isLeapYear(year)) {
			DaysCount = 365;
			CurSaints.remove(59);  // remove february 29 saint
		}

		YearDays.clear(); 

		// Create and fill the days list with saints
		if (!saints.isEmpty()) {

			for (int i=0; i < DaysCount; i+=1) {
				String s;
				try {
					// long saint name 
					s = CurSaints.get(CurDay.getDayOfYear()-1)[2];
					if (s.length()==0) s= CurSaints.get(CurDay.getDayOfYear()-1)[1];
				} catch (Exception e) {
					//in case of trouble with user list
					s= "" ;
				}

				YearDays.add( new CalDay(CurDay, CurDay.toString("dd/MM/yyyy"), CurSaints.get(CurDay.getDayOfYear()-1)[1],
						s, null, "","","","","",null));
				// increment day
				CurDay = CurDay.plusDays(1);	
			}
		}
		else {  // no saints to load, create list
			for (int i=0; i < DaysCount; i+=1) {
				YearDays.add(new CalDay(CurDay, CurDay.toString("dd/MM/yyyy"), "","",null, "","","","","",null));
				// increment day
				CurDay = CurDay.plusDays(1);
			}
		} // end yeardays list and saints


		// add moonphases to days list
		okMoon= Astr.GetMoonDays(MoonDays, y); 
		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy-HH:mm");
		DateTime tmpMoonD = new DateTime();
		if (okMoon ){ 
			for (int i=0; i<56; i+=1) {
				try {
					tmpMoonD = formatter.parseDateTime(MoonDays[i][0]);
					tmpMoonD = tmpMoonD.plusMinutes(Astr.getTZOff(tmpMoonD));
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
		if  (!feries.isEmpty()) {  
			// Mobile fetes
			DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
			Iterator<String[]> itr = feries.iterator();
			while(itr.hasNext()) {
				String [] element =  itr.next();
				String typday = element[0];
				String s1 = element[1];
				String s2 = element[2];

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
							else if (typday.equals("FETMER")) YearDays.get(mer.getDayOfYear()-1).ferie= s1;
						}	
					}
				}
				catch (Exception e) {
					//do nothing invalid entry;
				}
			}

		} // end feries

		// Add scolar holidays to days list
		if  (!vacscol.isEmpty()) {  
			DateTimeFormatter format = DateTimeFormat.forPattern("dd/MM/yyyy");
			Iterator<String[]> itr = vacscol.iterator();
			while(itr.hasNext()) {
				int yr = 0;
				String [] element =  itr.next();
				try {
					yr = Integer.parseInt(element[0]);
					if (yr==year){
						DateTime datebeg = format.parseDateTime(element[1]);
						DateTime dateend = format.parseDateTime(element[2]);
						int j = datebeg.getDayOfYear();
						while (j <= dateend.getDayOfYear()){
							YearDays.get(j-1).typevacscol = element[3];	
							String ss = YearDays.get(j-1).zonevacscol;
							YearDays.get(j-1).zonevacscol = ss+ element[4];
							j +=1;
						}
					}		
				} catch (NumberFormatException e) {
					// do nothing invalid entry
				}
			}

		} // end vacscol


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

		// Retrieve table font 
		setFont(table.getFont());
		quarter = Integer.parseInt(table.getName())-1;
		month = column+1+quarter*3;
		DateTime dt;
		setBackground(colback);
		String caption="";
		setForeground(table.getForeground());
		//System.out.println(width);
		try {
			dt = new DateTime(year, month, row+1, 12, 0 );
			int dow = dt.getDayOfWeek();
			int dy = dt.getDayOfYear();
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
				setBackground(colsun);
			}

			// Couleur dimanches
			if (dow == 7) {  

				//component.setBackground(clr);
				setBackground(colsun);
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