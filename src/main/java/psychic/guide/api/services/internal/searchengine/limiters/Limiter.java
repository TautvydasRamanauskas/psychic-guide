package psychic.guide.api.services.internal.searchengine.limiters;

public interface Limiter {
	int getScore();

	void useLimit();
}
