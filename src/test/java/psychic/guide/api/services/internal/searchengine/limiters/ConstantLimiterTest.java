package psychic.guide.api.services.internal.searchengine.limiters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantLimiterTest {
	private static final int LIMIT = 77;

	private Limiter limiter;

	@Before
	public void setUp() {
		limiter = new ConstantLimiter(LIMIT);
	}

	@Test
	public void getScore() {
		int score = limiter.getScore();
		assertEquals(LIMIT, score);
	}

	@Test
	public void useLimit() {
		int scoreBefore = limiter.getScore();
		limiter.useLimit();
		int scoreAfter = limiter.getScore();
		assertEquals(scoreBefore, scoreAfter);
	}
}