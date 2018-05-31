package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.SearchService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
	public ResponseEntity<List<ResultEntry>> search(@PathVariable("searchKeyword") String searchKeyword,
											   HttpServletRequest request) {
		logger.info("Searching for keyword - {}", searchKeyword);
		String remoteAddress = request.getRemoteAddr();
		List<ResultEntry> searchResult = searchService.search(searchKeyword, remoteAddress);
		logger.info("Returning {} results", searchResult.size());
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/popular/", method = RequestMethod.GET)
	public ResponseEntity<List<String>> popular() {
		logger.info("Retrieving most popular searches");
		List<String> mostPopularSearches = searchService.mostPopular();
		return new ResponseEntity<>(mostPopularSearches, HttpStatus.OK);
	}
}
