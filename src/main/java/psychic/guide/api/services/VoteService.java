package psychic.guide.api.services;

import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;

public interface VoteService {
	int calculateVoteValue(Result result);

	void addVote(Vote vote);

	void removeVote(Vote vote);

	Vote getVote(Result result, User user);
}
