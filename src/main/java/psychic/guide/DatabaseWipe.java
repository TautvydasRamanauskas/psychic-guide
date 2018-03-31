package psychic.guide;

import psychic.guide.api.services.BookmarkServiceImpl;
import psychic.guide.api.services.LinkServiceImpl;

public class DatabaseWipe {
	public static void main(String[] args) {
		BookmarkServiceImpl bookmarkService = new BookmarkServiceImpl();
		bookmarkService.clear();

		LinkServiceImpl linkService = new LinkServiceImpl();
		linkService.clear();
	}
}
