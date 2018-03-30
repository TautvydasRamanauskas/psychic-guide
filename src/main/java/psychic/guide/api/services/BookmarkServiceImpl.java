package psychic.guide.api.services;

import org.springframework.stereotype.Service;
import psychic.guide.api.ResultEntry;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	private static final String FILE_NAME = "data/bookmarks.ser";
	private final Map<String, Collection<ResultEntry>> ipToText = read();

	@Override
	public void addBookmark(ResultEntry entry, String ip) {
		entry.setBookmark(true);
		Collection<ResultEntry> bookmarks = ipToText.computeIfAbsent(ip, k -> new HashSet<>());
		bookmarks.add(entry);
		save();
	}

	@Override
	public void removeBookmark(ResultEntry entry, String ip) {
		Collection<ResultEntry> bookmarks = ipToText.get(ip);
		if (bookmarks != null) {
			bookmarks.remove(entry);
		}
		save();
	}

	@Override
	public boolean containsBookmark(ResultEntry entry, String ip) {
		Collection<ResultEntry> bookmarks = ipToText.get(ip);
		return bookmarks != null && bookmarks.contains(entry);
	}

	@Override
	public List<ResultEntry> bookmarks(String ip) {
		Collection<ResultEntry> bookmarks = ipToText.getOrDefault(ip, new HashSet<>());
		return bookmarks.stream().sorted().collect(Collectors.toList());
	}

	private synchronized void save() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(ipToText);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Collection<ResultEntry>> read() {
		try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
			 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			//noinspection unchecked
			return (Map<String, Collection<ResultEntry>>) inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
