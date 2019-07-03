package spider;

import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import newssites.Link;
import newssites.Newssites;

public class Spider {

	public static void main(String[] args) {

		//new Thread( new ParseTest("https://d24am.com") ).run();
		
		try {
			ArrayList< Link > seeds = Newssites.getSeeds();
			
			for ( Link seed : seeds ) {
				if ( !seed.getLink().contains("portaldoholanda") )
					new Thread( new ParseTest()).run();
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//new Thread( new ParseTest("http://portaldoholanda.com.br") ).run();
		//new Thread( new ParseTest("https://d24am.com/") ).run();
		
		/*
		 * ParsingEngine.init(); Document doc = ParsingEngine.getDocument(
		 * "https://portaldoholanda.com.br/fdn/em-manaus-supostos-membros-da-fdn-sao-presos-com-armas-escondidas-em-carro"
		 * ); System.out.println(doc.title()); System.out.println(
		 * ParsingEngine.containsBlackList(doc.title()) ); System.out.println(
		 * ParsingEngine.searchKeywords((doc.text()) )); System.out.println(
		 * ParsingEngine.searchBairros((doc.text()) ));
		 */
		// System.out.println( ParsingEngine.containsBlackList(doc.text()) );

		/*
		 * try {
		 * 
		 * // System.out.println(MySQLAccess.totalSources());
		 * 
		 * long startTime; // long startRemoveDuplicatesTime; long estimatedTime;
		 * 
		 * int minutes; int seconds; ParsingEngine.init();
		 * 
		 * while (true) {
		 * 
		 * startTime = System.currentTimeMillis();
		 * 
		 * ParsingEngine.start("silence");
		 * 
		 * estimatedTime = System.currentTimeMillis() - startTime;
		 * 
		 * minutes = (int) (estimatedTime / (1000 * 60)); seconds = (int)
		 * ((estimatedTime / 1000) % 60);
		 * 
		 * System.out.println(""); System.out.println("All tasks completed in " +
		 * minutes + " minutes, " + seconds + " seconds");
		 * 
		 * }
		 * 
		 * } catch (ParseException | InterruptedException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

	}

}
