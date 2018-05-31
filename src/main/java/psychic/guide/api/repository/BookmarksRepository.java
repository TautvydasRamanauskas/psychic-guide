package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.Bookmark;

public interface BookmarksRepository extends CrudRepository<Bookmark, Long> {

}
