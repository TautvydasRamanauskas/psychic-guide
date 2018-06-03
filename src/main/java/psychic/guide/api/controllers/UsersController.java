package psychic.guide.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import psychic.guide.api.model.Options;
import psychic.guide.api.model.User;
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
	public ResponseEntity<Object> createUser(@RequestBody UserCredentials userCredentials) {
		TokenValidator tokenValidator = new FacebookTokenValidator();
		long userId = Long.valueOf(userCredentials.getUserId());
		boolean valid = tokenValidator.validate(userCredentials.getToken(), userId);
		if (valid) {
			logger.info("User successfully authenticated");
			User user = getUser(userId);
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

	private User getUser(long facebookId) {
		return ((List<User>) userRepository.findAll()).stream()
				.filter(u -> facebookId == u.getFacebookId())
				.findAny()
				.orElseGet(() -> createUser(facebookId));
	}

	private User createUser(long facebookId) {
		User newUser = new User().setFacebookId(facebookId);
		userRepository.save(newUser);
		return newUser;
	}

	private static class UserCredentials {
		private String userId;
		private String token;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}
