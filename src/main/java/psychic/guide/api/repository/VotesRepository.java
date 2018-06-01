package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;
import psychic.guide.api.model.Vote;

import java.util.List;

public interface VotesRepository extends CrudRepository<Vote, Long> {
	List<Vote> getVotesByResult(@Param("resultId") Result result);

	Vote getVoteByResultAndUser(@Param("resultId") Result result, @Param("userId") User user);

	@Transactional
	void deleteVoteByResultAndUser(@Param("resultId") Result result, @Param("userId") User user);
}
