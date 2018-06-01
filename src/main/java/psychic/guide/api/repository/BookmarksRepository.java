package psychic.guide.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import psychic.guide.api.model.Bookmark;
import psychic.guide.api.model.Result;
import psychic.guide.api.model.User;

public interface BookmarksRepository extends CrudRepository<Bookmark, Long> {
	@Transactional
	void deleteBookmarkByResultAndUser(@Param("result") Result result, @Param("user") User user);

	Bookmark findBookmarkByResultAndUser(@Param("result") Result result, @Param("user") User user);

	Iterable<Bookmark> getBookmarksByUser(@Param("user") User user);
}
