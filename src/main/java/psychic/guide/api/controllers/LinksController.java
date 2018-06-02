package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.Link;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.LinkService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/link/")
public class LinksController {
	private final LinkService linkService;
	private final Logger logger;

	@Autowired
	public LinksController(LinkService linkService) {
		this.linkService = linkService;
		this.logger = LoggerFactory.getLogger(LinksController.class);
	}

	@ResponseBody
	@RequestMapping(path = "{link}", method = RequestMethod.POST)
	public ResponseEntity<List<ResultEntry>> open(@PathVariable("link") String link, @RequestBody User user) {
		logger.info("Retrieving link- {}", link);
		List<ResultEntry> searchResult = linkService.get(link, user);
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Link> create(@RequestBody List<ResultEntry> results) {
		Link link = linkService.generate(results);
		logger.info("Generating link- {}", link);
		return new ResponseEntity<>(link, HttpStatus.OK);
	}
}
