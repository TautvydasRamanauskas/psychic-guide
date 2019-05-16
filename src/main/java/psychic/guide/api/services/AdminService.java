package psychic.guide.api.services;

import psychic.guide.api.model.User;
import psychic.guide.api.model.data.CacheStatistic;
import psychic.guide.api.model.data.UserIdLevel;

import java.util.List;
import java.util.Map;

public interface AdminService {
	Map<String, Integer> limits();

	List<User> users();

	List<CacheStatistic> searches();

	void level(UserIdLevel userIdLevel);

	void cleanCache(String keyword);

	long linksCount();

	void deleteLinks();

	long bookmarksCount();

	void deleteBookmarks();
}
