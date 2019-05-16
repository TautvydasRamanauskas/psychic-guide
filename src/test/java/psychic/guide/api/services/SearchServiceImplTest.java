package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.Search;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.repository.SearchesRepository;
import psychic.guide.api.services.internal.searchengine.SearchApiService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static psychic.guide.api.services.ServiceModelUtils.*;

public class SearchServiceImplTest {
	private SearchesRepository searchesRepository;
	private ResultsRepository resultsRepository;
	private SearchService searchService;

	@Before
	public void setUp() {
		BookmarkService bookmarkService = mock(BookmarkService.class);
		VoteService voteService = mock(VoteService.class);
		SearchApiService searchAPIService = mock(SearchApiService.class);
		ReferenceRepository referenceRepository = mock(ReferenceRepository.class);

		searchesRepository = mock(SearchesRepository.class);
		resultsRepository = mock(ResultsRepository.class);
		searchService = new SearchServiceImpl(
				bookmarkService,
				voteService,
				searchAPIService,
				searchesRepository,
				resultsRepository,
				referenceRepository
		);
	}

	@Test
	public void searchCached() {
		final long searchId = 1;
		final String keyword = "keyword";
		final long userId = 1;
		final boolean useCache = true;
		final long resultIdOne = 1;
		final long resultIdTwo = 2;

		Search search = createSearch(searchId);
		Options options = createOptions(useCache);
		User user = createUser(userId);
		user.setOptions(options);
		when(searchesRepository.findByKeyword(keyword)).thenReturn(search);

		Result resultOne = createResult(resultIdOne);
		Result resultTwo = createResult(resultIdTwo);
		List<Result> results = List.of(resultOne, resultTwo);
		when(resultsRepository.findResultsByKeyword(keyword)).thenReturn(results);

		List<ResultEntry> serviceResults = searchService.search(keyword, user);
		assertEquals(results.size(), serviceResults.size());
		assertEquals(resultIdOne, serviceResults.get(0).getId());
		assertEquals(resultIdTwo, serviceResults.get(1).getId());
	}

	@Test
	public void mostPopular() {
		final long searchIdOne = 1;
		final long searchIdTwo = 2;

		Search searchOne = createSearch(searchIdOne);
		Search searchTwo = createSearch(searchIdTwo);
		List<Search> searches = List.of(searchOne, searchTwo);
		when(searchesRepository.findAll()).thenReturn(searches);
		List<Search> serviceSearches = searchService.mostPopular();

		assertEquals(searches.size(), serviceSearches.size());
		assertEquals(searchIdOne, serviceSearches.get(0).getId());
		assertEquals(searchIdTwo, serviceSearches.get(1).getId());
	}

	@Test
	public void search() {
		// not tested because of search limits
	}
}