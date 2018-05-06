package psychic.guide.api.services.internal.searchengine;

import org.json.JSONArray;
import org.json.JSONObject;
import psychic.guide.api.services.internal.model.SearchResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GoogleSearchApi implements SearchAPIService {
	private static final String GOOGLE_ENGINE_ID = "001435949413411837190:zwoql81phfa";
	private static final String GOOGLE_API_KEY = "AIzaSyD88NTT8RHl-GjvcxwzJ0tgfU4qzDgtxB0";
	private static final String REQUEST_URL_TEMPLATE = String.format("https://www.googleapis.com/customsearch/v1?" +
			"q=%%s&" + "cx=%s&" + "key%s", GOOGLE_ENGINE_ID, GOOGLE_API_KEY);
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
			URL url = new URL(String.format(REQUEST_URL_TEMPLATE, keyword));
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
