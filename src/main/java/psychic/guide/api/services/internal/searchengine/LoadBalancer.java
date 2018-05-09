package psychic.guide.api.services.internal.searchengine;

import psychic.guide.api.services.internal.model.SearchResult;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LoadBalancer implements SearchAPIService {
	private final Map<Limit, SearchAPIService> services;
	private final Timer timer;

	public LoadBalancer() {
		services = createServicesMap();
		timer = createAndStartTimer();
	}

	@Override
	public List<SearchResult> search(String keyword) {
//		SearchAPIService service = services.entrySet().stream()
//				.max((eOne, eTwo) -> compare(eOne.getKey(), eTwo.getKey()))
//				.map(Map.Entry::getValue)
//				.orElse(null);
//		if (service != null) {
//			return service.search(keyword);
//		}
//		return new ArrayList<>();

		List<SearchResult> results = services.get(Limit.BING).search(keyword);
		Collections.shuffle(results);
		return results;
	}

	public void stopTimer() {
		timer.cancel();
	}

	private static Map<Limit, SearchAPIService> createServicesMap() {
		Map<Limit, SearchAPIService> services = new HashMap<>();
		services.put(Limit.GOOGLE, new GoogleSearchApi());
		services.put(Limit.YANDEX, new YandexSearchApi());
		services.put(Limit.BING, new AzureDemoSearchApi());
		return services;
	}

	private static Timer createAndStartTimer() {
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
		GOOGLE(100), YANDEX(10), BING(0);

		private int limit;
		private int current;

		Limit(int limit) {
			this.limit = limit;
			this.current = 0;
		}

		public void reset() {
			current = 0;
		}

		public int available() {
			if (limit == 0) {
				return 100;
			}
			return 100 - (10 * current / limit);
		}
	}
}
