package newssites;

import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

public class Newssites {
	
	public static void main(String [] args ) {
		
		try {
			System.out.println(Newssites.getKeywords().toJSONString());
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
	static final String neighborhoodsRoute = "neighborhoods/";
	static final String linksRoute = "links/";
	static final String addRoute = "add/";
	static final String removeRoute = "remove/";
	static final String requestError = "[{\"message\": \"Error in request.\"}]";
	static final String errorparse = "[{\"message\": \"Erro in parsing json\"}]";
	static final JSONParser parser = new JSONParser();
	static final JSONArray error = new JSONArray();
	
	///////////////// KEYWORDS ///////////////
	
	// Get list of all keywords
	public static JSONArray getKeywords() throws ParseException {
		
		try {
			// Get response
			String response = Requests.sendGet(baseurl + keywordsRoute, "");
			return (JSONArray) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
		
	}
	
	// Add a new keyword
	public static JSONArray addKeyword( String keyword, boolean blacklist ) throws ParseException {
		
		try {
			
			JSONObject keywordjson = new JSONObject();
			keywordjson.put("keyword",keyword);
		    keywordjson.put("blacklist",blacklist);
			
			String response = Requests.sendPost(
					baseurl + keywordsRoute + addRoute, keywordjson.toString());
			return (JSONArray) parser.parse(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return (JSONArray) parser.parse(requestError);
		}
		
	}
}
