package spider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.parser.ParseException;
import org.jsoup.nodes.Document;

import newssites.Keyword;
import newssites.Link;
import newssites.Neighborhood;
import newssites.Newssites;

public class ParseTest {
	
	public static void main(String[] args) {
		
		ParsingEngine.init();
		
		String url = "https://www.portaldoholanda.com.br/policial/";
		
		Document doc = ParsingEngine.getDocument(url);
		boolean debug = true;
		
		if ( doc == null ) {
			System.out.println("null");
			return;
		}
		
		Set<Keyword> foundedKeywords = ParsingEngine.searchKeywords(doc.title()); // number of keywords occurrences
		foundedKeywords.addAll(ParsingEngine.searchKeywords(url));
		Set<Neighborhood> foundedBairros = ParsingEngine.searchBairros(doc.title()); // number of keywords occurrences
		foundedBairros.addAll(ParsingEngine.searchBairros( url ));
		foundedBairros.addAll(ParsingEngine.searchBairros( doc.text() ));
		
		boolean containsBlackList = ParsingEngine.containsBlackList( url );
		
		if ( containsBlackList )
			return;
		
		try{
			containsBlackList = ParsingEngine.containsBlackList( doc.title() );
		} catch ( NullPointerException e ) {
			return;
		}
		
		//Thread.sleep(200);


		if (debug) {
			System.out.printf("url: %s\ntext: %s\n", url, doc.text());
			System.out.println(foundedKeywords);
			System.out.println(foundedBairros);
			// Newssites.add
		} else
			System.out.println(".");
		
		Link linkToAdd = null;
		
		// Add URL in list of links if wasn't find any keywords in blacklist
		if ( !containsBlackList ) {
			if ( debug ) {
				System.out.println("Adding url in links...");
			}
			linkToAdd = Newssites.addLink(url, false);	
		}
		
		if ( linkToAdd == null )
			return;

		// Adiciona no repositório caso encontre uma notícia com alguma das palavras chave
		// e o nome de algum bairro de manaus
		if (!foundedBairros.isEmpty() && !foundedKeywords.isEmpty() ) {

			System.out.println("Adding url in repository: " + linkToAdd );
			ArrayList<String> keywords_id = new ArrayList<String>();
			for (Iterator<Keyword> it = foundedKeywords.iterator(); it.hasNext();) {
				keywords_id.add(it.next().get_id());
			}

			try {
				Newssites.addRepository(linkToAdd.get_id(), foundedBairros.iterator().next().get_id(), keywords_id);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}

}
