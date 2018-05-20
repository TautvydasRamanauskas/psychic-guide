package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.internal.PersistenceSerializationService;
import psychic.guide.api.services.internal.PersistenceService;
import psychic.guide.api.services.internal.Searcher;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.PercentEncoder.encode;
import static psychic.guide.api.services.internal.PersistenceSerializationService.getFileNameFormat;

@Service
public class SearchServiceImpl implements SearchService {
	private static final String FILE_NAME = "searches";
	private static final String CACHE_FILE_NAME_FORMAT = "caches/%s";
	private static final int SHOWN_MOST_POPULAR_SEARCHES = 10;

	private final BookmarkService bookmarkService;
	private final VoteService voteService;
	private final PersistenceService<HashMap<String, AtomicInteger>> persistenceService;
	private final HashMap<String, AtomicInteger> searches;

	@Autowired
	public SearchServiceImpl(BookmarkService bookmarkService, VoteService voteService) {
		this(bookmarkService, voteService, new PersistenceSerializationService<>(FILE_NAME));
	}

	SearchServiceImpl(BookmarkService bookmarkService, VoteService voteService,
					  PersistenceService<HashMap<String, AtomicInteger>> persistenceService) {
		this.bookmarkService = bookmarkService;
		this.voteService = voteService;
		this.persistenceService = persistenceService;
		this.searches = persistenceService.readOrDefault(new HashMap<>());
	}

	@Override
	public List<ResultEntry> search(String keyword, String ip) {
		noteSearch(keyword);

		String cacheFileName = String.format(CACHE_FILE_NAME_FORMAT, encode(keyword));
		List<ResultEntry> cachedResults = readCache(cacheFileName);
		if (cachedResults == null) {
			Searcher searcher = new Searcher(new LoadBalancer());
			List<ResultEntry> results = searcher.search(keyword);
//			List<ResultEntry> results = readResults().stream()
//					.map(line -> parseResultEntry(line, ip))
//					.sorted()
//					.collect(Collectors.toList());
			cacheResults(cacheFileName, (ArrayList<ResultEntry>) results);
			return results;
		}
		return cachedResults;
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
		persistenceService.saveOnThread(searches);
	}

	private void noteSearch(String keyword) {
		AtomicInteger searchCount = searches.computeIfAbsent(keyword, k -> new AtomicInteger());
		searchCount.incrementAndGet();
		persistenceService.saveOnThread(searches);
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
		String[] splits = line.split("\\$");
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(splits[0]);
		resultEntry.setCount(Integer.valueOf(splits[1]));
		resultEntry.setReferences(Arrays.stream(splits[2].split("\\|")).collect(Collectors.toSet()));
		resultEntry.setBookmark(bookmarkService.containsBookmark(resultEntry, ip));
		resultEntry.setVoteValue(voteService.calculateVoteValue(resultEntry.getResult()));
		resultEntry.setPersonalVote(getVote(resultEntry.getResult(), ip));
		return resultEntry;
	}

	private int getVote(String title, String ip) {
		Vote vote = voteService.getVote(title, ip);
		return vote == null ? 0 : vote.getValue();
	}

	private ArrayList<ResultEntry> readCache(String fileName) {
		File file = new File(getFileNameFormat(fileName));
		if (file.exists()) {
			PersistenceService<ArrayList<ResultEntry>> service = new PersistenceSerializationService<>(fileName);
			return service.read();
		}
		return null;
	}

	private void cacheResults(String fileName, ArrayList<ResultEntry> results) {
		File file = new File(fileName);
		if (!file.exists()) {
			PersistenceService<ArrayList<ResultEntry>> service = new PersistenceSerializationService<>(fileName);
			service.saveOnThread(results);
		}
	}

	private int compareSearches(Map.Entry<String, AtomicInteger> entryOne, Map.Entry<String, AtomicInteger> entryTwo) {
		int searchCountOne = entryOne.getValue().get();
		int searchCountTwo = entryTwo.getValue().get();
		return Integer.compare(searchCountTwo, searchCountOne);
	}
}
