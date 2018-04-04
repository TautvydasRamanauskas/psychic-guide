package psychic.guide;

import psychic.guide.api.services.BookmarkServiceImpl;
import psychic.guide.api.services.LinkServiceImpl;
import psychic.guide.api.services.VoteServiceImpl;

public class DatabaseWipe {
	public static void main(String[] args) {
		BookmarkServiceImpl bookmarkService = new BookmarkServiceImpl(null);
		bookmarkService.clear();

		LinkServiceImpl linkService = new LinkServiceImpl(null);
		linkService.clear();

		VoteServiceImpl voteService = new VoteServiceImpl();
		voteService.clear();
	}
}
