package psychic.guide.api.services;


import psychic.guide.api.ResultEntry;

import java.util.List;

public interface SearchService {
	List<ResultEntry> search(String keyword);
}
