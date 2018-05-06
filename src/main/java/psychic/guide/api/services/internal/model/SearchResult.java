package psychic.guide.api.services.internal.model;

import java.util.HashMap;
import java.util.List;

public class SearchResult {
	public final String url;
	public String displayUrl;
	public String snippet;
	public List about;
	public String name;
	public String dateLastCrawled;
	public String id;
	public String deepLinks;

	public SearchResult(HashMap value) {
		displayUrl = (String) value.get("displayUrl");
		snippet = (String) value.get("snippet");
		about = (List) value.get("about");
		name = (String) value.get("name");
		dateLastCrawled = (String) value.get("dateLastCrawled");
		id = (String) value.get("id");
		deepLinks = (String) value.get("deepLinks");
		url = (String) value.get("url");
	}

	public SearchResult(String url) {
		this.url = url;
	}
}
