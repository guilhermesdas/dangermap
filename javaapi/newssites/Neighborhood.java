package newssites;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Neighborhood {

	private String name;
	private String _id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public static Neighborhood toNeighborhood( JSONObject json ) {
		
		return new Neighborhood ( 
				(json.get("name")).toString(),
				(json.get("_id")).toString()
				);
	}
	
	public Neighborhood(String name, String _id) {
		super();
		this.name = name;
		this._id = _id;
	}
	
	public static ArrayList<Neighborhood> toNeighborhoods( JSONArray jsonarray ) {
		
		ArrayList<Neighborhood> list = new ArrayList<Neighborhood>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add(
					toNeighborhood(
							(((JSONObject) jsonarray.get(i)))
							)
					);			
		}
		//System.out.println(list);
		return list;
	}
	
	public String toString() {
		return String.format("%s, %s\n",name,_id);
	}
	
}
