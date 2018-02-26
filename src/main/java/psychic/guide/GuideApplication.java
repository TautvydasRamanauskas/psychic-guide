package psychic.guide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class GuideApplication {

	public static void main(String[] args) {
//		Document document = fetch("https://www.techradar.com/news/mobile-computing/laptops/best-laptops-1304361");
//		Set<String> brandList = fetchBrandList();

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

	private static Set<String> fetchBrandList() {
		Path path = new File("brands").toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}
}
