package psychic.guide.api.services.internal.matchers;

import info.debatty.java.stringsimilarity.Cosine;

class CosineMatcher extends StringSimilarityMatcher {
	public CosineMatcher() {
		super(new Cosine());
	}
}
