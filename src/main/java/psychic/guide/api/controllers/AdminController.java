package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.Limits;
import psychic.guide.api.services.AdminService;

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
}
