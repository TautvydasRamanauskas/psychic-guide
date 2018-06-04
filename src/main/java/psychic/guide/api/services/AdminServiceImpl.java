package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psychic.guide.api.model.Limits;
import psychic.guide.api.model.Search;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.CacheStatistic;
import psychic.guide.api.model.data.UserIdLevel;
import psychic.guide.api.repository.*;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminServiceImpl implements AdminService {
	private final LoadBalancer loadBalancer;
	private final UserRepository userRepository;
	private final SearchesRepository searchesRepository;
	private final ResultsRepository resultsRepository;
	private final LinksRepository linksRepository;
	private final BookmarksRepository bookmarksRepository;

	@Autowired
	public AdminServiceImpl(LoadBalancer loadBalancer,
							UserRepository userRepository,
							SearchesRepository searchesRepository,
							ResultsRepository resultsRepository,
							LinksRepository linksRepository,
							BookmarksRepository bookmarksRepository) {
		this.loadBalancer = loadBalancer;
		this.userRepository = userRepository;
		this.searchesRepository = searchesRepository;
		this.resultsRepository = resultsRepository;
		this.linksRepository = linksRepository;
		this.bookmarksRepository = bookmarksRepository;
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

	@Override
	public long linksCount() {
		return linksRepository.count();
	}

	@Override
	public long bookmarksCount() {
		return bookmarksRepository.count();
	}
}
