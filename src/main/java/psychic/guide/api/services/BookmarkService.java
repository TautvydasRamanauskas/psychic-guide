package psychic.guide.api.services;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BookmarkService {
	private static final String FILE_NAME = "data/bookmarks.ser";
	private Map<String, Collection<String>> ipToText = read();

	public void addBookmark(String ip, String text) {
		Collection<String> bookmarks = ipToText.computeIfAbsent(ip, k -> new HashSet<>());
		bookmarks.add(text);
		save();
	}

	public void removeBookmark(String ip, String text) {
		Collection<String> bookmarks = ipToText.get(ip);
		if (bookmarks != null) {
			bookmarks.remove(text);
		}
		save();
	}

	public boolean containsBookmark(String ip, String text) {
		Collection<String> bookmarks = ipToText.get(ip);
		return bookmarks != null && bookmarks.contains(text);
	}

	public void save() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(ipToText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Collection<String>> read() {
		boolean fileReady = ensureFileExists();
		if (fileReady) {
			try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
				 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
				//noinspection unchecked
				return (Map<String, Collection<String>>) inputStream.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new HashMap<>();
	}

	private static boolean ensureFileExists() {
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			try {
				return file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
