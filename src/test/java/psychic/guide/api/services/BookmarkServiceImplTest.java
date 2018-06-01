package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;

import java.util.*;

import static org.junit.Assert.*;

public class BookmarkServiceImplTest {
	private static final User IP_ONE = new User();
	private static final User IP_TWO = new User();

	private TestPersistenceService<HashMap<String, Collection<ResultEntry>>> persistenceService;
	private BookmarkServiceImpl bookmarkService;
	private ResultEntry resultEntry;

	@Before
	public void setUp() throws Exception {
		resultEntry = new ResultEntry();

		HashMap<String, Collection<ResultEntry>> data = createData();
		VoteService voteService = new VoteServiceImpl(null);

		persistenceService = new TestPersistenceService<>(data);
		bookmarkService = new BookmarkServiceImpl(voteService, null, null, null, null);
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
		bookmarkService.removeBookmark(resultEntry, null);
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
		List<ResultEntry> bookmarksOne = bookmarkService.bookmarks(1L);
		assertFalse(bookmarksOne.isEmpty());
		assertEquals(resultEntry, bookmarksOne.iterator().next());

		List<ResultEntry> bookmarksTwo = bookmarkService.bookmarks(1L);
		assertTrue(bookmarksTwo.isEmpty());
	}

	@Test
	public void clear() throws Exception {
		HashMap<String, Collection<ResultEntry>> data = persistenceService.getData();
		assertTrue(data.isEmpty());
	}

	private HashMap<String, Collection<ResultEntry>> createData() {
		HashMap<String, Collection<ResultEntry>> data = new HashMap<>();
		Set<ResultEntry> resultEntries = new HashSet<>();
		resultEntries.add(resultEntry);
		data.put("", resultEntries);
		return data;
	}
}