package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.ResultEntry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
	private final BookmarkService bookmarkService;

	@Autowired
	public SearchServiceImpl(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	@Override
	public List<ResultEntry> search(String keyword, String ip) {
		return readResults().stream()
				.map(line -> parseResultEntry(line, ip))
				.sorted()
				.collect(Collectors.toList());
	}

	private static Set<String> readResults() {
		Path path = new File("data/results").toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	private ResultEntry parseResultEntry(String line, String ip) {
		String[] splits = line.split("\\|");
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(splits[0].trim());
		resultEntry.setCount(Integer.valueOf(splits[1].replaceFirst("count: ", "").trim()));
		resultEntry.setBookmark(bookmarkService.containsBookmark(resultEntry, ip));
		return resultEntry;
	}
}
