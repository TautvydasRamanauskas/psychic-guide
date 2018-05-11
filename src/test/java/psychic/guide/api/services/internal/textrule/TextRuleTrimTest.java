package psychic.guide.api.services.internal.textrule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextRuleTrimTest {
	@Test
	public void testTextRule() {
		TextRule testRule = new TextRuleTrim();
		String modifiedText = testRule.modify(" Text ");
		assertEquals("Text", modifiedText);
	}

	@Test
	public void testWithoutAvailableTrimming() {
		TextRule testRule = new TextRuleTrim();
		String modifiedText = testRule.modify("Text Text");
		assertEquals("Text Text", modifiedText);
	}
}