package psychic.guide.api;

public class Authenticator {
	private static final String KEY = "8NpIU1YWqm-z1tvRLEeJt-jRoYbl1qf5";

	public static boolean authenticate(String key) {
		return KEY.equals(key);
	}
}
