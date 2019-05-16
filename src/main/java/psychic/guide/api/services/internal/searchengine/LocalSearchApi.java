package psychic.guide.api.services.internal.searchengine;

import psychic.guide.api.services.internal.model.SearchResult;

import java.util.List;

public class LocalSearchApi implements SearchApiService {
	@Override
	public List<SearchResult> search(String keyword) {
		return List.of(new SearchResult("./data/source-local-1"));
	}
}
