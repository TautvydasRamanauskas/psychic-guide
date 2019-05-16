package psychic.guide.api.services.internal.matchers;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

class LevenshteinMatcher extends StringSimilarityMatcher {
	public LevenshteinMatcher() {
		super(new NormalizedLevenshtein());
	}
}