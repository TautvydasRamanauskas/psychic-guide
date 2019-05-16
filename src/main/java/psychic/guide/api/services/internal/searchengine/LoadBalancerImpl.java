package psychic.guide.api.services.internal.searchengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import psychic.guide.api.services.internal.model.SearchResult;
import psychic.guide.api.services.internal.searchengine.limiters.Limiter;

import java.util.*;
import java.util.stream.Collectors;

import static psychic.guide.api.services.internal.Globals.USE_AZURE_DEMO;
import static psychic.guide.api.services.internal.Globals.USE_LOCAL_SEARCH_API;

@Component
public class LoadBalancerImpl implements LoadBalancer {
	private final Logger logger;

	public LoadBalancerImpl() {
		this.logger = LoggerFactory.getLogger(LoadBalancer.class);
	}

	@Override
	public List<SearchResult> search(String keyword) {
		if (USE_LOCAL_SEARCH_API) {
			return SearchApi.LOCAL.getService().search(keyword);
		}
		if (USE_AZURE_DEMO) {
			List<SearchResult> results = SearchApi.AZURE_DEMO.getService().search(keyword);
			Collections.shuffle(results);
			return results;
		}
//		if (USE_GOOGLE_SCRAPE) {
//			return SearchApi.GOOGLE_SCRAPE.getService().search(keyword);
//		}

		SearchApi searchApi = selectSearchApi();
		Limiter limiter = searchApi.getLimiter();
		SearchApiService service = searchApi.getService();
		logger.info("Using {} as search engine", service.getClass().getSimpleName());
		limiter.useLimit();
		return service.search(keyword);
	}

	@Override
	public Map<String, Integer> getLimits() {
		return Arrays.stream(SearchApi.values())
				.collect(Collectors.toMap(SearchApi::getDisplayName, v -> v.getLimiter().getScore()));
	}

	private SearchApi selectSearchApi() {
		return Arrays.stream(SearchApi.values())
				.max(Comparator.comparing(v -> v.getLimiter().getScore()))
				.orElse(SearchApi.EMPTY);
	}
}
