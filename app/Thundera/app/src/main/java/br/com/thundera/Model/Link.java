package br.com.thundera.Model;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Link {

	private String link;
	private boolean isBaseURL;
	private String visitedOn;
	private String _id;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public boolean isBaseURL() {
		return isBaseURL;
	}
	public void setBaseURL(boolean isBaseURL) {
		this.isBaseURL = isBaseURL;
	}
	public String getVisitedOn() {
		return visitedOn;
	}
	public void setVisitedOn(String visitedOn) {
		this.visitedOn = visitedOn;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	
	public String toString() {
		return String.format("%s, %s, %s\n",link, isBaseURL, _id);
	}
	
	public static Link toLink( JSONObject json ) {
		
		return new Link ( 
				(json.get("link")).toString(),
				(json.get("isBaseURL")).toString().equals("true") ? true : false,
				(json.get("_id")).toString()
				);
		
	}
	
	public static ArrayList<Link> toLinks( JSONArray jsonarray ) {
		
		ArrayList<Link> list = new ArrayList<Link>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add(
					toLink(
							(((JSONObject) jsonarray.get(i)))
							)
					);			
		}
		//System.out.println(list);
		return list;
	}
	
	public Link(String link, boolean isBaseURL, String _id) {
		super();
		this.link = link;
		this.isBaseURL = isBaseURL;
		this._id = _id;
	}
	
}
