package psychic.guide.api.services.internal.matchers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainsMatcherTest {
	private StringMatcher matcher;

	@Before
	public void setUp() {
		matcher = new ContainsMatcher();
	}

	@Test
	public void match() {
		assertTrue(matcher.match("String one", "String one"));
		assertTrue(matcher.match("String one", "one"));
		assertTrue(matcher.match("String one", "String"));
		assertTrue(matcher.match("String", "String one"));
		assertTrue(matcher.match("one", "String one"));

		assertFalse(matcher.match("String two", "String one"));
		assertFalse(matcher.match("String onen", "String two"));
		assertFalse(matcher.match("ones", "String two"));
		assertFalse(matcher.match("String one", "ones"));
	}
}