package psychic.guide.api.services.internal;

import org.jsoup.nodes.Document;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.internal.model.SearchResult;
import psychic.guide.api.services.internal.searchengine.SearchAPIService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Searcher {
	private static final String BRANDS_LIST_PATH = "data/brands";
	private static final String SEARCH_WORD = "Best";
	private final SearchAPIService searchService;
	private final Options options;
	private final Parser pageParser;

	public Searcher(SearchAPIService searchService, Options options) {
		this.searchService = searchService;
		this.options = options;
		this.pageParser = new Parser(fetchBrandList());
	}

	public List<ResultEntry> search(String keyword) {
		String searchText = createSearchText(keyword);
		List<SearchResult> searchResults = searchService.search(searchText);
		Collection<ResultEntry> parseResults = parseSearchResults(searchResults);
		List<ResultEntry> results = parseResults.stream()
				.filter(r -> r.getCount() >= options.getMinRating())
				.sorted()
				.collect(Collectors.toList());
		results.forEach(System.out::println);
		return results;
	}

	private String createSearchText(String keyword) {
		if (keyword.contains(SEARCH_WORD) || keyword.contains(SEARCH_WORD.toLowerCase())) {
			return keyword;
		}
		return SEARCH_WORD + " " + keyword;
	}

	private Set<ResultEntry> parseSearchResults(List<SearchResult> searchResults) {
		Set<ResultEntry> parseResults = new HashSet<>();
		for (SearchResult searchResult : searchResults) {
			Set<ResultEntry> parseResult = parsePage(searchResult);
			for (ResultEntry resultEntry : parseResult) {
				updateParseResults(parseResults, resultEntry);
			}
		}
		return parseResults;
	}

	private Set<ResultEntry> parsePage(SearchResult searchResult) {
		String url = searchResult.url;
		Document page = PageFetcher.fetch(url);
		return page == null ? new HashSet<>() : pageParser.parse(page, url);
	}

	private void updateParseResults(Set<ResultEntry> parseResults, ResultEntry resultEntry) {
		ResultEntry existingEntry = parseResults.stream()
				.filter(r -> resultsMatch(resultEntry, r))
				.findAny()
				.orElse(null);
		if (existingEntry != null) {
			existingEntry.setCount(existingEntry.getCount() + 1);
			existingEntry.getReferences().addAll(resultEntry.getReferences());
		} else {
			parseResults.add(resultEntry);
		}
	}

	private boolean resultsMatch(ResultEntry resultEntryOne, ResultEntry resultEntryTwo) {
		String resultOne = resultEntryTwo.getResult();
		String resultTwo = resultEntryOne.getResult();
		return resultOne.contains(resultTwo) || resultTwo.contains(resultOne);
	}

	private static Set<String> fetchBrandList() {
		Path path = new File(BRANDS_LIST_PATH).toPath();
		try {
			return Files.lines(path).collect(Collectors.toSet());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptySet();
	}
}
