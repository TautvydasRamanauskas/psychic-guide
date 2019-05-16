package psychic.guide.api.services.internal.searchengine.limiters;

public class ConstantLimiter implements Limiter {
	private final int limit;

	public ConstantLimiter(int limit) {
		this.limit = limit;
	}

	@Override
	public int getScore() {
		return limit;
	}

	@Override
	public void useLimit() {

	}
}
