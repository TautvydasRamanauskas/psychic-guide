package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psychic.guide.api.model.Limits;
import psychic.guide.api.model.User;
import psychic.guide.api.repository.UserRepository;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

import java.util.List;

@Component
public class AdminServiceImpl implements AdminService {
	private final LoadBalancer loadBalancer;
	private final UserRepository userRepository;

	@Autowired
	public AdminServiceImpl(LoadBalancer loadBalancer, UserRepository userRepository) {
		this.loadBalancer = loadBalancer;
		this.userRepository = userRepository;
	}

	@Override
	public Limits limits() {
		return loadBalancer.getLimits();
	}

	@Override
	public List<User> users() {
		return (List<User>) userRepository.findAll();
	}
}
