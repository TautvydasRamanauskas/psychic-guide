package psychic.guide.api.services;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BookmarkService {
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

	public void save() {
		try (FileOutputStream fileOutputStream = new FileOutputStream("data/bookmarks.ser");
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(ipToText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean containsBookmark(String ip, String text) {
		Collection<String> bookmarks = ipToText.get(ip);
		return bookmarks != null && bookmarks.contains(text);
	}

	private Map<String, Collection<String>> read() {
		try (FileInputStream fileInputStream = new FileInputStream("data/bookmarks.ser");
			 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			//noinspection unchecked
			return (Map<String, Collection<String>>) inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
