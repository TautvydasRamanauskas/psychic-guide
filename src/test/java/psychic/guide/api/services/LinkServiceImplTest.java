package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.data.ResultEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class LinkServiceImplTest {
	private static final UUID LINK_ONE = UUID.randomUUID();

	private TestPersistenceService<HashMap<UUID, List<ResultEntry>>> persistenceService;
	private LinkServiceImpl linkService;

	@Before
	public void setUp() throws Exception {
		HashMap<UUID, List<ResultEntry>> data = new HashMap<>();
		data.put(LINK_ONE, new ArrayList<>());

		VoteService voteService = new VoteServiceImpl(null);

		persistenceService = new TestPersistenceService<>(data);
		linkService = new LinkServiceImpl(voteService, null, null, null, null);
	}

	@Test
	public void get() throws Exception {
		List<ResultEntry> resultEntries = linkService.get("", null);
		assertNotNull(resultEntries);
	}

	@Test
	public void generate() throws Exception {
//		UUID generateLink = linkService.generate(new ArrayList<>());
//		HashMap<UUID, List<ResultEntry>> data = persistenceService.getData();
//		List<ResultEntry> resultEntries = data.get(generateLink);
//		assertNotNull(resultEntries);
	}
}