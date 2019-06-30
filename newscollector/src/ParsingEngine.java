//import java.io.FileOutputStream;
import java.util.ArrayList;

// JSON
import org.json.simple.parser.ParseException;

// Newssites
import newssites.*;

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
	
	public static void init() throws ParseException {
		whiteList = Newssites.getKeywords();
	}
	
	public 

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

}
