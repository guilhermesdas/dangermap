package newssites;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class User {
	
	private String _id;
	public User(String _id, String name, String email, String username, String password) {
		super();
		this._id = _id;
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String email;
	private String username;
	private String password;

	public static User toUser( JSONObject json ) {
		
		return new User ( 
				json.get("_id").toString(),
				json.get("name").toString(),
				json.get("email").toString(),
				json.get("username").toString(),
				json.get("password").toString()
				);
	}
	
	public static ArrayList<User> toUsers( JSONArray jsonarray ) {
		
		ArrayList<User> list = new ArrayList<User>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add(
					toUser(
							(((JSONObject) jsonarray.get(i)))
							)
					);			
		}
		//System.out.println(list);
		return list;
	}

}
