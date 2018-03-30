package psychic.guide.api.services;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import psychic.guide.api.ResultEntry;

import java.io.*;
import java.util.*;

@Service
public class LinkServiceImpl implements LinkService, DisposableBean {
	private static final String FILE_NAME = "data/searches.ser";
	private final Map<UUID, List<ResultEntry>> links = read();

	@Override
	public List<ResultEntry> get(UUID link) {
		return links.getOrDefault(link, new ArrayList<>());
	}

	@Override
	public UUID generate(List<ResultEntry> results) {
		UUID link = UUID.randomUUID();
		links.put(link, results);
		return link;
	}

	@Override
	public void destroy() throws Exception {
		save();
	}

	private synchronized void save() {
		try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(links);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<UUID, List<ResultEntry>> read() {
		try (FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
			 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			//noinspection unchecked
			return (Map<UUID, List<ResultEntry>>) inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new HashMap<>();
	}
}
