package psychic.guide.api.services.internal;


import java.util.List;

public interface SearchAPIService {
	List<SearchResult> search(String keyword);
}
