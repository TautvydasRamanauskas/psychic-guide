package psychic.guide.api.services;

import psychic.guide.api.model.Link;
import psychic.guide.api.model.User;
import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

public interface LinkService {
	List<ResultEntry> get(String link, User user);

	Link generate(List<ResultEntry> results);
}
