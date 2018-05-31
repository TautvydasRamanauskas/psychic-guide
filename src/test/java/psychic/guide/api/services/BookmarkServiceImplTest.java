package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.data.ResultEntry;

import java.util.*;

import static org.junit.Assert.*;

public class BookmarkServiceImplTest {
	private static final String IP_ONE = "IP_ONE";
	private static final String IP_TWO = "IP_TWO";

	private TestPersistenceService<HashMap<String, Collection<ResultEntry>>> persistenceService;
	private BookmarkServiceImpl bookmarkService;
	private ResultEntry resultEntry;

	@Before
	public void setUp() throws Exception {
		resultEntry = new ResultEntry();

		HashMap<String, Collection<ResultEntry>> data = createData();
		VoteService voteService = new VoteServiceImpl(new TestPersistenceService<>(new HashMap<>()));

		persistenceService = new TestPersistenceService<>(data);
		bookmarkService = new BookmarkServiceImpl(voteService, persistenceService);
	}

	@Test
	public void addBookmark() throws Exception {
		bookmarkService.addBookmark(new ResultEntry(), IP_TWO);
		HashMap<String, Collection<ResultEntry>> data = persistenceService.getData();
		assertEquals(2, data.size());
		Collection<ResultEntry> resultEntries = data.get(IP_TWO);
		assertFalse(resultEntries.isEmpty());
	}

	@Test
	public void removeBookmark() throws Exception {
		bookmarkService.removeBookmark(resultEntry, IP_ONE);
		HashMap<String, Collection<ResultEntry>> data = persistenceService.getData();
		Collection<ResultEntry> resultEntries = data.get(IP_ONE);
		assertTrue(resultEntries.isEmpty());
	}

	@Test
	public void containsBookmark() throws Exception {
		assertTrue(bookmarkService.containsBookmark(resultEntry, IP_ONE));
		assertFalse(bookmarkService.containsBookmark(resultEntry, IP_TWO));
		assertFalse(bookmarkService.containsBookmark(new ResultEntry(), IP_ONE));
		assertFalse(bookmarkService.containsBookmark(new ResultEntry(), IP_TWO));
	}

	@Test
	public void bookmarks() throws Exception {
		List<ResultEntry> bookmarksOne = bookmarkService.bookmarks(IP_ONE);
		assertFalse(bookmarksOne.isEmpty());
		assertEquals(resultEntry, bookmarksOne.iterator().next());

		List<ResultEntry> bookmarksTwo = bookmarkService.bookmarks(IP_TWO);
		assertTrue(bookmarksTwo.isEmpty());
	}

	@Test
	public void clear() throws Exception {
		bookmarkService.clear();
		HashMap<String, Collection<ResultEntry>> data = persistenceService.getData();
		assertTrue(data.isEmpty());
	}

	private HashMap<String, Collection<ResultEntry>> createData() {
		HashMap<String, Collection<ResultEntry>> data = new HashMap<>();
		Set<ResultEntry> resultEntries = new HashSet<>();
		resultEntries.add(resultEntry);
		data.put(IP_ONE, resultEntries);
		return data;
	}
}