/*
 * Get Pasques date
 * Based on Butcher algorithm
 * Wikipedia http://fr.wikipedia.org/wiki/Calcul_de_la_date_de_P%C3%A2ques
 */
import org.joda.time.DateTime;


public class paques {
	
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

}
