package psychic.guide.api.services.security;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class FacebookTokenValidator implements TokenValidator {
	private static final String URL = "https://graph.facebook.com/me?access_token=%s";

	public boolean validate(String accessToken, long userId) {
		JSONObject results = fetchResults(accessToken);
		if (results != null) {
			long actualUserId = results.getLong("id");
			return Objects.equals(userId, actualUserId);
		}
		return false;
	}

	private JSONObject fetchResults(String accessToken) {
		StringBuilder results = new StringBuilder();
		try {
			java.net.URL url = new URL(String.format(URL, accessToken));
			URLConnection connection = url.openConnection();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					results.append(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new JSONObject(results.toString());
	}
}
