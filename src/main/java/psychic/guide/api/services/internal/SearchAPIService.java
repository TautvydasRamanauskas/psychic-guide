package psychic.guide.api.services.internal;


import psychic.guide.api.services.internal.model.SearchResult;

import java.util.List;

public interface SearchAPIService {
	List<SearchResult> search(String keyword);
}
