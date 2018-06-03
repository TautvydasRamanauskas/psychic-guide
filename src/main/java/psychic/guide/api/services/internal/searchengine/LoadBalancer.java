package psychic.guide.api.services.internal.searchengine;

import psychic.guide.api.model.Limits;

public interface LoadBalancer extends SearchAPIService {
	Limits getLimits();
}
