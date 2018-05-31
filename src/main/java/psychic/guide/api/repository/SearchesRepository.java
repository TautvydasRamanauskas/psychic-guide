package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.Search;

public interface SearchesRepository extends CrudRepository<Search, Long> {

}
