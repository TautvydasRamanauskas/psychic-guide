package psychic.guide.api.services.internal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PageFetcher {
	public static Document fetch(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
