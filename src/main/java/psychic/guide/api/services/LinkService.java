package psychic.guide.api.services;

import psychic.guide.api.ResultEntry;

import java.util.List;
import java.util.UUID;

public interface LinkService {
	List<ResultEntry> get(UUID link);
}
