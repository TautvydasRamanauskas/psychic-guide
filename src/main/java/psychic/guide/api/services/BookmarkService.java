package psychic.guide.api.services;

import psychic.guide.api.model.ResultEntry;

import java.util.List;

public interface BookmarkService {
	void addBookmark(ResultEntry entry, String ip);

	void removeBookmark(ResultEntry entry, String ip);

	boolean containsBookmark(ResultEntry entry, String ip);

	List<ResultEntry> bookmarks(String ip);
}
