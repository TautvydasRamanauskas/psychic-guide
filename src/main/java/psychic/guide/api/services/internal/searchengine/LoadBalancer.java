package psychic.guide.api.services.internal.searchengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psychic.guide.api.model.Options;
import psychic.guide.api.services.internal.model.SearchResult;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LoadBalancer implements SearchAPIService {
	private static final Map<Limit, Limit> LIMITS = Map.of(Limit.GOOGLE, Limit.GOOGLE, Limit.YANDEX, Limit.YANDEX);
	private final Options options;
	private final Map<Limit, SearchAPIService> services;
	private final Timer timer;
	private final Logger logger;

	public LoadBalancer(Options options) {
		this.options = options;
		this.services = createServicesMap();
		this.timer = createAndStartTimer();
		this.logger = LoggerFactory.getLogger(SearchAPIService.class);
	}

	@Override
	public List<SearchResult> search(String keyword) {
		SearchAPIService service = services.entrySet().stream()
				.max((eOne, eTwo) -> compare(eOne.getKey(), eTwo.getKey()))
				.map(Map.Entry::getValue)
				.orElse(null);
		if (service != null) {
			logger.info("Using {} as search engine", service.getClass().getSimpleName());
			return service.search(keyword);
		}
		return new ArrayList<>();

//		List<SearchResult> results = services.get(Limit.BING).search(keyword);
//		Collections.shuffle(results);
//		return results;
	}

	public void stopTimer() {
		timer.cancel();
	}

	private Map<Limit, SearchAPIService> createServicesMap() {
		Map<Limit, SearchAPIService> services = new HashMap<>();
		if (options.isUseGoogle()) {
			services.put(Limit.GOOGLE, new GoogleSearchApi());
		}
		if (options.isUseYandex()) {
			services.put(Limit.YANDEX, new YandexSearchApi());
		}
//		services.put(Limit.BING, new AzureDemoSearchApi());
		return services;
	}

	private Timer createAndStartTimer() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Arrays.stream(Limit.values()).forEach(Limit::reset);
			}
		};
		Date firstMidnight = new Date(LocalTime.MIDNIGHT.getLong(ChronoField.MILLI_OF_DAY));
		timer.scheduleAtFixedRate(timerTask, firstMidnight, ChronoUnit.DAYS.getDuration().toMillis());
		return timer;
	}

	private static int compare(Limit limitOne, Limit limitTwo) {
		return Integer.compare(limitOne.available(), limitTwo.available());
	}

	private enum Limit {
		GOOGLE(100), YANDEX(10)/*, BING(-1)*/; // TODO: persist limits (Map?)

		private final int limit;
		private int current;

		Limit(int limit) {
			this.limit = limit;
			this.current = 0;
		}

		public void reset() {
			current = 0;
		}

		public int available() {
			if (limit == -1) {
				return 0;
			}
			if (limit == 0) {
				return 100;
			}
			return 100 - (10 * current / limit);
		}
	}
}
