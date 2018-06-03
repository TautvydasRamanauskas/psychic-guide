package psychic.guide.api;

import psychic.guide.api.model.Reference;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.services.BookmarkService;
import psychic.guide.api.services.VoteService;

import java.util.List;
import java.util.stream.Collectors;

public class ResultsConverter {
	public static List<Result> entriesToResults(List<ResultEntry> results,
										  ResultsRepository resultsRepository) {
		return results.stream()
				.map(ResultEntry::getId)
				.map(resultsRepository::findOne)
				.collect(Collectors.toList());
	}

	public static List<ResultEntry> resultsToEntries(List<Result> results,
											   User user,
											   VoteService voteService,
											   BookmarkService bookmarkService,
											   ReferenceRepository referenceRepository) {
		return results.stream()
				.map(r -> {
					ResultEntry entry = new ResultEntry();
					entry.setId(r.getId());
					entry.setResult(r.getResult());
					entry.setPersonalVote(getVote(user, r, voteService));
					entry.setVoteValue(voteService.calculateVoteValue(r));
					entry.setBookmark(bookmarkService.containsBookmark(entry, user));
					entry.setReferences(referenceRepository.findReferencesByResult(r).stream().map(Reference::getUrl).collect(Collectors.toSet()));
					return entry;
				})
				.collect(Collectors.toList());
	}

	private static int getVote(User user, Result result, VoteService voteService) {
		Vote vote = voteService.getVote(result, user);
		return vote == null ? 0 : vote.getValue();
	}
}
