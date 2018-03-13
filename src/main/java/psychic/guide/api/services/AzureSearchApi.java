package psychic.guide.api.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.SearchResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class AzureSearchApi implements SearchService {
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
				.map(v -> ((HashMap) v))
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
}
