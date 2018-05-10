package psychic.guide.api.services.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PercentEncoder {
	private static final String ENC = "UTF-8";
	private static final String REPLACE_FROM = "+";
	private static final String REPLACE_TO = "%20";

	public static String encode(String string) {
		try {
			return URLEncoder.encode(string, ENC).replace(REPLACE_FROM, REPLACE_TO);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}
}
