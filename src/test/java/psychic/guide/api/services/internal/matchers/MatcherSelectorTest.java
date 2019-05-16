package psychic.guide.api.services.internal.matchers;

import org.junit.Before;
import org.junit.Test;
import psychic.guide.api.model.Options;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class MatcherSelectorTest {
	private MatcherSelector matcherSelector;

	@Before
	public void setUp() {
		matcherSelector = new MatcherSelector();
	}

	@Test
	public void select() {
		Arrays.stream(MatcherType.values()).forEach(this::testSingleSelect);
	}

	private void testSingleSelect(MatcherType matcherType) {
		Options options = createOptions(matcherType.name());
		StringMatcher stringMatcher = matcherSelector.select(options);
		assertEquals(matcherType.getMatcher().getClass(), stringMatcher.getClass());
	}

	private static Options createOptions(String matcherName) {
		Options options = new Options();
		options.setMatcher(matcherName);
		return options;
	}
}