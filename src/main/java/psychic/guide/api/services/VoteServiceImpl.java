package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;
import psychic.guide.api.repository.VotesRepository;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {
	private final VotesRepository votesRepository;

	@Autowired
	public VoteServiceImpl(VotesRepository votesRepository) {
		this.votesRepository = votesRepository;
	}

	@Override
	public int calculateVoteValue(Result result) {
		List<Vote> votes = votesRepository.getVotesByResult(result);
		return votes.stream()
				.map(Vote::getValue)
				.reduce(Integer::sum)
				.orElse(0);
	}

	@Override
	public void addVote(Vote vote) {
		votesRepository.save(vote);
	}

	@Override
	public void removeVote(Vote vote) {
		votesRepository.deleteVoteByResultAndUser(vote.getResult(), vote.getUser());
	}

	@Override
	public Vote getVote(Result result, User user) {
		return votesRepository.getVoteByResultAndUser(result, user);
	}
}
