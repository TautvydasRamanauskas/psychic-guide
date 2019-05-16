package psychic.guide.api.services.internal.matchers;

import org.junit.Assert;
import org.junit.Test;

public class ShingleLettersMatcherTest {
	private final ShingleLettersMatcher matcher = new ShingleLettersMatcher();

	@Test
	public void testCreateShingles() {
		Assert.assertEquals(0, matcher.createShingles("").size());
		Assert.assertEquals(1, matcher.createShingles("T").size());
		Assert.assertEquals(1, matcher.createShingles("Te").size());
		Assert.assertEquals(1, matcher.createShingles("Tes").size());
		Assert.assertEquals(2, matcher.createShingles("Test").size());
		Assert.assertEquals(2, matcher.createShingles("Test ").size());
		Assert.assertEquals(5, matcher.createShingles("Test Test Test").size());
	}
}