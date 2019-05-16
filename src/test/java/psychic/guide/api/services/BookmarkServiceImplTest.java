package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.Bookmark;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.BookmarksRepository;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.repository.UserRepository;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static psychic.guide.api.services.ServiceModelUtils.*;

public class BookmarkServiceImplTest {
	private BookmarksRepository bookmarksRepository;
	private ResultsRepository resultsRepository;
	private ReferenceRepository referenceRepository;
	private UserRepository userRepository;
	private BookmarkService bookmarkService;

	@Before
	public void setUp() {
		VoteService voteService = mock(VoteService.class);
		bookmarksRepository = mock(BookmarksRepository.class);
		resultsRepository = mock(ResultsRepository.class);
		referenceRepository = mock(ReferenceRepository.class);
		userRepository = mock(UserRepository.class);
		bookmarkService = new BookmarkServiceImpl(voteService, bookmarksRepository, resultsRepository, referenceRepository, userRepository);
	}

	@Test
	public void addBookmark() {
		final long resultEntryId = 1;
		final long userId = 1;

		ResultEntry resultEntry = createResultEntry(resultEntryId);
		User user = createUser(userId);
		bookmarkService.addBookmark(resultEntry, user);

		verify(resultsRepository, times(1)).findOne(userId);
		verify(bookmarksRepository, times(1)).save(any(Bookmark.class));
	}

	@Test
	public void removeBookmark() {
		final long resultEntryId = 1;
		final long userId = 1;

		ResultEntry resultEntry = createResultEntry(resultEntryId);
		User user = createUser(userId);
		bookmarkService.removeBookmark(resultEntry, user);

		verify(resultsRepository, times(1)).findOne(userId);
		verify(bookmarksRepository, times(1)).deleteBookmarkByResultAndUser(null, user);
	}

	@Test
	public void containsBookmark() {
		final long resultId = 1;
		final long userId = 1;
		final long bookmarkId = 1;
		final long resultEntryIdOne = 1;
		final long resultEntryIdTwo = 2;

		Result result = createResult(resultId);
		when(resultsRepository.findOne(result.getId())).thenReturn(result);

		User user = createUser(userId);
		Bookmark bookmark = createBookmark(bookmarkId, null);
		when(bookmarksRepository.findBookmarkByResultAndUser(result, user)).thenReturn(bookmark);

		ResultEntry resultEntryOne = createResultEntry(resultEntryIdOne);
		ResultEntry resultEntryTwo = createResultEntry(resultEntryIdTwo);

		assertTrue(bookmarkService.containsBookmark(resultEntryOne, user));
		assertFalse(bookmarkService.containsBookmark(resultEntryTwo, user));
		assertFalse(bookmarkService.containsBookmark(resultEntryOne, null));
		assertFalse(bookmarkService.containsBookmark(resultEntryTwo, null));
	}

	@Test
	public void bookmarks() {
		final long userId = 1;
		final long bookmarkIdOne = 1;
		final long bookmarkIdTwo = 2;
		final long resultIdOne = 1;
		final long resultIdTwo = 2;

		User user = createUser(userId);
		when(userRepository.findOne(user.getId())).thenReturn(user);

		when(referenceRepository.findReferencesByResult(any(Result.class))).thenReturn(emptyList());

		Result resultOne = createResult(resultIdOne);
		Result resultTwo = createResult(resultIdTwo);
		Bookmark bookmarkOne = createBookmark(bookmarkIdOne, resultOne);
		Bookmark bookmarkTwo = createBookmark(bookmarkIdTwo, resultTwo);
		List<Bookmark> bookmarks = List.of(bookmarkOne, bookmarkTwo);
		when(bookmarksRepository.getBookmarksByUser(user)).thenReturn(bookmarks);

		List<ResultEntry> serviceBookmarks = bookmarkService.bookmarks(userId);

		assertEquals(bookmarks.size(), serviceBookmarks.size());
		assertEquals(resultIdOne, serviceBookmarks.get(0).getId());
		assertEquals(resultIdTwo, serviceBookmarks.get(1).getId());
	}
}