package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.Vote;

public interface VotesRepository extends CrudRepository<Vote, Long> {

}
