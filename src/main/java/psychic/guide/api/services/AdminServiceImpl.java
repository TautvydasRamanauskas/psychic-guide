package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psychic.guide.api.model.Limits;
import psychic.guide.api.model.Search;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.CacheStatistic;
import psychic.guide.api.model.data.UserIdLevel;
import psychic.guide.api.repository.ResultsRepository;
import psychic.guide.api.repository.SearchesRepository;
import psychic.guide.api.repository.UserRepository;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminServiceImpl implements AdminService {
	private final LoadBalancer loadBalancer;
	private final UserRepository userRepository;
	private final SearchesRepository searchesRepository;
	private final ResultsRepository resultsRepository;

	@Autowired
	public AdminServiceImpl(LoadBalancer loadBalancer,
							UserRepository userRepository,
							SearchesRepository searchesRepository,
							ResultsRepository resultsRepository) {
		this.loadBalancer = loadBalancer;
		this.userRepository = userRepository;
		this.searchesRepository = searchesRepository;
		this.resultsRepository = resultsRepository;
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
	public List<CacheStatistic> searches() {
		List<Search> searches = (List<Search>) searchesRepository.findAll();
		return searches.stream()
				.map(s -> new CacheStatistic(s, resultsRepository.countByKeyword(s.getKeyword())))
				.sorted()
				.collect(Collectors.toList());
	}

	@Override
	public void level(UserIdLevel userIdLevel) {
		User user = userRepository.findOne(userIdLevel.getUserId());
		user.setLevel(userIdLevel.getLevel());
		userRepository.save(user);
	}

	@Override
	public void cleanCache(String keyword) {
		resultsRepository.removeByKeyword(keyword);
	}
}
