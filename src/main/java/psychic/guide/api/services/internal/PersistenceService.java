package psychic.guide.api.services.internal;

public interface PersistenceService <T> {
	void save(T object);

	T read();

	T readOrDefault(T defaultResult);
}
