package psychic.guide.api.services;

import org.springframework.stereotype.Service;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.internal.PersistenceSerializationService;
import psychic.guide.api.services.internal.PersistenceService;

import java.util.HashMap;
import java.util.Map;

@Service
public class VoteServiceImpl implements VoteService {
	private static final String FILE_NAME = "votes";

	private PersistenceService<HashMap<String, Map<String, Vote>>> persistenceService;
	private final HashMap<String, Map<String, Vote>> votes; // title -> ip -> vote

	public VoteServiceImpl() {
		this(new PersistenceSerializationService<>(FILE_NAME));
	}

	VoteServiceImpl(PersistenceService<HashMap<String, Map<String, Vote>>> persistenceService) {
		this.persistenceService = persistenceService;
		this.votes = persistenceService.readOrDefault(new HashMap<>());
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
		Map<String, Vote> votesByIp = votes.computeIfAbsent(vote.getResult().getResult(), t -> new HashMap<>());
		votesByIp.put(vote.getUser().getId() + "", vote);
		persistenceService.saveOnThread(votes);
	}

	@Override
	public void removeVote(Vote vote) {
		Map<String, Vote> votesByIp = votes.computeIfAbsent(vote.getResult().getResult(), t -> new HashMap<>());
		votesByIp.remove(vote.getUser().getId() + "");
		persistenceService.saveOnThread(votes);
	}

	@Override
	public Vote getVote(String title, User user) {
		Map<String, Vote> votesByIp = votes.get(title);
		if (votesByIp != null) {
			return votesByIp.get(user.toString()); // TODO
		}
		return null;
	}

	public void clear() {
		votes.clear();
		persistenceService.saveOnThread(votes);
	}
}
