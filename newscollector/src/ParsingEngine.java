import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

// JSON
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
			init();
			start("debug");
			System.out.println("Finished");
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
	public static ArrayList<String> whiteList;
	public static ArrayList<String> bairros;
	
	public static void init() throws ParseException {
		whiteList = Newssites.getKeywords();
		bairros = Newssites.getNeighborhoods();
	}
	
	// Start spider
	public static void start(String arg) throws ParseException, InterruptedException {
		
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
		ArrayList<String> seeds = Newssites.getSeeds();
		// For each seed...
		for ( String source : seeds ) {
			partial = 0;
			add = 0;
			
			startTime = System.currentTimeMillis();
			
			Elements links = null;
			
			int totalKeyWords = 0;
			int totalLinks = 0;
			
			// Get elements from link
			links = getURL(source);
			totalLinks = links.size();
			result = result + totalLinks;
			
			if ( links != null ) {
				
				for ( Element link : links ) {

	    			//System.out.println(link.text());
	    			//if (link.text().split(" ").length > minimumWordsInAFrase) {
		    			ArrayList<String> foundedKeywords = searchKeywords(link.text()); //number of keywords occurrences
		    			ArrayList<String> foundedBairros = searchBairros(link.text()); //number of keywords occurrences
		    			
		    			//if ( !foundedBairros.isEmpty() && !foundedKeywords.isEmpty() ) {
			    			Date dNow = new Date( );
			    		    SimpleDateFormat ft = 
			    		    new SimpleDateFormat ("yyyy/MM/dd HH:mm");	
			    		    //hash = basededados.calculaMD5("0\n"+url+"\n"+text+"\n");  
			    			partial++;
		    		
			    			Thread.sleep(delay);			    			

			    		    url = link.attr("abs:href").replace("'", "''").replaceAll("[\\t\\n\\r]"," ");
			    		    text = link.text().replace("'", "''").replaceAll("[\\t\\n\\r]"," ");
			    			
			    			totalKeyWords = totalKeyWords+foundedKeywords.size();
			    			
			    			//new Scanner(System.in).next();
			    			
			    			if ( !foundedBairros.isEmpty() || !foundedKeywords.isEmpty() ) {		    		    
				    		    if (debug) {
				    				System.out.printf("url: %s\ntext: %s\n", url, text );
					    		    System.out.println(foundedKeywords);
					    		    System.out.println(foundedBairros);
					    		    //Newssites.add
				    			}
				    			else 
				    				System.out.print(".");
			    				
			    			}
			    			
		    			//}
		    			 
	    			//}
	    		}
	    	}
		}
	}

	// get founded bairros in a text
	public static ArrayList<String> searchBairros(String text) {
		
		ArrayList<String> foundedBairros = new ArrayList<String>();
		
		for ( String word : bairros ) {
			
			if (text.toLowerCase().contains(word.toLowerCase())) { 
				//System.out.println(parts.length);
				foundedBairros.add(word);
			}		
		}		
		
		return foundedBairros;
	}

	// get founded keywords in a text
	public static ArrayList<String> searchKeywords(String text) {
		
		ArrayList<String> foundedWords = new ArrayList<String>();
			
		for ( String word : whiteList ) {
			
			if (text.toLowerCase().contains(word.toLowerCase())) { 
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
