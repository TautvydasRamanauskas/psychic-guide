package psychic.guide.api.services.internal.textrule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextRuleParenthesisTest {
	@Test
	public void testTextRule() {
		TextRule testRule = new TextRuleParenthesis();
		String modifiedText = testRule.modify("(Text) Text");
		assertEquals(" Text", modifiedText);
	}

	@Test
	public void testWithoutParenthesis() {
		TextRule testRule = new TextRuleParenthesis();
		String modifiedText = testRule.modify("Text Text");
		assertEquals("Text Text", modifiedText);
	}

	@Test
	public void testOnlyOpen() {
		TextRule testRule = new TextRuleParenthesis();
		String modifiedText = testRule.modify("(Text");
		assertEquals("(Text", modifiedText);
	}

	@Test
	public void testOnlyClose() {
		TextRule testRule = new TextRuleParenthesis();
		String modifiedText = testRule.modify("Text)");
		assertEquals("Text)", modifiedText);
	}

	@Test
	public void testMultiple() {
		TextRule testRule = new TextRuleParenthesis();
		String modifiedText = testRule.modify("(Text) Text (Text)");
		assertEquals(" Text ", modifiedText);
	}
}