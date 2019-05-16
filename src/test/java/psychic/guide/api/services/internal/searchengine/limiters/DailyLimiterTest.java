package psychic.guide.api.services.internal.searchengine.limiters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DailyLimiterTest {
	private static final int LIMIT = 66;
	private Limiter limiter;

	@Before
	public void setUp() {
		limiter = new DailyLimiter(LIMIT);
	}

	@Test
	public void getScore() {
		int score = limiter.getScore();
		assertEquals(LIMIT, score);
	}

	@Test
	public void useLimit() {
		int scoreBefore = limiter.getScore();
		assertEquals(LIMIT, scoreBefore);

		limiter.useLimit();

		int scoreAfter = limiter.getScore();
		assertEquals(LIMIT - 1, scoreAfter);
	}
}