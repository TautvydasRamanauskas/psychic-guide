package psychic.guide.api.services;

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
	public List<ResultEntry> search(String keyword) {
		return readResults().stream().map(SearchServiceImpl::parseResultEntry).collect(Collectors.toList());
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

	private static ResultEntry parseResultEntry(String line) {
		String[] splits = line.split("\\|");
		String result = splits[0].trim();
		String count = splits[1].replaceFirst("count: ", "").trim();
		return new ResultEntry(result, Integer.valueOf(count));
	}
}
