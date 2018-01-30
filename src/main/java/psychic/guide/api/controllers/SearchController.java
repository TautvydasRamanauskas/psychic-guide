package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.services.SearchService;

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

    @RequestMapping(path = "/{searchKeyword}", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@PathVariable("searchKeyword") String searchKeyword) {
        logger.info("Searching for keyword - {}", searchKeyword);
        Object searchResult = searchService.search(searchKeyword);
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }
}
