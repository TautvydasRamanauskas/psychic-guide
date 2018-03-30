package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.services.BookmarkService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static psychic.guide.api.Authenticator.authenticate;

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
	public ResponseEntity<Object> addBookmark(@RequestBody Map<String, Object> body,
											  HttpServletRequest request) {
		if (!authenticate(body.get("key").toString())) {
			logger.info("Failed to authenticate");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String title = (String) body.get("title");
		if (title == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("Adding bookmark for title - {}", title);
		String remoteAddress = request.getRemoteAddr();
		bookmarkService.addBookmark(title, remoteAddress);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> removeBookmark(@RequestBody Map<String, Object> body,
												 HttpServletRequest request) {
		if (!authenticate(body.get("key").toString())) {
			logger.info("Failed to authenticate");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String title = (String) body.get("title");
		if (title == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("Removing bookmark for title - {}", title);
		String remoteAddress = request.getRemoteAddr();
		bookmarkService.removeBookmark(title, remoteAddress);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
