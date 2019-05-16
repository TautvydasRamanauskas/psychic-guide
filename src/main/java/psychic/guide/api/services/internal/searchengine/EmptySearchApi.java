package psychic.guide.api.services.internal.searchengine;

import psychic.guide.api.services.internal.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class EmptySearchApi implements SearchApiService {
	@Override
	public List<SearchResult> search(String keyword) {
		return new ArrayList<>();
	}
}
