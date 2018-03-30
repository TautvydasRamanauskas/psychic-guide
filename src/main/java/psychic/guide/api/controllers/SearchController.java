package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.ResultEntry;
import psychic.guide.api.services.SearchService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static psychic.guide.api.Authenticator.authenticate;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/search")
public class SearchController {
	private final SearchService searchService;
	private final Logger logger;

	@Autowired
	public SearchController(SearchService searchService) {
		this.searchService = searchService;
		this.logger = LoggerFactory.getLogger(SearchController.class);
	}

	@ResponseBody
	@RequestMapping(path = "/{searchKeyword}", method = RequestMethod.POST)
	public ResponseEntity<Object> search(@PathVariable("searchKeyword") String searchKeyword,
										 @RequestBody Map<String, Object> body,
										 HttpServletRequest request) {
		if (!authenticate(body.get("key").toString())) {
			logger.info("Failed to authenticate");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		logger.info("Searching for keyword - {}", searchKeyword);
		String remoteAddress = request.getRemoteAddr();
		List<ResultEntry> searchResult = searchService.search(searchKeyword, remoteAddress);
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}
}
