package psychic.guide.api.model;

public class Limits {
	private static final int DAILY_GOOGLE_LIMIT = 100;
	private static final int DAILY_YANDEX_LIMIT = 10;

	private int google = DAILY_GOOGLE_LIMIT;
	private int yandex = DAILY_YANDEX_LIMIT;

	public int getGoogle() {
		return google;
	}

	public int getYandex() {
		return yandex;
	}

	public void useGoogle() {
		google--;
	}

	public void useYandex() {
		yandex--;
	}

	public void reset() {
		google = DAILY_GOOGLE_LIMIT;
		yandex = DAILY_YANDEX_LIMIT;
	}
}
