//import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

// JSON
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

// Newssites
import newssites.Newssites;

public class ParsingEngine {

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
	public static void start(String arg) {
		
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
		
	}
	
	public static void main(String args[]) {
		
		try {
			init();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println(e);			
		}
		
		System.out.println(
				searchKeywords("matei assassinado assassino morte morri morreu suspeito suspeita tiroteiro arroz feijao japones manaus") );
		System.out.println(
				searchBairros("alvorada santo antonio novo israel educandos centro são geraldo flores eldorado") );
		
	}

	// get founded bairros in a text
	public static ArrayList<String> searchBairros(String text) {

		System.out.println("[Search keywords] Text: " + text + "\n");
		
		ArrayList<String> foundedBairros = new ArrayList<String>();
		
		System.out.println("processando bairros...");		
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

		System.out.println("[Search keywords] Text: " + text + "\n");
		
		ArrayList<String> foundedWords = new ArrayList<String>();
		
		System.out.println("processando keywords...");		
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
	
	public Elements getURL(String url) {
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
