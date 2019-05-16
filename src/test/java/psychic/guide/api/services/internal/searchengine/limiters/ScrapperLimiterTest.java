package psychic.guide.api.services.internal.searchengine.limiters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScrapperLimiterTest {
	private Limiter limiter;

	@Before
	public void setUp() {
		limiter = new ScrapperLimiter();
	}

	@Test
	public void getScore() {
		int score = limiter.getScore();
		assertEquals(ScrapperLimiter.SCORE_AVAILABLE, score);
	}

	@Test
	public void useLimit() {
		int scoreBefore = limiter.getScore();
		assertEquals(ScrapperLimiter.SCORE_AVAILABLE, scoreBefore);

		limiter.useLimit();

		int scoreAfter = limiter.getScore();
		assertEquals(ScrapperLimiter.SCORE_UNAVAILABLE, scoreAfter);

	}
}