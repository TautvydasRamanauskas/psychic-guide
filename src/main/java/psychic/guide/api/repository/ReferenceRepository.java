package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import psychic.guide.api.model.Reference;
import psychic.guide.api.model.Result;

public interface ReferenceRepository extends CrudRepository<Reference, Long> {
	Reference findReferenceByUrlAndResult(@Param("url") String url, @Param("result") Result result);

	Iterable<Reference> findReferencesByResult(@Param("result") Result result);
}
