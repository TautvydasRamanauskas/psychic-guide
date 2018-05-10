package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.internal.PersistenceSerializationService;
import psychic.guide.api.services.internal.PersistenceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class LinkServiceImpl implements LinkService {
	private static final String FILE_NAME = "links";

	private final VoteService voteService;
	private final PersistenceService<HashMap<UUID, List<ResultEntry>>> persistenceService;
	private final HashMap<UUID, List<ResultEntry>> links;

	@Autowired
	public LinkServiceImpl(VoteService voteService) {
		this.voteService = voteService;
		this.persistenceService = new PersistenceSerializationService<>(FILE_NAME);
		this.links = persistenceService.readOrDefault(new HashMap<>());
	}

	@Override
	public List<ResultEntry> get(UUID link, String ip) {
		List<ResultEntry> linkEntries = links.computeIfAbsent(link, l -> new ArrayList<>());
		for (ResultEntry linkEntry : linkEntries) {
			linkEntry.setVoteValue(voteService.calculateVoteValue(linkEntry.getResult()));
			Vote vote = voteService.getVote(linkEntry.getResult(), ip);
			linkEntry.setPersonalVote(vote == null ? 0 : vote.getValue());
		}
		return linkEntries;
	}

	@Override
	public UUID generate(List<ResultEntry> results) {
		UUID link = UUID.randomUUID();
		links.put(link, results);
		persistenceService.save(links);
		return link;
	}

	public void clear() {
		links.clear();
		persistenceService.save(links);
	}
}
