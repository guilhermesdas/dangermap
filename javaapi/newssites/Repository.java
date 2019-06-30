package newssites;

import java.util.ArrayList;

public class Repository {
	
	private String link;
	private String neighborhood;
	private ArrayList<String> keywords;
	
	public Repository(String link, String neighborhood, ArrayList<String> keywords) {
		super();
		this.link = link;
		this.neighborhood = neighborhood;
		this.keywords = keywords;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public ArrayList<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	
	public String toString() {
		return String.format("Link: %s\nNeighborhood: %s\nKeywords: %s",
				this.link, this.neighborhood, this.keywords );
	}

}
