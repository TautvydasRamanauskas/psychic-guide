package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.Result;

public interface ResultsRepository extends CrudRepository<Result, Long> {

}
