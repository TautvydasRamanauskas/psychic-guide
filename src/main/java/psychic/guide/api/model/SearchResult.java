package psychic.guide.api.model;

import java.util.HashMap;

public class SearchResult
{
	public final String displayUrl;
	public final String snippet;
	public final String about;
	public final String name;
	public final String dateLastCrawled;
	public final String id;
	public final String deepLinks;
	public final String url;

	public SearchResult(HashMap value) {
		displayUrl = (String) value.get("displayUrl");
		snippet = (String) value.get("snippet");
		about = (String) value.get("about");
		name = (String) value.get("name");
		dateLastCrawled = (String) value.get("dateLastCrawled");
		id = (String) value.get("id");
		deepLinks = (String) value.get("deepLinks");
		url = (String) value.get("url");
	}
}
