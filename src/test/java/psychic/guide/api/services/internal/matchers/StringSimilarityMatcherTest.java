package psychic.guide.api.services.internal.matchers;

import info.debatty.java.stringsimilarity.interfaces.StringDistance;
import org.junit.Assert;
import org.junit.Test;

public class StringSimilarityMatcherTest {
	@Test
	public void match() {
		Assert.assertTrue(createAndMatch(10));
		Assert.assertTrue(createAndMatch(1));
		Assert.assertTrue(createAndMatch(0.9));

		Assert.assertFalse(createAndMatch(0.7));
		Assert.assertFalse(createAndMatch(0));
		Assert.assertFalse(createAndMatch(-1));
	}

	private boolean createAndMatch(double distance) {
		StringDistance stringDistance = (StringDistance) (s1, s2) -> distance;
		StringSimilarityMatcher stringSimilarityMatcher = new StringSimilarityMatcherWrapper(stringDistance);
		return stringSimilarityMatcher.match("", "");
	}

	private static class StringSimilarityMatcherWrapper extends StringSimilarityMatcher {
		protected StringSimilarityMatcherWrapper(StringDistance matcher) {
			super(matcher);
		}
	}
}