package psychic.guide.api.services.internal.searchengine;


import psychic.guide.api.services.internal.model.SearchResult;

import java.util.List;

public interface SearchAPIService {
	List<SearchResult> search(String keyword);
}