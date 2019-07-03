package spider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

// JSON
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import newssites.Keyword;
import newssites.Link;
import newssites.Neighborhood;
// Newssites
import newssites.Newssites;
import newssites.Repository;

public class ParsingEngine {
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static int minimumWordsInAFrase = 10;
	public static int retries = 10;
	public static int delay = 200;
	public static boolean debug = false;
	public static ArrayList<Keyword> whiteList;
	public static ArrayList<Keyword> blackList;
	public static ArrayList<Neighborhood> bairros;
	public static ArrayList<Link> links_db = new ArrayList<Link>();
	
	public static boolean init() {
		try {
			whiteList = Newssites.getKeywords();
			bairros = Newssites.getNeighborhoods();
			blackList = Newssites.getBlackList();
			ArrayList<Repository> reps = Newssites.getRepository();
			for ( Repository r : reps ) {
				try{
					links_db.add(r.getLink());				
				} catch ( NullPointerException e ) {
					
				}
			}
			//links_db.addAll( Newssites.getLinks() );
			System.out.println(links_db);
			System.out.println(links_db.size());
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return false;
		}
	}

	// Start spider
	public static int start2(String arg) throws ParseException, InterruptedException {

		System.out.println("thread start2 running");
		
		if (arg.equals("debug"))
			debug = true;

		int result = 0;

		String url;
		String text;

		//if ( debug ) {
			//System.out.println("Links to visit:\n" + links_db);
		//}
		
		//if (  )
		
		// For each seed...
		for (int i = links_db.size() - 1; i >= 0 ; i--) {

			//String linkstr = "http://d24am.com/";
			//String linkstr = seed;
			String linkstr = links_db.get(i).getLink();
			if ( linkstr.contains("wikipedia") )
				continue;
			
			if ( debug ) {
				System.out.println("Source: " + linkstr);
			}
			Elements links = null;

			int totalKeyWords = 0;
			int totalLinks = 0;

			// Get elements from link
			links = getURL(linkstr);
			
			if (links != null) {
				
				totalLinks = links.size();
				result = result + totalLinks;

				for (Element link : links) {
					
					//if (!linksPercorridos.contains(source.getLink())) {
					//	continue;
					//}
					//linksPercorridos.add(source.getLink());
					
					url = link.attr("abs:href").replace("'", "''").replaceAll("[\\t\\n\\r]", " ");
					text = link.text().replace("'", "''").replaceAll("[\\t\\n\\r]", " ");

					Document doc = getDocument(url);
					
					if ( doc == null ) {
						continue;
					}
					
					Set<Keyword> foundedKeywords = searchKeywords(doc.title()); // number of keywords occurrences
					Set<Neighborhood> foundedBairros = searchBairros(doc.title()); // number of keywords occurrences
					foundedBairros.addAll(ParsingEngine.searchBairros( url ));
					foundedBairros.addAll(ParsingEngine.searchBairros( doc.text() ));
					
					if ( containsBlackList( url ) )
						continue;
					
					boolean containsBlackList = containsBlackList( doc.title() );
					
					Thread.sleep(delay);
					totalKeyWords = totalKeyWords + foundedKeywords.size();


					if (debug) {
						System.out.printf("url: %s\ntext: %s\n", url, text);
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
						continue;

					// Adiciona no repositório caso encontre uma notícia com alguma das palavras chave
					// e o nome de algum bairro de manaus
					if (!foundedBairros.isEmpty() && !foundedKeywords.isEmpty() ) {

						System.out.println("Adding url in repository: " + linkToAdd );
						ArrayList<String> keywords_id = new ArrayList<String>();
						for (Iterator<Keyword> it = foundedKeywords.iterator(); it.hasNext();) {
							keywords_id.add(it.next().get_id());
						}

						Newssites.addRepository(linkToAdd.get_id(), foundedBairros.iterator().next().get_id(), keywords_id);

					}

				}
			}
		}

		return result;
	}
	
	// Start spider
	public static int start(String arg, String seed) throws ParseException, InterruptedException {

		if (arg.equals("debug"))
			debug = true;

		int result = 0;

		String url;
		String text;

		//if ( debug ) {
			//System.out.println("Links to visit:\n" + links_db);
		//}
		
		// For each seed...
		//for (int i = links_db.size() - 1; i >= 0 ; i--) {

			//String linkstr = "http://d24am.com/";
			String linkstr = seed;
			//if ( !linkstr.contains("d24am") )
			//	return 0;
			
			if ( debug ) {
				System.out.println("Source: " + linkstr);
			}
			Elements links = null;

			int totalKeyWords = 0;
			int totalLinks = 0;

			// Get elements from link
			links = getURL(linkstr);
			totalLinks = links.size();
			result = result + totalLinks;

			if (links != null) {

				for (Element link : links) {
					
					//if (!linksPercorridos.contains(source.getLink())) {
					//	continue;
					//}
					//linksPercorridos.add(source.getLink());
					
					url = link.attr("abs:href").replace("'", "''").replaceAll("[\\t\\n\\r]", " ");
					text = link.text().replace("'", "''").replaceAll("[\\t\\n\\r]", " ");

					Document doc = getDocument(url);
					
					if ( doc == null ) {
						continue;
					}
					
					Set<Keyword> foundedKeywords = searchKeywords(doc.title()); // number of keywords occurrences
					Set<Neighborhood> foundedBairros = searchBairros(doc.title()); // number of keywords occurrences
					foundedBairros.addAll(ParsingEngine.searchBairros( url ));
					foundedBairros.addAll(ParsingEngine.searchBairros( doc.text() ));
					
					if ( containsBlackList( url ) )
						continue;
					
					boolean containsBlackList = containsBlackList( doc.title() );
					
					Thread.sleep(delay);
					totalKeyWords = totalKeyWords + foundedKeywords.size();


					if (debug) {
						System.out.printf("url: %s\ntext: %s\n", url, text);
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
						continue;

					// Adiciona no repositório caso encontre uma notícia com alguma das palavras chave
					// e o nome de algum bairro de manaus
					if (!foundedBairros.isEmpty() && !foundedKeywords.isEmpty() ) {

						System.out.println("Adding url in repository: " + linkToAdd );
						ArrayList<String> keywords_id = new ArrayList<String>();
						for (Iterator<Keyword> it = foundedKeywords.iterator(); it.hasNext();) {
							keywords_id.add(it.next().get_id());
						}

						Newssites.addRepository(linkToAdd.get_id(), foundedBairros.iterator().next().get_id(), keywords_id);

					}

				}
			}
		//}

		return result;
	}
	
	public static boolean containsBlackList(String text) {
	
		//System.out.println(blackList);
		
		text = text.toLowerCase();
		
		int nblacklists = 0;
		for ( Keyword keyword : blackList ) {
			if ( text.contains( keyword.getKeyword().toLowerCase() ) ) {
				//System.out.println("Contém blackword " + keyword.getKeyword() );
				nblacklists++;
			}
		}
		
		return nblacklists > 0;
		
	}
	

	// get founded bairros in a text
	public static Set<Neighborhood> searchBairros(String text) {

		Set<Neighborhood> foundedBairros = new HashSet<Neighborhood>();

		for (Neighborhood word : bairros) {

			if (text.toLowerCase().contains(word.getName().toLowerCase())) {
				//System.out.println("Contém bairro " + word.getName() );
				foundedBairros.add(word);
			}
		}

		return foundedBairros;
	}

	// get founded keywords in a text
	public static Set<Keyword> searchKeywords(String text) {

		Set<Keyword> foundedWords = new HashSet<Keyword>();

		for (Keyword word : whiteList) {

			if (text.toLowerCase().contains(word.getKeyword().toLowerCase())) {
				System.out.println("Contém keyword " + word.getKeyword() );
				foundedWords.add(word);
			}
		}

		return foundedWords;
	}

	/*
	 * public boolean chkBlackList(String text) { MySQLAccess basededados = new
	 * MySQLAccess(); String blackList = basededados.getBlackList(); String[] parts
	 * = blackList.split("#");
	 * 
	 * //System.out.println("blacklist...");
	 * 
	 * for (int i = 0;i < parts.length;i++) { if
	 * (text.toLowerCase().contains(parts[i].toLowerCase())) return true; } return
	 * false; }
	 */

	public static Document getDocument(String url) {
		Document document = null;
		//Elements links = null;

		int i = 0;
		boolean sucess = false;

		while (i < retries) {
			try {
				// if (i > 0) System.out.print("retrying..."+i);
				if (i > 0)
					System.out.print("!");

				if (!url.startsWith("http://") && !url.startsWith("https://"))
					url = "http://" + url;

				document = Jsoup.connect(url).userAgent(
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
						.followRedirects(false).timeout(10000).get();
				sucess = true;
				break;
			} catch (SocketTimeoutException ex) {
			} catch (MalformedURLException ep) {
			} catch (IllegalArgumentException ep) {
			} catch (IOException e) {
			} finally {
				i++;
			}
		}

		//if (sucess)
		//	links = document.select("a[href]");
		return document;
	}
	
	public static Elements getURL(String url) {
		Document document = null;
		Elements links = null;

		int i = 0;
		boolean sucess = false;

		while (i < retries) {
			try {
				// if (i > 0) System.out.print("retrying..."+i);
				if (i > 0)
					System.out.print("!");

				if (!url.startsWith("http://") && !url.startsWith("https://"))
					url = "http://" + url;

				document = Jsoup.connect(url).userAgent(
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
						.followRedirects(false).timeout(10000).get();
				sucess = true;
				break;
			} catch (IOException ex) {
				System.out.println(ex.getMessage());
			} finally {
				i++;
			}
		}

		if (sucess)
			links = document.select("a[href]");
		return links;
	}

}
