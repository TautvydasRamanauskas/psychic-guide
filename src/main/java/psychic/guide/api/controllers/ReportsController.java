package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.ResultEntry;
import psychic.guide.api.services.ReportService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/report/")
public class ReportsController {
	private final ReportService reportService;
	private final Logger logger;

	@Autowired
	public ReportsController(ReportService reportService) {
		this.reportService = reportService;
		this.logger = LoggerFactory.getLogger(ReportsController.class);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> generate(@RequestBody List<ResultEntry> entries) {
		logger.info("Generating report of {} entries", entries.size());
		String doc = reportService.generateDoc(entries);
		return new ResponseEntity<>(doc, HttpStatus.OK);
	}
}
