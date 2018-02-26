package psychic.guide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GuideApplication {

	public static void main(String[] args) {
//		Document document = fetch("https://www.techradar.com/news/mobile-computing/laptops/best-laptops-1304361");

		SpringApplication.run(GuideApplication.class, args);
	}

	private static Document fetch(String url) {
		try {
			return Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
