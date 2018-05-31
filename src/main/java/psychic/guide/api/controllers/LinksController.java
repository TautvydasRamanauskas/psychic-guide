package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.data.ResultEntry;
import psychic.guide.api.services.LinkService;

import javax.servlet.http.HttpServletRequest;
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
	public ResponseEntity<List<ResultEntry>> open(@PathVariable("link") String link,
											 HttpServletRequest request) {
		logger.info("Retrieving link- {}", link);
		String remoteAddress = request.getRemoteAddr();
		List<ResultEntry> searchResult = linkService.get(UUID.fromString(link), remoteAddress);
		return new ResponseEntity<>(searchResult, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<UUID> search(@RequestBody List<ResultEntry> results) {
		UUID link = linkService.generate(results);
		logger.info("Generating link- {}", link);
		return new ResponseEntity<>(link, HttpStatus.OK);
	}
}
