package psychic.guide.api.services;

import psychic.guide.api.services.internal.PersistenceService;

class TestPersistenceService<T> implements PersistenceService<T> {
	private final T data;

	TestPersistenceService(T data) {
		this.data = data;
	}

	@Override
	public T readOrDefault(T defaultResult) {
		return data;
	}

	@Override
	public T read() {
		return data;
	}

	@Override
	public void save(T object) {

	}

	@Override
	public void saveOnThread(T object) {

	}

	public T getData() {
		return data;
	}
}
