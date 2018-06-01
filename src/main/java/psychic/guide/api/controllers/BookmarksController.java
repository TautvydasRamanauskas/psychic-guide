package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.BookmarkService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/bookmark")
public class BookmarksController {
	private final BookmarkService bookmarkService;
	private final Logger logger;

	@Autowired
	public BookmarksController(BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
		this.logger = LoggerFactory.getLogger(BookmarksController.class);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> addBookmark(@RequestBody PersonalResultEntry personalResultEntry) {
		ResultEntry entry = personalResultEntry.getEntry();
		User user = personalResultEntry.getUser();
		logger.info("Adding bookmark for title - {}", entry.getResult());
		bookmarkService.addBookmark(entry, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> removeBookmark(@RequestBody PersonalResultEntry personalResultEntry) {
		ResultEntry entry = personalResultEntry.getEntry();
		User user = personalResultEntry.getUser();
		logger.info("Removing bookmark for title - {}", entry.getResult());
		bookmarkService.removeBookmark(entry, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	public ResponseEntity<List<ResultEntry>> search(@PathVariable("userId") long userId) {
		logger.info("Retrieving bookmarks for user - {}", userId);
		List<ResultEntry> searchResult = bookmarkService.bookmarks(userId);
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

	private static class PersonalResultEntry {
		private ResultEntry entry;
		private User user;

		public ResultEntry getEntry() {
			return entry;
		}

		public void setEntry(ResultEntry entry) {
			this.entry = entry;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
	}
}
