package psychic.guide.api.model;

public class Limits {
	private int google = 100;
	private int yandex = 10;

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
		google = 100;
		yandex = 10;
	}
}
