package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import psychic.guide.api.model.Result;

import java.util.List;

public interface ResultsRepository extends CrudRepository<Result, Long> {
	List<Result> findResultsByKeyword(@Param("keyword") String keyword);

	Result findResultByResultAndKeyword(@Param("result") String result, @Param("keyword") String keyword);

	long countByKeyword(@Param("keyword") String keyword);

	@Transactional
	void removeByKeyword(@Param("keyword") String keyword);
}
