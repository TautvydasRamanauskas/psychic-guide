package psychic.guide.api.services;

import org.springframework.stereotype.Service;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.internal.PersistenceSerializationService;
import psychic.guide.api.services.internal.PersistenceService;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteServiceImpl implements VoteService {
	private static final String FILE_NAME = "votes";

	private PersistenceService<Map<String, Map<String, Vote>>> persistenceService;
	private final Map<String, Map<String, Vote>> votes; // title -> ip -> vote

	public VoteServiceImpl() {
		this.persistenceService = new PersistenceSerializationService<>(FILE_NAME);
		this.votes = persistenceService.read();
	}

	@Override
	public int calculateVoteValue(String title) {
		Map<String, Vote> votesByIp = votes.get(title);
		if (votesByIp != null) {
			return votesByIp.values().stream()
					.map(Vote::getValue)
					.reduce((vOne, vTwo) -> vOne + vTwo)
					.orElse(0);
		}
		return 0;
	}

	@Override
	public void addVote(Vote vote) {
		Map<String, Vote> votesByIp = votes.computeIfAbsent(vote.getTitle(), t -> new HashMap<>());
		votesByIp.put(vote.getIp(), vote);
		persistenceService.save(votes);
	}

	@Override
	public void removeVote(Vote vote) {
		Map<String, Vote> votesByIp = votes.computeIfAbsent(vote.getTitle(), t -> new HashMap<>());
		votesByIp.remove(vote.getIp());
		persistenceService.save(votes);
	}

	@Override
	public Vote getVote(String title, String ip) {
		Map<String, Vote> votesByIp = votes.get(title);
		if (votesByIp != null) {
			return votesByIp.get(ip);
		}
		return null;
	}

	public void clear() {
		votes.clear();
		persistenceService.save(votes);
	}
}
