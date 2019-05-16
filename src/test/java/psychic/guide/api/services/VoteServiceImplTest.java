package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.Vote;
import psychic.guide.api.repository.VotesRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static psychic.guide.api.services.ServiceModelUtils.createResult;
import static psychic.guide.api.services.ServiceModelUtils.createVote;

public class VoteServiceImplTest {
	private final static String RESULT_ONE = "RESULT_ONE";
	private final static String RESULT_TWO = "RESULT_TWO";
	private final static int VOTE_VALUE_ONE = 1;
	private final static int VOTE_VALUE_TWO = 2;
	private final static int RESULT_ID = 1;
	private final static int USER_ID = 1;

	private VotesRepository votesRepository;
	private VoteService voteService;

	@Before
	public void setUp() {
		votesRepository = mock(VotesRepository.class);
		voteService = new VoteServiceImpl(votesRepository);
	}

	@Test
	public void calculateVoteValue() {
		Vote voteOne = createVote(VOTE_VALUE_ONE, RESULT_ONE, RESULT_ID, USER_ID);
		Vote voteTwo = createVote(VOTE_VALUE_TWO, RESULT_ONE, RESULT_ID, USER_ID);
		Vote voteThree = createVote(VOTE_VALUE_ONE, RESULT_TWO, RESULT_ID, USER_ID);
		List<Vote> votes = List.of(voteOne, voteTwo, voteThree);
		when(votesRepository.getVotesByResult(any())).thenReturn(votes);

		Result result = createResult(RESULT_ID, "");
		int voteValue = voteService.calculateVoteValue(result);
		assertEquals(4, voteValue);
	}

	@Test
	public void addVote() {
		Vote vote = createVote(VOTE_VALUE_ONE, RESULT_ONE, RESULT_ID, USER_ID);
		voteService.addVote(vote);
		verify(votesRepository, times(1)).save(vote);
	}

	@Test
	public void removeVote() {
		Vote vote = createVote(VOTE_VALUE_ONE, RESULT_ONE, RESULT_ID, USER_ID);
		voteService.removeVote(vote);
		verify(votesRepository, times(1)).deleteVoteByResultAndUser(vote.getResult(), vote.getUser());
	}

	@Test
	public void getVote() {
		Vote vote = createVote(VOTE_VALUE_ONE, RESULT_ONE, RESULT_ID, USER_ID);
		when(votesRepository.getVoteByResultAndUser(vote.getResult(), vote.getUser())).thenReturn(vote);

		Vote serviceVote = voteService.getVote(vote.getResult(), vote.getUser());
		assertEquals(vote, serviceVote);
	}
}