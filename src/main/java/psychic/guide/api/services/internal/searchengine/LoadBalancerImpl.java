package psychic.guide.api.services.internal.searchengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import psychic.guide.api.model.Limits;
import psychic.guide.api.services.internal.model.SearchResult;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class LoadBalancerImpl implements LoadBalancer {
	private final Map<SearchAPI, SearchAPIService> services;
	private final Limits limits;
	private final Timer timer;
	private final Logger logger;

	public LoadBalancerImpl() {
		this.services = createServicesMap();
		this.limits = new Limits();
		this.timer = createAndStartTimer();
		this.logger = LoggerFactory.getLogger(SearchAPIService.class);
	}

	@Override
	public List<SearchResult> search(String keyword) {
		SearchAPIService service = selectService();
		if (service != null) {
			logger.info("Using {} as search engine", service.getClass().getSimpleName());
			return service.search(keyword);
		}
		return new ArrayList<>();

//		List<SearchResult> results = services.get(Limit.BING).search(keyword);
//		Collections.shuffle(results);
//		return results;
	}

	@Override
	public Limits getLimits() {
		return limits;
	}

	public void stopTimer() {
		timer.cancel();
	}

	private Map<SearchAPI, SearchAPIService> createServicesMap() {
		Map<SearchAPI, SearchAPIService> services = new HashMap<>();
		services.put(SearchAPI.GOOGLE, new GoogleSearchApi());
		services.put(SearchAPI.YANDEX, new YandexSearchApi());
//		services.put(Limit.BING, new AzureDemoSearchApi());
		return services;
	}

	private Timer createAndStartTimer() {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				limits.reset();
			}
		};
		long delay = ChronoUnit.DAYS.getDuration().toMillis()-LocalTime.MIDNIGHT.getLong(ChronoField.MILLI_OF_DAY);
		timer.scheduleAtFixedRate(timerTask, delay, ChronoUnit.DAYS.getDuration().toMillis());
		return timer;
	}

	private SearchAPIService selectService() {
		if (limits.getGoogle() > limits.getYandex()) {
			return services.get(SearchAPI.GOOGLE);
		}
		return services.get(SearchAPI.YANDEX);
	}

	private enum SearchAPI {
		YANDEX, GOOGLE
	}
}
