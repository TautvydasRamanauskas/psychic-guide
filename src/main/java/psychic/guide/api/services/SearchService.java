package psychic.guide.api.services;


import psychic.guide.api.model.SearchResult;

import java.util.List;

public interface SearchService {
	List<SearchResult> search(String keyword);
}
