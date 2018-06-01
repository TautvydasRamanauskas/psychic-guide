package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class VoteServiceImplTest {
	private final static String TITLE_ONE = "TITLE_ONE";
	private final static String TITLE_TWO = "TITLE_TWO";
	private final static String TITLE_THREE = "TITLE_THREE";
	private final static String IP_ONE = "IP_ONE";
	private final static String IP_TWO = "IP_TWO";
	private final static int VALUE_ONE = 1;
	private final static int VALUE_TWO = 2;

	private TestPersistenceService<HashMap<String, Map<String, Vote>>> persistenceService;
	private VoteServiceImpl voteService;

	@Before
	public void setUp() throws Exception {
		HashMap<String, Map<String, Vote>> data = new HashMap<>();

		Map<String, Vote> titleOneMap = new HashMap<>();
		titleOneMap.put(IP_ONE, createVote(TITLE_ONE, IP_ONE, VALUE_ONE));
		titleOneMap.put(IP_TWO, createVote(TITLE_ONE, IP_TWO, VALUE_TWO));
		data.put(TITLE_ONE, titleOneMap);

		Map<String, Vote> titleTwoMap = new HashMap<>();
		titleTwoMap.put(IP_ONE, createVote(TITLE_TWO, IP_ONE, VALUE_ONE));
		titleTwoMap.put(IP_TWO, createVote(TITLE_TWO, IP_TWO, VALUE_TWO));
		data.put(TITLE_TWO, titleTwoMap);

		persistenceService = new TestPersistenceService<>(data);
		voteService = new VoteServiceImpl(null);
	}

	@Test
	public void testCalculateVoteValue() throws Exception {
		int voteValue = voteService.calculateVoteValue(null);
		assertEquals(3, voteValue);
	}

	@Test
	public void testAddVote() throws Exception {
		voteService.addVote(createVote(TITLE_THREE, IP_ONE, VALUE_ONE));
		HashMap<String, Map<String, Vote>> data = persistenceService.getData();
		assertTrue(data.containsKey(TITLE_THREE));
	}

	@Test
	public void testRemoveVote() throws Exception {
		voteService.removeVote(createVote(TITLE_ONE, IP_ONE, VALUE_ONE));
		HashMap<String, Map<String, Vote>> data = persistenceService.getData();
		assertFalse(data.get(TITLE_ONE).containsKey(IP_ONE));
	}

	@Test
	public void testGetVote() throws Exception {
		Vote vote = voteService.getVote(null, new User());
		assertEquals(TITLE_TWO, vote.getResult().getResult());
		assertEquals(IP_TWO, vote.getUser().getId());
		assertEquals(VALUE_TWO, vote.getValue());
	}

	private static Vote createVote(String title, String ip, int value) {
		Vote vote = new Vote();
		vote.setResult(new Result().setResult(title));
		vote.setUser(new User().setId(Long.parseLong(ip)));
		vote.setValue(value);
		return vote;
	}
}