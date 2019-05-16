package psychic.guide.api.services.internal.searchengine;

import psychic.guide.api.services.internal.searchengine.limiters.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum SearchApi {
	YANDEX(new YandexSearchApi(), new DailyLimiter(Limits.DAILY_YANDEX)),
	GOOGLE(new GoogleSearchApi(), new DailyLimiter(Limits.DAILY_GOOGLE)),
	EMPTY(new EmptySearchApi(), new ConstantLimiter(Limits.EMPTY)),
	AZURE_DEMO(new AzureDemoSearchApi(), new ConstantLimiter(0)),
	LOCAL(new LocalSearchApi(), new ConstantLimiter(0)),
	GOOGLE_SCRAPE(new GoogleScrapeApi(), new ScrapperLimiter());

	private final SearchApiService service;
	private final Limiter limiter;

	SearchApi(SearchApiService service, Limiter limiter) {
		this.service = service;
		this.limiter = limiter;
	}

	public SearchApiService getService() {
		return service;
	}

	public Limiter getLimiter() {
		return limiter;
	}

	public String getDisplayName() {
		String[] words = name().toLowerCase().split("_");
		return Arrays.stream(words)
				.map(n -> n.substring(0, 1).toUpperCase() + n.substring(1))
				.collect(Collectors.joining(" "));
	}
}
