package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.ResultEntry;
import psychic.guide.api.services.LinkService;

import java.util.List;
import java.util.UUID;

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
	@RequestMapping(path = "{link}", method = RequestMethod.GET)
	public ResponseEntity<Object> search(@PathVariable("link") String link) {
		logger.info("Retrieving link- {}", link);
		List<ResultEntry> searchResult = linkService.get(UUID.fromString(link));
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}
}
