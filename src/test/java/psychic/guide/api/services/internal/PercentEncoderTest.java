package psychic.guide.api.services.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static psychic.guide.api.services.internal.PercentEncoder.encode;

public class PercentEncoderTest {
	@Test
	public void testEncoder() {
		assertEquals("Ladies%20%2B%20Gentlemen", encode("Ladies + Gentlemen"));
		assertEquals("An%20encoded%20string%21", encode("An encoded string!"));
		assertEquals("Dogs%2C%20Cats%20%26%20Mice", encode("Dogs, Cats & Mice"));
		assertEquals("%E2%98%83", encode("☃"));
	}
}