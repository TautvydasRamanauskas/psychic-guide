package psychic.guide.api.services.internal.textrule;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextRuleReviewTest {
	@Test
	public void testTextRule() {
		TextRule testRule = new TextRuleReview();
		String modifiedText = testRule.modify("Review Text");
		assertEquals(" Text", modifiedText);
	}

	@Test
	public void testWithoutReview() {
		TextRule testRule = new TextRuleReview();
		String modifiedText = testRule.modify("Text Text");
		assertEquals("Text Text", modifiedText);
	}

	@Test
	public void testReviewInTheEnd() {
		TextRule testRule = new TextRuleReview();
		String modifiedText = testRule.modify("Text Review");
		assertEquals("Text ", modifiedText);
	}
}