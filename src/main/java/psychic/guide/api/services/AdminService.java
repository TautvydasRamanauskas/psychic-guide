package psychic.guide.api.services;

import psychic.guide.api.model.Limits;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.CacheStatistic;
import psychic.guide.api.model.data.UserIdLevel;

import java.util.List;

public interface AdminService {
	Limits limits();

	List<User> users();

	List<CacheStatistic> searches();

	void level(UserIdLevel userIdLevel);

	void cleanCache(String keyword);

	long linksCount();

	void deleteLinks();

	long bookmarksCount();

	void deleteBookmarks();
}
