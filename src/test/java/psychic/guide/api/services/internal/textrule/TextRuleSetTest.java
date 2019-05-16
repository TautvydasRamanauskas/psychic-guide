package psychic.guide.api.services.internal.textrule;

import org.junit.Assert;
import org.junit.Test;

public class TextRuleSetTest {
	@Test
	public void testTextRuleParenthesis() {
		TextRule textRule = new TextRuleSet();
		String modifiedText = textRule.modify("(Text)_Text");
		Assert.assertEquals("_Text", modifiedText);
	}

	@Test
	public void testTextRuleReview() {
		TextRule textRule = new TextRuleSet();
		String modifiedText = textRule.modify("Review_Text");
		Assert.assertEquals("_Text", modifiedText);
	}

	@Test
	public void testTextRuleNumber() {
		TextRule textRule = new TextRuleSet();
		String modifiedText = textRule.modify("21._Text");
		Assert.assertEquals("_Text", modifiedText);
	}

	@Test
	public void testTextRuleTrim() {
		TextRule textRule = new TextRuleSet();
		String modifiedText = textRule.modify(" Text ");
		Assert.assertEquals("Text", modifiedText);
	}

	@Test
	public void testTextRuleSet() {
		TextRule textRule = new TextRuleSet();
		String modifiedText = textRule.modify("101. (Text) Review Text (Text)");
		Assert.assertEquals("Text", modifiedText);
	}
}