package psychic.guide.api.services;

import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

public interface SearchService {
	List<ResultEntry> search(String keyword, User user);

	List<String> mostPopular();
}
