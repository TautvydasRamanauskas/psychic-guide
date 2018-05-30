package psychic.guide.api.services.internal.repository;

import org.springframework.data.repository.CrudRepository;
import psychic.guide.api.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
