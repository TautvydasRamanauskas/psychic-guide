package psychic.guide.api.services.internal.searcher;

import psychic.guide.api.model.data.ResultEntry;

import java.util.List;

public interface Searcher {
    List<ResultEntry> search(String keyword);
}
