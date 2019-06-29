package newssites;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONObject;

public class Requests {

	private static final String USER_AGENT = "Mozilla/5.0";

	public static void main(String[] args) throws Exception {

		Requests http = new Requests();

		//System.out.println("Testing 1 - Send Http GET request");
		//http.sendGet();
		
		JSONObject obj = new JSONObject();

	     obj.put("keyword","12312");
	     obj.put("blacklist","false");
	     
	     /*ArrayList urlParameters = new ArrayList();
	     urlParameters.ad
	     
		
	     StringEntity data = new StringEntity(obj.toString());*/
	     
		//System.out.println("\nTesting 2 - Send Http POST request");
		//http.sendPost("http://localhost:3000/keywords/add",obj.toString());
		
		http.sendGet("http://localhost:3000/keywords/","");


	}
	
	// HTTP POST request
	public static String sendPost(String url, String params) throws IOException {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(params);
		wr.flush();
		wr.close();

		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + params);
		System.out.println("Response Code : " + con.getResponseCode());

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		//System.out.println(response.toString());
		return response.toString();
		
	}

	// HTTP GET request
	public static String sendGet(String url, String params) throws IOException {

		// Create url connection object
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// request header
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		

		//print result
		//System.out.println(response.toString());
		return response.toString();
		
	}

}
