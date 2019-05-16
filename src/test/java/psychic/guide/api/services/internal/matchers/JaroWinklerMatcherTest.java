package psychic.guide.api.services.internal.matchers;

import org.junit.Before;
import org.junit.Test;

public class JaroWinklerMatcherTest {
	private StringMatcher matcher;

	@Before
	public void setUp() {
		matcher = new JaroWinklerMatcher();
	}

	@Test
	public void match() {
		// TODO
	}
}