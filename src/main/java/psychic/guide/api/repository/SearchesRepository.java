package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import psychic.guide.api.model.Search;

public interface SearchesRepository extends CrudRepository<Search, Long> {
	Search findByKeyword(@Param("keyword") String keyword);
}
