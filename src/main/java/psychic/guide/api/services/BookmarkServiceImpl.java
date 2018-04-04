package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.internal.PersistenceSerializationService;
import psychic.guide.api.services.internal.PersistenceService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {
	private static final String FILE_NAME = "bookmarks";

	private final VoteService voteService;
	private final PersistenceService<Map<String, Collection<ResultEntry>>> persistenceService;
	private final Map<String, Collection<ResultEntry>> bookmarksByIp;

	@Autowired
	public BookmarkServiceImpl(VoteService voteService) {
		this.voteService = voteService;
		this.persistenceService = new PersistenceSerializationService<>(FILE_NAME);
		this.bookmarksByIp = persistenceService.read();
	}

	@Override
	public void addBookmark(ResultEntry entry, String ip) {
		entry.setBookmark(true);
		Collection<ResultEntry> bookmarks = bookmarksByIp.computeIfAbsent(ip, k -> new HashSet<>());
		bookmarks.add(entry);
		persistenceService.save(bookmarksByIp);
	}

	@Override
	public void removeBookmark(ResultEntry entry, String ip) {
		Collection<ResultEntry> bookmarks = bookmarksByIp.get(ip);
		if (bookmarks != null) {
			bookmarks.remove(entry);
		}
		persistenceService.save(bookmarksByIp);
	}

	@Override
	public boolean containsBookmark(ResultEntry entry, String ip) {
		Collection<ResultEntry> bookmarks = bookmarksByIp.get(ip);
		return bookmarks != null && bookmarks.contains(entry);
	}

	@Override
	public List<ResultEntry> bookmarks(String ip) {
		Collection<ResultEntry> bookmarks = bookmarksByIp.getOrDefault(ip, new HashSet<>());
		for (ResultEntry bookmark : bookmarks) {
			bookmark.setVoteValue(voteService.calculateVoteValue(bookmark.getResult()));
			Vote vote = voteService.getVote(bookmark.getResult(), ip);
			bookmark.setPersonalVote(vote == null ? 0 : vote.getValue());
		}
		return bookmarks.stream().sorted().collect(Collectors.toList());
	}

	public void clear() {
		bookmarksByIp.clear();
		persistenceService.save(bookmarksByIp);
	}
}
