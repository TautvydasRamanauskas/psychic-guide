package psychic.guide.api.services;

import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

public interface SearchService {
	List<ResultEntry> search(String keyword, String ip);

	List<String> mostPopular();
}
