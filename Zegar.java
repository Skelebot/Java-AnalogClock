package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	private static int promien = 300;
	//Środek ramki
	private static int srodekX = 0;
	private static int srodekY = 0;
	
	//Długości wskazówek zegara
	private static double dlugoscSec = promien;
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
		srodekX = 100;
		srodekY = 100;
		
		czas = LocalTime.now();
	}
	
	//Klasa rysująca panel zegara
	@SuppressWarnings("serial")
	static class PanelZegara extends JPanel implements Runnable {
		
		//Zmienne oznaczające końcowe punkty wskazówek
		public int secX;
		public int secY;
		
		public int minX;
		public int minY;
		
		public int hrX;
		public int hrY;
		
		@Override
		public void run() {
			
			//Przerysuj tarczę
			repaint();
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
		    
		    g2.drawOval(0, 0, promien, promien);
		    
		}
		
	}

}

/*Dodatkowe uwagi:
 * 
 * Linijki ze znaczkami @ jak na przykład @Override
 * nie są niezbędne, jednak zabezpieczają program przed błędami w wyniku
 * niepoprawnej modyfikacji kodu.
 * 
*/
