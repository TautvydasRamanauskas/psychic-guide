package psychic.guide.api.services.internal;

public interface PersistenceService<T> {
	void save(T object);

	void saveOnThread(T object);

	T read();

	T readOrDefault(T defaultResult);
}
