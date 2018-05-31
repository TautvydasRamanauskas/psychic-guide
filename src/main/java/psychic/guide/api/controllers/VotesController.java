package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;
import psychic.guide.api.services.VoteService;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/vote/")
public class VotesController {
	private final VoteService voteService;
	private final Logger logger;

	@Autowired
	public VotesController(VoteService voteService) {
		this.voteService = voteService;
		this.logger = LoggerFactory.getLogger(VotesController.class);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> addVote(@RequestBody Vote vote,
										  HttpServletRequest request) {
		logger.info("Adding vote to - {}", vote.getResult().getResult());
		vote.setUser(new User().setId(Long.parseLong(request.getRemoteAddr())));
		voteService.addVote(vote);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Object> removeVote(@RequestBody Vote vote,
											 HttpServletRequest request) {
		logger.info("Removing vote from - {}", vote.getResult().getResult());
		vote.setUser(new User().setId(Long.parseLong(request.getRemoteAddr())));
		voteService.removeVote(vote);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
