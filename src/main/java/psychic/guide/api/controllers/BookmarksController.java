package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.services.BookmarkService;

import javax.servlet.http.HttpServletRequest;
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
	public ResponseEntity<Object> addBookmark(@RequestBody ResultEntry entry,
											  HttpServletRequest request) {
		logger.info("Adding bookmark for title - {}", entry.getResult());
		String remoteAddress = request.getRemoteAddr();
		bookmarkService.addBookmark(entry, remoteAddress);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> removeBookmark(@RequestBody ResultEntry entry,
												 HttpServletRequest request) {
		logger.info("Removing bookmark for title - {}", entry.getResult());
		String remoteAddress = request.getRemoteAddr();
		bookmarkService.removeBookmark(entry, remoteAddress);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Object> search(HttpServletRequest request) {
		String remoteAddress = request.getRemoteAddr();
		logger.info("Retrieving bookmarks for ip - {}", remoteAddress);
		List<ResultEntry> searchResult = bookmarkService.bookmarks(remoteAddress);
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}
}
