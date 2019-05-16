package psychic.guide.api;

import org.junit.Test;
import org.mockito.Mockito;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.services.BookmarkService;
import psychic.guide.api.services.VoteService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResultsConverterTest {
	private static final long RESULT_ID_ONE = 1;
	private static final long RESULT_ID_TWO = 2;

	@Test
	public void entriesToResults() {
		List<ResultEntry> entries = Arrays.asList(createResultEntry(RESULT_ID_ONE), createResultEntry(RESULT_ID_TWO));
		ResultsRepository resultsRepository = createResultsRepository(RESULT_ID_ONE, RESULT_ID_TWO);
		List<Result> results = ResultsConverter.entriesToResults(entries, resultsRepository);

		assertEquals(entries.size(), results.size());
		assertEquals(RESULT_ID_ONE, results.get(0).getId());
		assertEquals(RESULT_ID_TWO, results.get(1).getId());
	}

	@Test
	public void resultsToEntries() {
		List<Result> results = Arrays.asList(createResult(RESULT_ID_ONE), createResult(RESULT_ID_TWO));
		User user = new User();
		VoteService voteService = Mockito.mock(VoteService.class);
		BookmarkService bookmarkService = Mockito.mock(BookmarkService.class);
		ReferenceRepository referenceRepository = Mockito.mock(ReferenceRepository.class);
		List<ResultEntry> resultEntries = ResultsConverter.resultsToEntries(results, user, voteService, bookmarkService, referenceRepository);

		assertEquals(results.size(), resultEntries.size());
		assertEquals(RESULT_ID_ONE, resultEntries.get(0).getId());
		assertEquals(RESULT_ID_TWO, resultEntries.get(1).getId());
	}

	private ResultEntry createResultEntry(long id) {
		ResultEntry resultEntry = new ResultEntry();
		resultEntry.setId(id);
		return resultEntry;
	}

	@SuppressWarnings("SameParameterValue")
	private ResultsRepository createResultsRepository(long resultIdOne, long resultIdTwo) {
		ResultsRepository resultsRepository = Mockito.mock(ResultsRepository.class);
		Mockito.when(resultsRepository.findOne(resultIdOne)).thenReturn(createResult(resultIdOne));
		Mockito.when(resultsRepository.findOne(resultIdTwo)).thenReturn(createResult(resultIdTwo));
		return resultsRepository;
	}

	private Result createResult(long id) {
		Result result = new Result();
		result.setId(id);
		return result;
	}
}