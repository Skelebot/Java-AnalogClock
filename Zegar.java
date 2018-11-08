package main;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.time.LocalTime;

import javax.swing.JFrame;
import javax.swing.JPanel;

//Specjalnie piszę program po polsku żeby był bardziej zrozumiały
//Normalnie napisałbym po angielsku bo jest krócej i lepiej wygląda
//ponieważ z reguły nie można używać polskich znaków w definicjach
//zmiennych itp.
public class Zegar {
	//Uwaga: typ int jest liczbą całkowitą, więc wszystko
	//co ma liczby po przecinku zgłosi wyjątek lub zostanie zaokrąglone
	//Typ Double może mieć liczby po przecinku.
	//Używamy w większości int ponieważ nie ma czegoś takiego
	//jak pół piksela na ekranie.
	
	//Promień tarczy zegara
	private static int promien = 150;
	//Środek ramki
	private static int srodekX = 0;
	private static int srodekY = 0;
	
	//Długości wskazówek zegara
	private static double dlugoscSec = promien * 0.95;
	private static double dlugoscMin = promien * 0.75;
	private static double dlugoscHr = promien * 0.5;
	
	private static JFrame ramka;
	private static PanelZegara panelzegara;
	
	//Lokalny czas, klasa dodana niedawno, w SE8
	static LocalTime czas;
	
	public static void main(String[] args) {
		//Inicjalizacja ramki (okna programu)
		ramka = new JFrame("Zegar");
		//Zmiana rozmiaru ramki. setSize() bierze pod uwagę dekoracje ramki (30 pikseli u góry i 7 na bokach)
		ramka.setSize(300 + 7, 300 + 30);
		panelzegara = new PanelZegara();
		ramka.add(panelzegara, BorderLayout.CENTER);
		//Nie można zmieniać rozmiaru ramki
		ramka.setResizable(false);
		//Zamknięcie ramki ma zamykać też program
		ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Pokaż ramkę
		ramka.setVisible(true);
		
		//Obliczanie środka okna na podstawie rozmiaru ramki
		srodekX = (ramka.getWidth() - 7)/2;
		srodekY = (ramka.getHeight() - 30)/2;
		
		czas = LocalTime.now();
		
		Thread th = new Thread(panelzegara);
		th.start();
	}
	
	//Klasa rysująca panel zegara
	@SuppressWarnings("serial")
	static class PanelZegara extends JPanel implements Runnable {
		
		//Zmienne oznaczające końcowe punkty wskazówek
		public Point sec;
		public Point min;
		public Point hr;
		
		public PanelZegara() {
			sec = new Point(0, 0);
			min = new Point(0, 0);
			hr = new Point(0, 0);
		}
		
		@Override
		public void run() {
			while(true) {
				//Do znajdowania kątu między godziną 12 a ręką zegara
				//używamy formuł z https://en.wikipedia.org/wiki/Clock_angle_problem
				czas = LocalTime.now();
				double katSec = czas.getSecond() * 6;
				sec = toCoords(dlugoscSec, katSec);
				
				double katMin = czas.getMinute() * 6;
				min = toCoords(dlugoscMin, katMin);
				
				double katHr = 0.5 * (60 * czas.getHour() + czas.getMinute());
				hr = toCoords(dlugoscHr, katHr);
			
			
				//Przerysuj tarczę
				repaint();
				
				//Wątek śpi pół sekundy aby zapobiegać zbyt częstemu odświeżaniu i zajmowaniu większości zużycia procesora
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//Metoda rysująca tarczę i wskazówki
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//Tworzymy obiekt do rysowania grafiki 2d i ustawiamy antyaliasing (wygładzanie linii)
			Graphics2D g2 = (Graphics2D)g;
		    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);
		    
		    //Czyścimy całą ramkę
		    g2.clearRect(0, 0, getWidth(), getHeight());
		    
		    g2.drawOval(0, 0, promien*2, promien*2);
		    
		    g2.drawLine(srodekX, srodekY, (int)sec.getX(), (int)sec.getY());
		    g2.drawLine(srodekX, srodekY, (int)min.getX(), (int)min.getY());
		    g2.drawLine(srodekX, srodekY, (int)hr.getX(), (int)hr.getY());
		    
		    //Rysowanie liczb - nie jest to niezbędne, ale zegarek wygląda ciekawiej
		    g2.drawChars(new char[]{'1','2'}, 0, 2, srodekX - 5, 12);	//12
		    g2.drawChars(new char[]{'3'}, 0, 1, srodekX*2 - 10, srodekY);	//3
		    g2.drawChars(new char[]{'6'}, 0, 1, srodekX - 3, srodekY*2 - 5);	//6
		    g2.drawChars(new char[]{'9'}, 0, 1, 5, srodekY);	//9
		}
		
	}
	
	//Zamiana kątu na koordynaty końcowe uzywając długości wskazówki
	public static Point toCoords(double length, double degrees) {
		Point wartosc = new Point();
		
		//length to długość wyrażona w promień * cośtam, na przykład 0.75
		wartosc.x = (int)(Math.sin(Math.toRadians(degrees)) * length + srodekX);
	    wartosc.y = (int)(Math.cos(Math.toRadians(degrees))* -1 * length + srodekY);
		
		return wartosc;
	}

}

/*Dodatkowe uwagi:
 * 
 * Linijki ze znaczkami @ jak na przykład @Override
 * nie są niezbędne, jednak zabezpieczają program przed błędami w wyniku
 * niepoprawnej modyfikacji kodu.
 * 
*/
