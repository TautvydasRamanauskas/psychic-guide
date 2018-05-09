package psychic.guide.api;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SearchProperties {
	private static final SearchProperties INSTANCE = new SearchProperties();
	private final Properties properties;

	private SearchProperties() {
		properties = loadProperties();
	}

	public static String get(String key) {
		return INSTANCE.properties.getProperty(key);
	}

	private static Properties loadProperties() {
		Properties properties = new Properties();
		try (FileInputStream inputStream = new FileInputStream(new File("."))) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
