package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.Reference;

public interface ReferenceRepository extends CrudRepository<Reference, Long> {

}
