package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.internal.PersistenceSerializationService;
import psychic.guide.api.services.internal.PersistenceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
	private static final String FILE_NAME = "searches";
	private static final int SHOWN_MOST_POPULAR_SEARCHES = 10;

	private final BookmarkService bookmarkService;
	private final VoteService voteService;
	private final PersistenceService<HashMap<String, AtomicInteger>> persistenceService;
	private final HashMap<String, AtomicInteger> searches;

	@Autowired
	public SearchServiceImpl(BookmarkService bookmarkService, VoteService voteService) {
		this.bookmarkService = bookmarkService;
		this.voteService = voteService;
		this.persistenceService = new PersistenceSerializationService<>(FILE_NAME);
		this.searches = persistenceService.read();
	}

	@Override
	public List<ResultEntry> search(String keyword, String ip) {
		AtomicInteger searchCount = searches.computeIfAbsent(keyword, k -> new AtomicInteger());
		searchCount.incrementAndGet();
		persistenceService.save(searches);

		return readResults().stream()
				.map(line -> parseResultEntry(line, ip))
				.sorted()
				.collect(Collectors.toList());
	}

	@Override
	public List<String> mostPopular() {
		return searches.entrySet().stream()
				.sorted(this::compareSearches)
				.map(Map.Entry::getKey)
				.limit(SHOWN_MOST_POPULAR_SEARCHES)
				.collect(Collectors.toList());
	}

	public void clear() {
		searches.clear();
		persistenceService.save(searches);
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
		resultEntry.setVoteValue(voteService.calculateVoteValue(resultEntry.getResult()));
		resultEntry.setPersonalVote(getVote(resultEntry.getResult(), ip));
		return resultEntry;
	}

	private int getVote(String title, String ip) {
		Vote vote = voteService.getVote(title, ip);
		return vote == null ? 0 : vote.getValue();
	}

	private int compareSearches(Map.Entry<String, AtomicInteger> entryOne, Map.Entry<String, AtomicInteger> entryTwo) {
		int searchCountOne = entryOne.getValue().get();
		int searchCountTwo = entryTwo.getValue().get();
		return Integer.compare(searchCountTwo, searchCountOne);
	}
}
