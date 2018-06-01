package psychic.guide.api.services;

import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

public interface BookmarkService {
	void addBookmark(ResultEntry entry, User user);

	void removeBookmark(ResultEntry entry, User user);

	boolean containsBookmark(ResultEntry entry, User user);

	List<ResultEntry> bookmarks(long userId);
}
