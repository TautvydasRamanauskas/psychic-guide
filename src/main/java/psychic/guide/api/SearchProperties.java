package psychic.guide.api;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.IntStream;

public class SearchProperties {
	private static final String FILE_PATH = "src/main/resources/search.properties";
	private static final SearchProperties INSTANCE = new SearchProperties();
	private final Properties properties;

	private SearchProperties() {
		properties = loadProperties();
	}

	public static String get(String key) {
		return INSTANCE.properties.getProperty(key);
	}

	public static byte[] getByteArray(String key) {
		String property = get(key);
		String[] bytesStrings = property.split(", ");
		byte[] bytes = new byte[bytesStrings.length];
		IntStream.range(0, bytes.length).forEach(i -> bytes[i] = Byte.parseByte(bytesStrings[i].replace("0x", ""), 16));
		return bytes;
	}

	private static Properties loadProperties() {
		Properties properties = new Properties();
		try (FileInputStream inputStream = new FileInputStream(new File(FILE_PATH))) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
