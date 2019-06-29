package newssites;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Newssites {
	
	public static void main(String [] args ) {
		
		try {
			System.out.println(Newssites.addLink("url", false).toJSONString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	// Routes to server
	static final String baseurl = "http://localhost:3000/";
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
	
	//// GENERIC GET AND POST
	
	protected static JSONArray get(String url, String data) throws ParseException {
		
		try {
			// Get response
			String response = Requests.sendGet(url, data);
			return (JSONArray) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
	}
	
	protected static JSONObject post( String url, String data ) throws ParseException {
		
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
	public static JSONArray getKeywords() throws ParseException {
		
		return get(baseurl + keywordsRoute,"");
		
	}
	
	// Add a new keyword
	public static JSONObject addKeyword( String keyword, boolean blacklist ) throws ParseException {
			
		JSONObject json = new JSONObject();
		json.put("keyword",keyword);
		json.put("blacklist",blacklist);
		
		return post(
				baseurl + keywordsRoute + addRoute, json.toString());
	
	}	
	
	///////////////// NEIGHBORHOODS ///////////////
	
	// Get list of all keywords
	public static JSONArray getNeighborhoods() throws ParseException {
		
		try {
			// Get response
			String response = Requests.sendGet(baseurl + neighborhoodsRoute, "");
			return (JSONArray) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
		
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
	public static JSONArray getLinks() throws ParseException {
		
		try {
			// Get response
			String response = Requests.sendGet(baseurl + linksRoute, "");
			return (JSONArray) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
		
	}
	
	// Add a new keyword
	@SuppressWarnings("unchecked")
	public static JSONObject addLink( String link, boolean isBaseUrl ) throws ParseException {
		
		try {
			
			JSONObject json = new JSONObject();
			json.put("link",link);
			json.put("isBaseURL", isBaseUrl);
			
			String response = Requests.sendPost(
					baseurl + linksRoute + addRoute, json.toString());
			return (JSONObject) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONObject) parser.parse(requestError);
		}
		
	}
	
	///////////////// REPOSITORY ///////////////
	
	// Get list of all links
	public static JSONArray getRepository() throws ParseException {
		
		try {
			// Get response
			String response = Requests.sendGet(baseurl + repositoryRoute, "");
			return (JSONArray) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
		
	}
	
	// Add a new keyword
	@SuppressWarnings("unchecked")
	public static JSONObject addRepository( String link_id, String neighborhood_id, String keywords_id ) throws ParseException {
		
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
