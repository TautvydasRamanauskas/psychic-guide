package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import psychic.guide.api.model.Link;

public interface LinksRepository extends CrudRepository<Link, Long> {
	Link getLinkByLink(@Param("link") String link);
}
