package psychic.guide.api.services.internal.searchengine;

import org.json.JSONArray;
import org.json.JSONObject;
import psychic.guide.api.SearchProperties;
import psychic.guide.api.services.internal.model.SearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static psychic.guide.api.services.internal.PercentEncoder.encode;

public class GoogleSearchApi implements SearchAPIService {
	private static final String REQUEST_URL_TEMPLATE = "https://www.googleapis.com/customsearch/v1?" +
			"q=%s" +
			"&cx=&" + SearchProperties.get("google.engine.id") +
			"&key=" + SearchProperties.get("google.api.key");
	private static final String KEY_ITEMS = "items";
	private static final String KEY_LINK = "link";

	@Override
	public List<SearchResult> search(String keyword) {
		JSONObject results = fetchResults(keyword);
		JSONArray items = results.getJSONArray(KEY_ITEMS);
		return IntStream.range(0, items.length())
				.mapToObj(items::getJSONObject)
				.map(i -> new SearchResult(i.getString(KEY_LINK)))
				.collect(Collectors.toList());
	}

	private JSONObject fetchResults(String keyword) {
		StringBuilder results = new StringBuilder();
		try {
			URL url = new URL(String.format(REQUEST_URL_TEMPLATE, encode(keyword)));
			URLConnection connection = url.openConnection();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					results.append(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JSONObject(results.toString());
	}
}
