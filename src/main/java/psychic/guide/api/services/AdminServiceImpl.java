package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psychic.guide.api.model.Limits;
import psychic.guide.api.model.Search;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.UserIdLevel;
import psychic.guide.api.repository.SearchesRepository;
import psychic.guide.api.repository.UserRepository;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.util.List;

@Component
public class AdminServiceImpl implements AdminService {
	private final LoadBalancer loadBalancer;
	private final UserRepository userRepository;
	private final SearchesRepository searchesRepository;

	@Autowired
	public AdminServiceImpl(LoadBalancer loadBalancer,
							UserRepository userRepository,
							SearchesRepository searchesRepository) {
		this.loadBalancer = loadBalancer;
		this.userRepository = userRepository;
		this.searchesRepository = searchesRepository;
	}

	@Override
	public Limits limits() {
		return loadBalancer.getLimits();
	}

	@Override
	public List<User> users() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public List<Search> searches() {
		return (List<Search>) searchesRepository.findAll();
	}

	@Override
	public void level(UserIdLevel userIdLevel) {
		User user = userRepository.findOne(userIdLevel.getUserId());
		user.setLevel(userIdLevel.getLevel());
		userRepository.save(user);
	}
}
