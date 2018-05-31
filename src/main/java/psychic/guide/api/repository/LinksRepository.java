package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.Link;

public interface LinksRepository extends CrudRepository<Link, Long> {

}
