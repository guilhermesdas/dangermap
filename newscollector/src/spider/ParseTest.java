package spider;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import newssites.*;

public class ParseTest implements Runnable {
	
	public static void main(String[] args) {

				
		/*String url = "https://d.emtempo.com.br/policia/163288/em-manaus-homem-que-cumpre-pena-no-regime-aberto-desaparece";
		Document doc = ParsingEngine.getDocument(url);
		Elements es = doc.getAllElements();
		ParsingEngine.init();
		for ( Element e : es ) {
			//Tag tag = e.tag();
			//Elements ess = e.getElementsByTag(tag.getTitle());
			if ( e.text().split(" ").length > 15 && !ParsingEngine.containsBlackList(e.text()) )
				System.out.println( e.text() + e.id() );
		}*/
		
		//updateAllBriefs();
		
		new Thread( new ParseTest() ).run();
		
	}
	
	public static void updateAllBriefs(){
		
		//new Thread( new ParseTest( ) ).run();

		// Get all repositories
		ArrayList<Repository> reps = Newssites.getRepository();
		
		if (reps == null || reps.isEmpty() ) {
			System.out.println("No repository founded");
			return;
		}
		
		int skippedNews = 0;		
		
		// Update all briefs
		for ( int i = 0; i < reps.size(); i++) {

			// Get document from url
			Document doc = ParsingEngine.getDocument(reps.get(i).getLink().getLink());
			
			if ( doc == null ) {
				skippedNews++;
				continue;
			}

			// Encoding string correctly
			
			JSONObject response = Newssites.updateRepositoryBrief(reps.get(i).get_id(),
					Newssites.removeBugInChar( doc.title() ) );
			System.out.println( response.toString() );
			System.out.println("Rep index: " + i);
			
		}
		
		System.out.println("Skipped news: " + skippedNews );
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			long startTime;
			long estimatedTime;

			int minutes;
			int seconds;

			while (true) {
				
				ParsingEngine.init();

				startTime = System.currentTimeMillis();

				ParsingEngine.start("silence");

				estimatedTime = System.currentTimeMillis() - startTime;

				minutes = (int) (estimatedTime / (1000 * 60));
				seconds = (int) ((estimatedTime / 1000) % 60);

				System.out.println("");
				System.out.println("All tasks completed in " + minutes + " minutes, " + seconds + " seconds");

			}
			
		} catch (ParseException | InterruptedException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
