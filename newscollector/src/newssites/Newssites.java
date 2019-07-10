package newssites;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


import org.json.simple.parser.JSONParser;
import static java.nio.charset.StandardCharsets.*;

public class Newssites {
	
	public static void main(String [] args ) {
		
		System.out.println( findLinks("emtempo") );
		
		/*try {
			ArrayList<Repository> reps = Newssites.getRepository();
			Repository rep = reps.get(10);
			String url = rep.getLink().getLink(); //"áéíóúÁÉÍÓÚãẽĩõũÃẼĨÕŨâêîôûÂÊÎÔÛçñ\u00C1\u00C0";
			byte[] txt =  removeBugInChar( ParsingEngine.getDocument(url).title()).getBytes(UTF_8);
			System.out.println(txt);
			//System.out.println(new String( txt,  ));
			//StringBuilder strb = new StringBuilder( new String( txt, ISO_8859_2 ) );
			String brief = new String( txt, ISO_8859_1 );//ParsingEngine.getDocument(url).title(); //
			System.out.println(brief);
			
			Newssites.updateRepositoryBrief(rep.get_id(),brief) ;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
	}
	
	// Routes to server
	static String baseurl = "http://localhost:3000/";
	static final String keywordsRoute = "keywords/";
	static final String repositoryRoute = "repository/";
	static final String usersRoute = "users/";
	static final String neighborhoodsRoute = "neighborhood/";
	static final String linksRoute = "links/";
	static final String addRoute = "add/";
	static final String removeRoute = "remove/";
	static final String requestError = "[{\"message\": \"Error in request.\"}]";
	static final String errorparse = "[{\"message\": \"Erro in parsing json\"}]";
	static final JSONParser parser = new JSONParser();
	static final JSONArray error = new JSONArray();
	
	public static String removeBugInChar(String s) {
		s = s.replaceAll("Á", "A");
		s = s.replaceAll("É", "E");
		s = s.replaceAll("Ê", "E");
		s = s.replaceAll("Í","I");
		s = s.replaceAll("Ó","O");
		s = s.replaceAll("Ú", "U");
		s = s.replaceAll("Ç", "C");
		s = s.replaceAll("“","\"");
		s = s.replaceAll("‘", "\"");
		s = s.replaceAll("’", "\"");
		s = s.replaceAll("”", "\"");
		byte[] txt =  s.getBytes(UTF_8);
		String brief = new String( txt, ISO_8859_1 );//ParsingEngine.getDocument(url).title(); //
		
		return brief;
	}
	
	public static void setIP(String IP) {
		baseurl = "http://" + IP + ":3000/";
	}
	
	//// GENERIC GET AND POST
	
	private static JSONArray get(String url, String data) throws ParseException {
		
		try {
			// Get response
			String response = Requests.sendGet(url, data);
			JSONArray array = (JSONArray) parser.parse(response);
			//ArrayList list = new ArrayList();
			
			return array;
			 
		} catch (IOException | ClassCastException | NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return (JSONArray) parser.parse(requestError);
		}
	}
	
	private static JSONObject post( String url, String data ) throws ParseException {
		
		try {
			String response = Requests.sendPost(url, data);
			return (JSONObject) parser.parse(response);
			
		} catch (IOException | ClassCastException | NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return (JSONObject) parser.parse(requestError);
		}
		
	}	
	
	///////////////// KEYWORDS ///////////////
	
	// Get list of all keywords
	public static ArrayList<Keyword> getKeywords() {
		
		try{
			return Keyword.toKeywords(get(baseurl + keywordsRoute,""));
		} catch ( ParseException e ) {
			return null;
		}
		
	}
	
	// Get list of all keywords
	public static ArrayList<Keyword> getBlackList() throws ParseException {
		
		try{
			return Keyword.toKeywords(get(baseurl + keywordsRoute + "blacklist",""));
		} catch ( ParseException ex )  {
			return null;
		}
		
	}
	
	///////////////// NEIGHBORHOODS ///////////////
	
	// Get list of all keywords
	public static ArrayList<Neighborhood> getNeighborhoods() throws ParseException {
		
		return Neighborhood.toNeighborhoods(get(baseurl + neighborhoodsRoute,""));
		
	}
	
	// Add a new keyword
	public static JSONObject addNeighborhoods( String name ) throws ParseException {
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("name",name);
			
			String response = Requests.sendPost(
					baseurl + neighborhoodsRoute + addRoute, json.toString());
			return (JSONObject) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONObject) parser.parse(requestError);
		}
		
	}
	
	///////////////// LINKS ///////////////
	
	// Get list of all links
	public static ArrayList<Link> getLinks() throws ParseException {
		
		return Link.toLinks(get(baseurl + linksRoute,""));
		
	}
	
	// Get list of all links
	public static ArrayList<Link> findLinks(String url) {
		
		try {
			return Link.toLinks(get(baseurl + linksRoute + "find?link=" + url,""));
		}
		catch ( ParseException | NullPointerException ex ) {
			return new ArrayList<Link>();
		}
		
	}

	public static ArrayList<Link> getSeeds() throws ParseException {
		
		return Link.toLinks(get(baseurl + linksRoute + "seeds/",""));
		
	}
	
	// Add a new keyword
	@SuppressWarnings("unchecked")
	public static Link addLink( String link, boolean isBaseUrl ) {
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("link",link);
			json.put("isBaseURL", isBaseUrl);
			
			String response = Requests.sendPost(
					baseurl + linksRoute + addRoute, json.toString());
			return Link.toLink( (JSONObject) parser.parse(response));
			
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
	
	///////////////// REPOSITORY ///////////////
	
	// Get list of all links
	public static ArrayList<Repository> getRepository() {

		try {
			return Repository.toRepositories(get(baseurl + repositoryRoute,""));
		} catch ( ParseException pex ) {
			System.out.println(pex.getMessage());
			return null;
		}
	}
	
	// Update repositories  with brief
	public static JSONObject updateRepositoryBrief( String _id, String brief ) {				

		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put( "_id", _id );
		jsonobj.put("brief", brief);
		
		try {
			return post(baseurl + repositoryRoute + "/updatebrief", jsonobj.toString());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	// Add a new keyword
	@SuppressWarnings("unchecked")
	public static JSONObject addRepository( String link_id, String neighborhood_id, ArrayList<String> keywords_id, String brief ) throws ParseException {
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("link",link_id);
			json.put("neighborhood", neighborhood_id);
			json.put("keywords", keywords_id);
			json.put("brief",brief);
			
			String response = Requests.sendPost(
					baseurl + repositoryRoute + addRoute, json.toString());
			return (JSONObject) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONObject) parser.parse(requestError);
		}
		
	}
	
}
