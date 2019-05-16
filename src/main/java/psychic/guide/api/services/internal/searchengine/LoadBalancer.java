package psychic.guide.api.services.internal.searchengine;

import java.util.Map;

public interface LoadBalancer extends SearchApiService {
	Map<String, Integer> getLimits();
}
