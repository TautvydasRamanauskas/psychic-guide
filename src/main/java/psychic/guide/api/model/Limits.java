package psychic.guide.api.model;

public class Limits {
	private int google = 90;
	private int yandex = 9;

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
