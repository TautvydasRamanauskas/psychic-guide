package psychic.guide.api.services.internal.searcher;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.internal.searchengine.SearchApiService;

import java.util.List;

import static org.mockito.Mockito.mock;

public class SearcherImplTest {
	private SearchApiService searchApiService;
	private Options options;
	private Searcher searcher;

	@Before
	public void setUp() {
		searchApiService = mock(SearchApiService.class);
		options = new Options();
		searcher = new SearcherImpl(searchApiService, options);
	}

	// TODO
	@Ignore
	@Test
	public void search() {
		final String keyword = "keyword";

		List<ResultEntry> resultEntries = searcher.search(keyword);
	}
}