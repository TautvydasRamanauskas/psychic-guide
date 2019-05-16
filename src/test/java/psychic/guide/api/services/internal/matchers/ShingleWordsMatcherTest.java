package psychic.guide.api.services.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShingleWordsMatcherTest {
	private final StringMatcher matcher = new ShingleWordsMatcher();

	@Test
	public void testMatching() {
		matchTrue("", "");
		matchTrue("One", "One");
		matchTrue("One Two", "One Two");
		matchTrue("One Two", "One Two Three");
		matchTrue("One Two Three", "One Two");
		matchTrue("One Two Three", "One Two Three");
		matchTrue("One Two Three Four", "One Two Three");
		matchTrue("One Two Three Four", "One Two Three Four");
		matchTrue("One Two Three Four Five", "One Two Three");
		matchTrue("One Two Three Four Five", "One Two Three Four");
		matchTrue("One Two Three Four Five", "One Two Three Four Five");
		matchTrue("One Two Three Four Five", "One Two Three Four Five Six");
		matchTrue("One Two Three Four Five", "One Two Three Four Five Six Seven");


		matchFalse("One", "");
		matchFalse("One Two", "");
		matchFalse("One Two", "One");
		matchFalse("One Two Three", "");
		matchFalse("One Two Three", "One");
		matchFalse("One Two Three Four", "");
		matchFalse("One Two Three Four", "One");
		matchFalse("One Two Three Four", "One Two");
		matchFalse("One Two Three Four Five", "");
		matchFalse("One Two Three Four Five", "One");
		matchFalse("One Two Three Four Five", "One Two");
		matchFalse("One Two Three Four Five", "One Two Three Four Five Six Seven Eight");
	}

	private void matchTrue(String stringOne, String stringTwo) {
		assertTrue(matcher.match(stringOne, stringTwo));
	}

	private void matchFalse(String stringOne, String stringTwo) {
		assertFalse(matcher.match(stringOne, stringTwo));
	}
}