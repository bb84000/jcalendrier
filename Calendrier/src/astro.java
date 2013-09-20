/*
 * Astro class
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
 */

import org.joda.time.DateTime;

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
			DateTime dt = new DateTime(2013,m,j,0,0 );
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
}
