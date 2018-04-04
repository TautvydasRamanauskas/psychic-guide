package psychic.guide.api.services;

import psychic.guide.api.model.ResultEntry;

import java.util.List;
import java.util.UUID;

public interface LinkService {
	List<ResultEntry> get(UUID link);

	UUID generate(List<ResultEntry> results);
}
