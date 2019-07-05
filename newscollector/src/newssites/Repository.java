package newssites;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Repository {
	
	private Link link;
	private Neighborhood neighborhood;
	private ArrayList<Keyword> keywords;
	private String _id;
	private String brief;
	


	public Repository(Link link, Neighborhood neighborhood, ArrayList<Keyword> keywords, String _id, String brief) {
		super();
		this.link = link;
		this.neighborhood = neighborhood;
		this.keywords = keywords;
		this._id = _id;
		this.brief = brief;
	}



	public Link getLink() {
		return link;
	}


	public String getBrief() {
		return brief;
	}



	public void setBrief(String brief) {
		this.brief = brief;
	}



	public void setLink(Link link) {
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
		return String.format("%s\n%s\n%s\n[%s]\n%s\n\n",
				this._id, this.link.getLink(), this.neighborhood, this.keywords, this.brief );
	}
	
	public static Repository toRepository( JSONObject json ) {
		
		return new Repository ( 
				Link.toLink( (JSONObject) (json.get("link"))),
				Neighborhood.toNeighborhood((JSONObject) json.get("neighborhood")),
				Keyword.toKeywords( (JSONArray) json.get("keywords") ),
				json.get("_id").toString(),
				json.get("brief").toString()
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
