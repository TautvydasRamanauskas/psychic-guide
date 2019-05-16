package psychic.guide.api.services.internal.matchers;

import info.debatty.java.stringsimilarity.interfaces.StringDistance;

public abstract class StringSimilarityMatcher implements StringMatcher {
	private static final double MATCHER_DISTANCE_MIN = 0.75;
	private final StringDistance matcher;

	protected StringSimilarityMatcher(StringDistance matcher) {
		this.matcher = matcher;
	}

	@Override
	public boolean match(String stringOne, String stringTwo) {
		return matcher.distance(stringOne, stringTwo) > MATCHER_DISTANCE_MIN;
	}
}
