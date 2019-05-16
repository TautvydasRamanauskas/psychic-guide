package psychic.guide.api.services.internal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PageFetcher {
	public static Document fetch(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document loadLocal(String path) {
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(path));
			String html = new String(bytes);
			return Jsoup.parse(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
