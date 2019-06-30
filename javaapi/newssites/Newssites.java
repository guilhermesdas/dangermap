package newssites;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Newssites {
	
	public static void main(String [] args ) {
		
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("5d19060725c38d0f77d325a2");
		keywords.add("5d19060725c38d0f77d325ad");
		
		try {
			//System.out.println(Newssites.addRepository(
			//		"5d1905d725c38d0f77d3255c",
			//		"5d1905d725c38d0f77d32565",
			//		keywords ));
			System.out.println(Newssites.getNeighborhoods());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	public static ArrayList<String> getKeywords() throws ParseException {
		
		JSONArray jsonarray = get(baseurl + keywordsRoute,"");
		ArrayList list = new ArrayList();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add(((JSONObject) jsonarray.get(i)).get("keyword") );			
		}
		//System.out.println(list);
		return list;
		
	}
	
	// Add a new keyword
	public static String addKeyword( String keyword, boolean blacklist ) throws ParseException {
			
		JSONObject json = new JSONObject();
		json.put("keyword",keyword);
		json.put("blacklist",blacklist);
		
		return post(
				baseurl + keywordsRoute + addRoute, json.toString()).toString();
	
	}	
	
	///////////////// NEIGHBORHOODS ///////////////
	
	// Get list of all keywords
	public static ArrayList<String> getNeighborhoods() throws ParseException {
		
		JSONArray jsonarray = get(baseurl + neighborhoodsRoute,"");
		ArrayList<String> list = new ArrayList<String>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add( ((JSONObject) jsonarray.get(i)).get("name").toString() );			
		}
		//System.out.println(list);
		return list;
		
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
	public static ArrayList<Repository> getRepository() throws ParseException {
		
		JSONArray jsonarray = get(baseurl + repositoryRoute,"");
		ArrayList<Repository> list = new ArrayList<Repository>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			JSONObject repjson = ((JSONObject) jsonarray.get(i));
			
			ArrayList<String> keywords = new ArrayList<String>();
			
			JSONArray keywordsjson = (JSONArray) repjson.get("keywords");
			for ( int j = 0; j < keywordsjson.size(); j++ ) {
				keywords.add(
						((JSONObject) keywordsjson.get(j)).get("keyword").toString()
						);
			}
			
			Repository rep = new Repository(
					(String) ((JSONObject) repjson.get("link")).get("link"),
					(String) ((JSONObject) repjson.get("neighborhood")).get("name"),
					keywords );
			list.add(rep);			
		}
		//System.out.println(list);
		return list;
		
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
