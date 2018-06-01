package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class SearchServiceImplTest {
	private static final String SEARCH_ONE = "SEARCH_ONE";
	private static final String SEARCH_TWO = "SEARCH_TWO";
	private static final String SEARCH_THREE = "SEARCH_THREE";
	private static final String SEARCH_FOUR = "SEARCH_FOUR";
	private static final String SEARCH_FIVE = "SEARCH_FIVE";
	private static final String SEARCH_SIX = "SEARCH_SIX";
	private static final String SEARCH_SEVEN = "SEARCH_SEVEN";
	private static final String SEARCH_EIGHT = "SEARCH_EIGHT";
	private static final String SEARCH_NINE = "SEARCH_NINE";
	private static final String SEARCH_TEN = "SEARCH_TEN";
	private static final String SEARCH_ELEVEN = "SEARCH_ELEVEN";

	private TestPersistenceService<HashMap<String, AtomicInteger>> persistenceService;
	private SearchServiceImpl searchService;

	@Before
	public void setUp() throws Exception {
		HashMap<String, AtomicInteger> data = new HashMap<>();
		data.put(SEARCH_ONE, new AtomicInteger(100));
		data.put(SEARCH_TWO, new AtomicInteger(99));
		data.put(SEARCH_THREE, new AtomicInteger(98));
		data.put(SEARCH_FOUR, new AtomicInteger(97));
		data.put(SEARCH_FIVE, new AtomicInteger(96));
		data.put(SEARCH_SIX, new AtomicInteger(95));
		data.put(SEARCH_SEVEN, new AtomicInteger(94));
		data.put(SEARCH_EIGHT, new AtomicInteger(93));
		data.put(SEARCH_NINE, new AtomicInteger(92));
		data.put(SEARCH_TEN, new AtomicInteger(91));
		data.put(SEARCH_ELEVEN, new AtomicInteger(90));

		VoteService voteService = new VoteServiceImpl(null);
		BookmarkService bookmarkService = new BookmarkServiceImpl(voteService, null,null,null, null);

		persistenceService = new TestPersistenceService<>(data);
		searchService = new SearchServiceImpl(bookmarkService, voteService, null, null, null);
	}

	@Test
	public void search() throws Exception {
		// not tested because of limit usage
	}

	@Test
	public void mostPopular() throws Exception {
		List<String> mostPopular = searchService.mostPopular();
		assertEquals(10, mostPopular.size());
		assertFalse(mostPopular.contains(SEARCH_ELEVEN));
		assertTrue(mostPopular.contains(SEARCH_SIX));
	}

	@Test
	public void clear() throws Exception {
		HashMap<String, AtomicInteger> data = persistenceService.getData();
		assertTrue(data.isEmpty());
	}
}