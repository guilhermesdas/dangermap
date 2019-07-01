package newssites;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Keyword {
	
	private String keyword;
	private String _id;
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String toString() {
		return String.format("%s, %s\n",keyword, _id);
	}
	
	public static Keyword toKeyword( JSONObject json ) {
		
		return new Keyword ( 
				(json.get("keyword")).toString(),
				(json.get("_id")).toString()
				);
	}
	
	public static ArrayList<Keyword> toKeywords( JSONArray jsonarray ) {
		
		ArrayList<Keyword> list = new ArrayList<Keyword>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add(
					toKeyword(
							(((JSONObject) jsonarray.get(i)))
							)
					);			
		}
		//System.out.println(list);
		return list;
	}
	
	public Keyword(String keyword, String _id) {
		super();
		this.keyword = keyword;
		this._id = _id;
	}

}
