package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.FacebookResponse;
import psychic.guide.api.repository.OptionsRepository;
import psychic.guide.api.repository.UserRepository;
import psychic.guide.api.services.security.FacebookTokenValidator;
import psychic.guide.api.services.security.TokenValidator;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/user/")
public class UsersController {
	private final UserRepository userRepository;
	private final OptionsRepository optionsRepository;
	private final Logger logger;

	@Autowired
	public UsersController(UserRepository userRepository, OptionsRepository optionsRepository) {
		this.userRepository = userRepository;
		this.optionsRepository = optionsRepository;
		this.logger = LoggerFactory.getLogger(UsersController.class);
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public ResponseEntity<Object> createUser(@RequestBody FacebookResponse facebookResponse) {
		TokenValidator tokenValidator = new FacebookTokenValidator();
		long userId = Long.valueOf(facebookResponse.getUserID());
		boolean valid = tokenValidator.validate(facebookResponse.getAccessToken(), userId);
		if (valid) {
			logger.info("User successfully authenticated");
			User user = getUser(userId, facebookResponse);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/options/", method = RequestMethod.PUT)
	public ResponseEntity<Options> updateUserOptions(@RequestBody Options options) {
		logger.info("Updating options - {}", options.getId());
		optionsRepository.save(options);
		return new ResponseEntity<>(options, HttpStatus.OK);
	}

	private User getUser(long facebookId, FacebookResponse facebookResponse) {
		return ((List<User>) userRepository.findAll()).stream()
				.filter(u -> facebookId == u.getFacebookId())
				.findAny()
				.orElseGet(() -> createUser(facebookId, facebookResponse));
	}

	private User createUser(long facebookId, FacebookResponse facebookResponse) {
		User newUser = new User();
		newUser.setFacebookId(facebookId);
		newUser.setName(facebookResponse.getName());
		newUser.setEmail(facebookResponse.getEmail());
		newUser.setPicture(facebookResponse.getPicture());
		userRepository.save(newUser);
		return newUser;
	}

}
