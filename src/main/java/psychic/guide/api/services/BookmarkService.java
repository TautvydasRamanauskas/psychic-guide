package psychic.guide.api.services;

public interface BookmarkService {
	void addBookmark(String text, String ip);

	void removeBookmark(String text, String ip);

	boolean containsBookmark(String text, String ip);
}
