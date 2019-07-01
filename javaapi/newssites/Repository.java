package newssites;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Repository {
	
	private String link;
	private Neighborhood neighborhood;
	private ArrayList<Keyword> keywords;
	private String _id;
	
	
	
	public Repository(String link, Neighborhood neighborhood, ArrayList<Keyword> keywords) {
		super();
		this.link = link;
		this.neighborhood = neighborhood;
		this.keywords = keywords;
	}



	public Repository(String link, Neighborhood neighborhood, ArrayList<Keyword> keywords, String _id) {
		super();
		this.link = link;
		this.neighborhood = neighborhood;
		this.keywords = keywords;
		this._id = _id;
	}



	public String getLink() {
		return link;
	}



	public void setLink(String link) {
		this.link = link;
	}



	public Neighborhood getNeighborhood() {
		return neighborhood;
	}



	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}



	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}



	public void setKeywords(ArrayList<Keyword> keywords) {
		this.keywords = keywords;
	}



	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String toString() {
		return String.format("Link: %s\nNeighborhood: %s\nKeywords: %s",
				this.link, this.neighborhood, this.keywords );
	}
	
	public static Repository toRepository( JSONObject json ) {
		
		return new Repository ( 
				(json.get("link")).toString(),
				Neighborhood.toNeighborhood((JSONObject) json.get("neighborhood")),
				Keyword.toKeywords( (JSONArray) json.get("keywords") )
				);
	}
	
	public static ArrayList<Repository> toRepositories( JSONArray jsonarray ) {
		
		ArrayList<Repository> list = new ArrayList<Repository>();
		for ( int i = 0; i < jsonarray.size(); i++ ) {
			list.add(
					toRepository(
							(((JSONObject) jsonarray.get(i)))
							)
					);			
		}
		//System.out.println(list);
		return list;
	}
	
}
