package psychic.guide.api.services;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.ResultEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LinkServiceImplTest {
	private static final UUID LINK_ONE = UUID.randomUUID();

	private TestPersistenceService<HashMap<UUID, List<ResultEntry>>> persistenceService;
	private LinkServiceImpl linkService;

	@Before
	public void setUp() throws Exception {
		HashMap<UUID, List<ResultEntry>> data = new HashMap<>();
		data.put(LINK_ONE, new ArrayList<>());

		VoteService voteService = new VoteServiceImpl(new TestPersistenceService<>(new HashMap<>()));

		persistenceService = new TestPersistenceService<>(data);
		linkService = new LinkServiceImpl(voteService, persistenceService);
	}

	@Test
	public void get() throws Exception {
		List<ResultEntry> resultEntries = linkService.get(LINK_ONE, "");
		assertNotNull(resultEntries);
	}

	@Test
	public void generate() throws Exception {
		UUID generateLink = linkService.generate(new ArrayList<>());
		HashMap<UUID, List<ResultEntry>> data = persistenceService.getData();
		List<ResultEntry> resultEntries = data.get(generateLink);
		assertNotNull(resultEntries);
	}

	@Test
	public void clear() throws Exception {
		linkService.clear();
		HashMap<UUID, List<ResultEntry>> data = persistenceService.getData();
		assertTrue(data.isEmpty());
	}
}