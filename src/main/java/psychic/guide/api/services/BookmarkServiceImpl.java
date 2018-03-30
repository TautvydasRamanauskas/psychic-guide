package psychic.guide.api.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	private static final String FILE_NAME = "data/bookmarks.ser";
	private Map<String, Collection<String>> ipToText = read();

	@Override
	public void addBookmark(String text, String ip) {
		Collection<String> bookmarks = ipToText.computeIfAbsent(ip, k -> new HashSet<>());
		bookmarks.add(text);
		save();
	}

	@Override
	public void removeBookmark(String text, String ip) {
		Collection<String> bookmarks = ipToText.get(ip);
		if (bookmarks != null) {
			bookmarks.remove(text);
		}
		save();
	}

	@Override
	public boolean containsBookmark(String text, String ip) {
		Collection<String> bookmarks = ipToText.get(ip);
		return bookmarks != null && bookmarks.contains(text);
	}

	private synchronized void save() {
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
