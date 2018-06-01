package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.*;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.repository.BookmarksRepository;
import psychic.guide.api.repository.ReferenceRepository;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	private final VoteService voteService;
	private final BookmarksRepository bookmarksRepository;
	private final ResultsRepository resultsRepository;
	private final ReferenceRepository referenceRepository;
	private final UserRepository userRepository;

	@Autowired
	public BookmarkServiceImpl(VoteService voteService, BookmarksRepository bookmarksRepository,
							   ResultsRepository resultsRepository, ReferenceRepository referenceRepository,
							   UserRepository userRepository) {
		this.voteService = voteService;
		this.bookmarksRepository = bookmarksRepository;
		this.resultsRepository = resultsRepository;
		this.referenceRepository = referenceRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void addBookmark(ResultEntry entry, User user) {
		Result result = resultsRepository.findOne(entry.getId());
		bookmarksRepository.save(new Bookmark().setResult(result).setUser(user));
	}

	@Override
	public void removeBookmark(ResultEntry entry, User user) {
		Result result = resultsRepository.findOne(entry.getId());
		bookmarksRepository.deleteBookmarkByResultAndUser(result, user);
	}

	@Override
	public boolean containsBookmark(ResultEntry entry, User user) {
		Result result = resultsRepository.findOne(entry.getId());
		Bookmark bookmark = bookmarksRepository.findBookmarkByResultAndUser(result, user);
		return bookmark != null;
	}

	@Override
	public List<ResultEntry> bookmarks(long userId) {
		User user = userRepository.findOne(userId);
		Iterable<Bookmark> bookmarks = bookmarksRepository.getBookmarksByUser(user);
		List<ResultEntry> results = new ArrayList<>();
		for (Bookmark bookmark : bookmarks) {
			Result result = bookmark.getResult();

			ResultEntry resultEntry = new ResultEntry();
			resultEntry.setBookmark(true);
			resultEntry.setReferences(iterableToSet(result));
			resultEntry.setId(result.getId());
			resultEntry.setCount(result.getRating());
			resultEntry.setResult(result.getResult());
			resultEntry.setVoteValue(voteService.calculateVoteValue(result.getResult()));
			Vote vote = voteService.getVote(result.getResult(), user);
			resultEntry.setPersonalVote(vote == null ? 0 : vote.getValue());
			results.add(resultEntry);
		}
		return results.stream().sorted().collect(Collectors.toList());
	}

	private Set<String> iterableToSet(Result result) {
		Set<String> set = new HashSet<>();
		Iterable<Reference> references = referenceRepository.findReferencesByResult(result);
		for (Reference reference : references) {
			set.add(reference.getUrl());
		}
		return set;
	}
}
