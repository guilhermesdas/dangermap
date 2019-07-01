import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.ArrayList;
import java.util.Date;
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
public class ParsingEngine {
	
	public static void main(String args[]) {
		
		/*
		 * try { init(); } catch (ParseException e) { // TODO Auto-generated catch block
		 * System.out.println(e); }
		 * 
		 * System.out.println(
		 * searchKeywords("matei assassinado assassino morte morri morreu suspeito suspeita tiroteiro arroz feijao japones manaus"
		 * ) ); System.out.println(
		 * searchBairros("alvorada santo antonio novo israel educandos centro são geraldo flores eldorado"
		 * ) );
		 */
		
		
		try {
			
			//System.out.println(MySQLAccess.totalSources());
			
			long startTime;
			int totalNews;
			//long startRemoveDuplicatesTime;
			long estimatedTime;
			
			int minutes;
			int seconds;
			
			Date dNow;
			
			init();
			
			while(true) {
				
				startTime = System.currentTimeMillis();
				
				totalNews = start("debug");		
				
				estimatedTime = System.currentTimeMillis() - startTime;
				//long estimatedRemoveDuplicatesTime = endTime - startRemoveDuplicatesTime;
				
				minutes = (int) (estimatedTime / (1000 * 60));
			    seconds = (int) ((estimatedTime / 1000) % 60);
			    //int milliseconds = (int) (estimatedTime % 1000);			
			    
			    System.out.println("");
			    System.out.println("All tasks completed in "+minutes+" minutes, "+seconds+" seconds");
				
			}
			
		
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static int minimumWordsInAFrase = 10;
	public static int retries = 10;
	public static int delay = 200;
	public static boolean debug = false;
	public static ArrayList<Keyword> whiteList;
	public static ArrayList<Neighborhood> bairros;
	
	public static void init() throws ParseException {
		whiteList = Newssites.getKeywords();
		bairros = Newssites.getNeighborhoods();
	}
	
	// Start spider
	public static int start(String arg) throws ParseException, InterruptedException {
		
		if (arg.equals("debug") )
			debug = true;
		
		int result = 0;
		int partial = 0;
		int add = 0;
		
		long startTime;
		long estimatedTime;
		int minutes;
		int seconds;
		
		String hash;
		String url;
		String text;
		
		// Get all seeds
		ArrayList<Link> seeds = Newssites.getSeeds();
		// For each seed...
		for ( Link source : seeds ) {
			partial = 0;
			add = 0;
			
			startTime = System.currentTimeMillis();
			
			Elements links = null;
			
			int totalKeyWords = 0;
			int totalLinks = 0;
			
			// Get elements from link
			links = getURL(source.getLink());
			totalLinks = links.size();
			result = result + totalLinks;
			
			if ( links != null ) {
				
				for ( Element link : links ) {

	    			//System.out.println(link.text());
	    			//if (link.text().split(" ").length > minimumWordsInAFrase) {
		    			Set<Keyword> foundedKeywords = searchKeywords(link.text()); //number of keywords occurrences
		    			Set<Neighborhood> foundedBairros = searchBairros(link.text()); //number of keywords occurrences
		    			
		    			//if ( !foundedBairros.isEmpty() && !foundedKeywords.isEmpty() ) {
			    			Date dNow = new Date( );
			    		    SimpleDateFormat ft = 
			    		    new SimpleDateFormat ("yyyy/MM/dd HH:mm");	
			    		    //hash = basededados.calculaMD5("0\n"+url+"\n"+text+"\n");  
			    			partial++;

			    		    url = link.attr("abs:href").replace("'", "''").replaceAll("[\\t\\n\\r]"," ");
			    		    text = link.text().replace("'", "''").replaceAll("[\\t\\n\\r]"," ");			    			
		    				
			    		    if (debug) {
			    				System.out.printf("url: %s\ntext: %s\n", url, text );
				    		    System.out.println(foundedKeywords);
				    		    System.out.println(foundedBairros);
				    		    //Newssites.add
			    			}
			    			else 
			    				System.out.print(".");
			    			
			    			Thread.sleep(delay);			    			
			    			totalKeyWords = totalKeyWords+foundedKeywords.size();
			    			
			    			//new Scanner(System.in).next();
			    			
			    			if ( !foundedBairros.isEmpty() && !foundedKeywords.isEmpty() ) {
				    		    
			    				ArrayList<String> keywords_id = new ArrayList<String>();
			    				for ( Iterator<Keyword> it = foundedKeywords.iterator(); it.hasNext(); ) {
			    					keywords_id.add(it.next().get_id());
			    				}
				    		    			    				
			    				Newssites.addRepository(
			    						url,
			    						foundedBairros.iterator().next().get_id(),
			    						keywords_id );
			    				
			    			}
			    			
			    			
			    			
		    			//}
		    			 
	    			//}
	    		}
	    	}
		}
		
		return result;
	}

	// get founded bairros in a text
	public static Set<Neighborhood> searchBairros(String text) {
		
		Set<Neighborhood> foundedBairros = new HashSet<Neighborhood>();
		
		for ( Neighborhood word : bairros ) {
			
			if (text.toLowerCase().contains(word.getName().toLowerCase())) { 
				//System.out.println(parts.length);
				foundedBairros.add(word);
			}		
		}		
		
		return foundedBairros;
	}

	// get founded keywords in a text
	public static Set<Keyword> searchKeywords(String text) {

		Set<Keyword> foundedWords = new HashSet<Keyword>();
			
		for ( Keyword word : whiteList ) {
			
			if (text.toLowerCase().contains(word.getKeyword().toLowerCase())) { 
				//System.out.println(parts.length);
				foundedWords.add(word);
			}		
		}		
		
		return foundedWords;
	}
	
	
	/*public boolean chkBlackList(String text) {
		MySQLAccess basededados = new MySQLAccess();
		String blackList = basededados.getBlackList();
		String[] parts = blackList.split("#");
		
		//System.out.println("blacklist...");
		
		for (int i = 0;i < parts.length;i++) {
			if (text.toLowerCase().contains(parts[i].toLowerCase())) return true;		
		}				
		return false;
	}*/
	
	public static Elements getURL(String url) {
	    Document document = null;
	    Elements links = null;
	    
	    int i = 0;	    
	    boolean sucess = false;
	    
	    while (i < retries) {
		    try {
		    	//if (i > 0) System.out.print("retrying..."+i);
		    	if (i > 0) 
		    		System.out.print("!");
		    	
	   			if (!url.startsWith("http://") && !url.startsWith("https://"))	
	   				url = "http://"+url;	    			    
		    	
		    	document = Jsoup.connect(url)
		    			.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
	                    .followRedirects(false)
	                    .timeout(10000)
	                    .get();		 
		    	sucess = true;
		    	break;
		    } catch (SocketTimeoutException ex){
	        } catch (MalformedURLException ep){
	        } catch (IOException e) {	        
	        } finally {
	        	i++;
	        }
	    }
	    
	    if (sucess) 
	    	links = document.select("a[href]");
	    return links;
	}
	
}
