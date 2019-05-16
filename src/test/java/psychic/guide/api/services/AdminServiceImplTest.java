package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import psychic.guide.api.model.Search;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.CacheStatistic;
import psychic.guide.api.model.data.UserIdLevel;
import psychic.guide.api.repository.*;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;
import psychic.guide.api.services.internal.searchengine.SearchApi;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static psychic.guide.api.services.ServiceModelUtils.*;

public class AdminServiceImplTest {
	private LoadBalancer loadBalancer;
	private UserRepository userRepository;
	private SearchesRepository searchesRepository;
	private ResultsRepository resultsRepository;
	private LinksRepository linksRepository;
	private BookmarksRepository bookmarksRepository;
	private AdminService adminService;

	@Before
	public void setUp() {
		loadBalancer = mock(LoadBalancer.class);
		userRepository = mock(UserRepository.class);
		searchesRepository = mock(SearchesRepository.class);
		resultsRepository = mock(ResultsRepository.class);
		linksRepository = mock(LinksRepository.class);
		bookmarksRepository = mock(BookmarksRepository.class);
		adminService = new AdminServiceImpl(
				loadBalancer,
				userRepository,
				searchesRepository,
				resultsRepository,
				linksRepository,
				bookmarksRepository
		);
	}

	@Test
	public void limits() {
		final String searchApiNameOne = SearchApi.GOOGLE.name();
		final String searchApiNameTwo = SearchApi.EMPTY.name();
		final int searchApiLimitOne = 5;
		final int searchApiLimitTwo = 10;

		Map<String, Integer> limits = Map.of(
				searchApiNameOne, searchApiLimitOne,
				searchApiNameTwo, searchApiLimitTwo
		);
		when(loadBalancer.getLimits()).thenReturn(limits);

		Map<String, Integer> serviceLimits = adminService.limits();
		assertTrue(serviceLimits.containsKey(searchApiNameOne));
		assertTrue(serviceLimits.containsKey(searchApiNameTwo));
		assertEquals(searchApiLimitOne, serviceLimits.get(searchApiNameOne).intValue());
		assertEquals(searchApiLimitTwo, serviceLimits.get(searchApiNameTwo).intValue());
	}

	@Test
	public void users() {
		final long userIdOne = 1;
		final long userIdTwo = 2;

		User userOne = createUser(userIdOne);
		User userTwo = createUser(userIdTwo);
		List<User> users = List.of(userOne, userTwo);
		when(userRepository.findAll()).thenReturn(users);

		List<User> serviceUsers = adminService.users();
		assertEquals(users.size(), serviceUsers.size());
		assertEquals(userIdOne, serviceUsers.get(0).getId());
		assertEquals(userIdTwo, serviceUsers.get(1).getId());
	}

	@Test
	public void searches() {
		final long searchIdOne = 1;
		final long searchIdTwo = 2;
		final long resultsCount = 2L;

		Search searchOne = createSearch(searchIdOne);
		Search searchTwo = createSearch(searchIdTwo);
		List<Search> searches = List.of(searchOne, searchTwo);
		when(searchesRepository.findAll()).thenReturn(searches);
		when(resultsRepository.countByKeyword(Mockito.anyString())).thenReturn(resultsCount);

		List<CacheStatistic> serviceSearches = adminService.searches();
		assertEquals(2, serviceSearches.size());
		assertEquals(searchIdOne, serviceSearches.get(0).getId());
		assertEquals(resultsCount, serviceSearches.get(0).getResultsCount());
		assertEquals(searchIdTwo, serviceSearches.get(1).getId());
		assertEquals(resultsCount, serviceSearches.get(1).getResultsCount());
	}

	@Test
	public void level() {
		final long userId = 1;
		final int levelOne = 1;
		final int levelTwo = 2;

		User user = createUser(userId, levelOne);
		when(userRepository.findOne(userId)).thenReturn(user);
		UserIdLevel userIdLevel = createUserIdLevel(userId, levelTwo);
		adminService.level(userIdLevel);

		verify(userRepository, times(1)).save(user);
		assertEquals(levelTwo, user.getLevel());
	}

	@Test
	public void cleanCache() {
		final String keyword = "keyword";

		adminService.cleanCache(keyword);

		verify(resultsRepository, times(1)).removeByKeyword(keyword);
	}

	@Test
	public void linksCount() {
		final long linksCount = 10;

		when(linksRepository.count()).thenReturn(linksCount);
		long serviceLinksCount = adminService.linksCount();

		assertEquals(linksCount, serviceLinksCount);
	}

	@Test
	public void deleteLinks() {
		adminService.deleteLinks();

		verify(linksRepository, times(1)).deleteAll();
	}

	@Test
	public void bookmarksCount() {
		final long bookmarksCount = 7;

		when(bookmarksRepository.count()).thenReturn(bookmarksCount);
		long serviceBookmarksCount = adminService.bookmarksCount();

		assertEquals(bookmarksCount, serviceBookmarksCount);
	}

	@Test
	public void deleteBookmarks() {
		adminService.deleteBookmarks();

		verify(bookmarksRepository, times(1)).deleteAll();
	}
}