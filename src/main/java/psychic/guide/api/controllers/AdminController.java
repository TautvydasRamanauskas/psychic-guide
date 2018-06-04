package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.Limits;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.CacheStatistic;
import psychic.guide.api.model.data.UserIdLevel;
import psychic.guide.api.services.AdminService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/admin")
public class AdminController {
	private final AdminService adminService;
	private final Logger logger;

	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
		this.logger = LoggerFactory.getLogger(AdminController.class);
	}

	@ResponseBody
	@RequestMapping(path = "/limits/", method = RequestMethod.GET)
	public ResponseEntity<Limits> limits() {
		logger.info("Retrieving search engines limits");
		Limits limits = adminService.limits();
		return new ResponseEntity<>(limits, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/users/", method = RequestMethod.GET)
	public ResponseEntity<List<User>> users() {
		logger.info("Retrieving users list");
		List<User> users = adminService.users();
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/searches/", method = RequestMethod.GET)
	public ResponseEntity<List<CacheStatistic>> searches() {
		logger.info("Retrieving searches");
		List<CacheStatistic> searches = adminService.searches();
		return new ResponseEntity<>(searches, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/links/", method = RequestMethod.GET)
	public ResponseEntity<Long> getLinksCount() {
		logger.info("Retrieving links count");
		long linksCount = adminService.linksCount();
		return new ResponseEntity<>(linksCount, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/bookmarks/", method = RequestMethod.GET)
	public ResponseEntity<Long> getBookmarksCount() {
		logger.info("Retrieving bookmarks count");
		long bookmarksCount = adminService.bookmarksCount();
		return new ResponseEntity<>(bookmarksCount, HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/level/", method = RequestMethod.POST)
	public ResponseEntity<Object> changeLevel(@RequestBody UserIdLevel userIdLevel) {
		logger.info("Updating user {} level to {}", userIdLevel.getUserId(), userIdLevel.getLevel());
		adminService.level(userIdLevel);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(path = "/searches/{keyword}", method = RequestMethod.DELETE)
	public ResponseEntity<List<CacheStatistic>> cleanCache(@RequestParam("keyword") String keyword) {
		logger.info("Cleaning cache for keyword - {}", keyword);
		adminService.cleanCache(keyword);
		List<CacheStatistic> searches = adminService.searches();
		return new ResponseEntity<>(searches, HttpStatus.OK);
	}

}
