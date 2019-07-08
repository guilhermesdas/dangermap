package br.com.thundera.Model;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Neighborhood implements Comparable<Neighborhood>{

	private String name;
	private String _id;
	private String lat;
	private String lng;
	
	public Neighborhood(String name, String _id, String lat, String lng) {
		super();
		this.name = name;
		this._id = _id;
		this.lat = lat;
		this.lng = lng;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
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
				(json.get("_id")).toString(),
				(json.get("lat")).toString(),
				(json.get("lng")).toString()
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

	@Override
	public int compareTo(Neighborhood o) {
		return getName().compareTo(o.getName());
	}
}
