package psychic.guide.api.services;

import psychic.guide.api.model.Vote;

public interface VoteService {
	int calculateVoteValue(String title);

	void addVote(Vote vote);

	void removeVote(Vote vote);

	Vote getVote(String title, String ip);
}
