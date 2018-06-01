package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import psychic.guide.api.model.Result;

public interface ResultsRepository extends CrudRepository<Result, Long> {
	Iterable<Result> findResultsByKeyword(@Param("keyword") String keyword);

	Result findResultByResultAndKeyword(@Param("result") String result, @Param("keyword") String keyword);
}
