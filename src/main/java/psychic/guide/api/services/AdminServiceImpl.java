package psychic.guide.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import psychic.guide.api.model.Limits;
import psychic.guide.api.services.internal.searchengine.LoadBalancer;

@Component
public class AdminServiceImpl implements AdminService {
	private final LoadBalancer loadBalancer;

	@Autowired
	public AdminServiceImpl(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	@Override
	public Limits limits() {
		return loadBalancer.getLimits();
	}
}
