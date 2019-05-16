package psychic.guide.api.services.internal.searchengine;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.services.internal.model.SearchResult;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class EmptySearchApiTest {
	private SearchApiService searchApiService;

	@Before
	public void setUp() {
		searchApiService = new EmptySearchApi();
	}

	@Test
	public void search() {
		final String keyword = "keyword";

		List<SearchResult> result = searchApiService.search(keyword);

		assertTrue(result.isEmpty());
	}
}