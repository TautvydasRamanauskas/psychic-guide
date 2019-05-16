package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.Link;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.LinksRepository;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static psychic.guide.api.services.ServiceModelUtils.*;

public class LinkServiceImplTest {
	private ResultsRepository resultsRepository;
	private LinksRepository linksRepository;
	private LinkService linkService;

	@Before
	public void setUp() {
		VoteService voteService = mock(VoteService.class);
		BookmarkService bookmarkService = mock(BookmarkService.class);
		ReferenceRepository referenceRepository = mock(ReferenceRepository.class);

		resultsRepository = mock(ResultsRepository.class);
		linksRepository = mock(LinksRepository.class);
		linkService = new LinkServiceImpl(voteService, bookmarkService, resultsRepository, linksRepository, referenceRepository);
	}

	@Test
	public void get() {
		final long userId = 1;
		final long resultIdOne = 1;
		final long resultIdTwo = 2;
		final long linkId = 1;
		final String linkText = "link";

		User user = createUser(userId);
		when(linksRepository.getLinkByLink(linkText)).thenReturn(null);
		List<ResultEntry> resultEntriesEmpty = linkService.get(linkText, user);

		assertTrue(resultEntriesEmpty.isEmpty());


		Result resultOne = createResult(resultIdOne);
		Result resultTwo = createResult(resultIdTwo);
		List<Result> results = List.of(resultOne, resultTwo);
		Link link = createLink(linkId, results);
		when(linksRepository.getLinkByLink(linkText)).thenReturn(link);
		List<ResultEntry> resultEntries = linkService.get(linkText, user);

		assertEquals(results.size(), resultEntries.size());
		assertEquals(resultIdOne, resultEntries.get(0).getId());
		assertEquals(resultIdTwo, resultEntries.get(1).getId());
	}

	@Test
	public void generate() {
		final long resultEntryIdOne = 1;
		final long resultEntryIdTwo = 2;

		ResultEntry resultEntryOne = createResultEntry(resultEntryIdOne);
		ResultEntry resultEntryTwo = createResultEntry(resultEntryIdTwo);
		Result resultOne = createResult(resultEntryIdOne);
		Result resultTwo = createResult(resultEntryIdTwo);
		when(resultsRepository.findOne(resultEntryIdOne)).thenReturn(resultOne);
		when(resultsRepository.findOne(resultEntryIdTwo)).thenReturn(resultTwo);


		List<ResultEntry> resultEntries = List.of(resultEntryOne, resultEntryTwo);
		Link link = linkService.generate(resultEntries);
		List<Result> results = link.getResults();

		verify(linksRepository, times(1)).save(link);
		assertEquals(resultEntries.size(), results.size());
		assertEquals(resultEntryIdOne, results.get(0).getId());
		assertEquals(resultEntryIdTwo, results.get(1).getId());
	}
}