package psychic.guide.api.services.internal.matchers;

import info.debatty.java.stringsimilarity.JaroWinkler;

class JaroWinklerMatcher extends StringSimilarityMatcher {
	protected JaroWinklerMatcher() {
		super(new JaroWinkler());
	}
}
