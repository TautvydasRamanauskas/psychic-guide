package psychic.guide.api.services.internal.searchengine;

import org.json.JSONArray;
import org.json.JSONObject;
import psychic.guide.api.services.internal.model.SearchResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AzureDemoSearchApi implements SearchApiService {
	private static final String DEMO_FILE = "data/azure-demo";
	private static final String KEY_DEMO = "Demo";
	private static final String KEY_WEB_PAGES = "webPages";
	private static final String KEY_VALUE = "value";

	public List<SearchResult> search(String keyword) {
		JSONObject json = new JSONObject(readDemo());
		JSONObject demo = json.getJSONObject(KEY_DEMO);
		JSONObject webPages = demo.getJSONObject(KEY_WEB_PAGES);
		JSONArray values = webPages.getJSONArray(KEY_VALUE);
		return values.toList().stream()
				.map(AzureDemoSearchApi::getUrl)
				.map(SearchResult::new)
				.collect(Collectors.toList());
	}

	private static String readDemo() {
		File file = new File(DEMO_FILE);
		try (Scanner scanner = new Scanner(file)) {
			return scanner.useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String getUrl(Object value) {
		Map map = (Map) value;
		return (String) map.get("url");
	}
}
