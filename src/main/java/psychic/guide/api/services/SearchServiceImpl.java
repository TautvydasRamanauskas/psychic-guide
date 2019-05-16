package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.*;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.repository.SearchesRepository;
import psychic.guide.api.services.internal.searchengine.SearchApiService;
import psychic.guide.api.services.internal.searcher.SearcherImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static psychic.guide.api.ResultsConverter.resultsToEntries;
import static psychic.guide.api.services.internal.Globals.*;

@Service
public class SearchServiceImpl implements SearchService {
	private static final int SHOWN_MOST_POPULAR_SEARCHES = 10;
	private static int searchCount = 0;

	private final BookmarkService bookmarkService;
	private final VoteService voteService;
	private final SearchApiService searchAPIService;
	private final SearchesRepository searchesRepository;
	private final ResultsRepository resultsRepository;
	private final ReferenceRepository referenceRepository;

	@Autowired
	public SearchServiceImpl(BookmarkService bookmarkService, VoteService voteService,
							 SearchApiService searchAPIService, SearchesRepository searchesRepository,
							 ResultsRepository resultsRepository, ReferenceRepository referenceRepository) {
		this.bookmarkService = bookmarkService;
		this.voteService = voteService;
		this.searchAPIService = searchAPIService;
		this.searchesRepository = searchesRepository;
		this.resultsRepository = resultsRepository;
		this.referenceRepository = referenceRepository;
	}

	@Override
	public List<ResultEntry> search(String keyword, User user) {
		if (user.getOptions().isUseCache() && searchesRepository.findByKeyword(keyword) != null && USE_SEARCHES_CACHE) {
			List<Result> results = resultsRepository.findResultsByKeyword(keyword);
			if (!results.isEmpty()) {
				return resultsToEntries(results, user, voteService, bookmarkService, referenceRepository);
			}
		}

		List<ResultEntry> results;
		if (MOCK_SEARCH_FINAL_RESULTS_FILE) {
			results = readMockResults().stream()
					.map(this::parseResultEntry)
					.sorted()
					.collect(Collectors.toList());
		} else if (MOCK_SEARCH_FINAL_RESULTS_GENERATE) {
			results = createMockResultsEntries();
			searchCount++;
		} else {
			results = new SearcherImpl(searchAPIService, user.getOptions()).search(keyword);
		}

		saveSearch(keyword);
		saveResults(results, keyword, user);
		return results;
	}

	@Override
	public List<Search> mostPopular() {
		List<Search> searches = (List<Search>) searchesRepository.findAll();
		return searches.stream()
				.sorted(this::compareSearches)
				.limit(SHOWN_MOST_POPULAR_SEARCHES)
				.collect(Collectors.toList());
	}

	private void saveSearch(String keyword) {
		Search search = getSearch(keyword);
		search.setSearchCount(search.getSearchCount() + 1);
		searchesRepository.save(search);
	}

	private Search getSearch(String keyword) {
		Iterable<Search> searches = searchesRepository.findAll();
		for (Search search : searches) {
			if (Objects.equals(search.getKeyword(), keyword)) {
				return search;
			}
		}
		return new Search().setKeyword(keyword);
	}

	private void saveResults(List<ResultEntry> results, String keyword, User user) {
		results.forEach(r -> {
			Result result = getResult(r, keyword);
			r.setId(result.getId());
			r.setVoteValue(voteService.calculateVoteValue(result));
			r.setPersonalVote(getVote(result, user));
			r.setBookmark(bookmarkService.containsBookmark(r, user));
		});
	}

	private static Set<String> readMockResults() {
		Path path = new File("data/results").toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}

	private ResultEntry parseResultEntry(String line) {
		String[] splits = line.split("\\$");
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setResult(splits[0]);
		resultEntry.setReferences(Arrays.stream(splits[2].split("\\|")).collect(Collectors.toSet()));
		return resultEntry;
	}

	private static List<ResultEntry> createMockResultsEntries() {
		int resultsCount = (int) (Math.random() * 10) + 1;
		return IntStream.range(0, resultsCount)
				.mapToObj(SearchServiceImpl::createMockEntry)
				.collect(Collectors.toList());
	}

	private static ResultEntry createMockEntry(int i) {
		int id = 10 * searchCount + i;

		int referencesCount = (int) (Math.random() * 5) + 1;
		Set<String> references = IntStream.range(0, referencesCount)
				.mapToObj(ri -> "http://www.reference-url.com/" + id + "/" + ri)
				.collect(Collectors.toSet());

		ResultEntry entry = new ResultEntry();
		entry.setId(id);
		entry.setResult("Item #" + id);
		entry.setReferences(references);
		return entry;
	}

	private Result getResult(ResultEntry entry, String keyword) {
		Result existingResult = resultsRepository.findResultByResultAndKeyword(entry.getResult(), keyword);
		if (existingResult == null) {
			Result result = new Result();
			result.setResult(entry.getResult());
			result.setKeyword(keyword);
			result.setRating(entry.getReferences().size());

			Result newResult = resultsRepository.save(result);
			entry.getReferences().stream()
					.filter(ref -> referenceRepository.findReferenceByUrlAndResult(ref, result) == null)
					.map(ref -> new Reference().setUrl(ref).setResult(result))
					.forEach(referenceRepository::save);
			return newResult;
		}
		return existingResult;
	}

	private int getVote(Result result, User user) {
		if (user == null) return 0;
		Vote vote = voteService.getVote(result, user);
		return vote == null ? 0 : vote.getValue();
	}

	private int compareSearches(Search searchOne, Search searchTwo) {
		int compareSearchCount = Long.compare(searchTwo.getSearchCount(), searchOne.getSearchCount());
		if (compareSearchCount == 0) {
			return searchTwo.getKeyword().compareToIgnoreCase(searchOne.getKeyword());
		}
		return compareSearchCount;
	}
}
