package newssites;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import org.json.simple.parser.JSONParser;

public class Newssites {
	
	public static void main(String [] args ) {
		
		/*ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("5d19060725c38d0f77d325a2");
		keywords.add("5d19060725c38d0f77d325ad");
		
		Newssites.setIP("192.168.1.104");
		System.out.println(baseurl);*/

			//System.out.println(Newssites.addRepository(
			//		"5d1905d725c38d0f77d3255c",
			//		"5d1905d725c38d0f77d32565",
			//		keywords ));
			try {
				System.out.println(getSeeds());
				ArrayList<Repository> reps = getRepository();
				for ( Repository rep : getRepository() ) {
					if ( rep.getLink().getLink().contains("pagina") )
						System.out.println(rep.get_id()) ;
				}
				//System.out.println( getRepository() );
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(getLinks());
		
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
	}
	
	private static JSONObject post( String url, String data ) throws ParseException {
		
		try {
			String response = Requests.sendPost(url, data);
			return (JSONObject) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
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


		JSONObject repjson = new JSONObject();
		repjson.put("_id", _id);
		repjson.put("brief",brief);
		try {
			return post(baseurl + repositoryRoute + "/updatebrief", repjson.toString());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	// Add a new keyword
	@SuppressWarnings("unchecked")
	public static JSONObject addRepository( String link_id, String neighborhood_id, ArrayList<String> keywords_id ) throws ParseException {
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("link",link_id);
			json.put("neighborhood", neighborhood_id);
			json.put("keywords", keywords_id);
			
			String response = Requests.sendPost(
					baseurl + repositoryRoute + addRoute, json.toString());
			return (JSONObject) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONObject) parser.parse(requestError);
		}
		
	}
	
	// Add a new keyword
	@SuppressWarnings("unchecked")
	public static JSONObject signup( String link_id, String neighborhood_id, String keywords_id ) throws ParseException {
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("links",link_id);
			json.put("neighborhood", neighborhood_id);
			json.put("keywords", keywords_id);
			
			String response = Requests.sendPost(
					baseurl + repositoryRoute + addRoute, json.toString());
			return (JSONObject) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONObject) parser.parse(requestError);
		}
		
	}
	
}
