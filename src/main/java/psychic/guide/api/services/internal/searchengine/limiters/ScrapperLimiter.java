package psychic.guide.api.services.internal.searchengine.limiters;

import java.time.Duration;
import java.time.LocalDateTime;

public class ScrapperLimiter implements Limiter {
	public static final int SCORE_AVAILABLE = 1000;
	public static final int SCORE_UNAVAILABLE = 0;

	private static final int SCRAPING_INTERVAL_SECONDS = 60;
	private LocalDateTime lastScrape = LocalDateTime.MIN;

	@Override
	public int getScore() {
		long secondsPassed = Duration.between(lastScrape, LocalDateTime.now()).toSeconds();
		return secondsPassed > SCRAPING_INTERVAL_SECONDS ? SCORE_AVAILABLE : SCORE_UNAVAILABLE;
	}

	@Override
	public void useLimit() {
		lastScrape = LocalDateTime.now();
	}
}
