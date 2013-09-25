/*
 * Astro class
 * 
 * TimeDate getPaques(int year)
 * 	Get Paques date (joda format)
 * 	Based on Butcher algorithm
 * 	Wikipedia http://fr.wikipedia.org/wiki/Calcul_de_la_date_de_P%C3%A2ques
 * 
 * void GetMoonDays(String[][] MoonDays, int year)
 * 	Fill an array with moon phases dates and type 
 * 		variable passed MoonDays must have at least 56 rows and 2 columns
 * 	
 * boolean isLeapYear(int year)
 *  This test (written for C, C#, Java, and most other C-like languages) utilizes a single TRUE/FALSE expression that consists of three separate tests:
 * 	4th year test: year & 3
 * 	100th year test: year % 25
 *	400th year test: year & 15
 * 	See: http://stackoverflow.com/questions/3220163/how-to-find-leap-year-programatically-in-c/11595914#11595914
 * 
 * int trunc (double x)
 * 	returns integer part of a double
 * 
 * double frac (double x)
 * 	returns fractional par of a double
 * 
 * DateTime GetSaisonDate(int year, int num) 
 *  returns date of new season
 *  num : spring 0, summer 1, autumn 2, winter 3
 * 
 * DateTime calcSunrise(DateTime dt, double latitude, double longitude, boolean rise)
 *  Return sunrise (rise = true) and sunset (rise = false)
 * 
 * sunrise and sunset based on parts of code from NOAA Solar Calculator
 * at http://www.srrb.noaa.gov/highlights/sunrise/sunrise.html    
 * 
 * julian / DateTime conversions
 * 
 */

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class astro {

	// Give paques day 
	
		public DateTime getPaques(int year){
			int n = year % 19 ;  							// Meton cycle
			int c = year / 100;								// Century
			int u = year % 100;								// Year rank
			int s = c / 4 ;									// Leap century
			int t = c % 4;									// Century rank
			int p = (c+8) / 25; 							// Proemptose
			int q = (c-p+1) / 3;							// Metemptose
			int e = ((19 * n) + c - s - q + 15) % 30;		// Epacte
			int b = u / 4;									// Leap year
			int d = u % 4;									// Reminder
			int L = (32 + (2 * t) + (2 * b) - e - d) % 7;	// Dominical letter
			int h = (n + (11 * e) + (22 * L)) / 451 ;		// Correction
			int m = (e + L - (7 * h) + 114) / 31 ;			// Paques Month
			int j= (e + L - (7 * h) + 114) % 31 ;			// Paques day	
			DateTime dt = new DateTime(year,m,j,0,0 );
			// Add 1 day for paques
			dt = dt.plusDays(1);	
			return dt;
		}
		
		public boolean GetMoonDays(String[][] MoonDays, int year){
			// const du jour julien du 01-01-2000 à 12h TU
			// ou 0h GMT 01-01-2000 12:00:00 TU 2451545 JJ
			// const JJ2000 = 2451545;	
			double AnDecim, T, T2, T3, Rd, AMSol, AMLun, LatLune, PhMoyL;
			byte gLunes, PhLun, HrLun, MnLun;
			int  AnPh, MoPh, LunAnW, LunMsW, JrLunEntW;
			double CptLMax, CptL, PentPhL, PentPhMoyL, PfracPhMoyL, Alpha, B, C, D, E;
			double LunAn, LunMs, JrLun, JrLunFrac, TotHeu, TotMin, TotSec;
			//boolean  AnBis;	
			// be sure there is enough place in the array
			if ((MoonDays.length<56) || (MoonDays[0].length <2)) {
				return false;
			}
			// Date au 31 décembre de l'année prédédente 
			
			AnPh = year-1;
			MoPh = 12;
			
			gLunes= 0;
			if (MoPh == 0) {
				MoPh = 12; 
				AnPh = AnPh - 1;   
			}
			
			CptLMax= 14; // définit le nb phase de lune ex: 13 lunes => 56 phases
			//AnBis = dt.yearOfEra().isLeap();
			//AnBis= ((year % 4)== 0);    // si = 0 année bissextile
			AnDecim = 0;
			if (isLeapYear(year)) {
				switch (MoPh) {	
					case 1: AnDecim= 4.24375935815675E-02;
					case 2: AnDecim= 0.124574871481376;
					case 3: AnDecim= 0.20534319474952;
					case 4: AnDecim= 0.288849427280992;
					case 5: AnDecim= 0.372355659812463;
					case 6: AnDecim= 0.455861892343935;
					case 7: AnDecim= 0.539368124875406;
					case 8: AnDecim= 0.624243312038541;
					case 9: AnDecim= 0.707749544570013;
					case 10: AnDecim= 0.791255777101484;
					case 11: AnDecim= 0.874762009632956;
					case 12: AnDecim= 0.958268242164428;
				}	
			}
			else {
				switch (MoPh) {
					case 1: AnDecim= 4.24375935815675E-02;
					case 2: AnDecim= 0.123205916849712;
					case 3: AnDecim= 0.203974240117857;
					case 4: AnDecim= 0.287480472649328;
					case 5: AnDecim= 0.3709867051808;
					case 6: AnDecim= 0.454492937712271;
					case 7: AnDecim= 0.537999170243743;
					case 8: AnDecim= 0.622874357406878;
					case 9: AnDecim= 0.706380589938349;
					case 10: AnDecim= 0.789886822469821;
					case 11: AnDecim= 0.873393055001292;
					case 12: AnDecim= 0.956899287532764;
				}
			}
			// calcul nb de lunaison CptL nb de lunes en 1 an 12.3685 => 365.25 / 29.53058
			// nombre de lunaisons depuis le 1/1/1900
			
			CptL= trunc(((AnPh + AnDecim) - 1900) * 12.3685);
			
			CptLMax= CptL + CptLMax;
			
			// Calcul des phases
			while  (CptL < CptLMax) {
			    T= CptL / 1236.85; 
			    T2= T*T; 
			    T3= T*T*T; 
			    Rd= Math.PI / 180;
		    	// anomalie moyenne du soleil : AMSol
			    AMSol= 359.2242 + (29.10535608 * CptL) - (0.0000333 * T2) - (0.00000347 * T3);
			    if (AMSol > 360) AMSol= frac(AMSol/360) * 360; // intervalle 0-360°
			    // anomalie moyenne de la lune : AMLun
			    AMLun= 306.0253 + (385.81691806 * CptL) + (0.0107306 * T2) + (0.00001236 * T3);
			    if (AMLun > 360) AMLun= frac(AMLun/360) * 360; // intervalle 0-360°
			    // Latitude de la lune
			    LatLune= 21.2964 + (390.67050646 * CptL)-(0.0016528 * T2)-(0.00000239 * T3);
			    if (LatLune > 360) LatLune= frac(LatLune/360) * 360; // intervalle 0-360°
			    // Phase moyenne de la Lune 2415020.75933 1er jour julien 1/1/-4711
			    PhMoyL= 2415020.75933 + (29.53058868 * CptL) + (0.0001178*T2)-(0.000000155*T3);
			    PhMoyL= PhMoyL + (0.00033*Math.sin((Rd*166.56) + (Rd*132.87)*T)-((Rd*0.009173*T2)));
			    // degrés en radian
			    AMSol= AMSol * Rd;
			    AMLun= AMLun * Rd;
			    LatLune= LatLune * Rd;
			    // correction de la phase vraie pour nouvelle et pleine lune
			    if ((frac(CptL) == 0.0) || (frac(CptL) == 0.5) || (frac(CptL) == -0.5)) { 
			    	PhMoyL= PhMoyL + ((0.1734 - 0.000393 * T) * Math.sin(AMSol));
			    	PhMoyL= PhMoyL + (0.0021 * Math.sin(2 * AMSol));
			    	PhMoyL= PhMoyL - (0.4068 * Math.sin(AMLun));
			    	PhMoyL= PhMoyL + (0.0161 * Math.sin(2 * AMLun));
			    	PhMoyL= PhMoyL - (0.0004 * Math.sin(3 * AMLun));
			    	PhMoyL= PhMoyL + (0.0104 * Math.sin(2 * LatLune));
			    	PhMoyL= PhMoyL - (0.0051 * Math.sin(AMSol + AMLun));
			    	PhMoyL= PhMoyL - (0.0074 * Math.sin(AMSol - AMLun));
			    	PhMoyL= PhMoyL + (0.0004 * Math.sin((2 * LatLune) + AMSol));
			    	PhMoyL= PhMoyL - (0.0004 * Math.sin((2 * LatLune) - AMSol));
			    	PhMoyL= PhMoyL - (0.0006000001 * Math.sin((2 * LatLune) + AMLun));
			    	PhMoyL= PhMoyL + (0.001 * Math.sin((2 * LatLune) - AMLun));
			    	PhMoyL= PhMoyL + 0.0005 * Math.sin(AMSol + (2 * AMLun));
			    }
			    else {
			    	// correction de la phase vraie pour premier et dernier quartier lune
			    	PhMoyL= PhMoyL + (0.1721 - 0.0004 * T) * Math.sin(AMSol);
			    	PhMoyL= PhMoyL + 0.0021 * Math.sin(2 * AMSol);
			    	PhMoyL= PhMoyL - 0.628  * Math.sin(AMLun);
			    	PhMoyL= PhMoyL + 0.0089 * Math.sin(2 * AMLun);
			    	PhMoyL= PhMoyL - 0.0004 * Math.sin(3 * AMLun);
			    	PhMoyL= PhMoyL + 0.0079 * Math.sin(2 * LatLune);
			    	PhMoyL= PhMoyL - 0.0119 * Math.sin(AMSol + AMLun);
			    	PhMoyL= PhMoyL - 0.0047 * Math.sin(AMSol - AMLun);
			    	PhMoyL= PhMoyL + 0.0003 * Math.sin(2 * LatLune + AMSol);
			    	PhMoyL= PhMoyL - 0.0004 * Math.sin(2 * LatLune - AMSol);
			    	PhMoyL= PhMoyL - 0.0006000001 * Math.sin(2 * LatLune + AMLun);
			    	PhMoyL= PhMoyL + 0.0021 * Math.sin(2 * LatLune - AMLun);
			    	PhMoyL= PhMoyL + 0.0003 * Math.sin(AMSol + 2 * AMLun);
			    	PhMoyL= PhMoyL + 0.0004 * Math.sin(AMSol - 2 * AMLun);
			    	PhMoyL= PhMoyL - 0.0003 * Math.sin(2 * AMSol - AMLun);
			    	// ajustement suivant le quartier
			    	if (CptL >= 0)  {
			    		if (frac(CptL) == 0.25) PhMoyL= PhMoyL + 0.0028 - 0.0004 * Math.cos(AMSol) + 0.0003 * Math.cos(AMLun);  //1er quartier
			    		if (frac(CptL) == 0.75) PhMoyL= PhMoyL - 0.0028 + 0.0004 * Math.cos(AMSol) - 0.0003 * Math.cos(AMLun);  //dernier quartier
			    	}
			    	else {
			    		if (frac(CptL) == -0.25) PhMoyL= PhMoyL - 0.0028 + 0.0004 * Math.cos(AMSol) - 0.0003 * Math.cos(AMLun);
			    		if (frac(CptL) == -0.75) PhMoyL= PhMoyL + 0.0028 - 0.0004 * Math.cos(AMSol) + 0.0003 * Math.cos(AMLun);
			    	}
			    }
			    // calcul des dates de lune calendrier
			    PhMoyL= PhMoyL + 0.5;
			    PentPhMoyL= trunc(PhMoyL);
			    PfracPhMoyL= frac(PhMoyL);

			    if (PentPhMoyL <  2299161) {
			    	PentPhL= PentPhMoyL;
			    } 
			    else {
			    	Alpha= trunc((PentPhMoyL - 1867216.25) / 36524.25);
			      	PentPhL= PentPhMoyL + 1 + Alpha - trunc(Alpha / 4);
			    }
			    B= PentPhL + 1524;
			    C= trunc((B - 122.1) / 365.25);
			    D= trunc(365.25 * C);
			    E= trunc((B - D) / 30.6001);
			    JrLun= B - D - trunc(30.6001 * E) + PfracPhMoyL;
			    LunAn= 1;
			    LunMs= 1; // initialisation
			    if (E < 13.5) LunMs= E - 1;
			    if (E > 13.5) LunMs= E - 13;
			    if (LunMs > 2.5) LunAn= C - 4716;
			    if (LunMs < 2.5) LunAn= C - 4715;
			    LunAnW= trunc(LunAn);
			    LunMsW= trunc(LunMs);
			    JrLunEntW= trunc(JrLun);
			    JrLunFrac= frac(JrLun);
			    TotSec= JrLunFrac * 86400;
			    TotHeu= (TotSec / 3600);
			    HrLun= (byte) trunc(TotHeu);
			    TotMin= frac(TotHeu) * 60;
			    MnLun= (byte) trunc(TotMin);
			    PhLun= 0;
			    if (CptL >= 0) { 
		    	      if (frac(CptL) == 0.0) PhLun= 0;    //NL
			    	  if (frac(CptL) == 0.125) PhLun= 1;  //PC
			    	  if (frac(CptL) == 0.25)  PhLun= 2;  //PQ		
			    	  if (frac(CptL) == 0.375) PhLun= 3;  //GC
			    	  if (frac(CptL) == 0.5)  PhLun= 4;   //PL
			    	  if (frac(CptL) == 0.625) PhLun= 5;  //GD
			    	  if (frac(CptL) == 0.75)  PhLun= 6;  //DQ	
			    	  if (frac(CptL) == 0.875) PhLun= 7;  //DC
			    }
			    else {
			    	  if (frac(CptL) == -0.875) PhLun= 7;
			    	  if (frac(CptL) == -0.75)  PhLun= 6;  //DQ
			    	  if (frac(CptL) == -0.625) PhLun= 5;
			    	  if (frac(CptL) == -0.5)   PhLun= 4;  //PL
			    	  if (frac(CptL) == -0.375) PhLun= 3;
			    	  if (frac(CptL) == -0.25)  PhLun= 2;  //PQ
			    	  if (frac(CptL) == -0.125) PhLun= 1;
			    	  if (frac(CptL) ==  0.0)   PhLun= 0;  // NL
			    }
				String[] Lunes = {"NL","PC","PQ","GC","PL","GD","DQ","DC"};
				// Heure de la lune
			    // seulement si année en cours
				if (LunAnW==year)
				{
				  MoonDays[gLunes][0]= String.format("%02d/%02d/%04d-%02d:%02d", JrLunEntW, LunMsW, LunAnW, HrLun,MnLun);
				  MoonDays[gLunes][1]= Lunes [PhLun];
				  gLunes += 1;
				}
			    CptL= CptL + 0.250;
			}
			return true;
			
		} // end of Get_MoonDays function
		
		// Fête des déportés
		public DateTime GetDeportes (int year) {
		 // dernier dimanche d'avril
			DateTime dt = new DateTime(year, 4, 30,0,0,0);           // On prend la fin du mois
		    int d = dt.getDayOfWeek();
			return dt.plusDays(-d);        // Et on retire les jours nécessaires
		}
		
		// fete des meres
		public DateTime GetFetmeres (int year) {
		 // dernier dimanche de mai, sauf si pentecôte
			DateTime dt = new DateTime(year, 5, 31,0,0,0);           // On prend la fin du mois
			int d = dt.getDayOfWeek();
			dt = dt.plusDays(-d); // Et on retire les jours nécessaires
			DateTime paq = getPaques(year);
			if (dt.getDayOfYear() == paq.getDayOfYear()+49) dt = dt.plusDays(7); 
			return   dt  ;    
		}
		
		// Meeus Astronmical Algorithms Chapter 27

		DateTime GetSaisonDate(int year, int num) {
			double jdeo,  yr, t, w, dl, s, julDay;
			double deltaT;
			double scl;
			// Caclul initial du jour julien
			yr=(year-2000)/1000.0;
			jdeo= 0;
			
			switch (num) {
				case 0: jdeo= 2451623.80984 + 365242.37404*yr + 0.05169*Math.pow(yr,2) - 0.00411*Math.pow(yr,3) - 0.00057*Math.pow(yr,4); break;
				case 1: jdeo= 2451716.56767 + 365241.62603*yr + 0.00325*Math.pow(yr,2) + 0.00888*Math.pow(yr,3) - 0.00030*Math.pow(yr,4); break;
				case 2: jdeo= 2451810.21715 + 365242.01767*yr - 0.11575*Math.pow(yr,2) + 0.00337*Math.pow(yr,3) + 0.00078*Math.pow(yr,4); break;
				case 3: jdeo= 2451900.05952 + 365242.74049*yr - 0.06223*Math.pow(yr,2) - 0.00823*Math.pow(yr,3) + 0.00032*Math.pow(yr,4); break;
				//default: jdeo= 0;
		  }
		   	    
		    t= (jdeo - 2451545.0)/36525.0;
		    w= (35999.373*t) - 2.47;
		    dl= 1 + 0.0334*CosD(w) + 0.0007*CosD(2*w);
		    // Correction périodique
		    s= periodic24(t);
		    julDay= jdeo + ( (0.00001*s) / dl ); 	// This is the answer in Julian Emphemeris Days

		    // écart entre UTC et DTD en secondes entre les années from Meeus Astronmical Algroithms Chapter 10
		    scl= (year - 2000) / 100.0;
		    deltaT= 102 + 102*scl + 25.3*Math.pow(scl,2);
		    // Special correction to avoid discontinurity in 2000
		    if ((year >=2000) && (year <=2100)) deltaT= deltaT+ 0.37 * ( year - 2100 );
		    // Ecart en jour fractionnaire
		    deltaT= deltaT/86400.0;
		    // On y est ! Conversion en date réelle 
		    julDay= julDay - deltaT; 
		    return julianTodt(julDay);
		}
		
		/*
		// convert DateTime to Julian date
		// (Julian day number algorithm adopted from Press et al.)
		*/
		
		public double dtTojulian(DateTime dt)
		{
			double jy, ja, jm;			//scratch
			int y, m, d, h, mn, s;
			y = dt.getYear();
			m = dt.getMonthOfYear();
			d = dt.getDayOfMonth();
			h = dt.getHourOfDay();
			mn = dt.getMinuteOfHour();
			s = dt.getSecondOfMinute();
			
			if( y == 0 ) {
				//alert("There is no year 0 in the Julian system!");
		        return 0;
		    }
		    if( y == 1582 && m == 10 && d > 4 && d < 15 ) {
				//alert("The dates 5 through 14 October, 1582, do not exist in the Gregorian system!");
		        return 0;
		    }

			//if( y < 0 )  ++y;
		    //if( era == "BCE" ) y = -y + 1;
			
		    if( m > 2 ) {
				jy = y;
				jm = m + 1;
			} else {
				jy = y - 1;
				jm = m + 13;
			}

			int intgr = trunc( trunc(365.25*jy) + trunc(30.6001*jm) + d + 1720995 );

			//check for switch to Gregorian calendar
		    double gregcal = 15 + 31*( 10 + 12*1582 );
			if( d + 31*(m + 12*y) >= gregcal ) {
				ja = trunc(0.01*jy);
				intgr += 2 - ja + trunc(0.25*ja);
			}
            
			//correct for half-day offset
			double dayfrac = h/24.0 - 0.5;
			if( dayfrac < 0.0 ) {
				dayfrac += 1.0;
				--intgr;
			}

			//now set the fraction of a day
			double frac = dayfrac + (mn + s/60.0)/60.0/24.0;
     	    double jd0 = (intgr + frac)*100000;
		   // round to seconds
		    double jd  = Math.floor(jd0);
		    
		    if( jd0 - jd > 0.5 ) ++jd;
		    return jd / 100000.0;
		}
		
		
		/*
		 * Converts a Julian day to a calendar date
		 */
		 public DateTime  julianTodt (double jDay) {
			 int j1, j2, j3, j4, j5;
			 //;			//scratch
			 // get the date from the Julian day number
			 int jDate = trunc (jDay);
			 double jTime = frac (jDay);
			 // Gregorian Correction 1582
			 int gregjd  = 2299161;
			 if( jDate >= gregjd ) {				
				int tmp = trunc (((jDate - 1867216) - 0.25 ) / 36524.25 );
					j1 = jDate + 1 + tmp - trunc(0.25*tmp);
				} else
					j1 = jDate;
			//correction for half day offset
			double dayfrac = jTime + 0.5;
			if( dayfrac >= 1.0 ) {
				dayfrac -= 1.0;
				++j1;
			}
			
			j2 = j1 + 1524;
			j3 = trunc( 6680.0 + ( (j2 - 2439870) - 122.1 )/365.25 );
			j4 = trunc(j3*365.25);
			j5 = trunc( (j2 - j4)/30.6001 );

			int d = j2 - j4 - trunc(j5*30.6001);
			int m = j5 - 1;
			if( m > 12 ) m -= 12;
			int y = j3 - 4715;
			if( m > 2 )   --y;
			if( y <= 0 )  --y;
			
			// get time of day from day fraction
			int hr  = trunc(dayfrac * 24.0);
			int mn  = trunc((dayfrac*24.0 - hr)*60.0);
			double	f  = ((dayfrac*24.0 - hr)*60.0 - mn)*60.0;
			int sc  = trunc(f);
				 f -= sc;
		    if( f > 0.5 ) ++sc;
		    /*boolean bc;
		    if( y < 0 ) {
		     	y = -y;
		        bc= true;
		    } else
		        bc= false;*/
		   if (sc == 60) {
			   sc = 59;
               	
		   }

			   
           DateTime dt = new DateTime(y,m,d,hr,mn,sc);
		   return dt;
		} // end julianTodt
		 
		// trunc double to integer
		public int trunc(double x) {
		   if (x>0.0) return (int) Math.floor(x);
		   else  return (int) Math.ceil(x);
		}
		
		// return fractional part of a double
		public double frac (double x){
			return x - trunc(x);
		}
		
		// Leap year test
		public boolean isLeapYear (int year) {
			return ((year & 3) == 0 && ((year % 25) != 0 || (year & 15) == 0));/* leap year */
		}
		
		// Used in seasons routine
		public double periodic24 (double t) {
			double result = 0;
			int [] A = {485,203,199,182,156,136,77,74,70,58,52,50,45,44,29,18,17,16,14,12,12,12,9,8};
			double [] B = {324.96,337.23,342.08,27.85,73.14,171.52,222.54,296.72,243.58,119.81,297.17,21.02,
				     247.54,325.15,60.93,155.12,288.79,198.04,199.76,95.39,287.11,320.81,227.73,15.45};
			double [] C = {1934.136,32964.467,20.186,445267.112,45036.886,22518.443,
					     65928.934,3034.906,9037.513,33718.147,150.678,2281.226,
		                             29929.562,31555.956,4443.417,67555.328,4562.452,62894.029,
					     31436.921,14577.848,31931.756,34777.259,1222.114,16859.074};
			for (int i=0; i<=23; i+= 1) {
			   result= result +  A[i]*CosD(B[i] + (C[i]*t));
			}
			return result;
		}
		
		// Cosinus avec entrée en degrés
		public double CosD (double x){
		  return Math.cos(Math.toRadians(x));
		}
		
		// Sinus avec entrée en degrés
		public double SinD (double x){
		  return Math.sin(Math.toRadians(x));
		}
		
		// Tangente avec entrée en degrés
		public double TanD (double x){
		  return Math.tan(Math.toRadians(x));
		}
		
		// Offset in minutes
		public int getTZOff(DateTime dt) {
			long TZoff = dt.getZone().getOffset(dt.getMillis());
			return trunc (TZoff/1000/60);
		}
		
		// DateTime to long String
		public String DateTimeToString (DateTime dt){
			
	
			try {
				DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
				return DateToString (dt)+" "+dt.toString(fmt);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "";
			} 
			
			
			 
		}
		
		public String DateToString (DateTime dt) {
			String[] Fdays = { "Dimanche", "Lundi", "Mardi", "Mercredi",
					"Jeudi", "Vendredi", "Samedi", "Dimanche" };
			String[] Fmonths = { "décembre", "janvier", "février", "mars",
					"avril", "mai", "juin", "juillet", "août", "septembre",
					"octobre", "novembre", "décembre" };
			try {
				return Fdays[dt.getDayOfWeek()]
						+ " " + dt.getDayOfMonth() + " "
						+ Fmonths[dt.getMonthOfYear()] + " " + dt.getYear() ;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return "";
			} 
		}
		
		
		
		//***********************************************************************/
		//* Name:    calcSunriseUTC								*/
		//* Type:    Function									*/
		//* Purpose: calculate the Universal Coordinated Time (UTC) of sunrise	*/
		//*			for the given day at the given location on earth	*/
		//* Arguments:										*/
		//*   JD  : julian day									*/
		//*   latitude : latitude of observer in degrees				*/
		//*   longitude : longitude of observer in degrees				*/
		//* rise = true sunrise else sunset
		//* Return value:										*/
		//*   time in minutes from zero Z							*/
		//*  
		//***********************************************************************/

		public DateTime calcSunrise(DateTime dt, double latitude, double longitude, boolean rise)
		{
			// check if sunrise or sunset
			int r ;
			if (rise) r =1;  	// sunrise
			else r= -1;			// sunset
			
			// longitudes are inversed (east negative)
			longitude = -1*longitude;
			
			double JD = dtTojulian(dt);
			double t = calcTimeJulianCent(JD);

			// *** Find the time of solar noon at the location, and use
			//     that declination. This is better than start of the 
			//     Julian day

			double noonmin = calcSolNoonUTC(t, longitude);
			double tnoon = calcTimeJulianCent (JD+noonmin/1440.0);

			// *** First pass to approximate sunrise or sunset (using solar noon)

			double eqTime = calcEquationOfTime(tnoon);
			double solarDec = calcSunDeclination(tnoon);
			double hourAngle = calcHourAngleSunrise(latitude, solarDec)*r;

			double delta = longitude - radToDeg(hourAngle);
			double timeDiff = 4 * delta;	// in minutes of time
			double timeUTC = 720 + timeDiff - eqTime;	// in minutes
			// *** Second pass includes fractional jday in gamma calc

			double newt = calcTimeJulianCent(calcJDFromJulianCent(t) + timeUTC/1440.0); 
			eqTime = calcEquationOfTime(newt);
			solarDec = calcSunDeclination(newt);
			hourAngle = calcHourAngleSunrise(latitude, solarDec)*r;
			delta = longitude - radToDeg(hourAngle);
			timeDiff = 4 * delta;
			timeUTC = 720 + timeDiff - eqTime; // in minutes
			int hr = (int) Math.floor(timeUTC /60);
			int mn= (int) Math.round(timeUTC- hr*60);
			int y = dt.getYear();
			int m = dt.getMonthOfYear();
			int d = dt.getDayOfMonth();
			return new DateTime(y,m,d,hr,mn,0);
		}
		
		
		//***********************************************************************/
		//* Name:    calcSolNoonUTC								*/
		//* Type:    Function									*/
		//* Purpose: calculate the Universal Coordinated Time (UTC) of solar	*/
		//*		noon for the given day at the given location on earth		*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//*   longitude : longitude of observer in degrees				*/
		//* Return value:										*/
		//*   time in minutes from zero Z							*/
		//***********************************************************************/

		public double calcSolNoonUTC(double t, double longitude)
			{
				// First pass uses approximate solar noon to calculate eqtime
				double tnoon = calcTimeJulianCent(calcJDFromJulianCent(t) + longitude/360.0);
				double eqTime = calcEquationOfTime(tnoon);
				double solNoonUTC = 720 + (longitude * 4) - eqTime; // min

				double newt = calcTimeJulianCent(calcJDFromJulianCent(t) -0.5 + solNoonUTC/1440.0); 

				eqTime = calcEquationOfTime(newt);
				// var solarNoonDec = calcSunDeclination(newt);
				solNoonUTC = 720 + (longitude * 4) - eqTime; // min
				
				return solNoonUTC;
			}

		//***********************************************************************/
		//* Name:    calcTimeJulianCent							*/
		//* Type:    Function									*/
		//* Purpose: convert Julian Day to centuries since J2000.0.			*/
		//* Arguments:										*/
		//*   jd : the Julian Day to convert						*/
		//* Return value:										*/
		//*   the T value corresponding to the Julian Day				*/
		//***********************************************************************/

		private double calcTimeJulianCent(double jd)
		{
			return (jd - 2451545.0)/36525.0;
		}

		//***********************************************************************/
		//* Name:    calcJDFromJulianCent							*/
		//* Type:    Function									*/
		//* Purpose: convert centuries since J2000.0 to Julian Day.			*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   the Julian Day corresponding to the t value				*/
		//***********************************************************************/

		private double calcJDFromJulianCent(double t)
			{
				double JD = t * 36525.0 + 2451545.0;
				return JD;
			}
		
		//***********************************************************************/
		//* Name:    calcObliquityCorrection						*/
		//* Type:    Function									*/
		//* Purpose: calculate the corrected obliquity of the ecliptic		*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   corrected obliquity in degrees						*/
		//***********************************************************************/

		private double calcObliquityCorrection(double t)
			{
				double e0 = calcMeanObliquityOfEcliptic(t);
				double omega = 125.04 - 1934.136 * t;
				double e = e0 + 0.00256 * Math.cos(degToRad(omega));
				return e;		// in degrees
			}

		
		//***********************************************************************/
		//* Name:    calcMeanObliquityOfEcliptic						*/
		//* Type:    Function									*/
		//* Purpose: calculate the mean obliquity of the ecliptic			*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   mean obliquity in degrees							*/
		//***********************************************************************/

		private double calcMeanObliquityOfEcliptic(double t)
			{
				double seconds = 21.448 - t*(46.8150 + t*(0.00059 - t*(0.001813)));
				double e0 = 23.0 + (26.0 + (seconds/60.0))/60.0;	
				return e0;		// in degrees
			}

		//***********************************************************************/
		//* Name:    calcSunApparentLong							*/
		//* Type:    Function									*/
		//* Purpose: calculate the apparent longitude of the sun			*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   sun's apparent longitude in degrees						*/
		//***********************************************************************/

		private double calcSunApparentLong(double t)
			{
				double o = calcSunTrueLong(t);
				double omega = 125.04 - 1934.136 * t;
				double lambda = o - 0.00569 - 0.00478 * Math.sin(degToRad(omega));
				return lambda;		// in degrees
			}
		
		
		//***********************************************************************/
		//* Name:    calcSunTrueLong								*/
		//* Type:    Function									*/
		//* Purpose: calculate the true longitude of the sun				*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   sun's true longitude in degrees						*/
		//***********************************************************************/

		private double calcSunTrueLong(double t)
			{
				double l0 = calcGeomMeanLongSun(t);
				double c = calcSunEqOfCenter(t);
				double O = l0 + c;
				return O;		// in degrees
			}
		
		//***********************************************************************/
		//* Name:    calGeomMeanLongSun							*/
		//* Type:    Function									*/
		//* Purpose: calculate the Geometric Mean Longitude of the Sun		*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   the Geometric Mean Longitude of the Sun in degrees			*/
		//***********************************************************************/

		private double calcGeomMeanLongSun(double t)
			{
				double L0 = 280.46646 + t * (36000.76983 + 0.0003032 * t);
				while(L0 > 360.0)
				{
					L0 -= 360.0;
				}
				while(L0 < 0.0)
				{
					L0 += 360.0;
				}
				return L0;		// in degrees
			}
		
		//***********************************************************************/
		//* Name:    calcSunEqOfCenter							*/
		//* Type:    Function									*/
		//* Purpose: calculate the equation of center for the sun			*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   in degrees										*/
		//***********************************************************************/

		private double calcSunEqOfCenter(double t)
			{
				double m = calcGeomMeanAnomalySun(t);
				double mrad = degToRad(m);
				double sinm = Math.sin(mrad);
				double sin2m = Math.sin(mrad+mrad);
				double sin3m = Math.sin(mrad+mrad+mrad);
				double C = sinm * (1.914602 - t * (0.004817 + 0.000014 * t)) + sin2m * (0.019993 - 0.000101 * t) + sin3m * 0.000289;
				return C;		// in degrees
			}

		//***********************************************************************/
		//* Name:    calGeomAnomalySun							*/
		//* Type:    Function									*/
		//* Purpose: calculate the Geometric Mean Anomaly of the Sun		*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   the Geometric Mean Anomaly of the Sun in degrees			*/
		//***********************************************************************/

		private double calcGeomMeanAnomalySun(double t)
			{
				double M = 357.52911 + t * (35999.05029 - 0.0001537 * t);
				return M;		// in degrees
			}	

		//***********************************************************************/
		//* Name:    calcSunDeclination							*/
		//* Type:    Function									*/
		//* Purpose: calculate the declination of the sun				*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   sun's declination in degrees							*/
		//***********************************************************************/

		private double calcSunDeclination(double t)
			{
				double e = calcObliquityCorrection(t);
				double lambda = calcSunApparentLong(t);
				double sint = Math.sin(degToRad(e)) * Math.sin(degToRad(lambda));
				double theta = radToDeg(Math.asin(sint));
				return theta;		// in degrees
			}

		//***********************************************************************/
		//* Name:    calcEquationOfTime							*/
		//* Type:    Function									*/
		//* Purpose: calculate the difference between true solar time and mean	*/
		//*		solar time									*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   equation of time in minutes of time						*/
		//***********************************************************************/

		private double calcEquationOfTime(double t)
			{
				double epsilon = calcObliquityCorrection(t);
				double l0 = calcGeomMeanLongSun(t);
				double e = calcEccentricityEarthOrbit(t);
				double m = calcGeomMeanAnomalySun(t);

				double y = Math.tan(degToRad(epsilon)/2.0);
				y *= y;

				double sin2l0 = Math.sin(2.0 * degToRad(l0));
				double sinm   = Math.sin(degToRad(m));
				double cos2l0 = Math.cos(2.0 * degToRad(l0));
				double sin4l0 = Math.sin(4.0 * degToRad(l0));
				double sin2m  = Math.sin(2.0 * degToRad(m));

				double Etime = y * sin2l0 - 2.0 * e * sinm + 4.0 * e * y * sinm * cos2l0
						- 0.5 * y * y * sin4l0 - 1.25 * e * e * sin2m;

				return radToDeg(Etime)*4.0;	// in minutes of time
			}
		
		//***********************************************************************/
		//* Name:    calcEccentricityEarthOrbit						*/
		//* Type:    Function									*/
		//* Purpose: calculate the eccentricity of earth's orbit			*/
		//* Arguments:										*/
		//*   t : number of Julian centuries since J2000.0				*/
		//* Return value:										*/
		//*   the unitless eccentricity							*/
		//***********************************************************************/

		private double calcEccentricityEarthOrbit(double t)
			{
				double e = 0.016708634 - t * (0.000042037 + 0.0000001267 * t);
				return e;		// unitless
			}
		
		//***********************************************************************/
		//* Name:    calcHourAngleSunrise							*/
		//* Type:    Function									*/
		//* Purpose: calculate the hour angle of the sun at sunrise for the	*/
		//*			latitude								*/
		//* Arguments:										*/
		//*   lat : latitude of observer in degrees					*/
		//*	solarDec : declination angle of sun in degrees				*/
		//* Return value:										*/
		//*   hour angle of sunrise in radians						*/
		//***********************************************************************/

		private double calcHourAngleSunrise(double lat, double solarDec)
			{
			// Todo change zenith angle	
			double latRad = degToRad(lat);
				double sdRad  = degToRad(solarDec);
				//double HAarg = (Math.cos(degToRad(90.833))/(Math.cos(latRad)*Math.cos(sdRad))-Math.tan(latRad) * Math.tan(sdRad));
				double HA = (Math.acos(Math.cos(degToRad(90.833))/(Math.cos(latRad)*Math.cos(sdRad))-Math.tan(latRad) * Math.tan(sdRad)));
				return HA;		// in radians
			}
		
			
		// Degree to radian conversion
		private double degToRad(double angleDeg) 
		{
			return (Math.PI * angleDeg / 180.0);
		}
		
		//Radian to degree conversion
		private double radToDeg(double angleRad) 
		{
			return (180.0 * angleRad / Math.PI);
		}
		

}
