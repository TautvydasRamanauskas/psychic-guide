package psychic.guide.api.services.internal.textrule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextRuleNumberTest {
	@Test
	public void testTextRule() {
		TextRule testRule = new TextRuleNumber();
		String modifiedText = testRule.modify("1. Text");
		assertEquals(" Text", modifiedText);
	}

	@Test
	public void testWithoutNumber() {
		TextRule testRule = new TextRuleNumber();
		String modifiedText = testRule.modify("Text Text");
		assertEquals("Text Text", modifiedText);
	}

	@Test
	public void testMultiDigitNumber() {
		TextRule testRule = new TextRuleNumber();
		String modifiedText = testRule.modify("101. Text");
		assertEquals(" Text", modifiedText);
	}

	@Test
	public void testNumberWithoutDot() {
		TextRule testRule = new TextRuleNumber();
		String modifiedText = testRule.modify("1 Text");
		assertEquals("Text", modifiedText);
	}
}