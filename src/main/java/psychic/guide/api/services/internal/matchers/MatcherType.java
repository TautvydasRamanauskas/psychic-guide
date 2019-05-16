package psychic.guide.api.services.internal.matchers;

public enum MatcherType {
	CONTAINS(new ContainsMatcher()),
	WORDS(new ShingleWordsMatcher()),
	LEVENSHTEIN(new LevenshteinMatcher()),
	COSINE(new CosineMatcher()),
	JARO_WINKLER(new JaroWinklerMatcher());

	private final StringMatcher matcher;

	MatcherType(StringMatcher matcher) {
		this.matcher = matcher;
	}

	public StringMatcher getMatcher() {
		return matcher;
	}
}
