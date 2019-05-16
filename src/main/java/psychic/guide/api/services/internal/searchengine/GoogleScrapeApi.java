package psychic.guide.api.services.internal.searchengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import psychic.guide.api.services.internal.model.SearchResult;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.PercentEncoder.encode;

public class GoogleScrapeApi implements SearchApiService {
	private static final String REQUEST_URL_TEMPLATE_1 = "https://www.google.com/search?q=%s";
	private static final String REQUEST_URL_TEMPLATE_2 = "https://www.google.com/search?q=%s&num=100";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";

	@Override
	public List<SearchResult> search(String keyword) {
		Document document = fetchResults(keyword);
		if (document != null) {
			Elements links = document.select(".r > a:first-of-type");
			return links.stream()
					.map(e -> e.attr("href"))
					.map(SearchResult::new)
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private Document fetchResults(String keyword) {
		String url = String.format(REQUEST_URL_TEMPLATE_1, encode(keyword));
		try {
			return Jsoup.connect(url).userAgent(USER_AGENT).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
