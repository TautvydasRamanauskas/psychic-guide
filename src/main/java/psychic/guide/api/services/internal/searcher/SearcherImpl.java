package psychic.guide.api.services.internal.searcher;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.internal.PageFetcher;
import psychic.guide.api.services.internal.matchers.MatcherSelector;
import psychic.guide.api.services.internal.matchers.StringMatcher;
import psychic.guide.api.services.internal.model.SearchResult;
import psychic.guide.api.services.internal.parser.Parser;
import psychic.guide.api.services.internal.searchengine.SearchApiService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.Globals.USE_LOCAL_SEARCH_API;

public class SearcherImpl implements Searcher {
	private static final String SEARCH_WORD = "Best";
	private final SearchApiService searchService;
	private final Options options;
	private final Parser pageParser;
	private final StringMatcher stringMatcher;
	private final Logger logger;

	public SearcherImpl(SearchApiService searchService, Options options) {
		this.searchService = searchService;
		this.options = options;
		this.pageParser = new Parser(options);
		this.stringMatcher = new MatcherSelector().select(options);
		this.logger = LoggerFactory.getLogger(Searcher.class);
	}

	public List<ResultEntry> search(String keyword) {
		String searchText = createSearchText(keyword);
		List<SearchResult> searchResults = searchService.search(searchText);
		Collection<ResultEntry> parseResults = parseSearchResults(searchResults);
		List<ResultEntry> results = parseResults.stream()
				.filter(r -> r.getReferences().size() >= options.getMinRating())
				.sorted()
				.collect(Collectors.toList());

		results.stream().map(ResultEntry::toString).forEach(logger::info);
		pageParser.persist();
		return results;
	}

	private String createSearchText(String keyword) {
		if (keyword.toLowerCase().contains(SEARCH_WORD.toLowerCase() + " ")) {
			return keyword;
		}
		return SEARCH_WORD + " " + keyword;
	}

	private Set<ResultEntry> parseSearchResults(List<SearchResult> searchResults) {
		Set<ResultEntry> parseResults = new HashSet<>();
		searchResults.stream()
				.map(this::parsePage)
				.flatMap(Collection::stream)
				.forEach(resultEntry -> updateParseResults(parseResults, resultEntry));
		return parseResults;
	}

	private Set<ResultEntry> parsePage(SearchResult searchResult) {
		String url = searchResult.url;
		Document page = USE_LOCAL_SEARCH_API ? PageFetcher.loadLocal(url) : PageFetcher.fetch(url);
		return page == null ? new HashSet<>() : pageParser.parse(page, url);
	}

	private void updateParseResults(Set<ResultEntry> parseResults, ResultEntry resultEntry) {
		parseResults.stream()
				.filter(r -> stringMatcher.match(r.getResult(), resultEntry.getResult()))
				.findAny()
				.ifPresentOrElse(
						e -> e.getReferences().addAll(resultEntry.getReferences()), // TODO: choose longer text | merge
						() -> parseResults.add(resultEntry) // TODO: alias
				);
	}
}
