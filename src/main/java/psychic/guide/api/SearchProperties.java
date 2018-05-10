package psychic.guide.api;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.stream.IntStream;

public final class SearchProperties {
	private static final String FILE_PATH = "src/main/resources/search.properties";
	private static final SearchProperties INSTANCE = new SearchProperties();
	private final Properties properties;

	private SearchProperties() {
		properties = loadProperties(FILE_PATH);
	}

	protected SearchProperties(String filePath) {
		properties = loadProperties(filePath);
	}

	public static SearchProperties getInstance() {
		return INSTANCE;
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public byte[] getByteArray(String key) {
		String property = get(key);
		if (property != null) {
			String[] bytesStrings = property.split(", ");
			byte[] bytes = new byte[bytesStrings.length];
			IntStream.range(0, bytes.length).forEach(i -> bytes[i] = Byte.parseByte(bytesStrings[i].replace("0x", ""), 16));
			return bytes;
		}
		return null;
	}

	private static Properties loadProperties(String filePath) {
		Properties properties = new Properties();
		try (FileInputStream inputStream = new FileInputStream(filePath)) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
